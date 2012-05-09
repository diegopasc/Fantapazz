//
//  MenuTabBarController.m
//  iFantapazz
//
//  Created by Michele Mastrogiovanni on 08/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "MenuTabBarController.h"
#import "NSString+SBJSON.h"
#import "PlayerAssignmentController.h"
#import "NSObject+SBJSON.h"
#import "Constants.h"

@implementation MenuTabBarController

@synthesize players, needsReload;

#define UNSELECTED                  0
#define SELECTED_TITOLARE           1
#define SELECTED_PANCHINARO         2

- (PlayerAssignmentController*) playerControllerWithTag:(int) aTag
{
    for ( int i = 0; i < [[ self viewControllers ] count ]; i ++ ) {
        
        UITabBarItem * item = [[ self.tabBar items ] objectAtIndex:i ];
        int tag = item.tag;
        
        if ( tag == aTag ) {
            return (PlayerAssignmentController*) [[ self viewControllers ] objectAtIndex:i ];
        }
    }
    
    return nil;
}

- (void)hudWasHidden {
    // Remove HUD from screen when the HUD was hidden
	[ HUD removeFromSuperview ];
	[ HUD release ];
}

- (void) startHUDUndeterminate:(NSString*) message
{
    // Should be initialized with the windows frame so the HUD disables all user input by coverin the entire screen
	HUD = [[ MBProgressHUD alloc ] initWithWindow:[UIApplication sharedApplication].keyWindow ];
    
    // Add HUD to screen
	[ self.view.window addSubview:HUD ];
    
    HUD.labelText = message;
    
    [ HUD show:YES ];
}

