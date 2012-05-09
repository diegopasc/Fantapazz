//
//  MenuTabBarController.m
//  iFantapazz
//
//  Created by Michele Mastrogiovanni on 08/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "MenuTabBarController.h"
#import "NSString+SBJSON.h"
#import "Constants.h"

@implementation MenuTabBarController

@synthesize players;

#define UNSELECTED                  0
#define SELECTED_TITOLARE           1
#define SELECTED_PANCHINARO         2

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

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void) connector:(Connector*)aConnector success:(NSData*) data withContext:(id) context
{
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
        
        [ ruoliPanchinari release ];
        ruoliPanchinari = [[ ret valueForKey:@"data.id-ruoli-panchinari" ] retain ];
        
        // Inspect calciatori list
        NSDictionary * calciatori = [ ret valueForKeyPath:@"data.calciatori" ];
        NSEnumerator * calciatoreID = [ calciatori keyEnumerator ];
        id calciatore = nil;
        while ((calciatore = [ calciatoreID nextObject ]) != nil) {
            
            NSMutableDictionary * player = [ NSMutableDictionary dictionaryWithDictionary:calciatore ];
            
            [ player setValue:[ NSNumber numberWithInt:UNSELECTED ] forKey:@"selected" ];
            [ player setValue:calciatoreID forKey:@"id_calciatore" ];
            
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
    
    if ( [[ self selectedViewController ] isKindOfClass:[ UITableViewController class ]] ) {
        [ ((UITableViewController*)[ self selectedViewController ]).tableView reloadData ];
    }
}

- (void) connector:(Connector*)aConnector fail:(NSError*) error withContext:(id) context
{
    
}

/*
// Implement loadView to create a view hierarchy programmatically, without using a nib.
- (void)loadView
{
}
*/

/*
// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad
{
    [super viewDidLoad];
}
*/

- (void) viewDidAppear:(BOOL)animated
{
    [ super viewDidAppear:animated ];
    
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

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

@end