- (void) stopHUDUndeterminate
{
    [ HUD hide:YES ];
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

- (void) updateInitialBadges
{
    for ( int i = 0; i < [[ self viewControllers ] count ]; i ++ ) {
        
        UITabBarItem * item = [[ self.tabBar items ] objectAtIndex:i ];
        int tag = item.tag;
        
        UIViewController * controller = [[ self viewControllers ] objectAtIndex:i ];
        if ( [ controller isKindOfClass:[ PlayerAssignmentController class ]] ) {
            
            if ( tag == 0 ) {
                
                controller.tabBarItem.badgeValue = [ NSString stringWithFormat:@"0/11" ];
                
            }
            else if ( tag == 1 ) {
                
                controller.tabBarItem.badgeValue = [ NSString stringWithFormat:@"0/%d", [ ruoliPanchinari count ]];
                
            }
            
        }
        
    }
}

- (NSArray*) selectedFormatione:(PlayerAssignmentController*) controller
{
    NSString * key = [[ formazioni allKeys ] objectAtIndex:selectedFormazione ];
    NSString * formazione = [ formazioni valueForKey:key ];
    NSArray * components = [ formazione componentsSeparatedByString:@"-" ];
    
    for ( int i = 0; i < [[ self viewControllers ] count ]; i ++ ) {
        
        UITabBarItem * item = [[ self.tabBar items ] objectAtIndex:i ];
        int tag = item.tag;
        
        if ( controller != [[ self viewControllers ] objectAtIndex:i ] ) {
            continue;
        }
        
        if ( tag == 0 ) {
            // Titolari
            
            NSMutableArray * roles = [[[ NSMutableArray alloc ] init ] autorelease ];
            
            [ roles addObject:@"1" ];
            
            for ( int j = 0; j < [[ components objectAtIndex:0 ] integerValue ]; j ++ )
                [ roles addObject:@"2" ];
            
            for ( int j = 0; j < [[ components objectAtIndex:1 ] integerValue ]; j ++ )
                [ roles addObject:@"3" ];
            
            for ( int j = 0; j < [[ components objectAtIndex:2 ] integerValue ]; j ++ )
                [ roles addObject:@"4" ];
            
            return roles;
            
        }
        else if ( tag == 1 ) {
            // Riserve
            
            return ruoliPanchinari;
            
        }
        
    }
    
    return nil;
}

#pragma mark - View lifecycle

- (void) showNotification:(NSString*) message withTitle:(NSString*) title
{
    UIAlertView *msg = [[UIAlertView alloc] initWithTitle:title
                                                  message:message
                                                 delegate:nil
                                        cancelButtonTitle:@"OK"
                                        otherButtonTitles:nil ];
    [ msg show ];
}

- (void) showAlert:(NSString*) message
{
    UIAlertView *msg = [[UIAlertView alloc] initWithTitle:@"Attenzione"
                                                  message:message
                                                 delegate:nil
                                        cancelButtonTitle:@"OK"
                                        otherButtonTitles:nil ];
    [ msg show ];
}

- (void) connector:(Connector*)aConnector success:(NSData*) data withContext:(id) aContext
{
    [ self stopHUDUndeterminate ];
    
    if ([ aContext isEqualToString:@"Submit" ]) {

        NSString * stringData = [[[ NSString alloc] initWithData:data encoding:NSASCIIStringEncoding ] autorelease ];
        
		id ret = [ stringData JSONFragmentValue ];
        
        if ( [[ ret valueForKey:@"status" ] intValue ] == 1 ) {
            
            NSString * msg = [ ret valueForKey:@"msg" ];
            
            [ self showNotification:msg withTitle:@"Invio formazione" ];
            
        }
        else {
            
            NSString * msg = [ ret valueForKey:@"msg" ];
            
            [ self showAlert:msg ];
            
        }
        
        return;

    }
    
    [ self.players removeAllObjects ];
    
	@try {
        
        NSString * stringData = [[[ NSString alloc] initWithData:data encoding:NSASCIIStringEncoding ] autorelease ];
        
		id ret = [ stringData JSONFragmentValue ];
		
#ifdef DEBUG_JSON
		[[ NSString stringWithFormat:@"Response JSON: (class: %@) %@", [[ ret class ] description ], [ ret description ]] limitedLog ];
#endif
        
        NSLog(@"Calciatori: %@", [ ret description ]);
        
        // Statistiche
        id statistiche = [ ret valueForKeyPath:@"data.statistiche" ];
        
        [ id_giornata release ];
        id_giornata = [[ ret valueForKeyPath:@"data.id_giornata" ] retain ];
        [ ngiornata release ];
        ngiornata = [[ ret valueForKeyPath:@"data.ngiornata" ] retain ];
        
        [ formazioni release ];
        formazioni = [(NSDictionary*)[ ret valueForKeyPath:@"data.id-schemi-disponibili" ] retain ];
        selectedFormazione = 0;
        
        if ( ruoliPanchinari != nil )
            [ ruoliPanchinari release ];
        ruoliPanchinari = [[ ret valueForKeyPath:@"data.id-ruoli-panchinari" ] retain ];
        
        // Inspect calciatori list
        NSDictionary * calciatori = [ ret valueForKeyPath:@"data.calciatori" ];
        NSEnumerator * calciatoreID = [ calciatori keyEnumerator ];
        id calciatore = nil;
        while ((calciatore = [ calciatoreID nextObject ]) != nil) {
            
            NSMutableDictionary * player = [ NSMutableDictionary dictionaryWithDictionary:[ calciatori valueForKey:calciatore ]];
            
            [ player setValue:[ NSNumber numberWithInt:UNSELECTED ] forKey:@"selected" ];
            [ player setValue:calciatore forKey:@"id_calciatore" ];
            
            // Statistiche calciatore
            id statisticheCalciatore = [ statistiche valueForKey:calciatore ];
            id HomeAndAway = [ statisticheCalciatore valueForKey:@"HomeAndAway" ];
            
            [ player setValue:[ HomeAndAway valueForKey:@"MV" ] forKey:@"mv" ];
            [ player setValue:[ HomeAndAway valueForKey:@"GolFa" ] forKey:@"golfa" ];
            [ player setValue:[ HomeAndAway valueForKey:@"GolSu" ] forKey:@"golsu" ];
            [ player setValue:[ HomeAndAway valueForKey:@"PI" ] forKey:@"pi" ];
            [ player setValue:[[ HomeAndAway valueForKey:@"cartellini" ] description ] forKey:@"cartellini" ];
            
            [ self.players addObject:player ];
            
        }
        
	}
	@catch (NSException * e) {
        NSError * error = [ NSError errorWithDomain:e.name code:100 userInfo:e.userInfo ];
        NSLog(@"Exception: %@", [ error description ]);
	}
    
    if ( [[ self selectedViewController ] isKindOfClass:[ PlayerAssignmentController class ]] ) {
        PlayerAssignmentController * c = (PlayerAssignmentController*)[ self selectedViewController ];
        c.needsReload = YES;
        [ c reloadData ];
    }
    else if ( [[ self selectedViewController ] isKindOfClass:[ UITableViewController class ]] ) {
        [ ((UITableViewController*)[ self selectedViewController ]).tableView reloadData ];
    }
    
    [ self updateInitialBadges ];
    
}

- (void) connector:(Connector*)aConnector fail:(NSError*) error withContext:(id) context
{
    [ self stopHUDUndeterminate ];
}

/*
 // Implement loadView to create a view hierarchy programmatically, without using a nib.
 - (void)loadView
 {
 }
 */

- (void) loadData
{
    [ self startHUDUndeterminate:@"Loading..." ];
    
    NSString * idSquadra = [[ NSUserDefaults standardUserDefaults ] objectForKey:@"fantapazz.login.id_squadra" ];
    
    NSString * loginURL = [ NSString stringWithFormat:@"%@/servizi/fantacalcio/invio-formazione?token=%@&id_squadra=%@",
                           FANTAPAZZ_URL,
                           FANTAPAZZ_API_TOKEN,
                           idSquadra ];
    
    NSLog(@"URL: %@", loginURL);
    
    NSMutableURLRequest *request = [[[ NSMutableURLRequest alloc ] init ] autorelease ];
    [ request setURL:[ NSURL URLWithString:loginURL ]];
    [ request setHTTPMethod:@"GET" ];
    
    Connector * connector = [[ Connector alloc ] initWithDelegate:self ];
    [ connector startRequest:request withContext:@"Refresh" ];
    [ connector release ];
}

// Implement viewDidLoad to do additional setup after loading the view, typically from a nib
- (void)viewDidLoad
{
    [ super viewDidLoad ];
    
    self.players = [ NSMutableArray array ];
}

- (void) viewDidAppear:(BOOL)animated
{
    [ super viewDidAppear:animated ];
    
    if ( needsReload ) {
        needsReload = NO;
        [ self loadData ];
    }
}

- (void)viewDidUnload
{
    self.players = nil;
    [ super viewDidUnload ];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

#pragma mark -
#pragma mark Actions

- (IBAction) send:(id)sender
{
    NSMutableArray * titolari = [[ NSMutableArray alloc ] init ];
    NSMutableArray * riserve = [[ NSMutableArray alloc ] init ];
    
    BOOL complete = YES;
    
    // Titolari
    NSArray * descriptions = [ self playerControllerWithTag:0 ].placeDescription;
    NSArray * assignements = [ self playerControllerWithTag:0 ].playerAssignement;
    
    for (int i = 0; i < [ descriptions count ]; i ++ ) {
        id player = [ assignements objectAtIndex:i ];
        id assigned = [ descriptions objectAtIndex:i ];
        if ( [[ assigned valueForKey:@"assigned" ] isEqual:@"YES" ] ) {
            [ titolari addObject:[ player valueForKey:@"id_calciatore" ]];
        }
        else {
            complete = NO;
        }
    }
    
    // Riserve
    descriptions = [ self playerControllerWithTag:1 ].placeDescription;
    assignements = [ self playerControllerWithTag:1 ].playerAssignement;
    
    for (int i = 0; i < [ descriptions count ]; i ++ ) {
        id player = [ assignements objectAtIndex:i ];
        id assigned = [ descriptions objectAtIndex:i ];
        if ( [[ assigned valueForKey:@"assigned" ] isEqual:@"YES" ] ) {
            [ riserve addObject:[ player valueForKey:@"id_calciatore" ]];
        }
        else {
            complete = NO;
        }
    }
    
    NSString * key = [[ formazioni allKeys ] objectAtIndex:selectedFormazione ];
    // NSString * formazioneSelected = [ formazioni valueForKey:key ];
    // NSArray * components = [ formazioneSelected componentsSeparatedByString:@"-" ];
    
    if ( ! complete ) {
        
        NSString * msg = [ NSString stringWithFormat:@"Formazione non completa" ];
        
        UIAlertView *message = [[UIAlertView alloc] initWithTitle:@"Attenzione"
                                                          message:msg
                                                         delegate:nil
                                                cancelButtonTitle:@"OK"
                                                otherButtonTitles:nil];
        [ message show ];
        
        return;
        
    }
    
    NSString * idSquadra = [[ NSUserDefaults standardUserDefaults ] objectForKey:@"fantapazz.login.id_squadra" ];
    
    NSString * userID = (NSString*) [[ NSUserDefaults standardUserDefaults ] objectForKey:@"fantapazz.login.uuid" ];
    
    NSMutableDictionary * formazione = [[ NSMutableDictionary alloc ] init ];
    [ formazione setValue:FANTAPAZZ_API_TOKEN forKey:@"token" ];
    [ formazione setValue:idSquadra forKey:@"id_squadra" ];
    [ formazione setValue:id_giornata forKey:@"id_giornata" ];
    [ formazione setValue:userID forKey:@"uid" ];
    [ formazione setValue:key forKey:@"id_schema" ];
    [ formazione setValue:titolari forKey:@"titolari" ];
    [ formazione setValue:riserve forKey:@"riserve" ];
    
    [ titolari release ];
    [ riserve release ];
    
    NSString *stringBoundary = @"0xKhTmLbOuNdArY";
    NSString *endItemBoundary = [ NSString stringWithFormat:@"--%@\r\n", stringBoundary ];
    NSString * multipart = [ NSString stringWithFormat:@"multipart/form-data; charset=utf-8; boundary=%@", stringBoundary ];
    
    NSString * urlString = [ NSString stringWithFormat:@"%@/servizi/fantacalcio/invio-formazione/send", FANTAPAZZ_URL ];
    NSURL * urlRequest = [ NSURL URLWithString:urlString ];
    
    NSMutableURLRequest *request = [[[ NSMutableURLRequest alloc ] initWithURL:urlRequest ] autorelease ];
    [ request setHTTPMethod:@"POST" ];
    [ request addValue:multipart forHTTPHeaderField:@"Content-Type" ];
    [ request setValue:@"application/json" forHTTPHeaderField:@"Accept" ];
    
    // Get content for call as a JSON fragment
    NSString * content = [ formazione JSONFragment ];
    [ formazione release ];
    
    // Setup request content
    NSMutableString * value = [[ NSMutableString alloc ] init ];
    [ value appendFormat:@"--%@\r\n", stringBoundary];
    [ value appendFormat:@"Content-Disposition: form-data; name=\"data\"\r\n\r\n" ];
    [ value appendFormat:@"%@\r\n", content ];
    [ value appendFormat:endItemBoundary ];
    
    NSLog(@"%@", value);
    
    // Setup POST body
    NSData *postData = [ value dataUsingEncoding:NSASCIIStringEncoding ];
    NSString *postLength = [ NSString stringWithFormat:@"%d", [ postData length ]];
    [ request setValue:postLength forHTTPHeaderField:@"Content-Length"];
    [ request setHTTPBody:postData ];
    
    [ self startHUDUndeterminate:@"Sto inviando..." ];
    
    Connector * submitFormazione = [[ Connector alloc ] initWithDelegate:self ];
    [ submitFormazione startRequest:request withContext:@"Submit" ];
    [ submitFormazione release ];
    
}

@end
