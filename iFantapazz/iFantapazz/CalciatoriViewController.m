//
//  CalciatoriViewController.m
//  iFantapazz
//
//  Created by Michele Mastrogiovanni on 25/02/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "CalciatoriViewController.h"
#import "Constants.h"
#import "NSString+SBJSON.h"
#import "AppDelegate.h"
#import "OHAttributedLabel.h"
#import "NSAttributedString+Attributes.h"
#import "NSObject+SBJSON.h"

#import "UITableViewController+Additions.h"

#define UNSELECTED                  0
#define SELECTED_TITOLARE           1
#define SELECTED_PANCHINARO         2

@interface CalciatoriViewController (PrivateMethods)

// - (NSFetchRequest*) mainFetchRequest;

- (void)configureCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath;

@end

@implementation CalciatoriViewController

- (NSArray*) calciatoriWithRuolo:(NSString*) ruolo andState:(NSInteger) state
{
    // Create and configure a fetch request with the Book entity.
    NSFetchRequest * fetchRequest = [[[ NSFetchRequest alloc] init ] autorelease ];
    
    NSEntityDescription * entity = [ NSEntityDescription entityForName:@"Calciatore" inManagedObjectContext:self.managedObjectContext ];
    [ fetchRequest setEntity:entity ];

    // Create the sort descriptors array.
    NSSortDescriptor *ruoloSort = [[ NSSortDescriptor alloc ] initWithKey:@"ruolo" ascending:NO ];
    NSSortDescriptor *nameSort = [[ NSSortDescriptor alloc ] initWithKey:@"calciatore" ascending:YES ];
    [ fetchRequest setSortDescriptors:[ NSArray arrayWithObjects:ruoloSort, nameSort, nil ]];
    [ ruoloSort release ];
    [ nameSort release ];

    // Predicate selector
    if ( ruolo == nil ) {
        NSPredicate * predicate = [ NSPredicate predicateWithFormat:@"selected = %d", state ];
        [ fetchRequest setPredicate:predicate ];
    }
    else {
        NSPredicate * predicate = [ NSPredicate predicateWithFormat:@"selected = %d AND ruolo = %@", state, ruolo ];
        [ fetchRequest setPredicate:predicate ];
    }
    
    NSError * error = nil;
    return [ self.managedObjectContext executeFetchRequest:fetchRequest error:& error ];
}

//- (void) loadValues
//{
//    
//}

/*
- (void) validate
{
    // Verifica ruoli panchinari
    // NSArray * panchinari = [ self calciatoriWithRuolo:nil withState:SELECTED_PANCHINARO ];
    
    int panchinariP = 0;
    int panchinariD = 0;
    int panchinariC = 0;
    int panchinariA = 0;
    int panchinari = 0;
    
    for ( id ruolo in ruoliPanchinari ) {
        switch ([ ruolo intValue ]) {
            case 1: panchinariP ++; break;
            case 2: panchinariD ++; break;
            case 3: panchinariC ++; break;
            case 4: panchinariA ++; break;
            case 0: panchinari ++; break;
        }
    }
    
    // Questo array ti dice i ruoli che deve avere ciascun panchianaro 
    // (quindi e' importante l'ordine di questo array!):
    // 1 = quel calciatore deve essere un portiere
    // 2 = quel calciatore deve essere un difensore
    // 3 = quel calciatore deve essere un centrocampista
    // 4 = quel calciatore deve essere un attaccante
    // 0 = quel calciatore non ha nessun vincolo di ruolo

    
    NSLog(@"Portieri: %@", [[ self calciatoriWithRuolo:@"P" andState:SELECTED_TITOLARE ] description ]);
}
*/

- (IBAction) send:(id) sender
{    
    int titolariP = 0;
    int titolariD = 0;
    int titolariC = 0;
    int titolariA = 0;
    
    int riserveP = 0;
    int riserveD = 0;
    int riserveC = 0;
    int riserveA = 0;
    
    int indexRiserva = 0;
    
    NSMutableArray * titolari = [[ NSMutableArray alloc ] init ];
    NSMutableArray * riserve = [[ NSMutableArray alloc ] init ];
    
	NSError * error = nil;
    NSFetchRequest * fetchRequest = [ self mainFetchRequest ];
	NSArray * list = [ self.managedObjectContext executeFetchRequest:fetchRequest error:& error ];
    if ( list != nil ) {
        for ( NSManagedObject * calciatore in list ) {
            
            // Discard fake object
            if ( [[ calciatore valueForKey:@"ruolo" ] isEqualToString:@"Z" ] )
                continue;
            
            if ( [[ calciatore valueForKey:@"selected" ] intValue ] == SELECTED_TITOLARE ) {
                if ( [[ calciatore valueForKey:@"ruolo" ] isEqualToString:@"P" ]) {
                    [ titolari addObject:[ calciatore valueForKey:@"id_calciatore" ]];
                    titolariP ++;
                }
                else if ( [[ calciatore valueForKey:@"ruolo" ] isEqualToString:@"D" ]) {
                    [ titolari addObject:[ calciatore valueForKey:@"id_calciatore" ]];
                    titolariD ++;
                }
                else if ( [[ calciatore valueForKey:@"ruolo" ] isEqualToString:@"C" ]) {
                    [ titolari addObject:[ calciatore valueForKey:@"id_calciatore" ]];
                    titolariC ++;
                }
                else if ( [[ calciatore valueForKey:@"ruolo" ] isEqualToString:@"A" ]) {
                    [ titolari addObject:[ calciatore valueForKey:@"id_calciatore" ]];
                    titolariA ++;
                }
            }
            else if ( [[ calciatore valueForKey:@"selected" ] intValue ] == SELECTED_PANCHINARO ) {

                // 1 = quel calciatore deve essere un portiere
                // 2 = quel calciatore deve essere un difensore
                // 3 = quel calciatore deve essere un centrocampista
                // 4 = quel calciatore deve essere un attaccante
                // 0 = quel calciatore non ha nessun vincolo di ruolo
                
                // int type = [[ ruoliPanchinari objectAtIndex:indexRiserva ] intValue ];
                indexRiserva ++;
                
                if ( [[ calciatore valueForKey:@"ruolo" ] isEqualToString:@"P" ]) {
                    [ riserve addObject:[ calciatore valueForKey:@"id_calciatore" ]];
                    riserveP ++;
                }
                else if ( [[ calciatore valueForKey:@"ruolo" ] isEqualToString:@"D" ]) {
                    [ riserve addObject:[ calciatore valueForKey:@"id_calciatore" ]];
                    riserveD ++;
                }
                else if ( [[ calciatore valueForKey:@"ruolo" ] isEqualToString:@"C" ]) {
                    [ riserve addObject:[ calciatore valueForKey:@"id_calciatore" ]];
                    riserveC ++;
                }
                else if ( [[ calciatore valueForKey:@"ruolo" ] isEqualToString:@"A" ]) {
                    [ riserve addObject:[ calciatore valueForKey:@"id_calciatore" ]];
                    riserveA ++;
                }
            }
            
            // NSLog(@"Calciatore: %@", [ calciatore description ]);
        }
    }
    
    NSString * key = [[ formazioni allKeys ] objectAtIndex:selectedFormazione ];
    NSString * formazioneSelected = [ formazioni valueForKey:key ];
    NSArray * components = [ formazioneSelected componentsSeparatedByString:@"-" ];
    
    if ( titolariP != 1 ) {
        
        NSString * msg = [ NSString stringWithFormat:@"Devi selezionare un solo portiere titolare" ];
        
        UIAlertView *message = [[UIAlertView alloc] initWithTitle:@"Attenzione"
                                                          message:msg
                                                         delegate:nil
                                                cancelButtonTitle:@"OK"
                                                otherButtonTitles:nil];
        [ message show ];
        return;
        
    }
    
    if ( titolariD != [[ components objectAtIndex:0 ] intValue ] ) {
        
        NSString * msg = [ NSString stringWithFormat:@"Hai selezionato solo %d di %d difensori", titolariD, [[ components objectAtIndex:0 ] intValue ] ];
        
        UIAlertView *message = [[UIAlertView alloc] initWithTitle:@"Attenzione"
                                                          message:msg
                                                         delegate:nil
                                                cancelButtonTitle:@"OK"
                                                otherButtonTitles:nil];
        [ message show ];
        return;
    }
    
    if ( titolariC != [[ components objectAtIndex:1 ] intValue ] ) {
        
        NSString * msg = [ NSString stringWithFormat:@"Hai selezionato solo %d di %d centrocampisti", titolariC, [[ components objectAtIndex:1 ] intValue ] ];
        
        UIAlertView *message = [[UIAlertView alloc] initWithTitle:@"Attenzione"
                                                          message:msg
                                                         delegate:nil
                                                cancelButtonTitle:@"OK"
                                                otherButtonTitles:nil];
        [ message show ];
        return;
    }
    
    if ( titolariA != [[ components objectAtIndex:2 ] intValue ] ) {
        
        NSString * msg = [ NSString stringWithFormat:@"Hai selezionato solo %d di %d attaccanti", titolariA, [[ components objectAtIndex:2 ] intValue ] ];
        
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

#pragma - Inherited methods

- (NSString*) sectionNameKeyPath
{
    return @"ruolo";
}

- (NSFetchRequest*) mainFetchRequest
{
    // Create and configure a fetch request with the Book entity.
	NSFetchRequest * fetchRequest = [[ NSFetchRequest alloc] init ];
	
	NSEntityDescription * entity = [ NSEntityDescription entityForName:@"Calciatore" inManagedObjectContext:managedObjectContext ];
	[ fetchRequest setEntity:entity ];
	
	// Create the sort descriptors array.
    NSSortDescriptor *ruoloSort = [[ NSSortDescriptor alloc ] initWithKey:@"ruolo" ascending:NO ];
    NSSortDescriptor *nameSort = [[ NSSortDescriptor alloc ] initWithKey:@"calciatore" ascending:YES ];
    [ fetchRequest setSortDescriptors:[ NSArray arrayWithObjects:ruoloSort, nameSort, nil ]];
	
	// Predicate selector
    //	NSPredicate * predicate = [ NSPredicate predicateWithFormat:@"name LIKE '%'" ];
    //	[ fetchRequest setPredicate:predicate ];
    
    [ ruoloSort release ];
    [ nameSort release ];
    
    return [ fetchRequest autorelease ];
}

#pragma mark - View lifecycle

- (void) mapCalciatore:(NSDictionary*) calciatore onEntity:(NSManagedObject*) object
{
    NSEnumerator * keyEnumerator = [ calciatore keyEnumerator ];
    NSString * key;
    while (( key = [ keyEnumerator nextObject ]) != nil ) {
        @try {
            NSString * coreDataKey = [[ key stringByReplacingOccurrencesOfString:@"-" withString:@"" ] lowercaseString ];
            id value = [ calciatore objectForKey:key ];
            if ( ! [ value isKindOfClass:[ NSString class ]] ) {
                NSLog(@"Object %@ is class: %@", key, [[ value class ] description ]);
            }
            [ object setValue:[ value description ] forKey:coreDataKey ];
        }
        @catch (NSException *exception) {
            NSLog(@"Exception: %@", [ exception description ]);
        }
    }
}

- (void) connector:(Connector*)aConnector fail:(NSError*) error withContext:(id) aContext
{
    [ self stopHUDUndeterminate ];
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
    
    // Begin core data operation
    NSManagedObjectContext * insertContext = [ self beginCoreDataOperation ];

    // Clear database
    [ self clearAllEntity:@"Calciatore" inContext:insertContext ];

	@try {
        
        NSString * stringData = [[[ NSString alloc] initWithData:data encoding:NSASCIIStringEncoding ] autorelease ];
        
		id ret = [ stringData JSONFragmentValue ];
		
#ifdef DEBUG_JSON
		[[ NSString stringWithFormat:@"Response JSON: (class: %@) %@", [[ ret class ] description ], [ ret description ]] limitedLog ];
#endif
        
        NSLog(@"Calciatori: %@", [ ret description ]);
        
        // Insert squadra selector object
        
        NSManagedObject * newManagedObject = [ NSEntityDescription insertNewObjectForEntityForName:@"Calciatore" inManagedObjectContext:insertContext ];
        
        // In this way object will be the first one
        [ newManagedObject setValue:@"Z" forKey:@"ruolo" ];

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
            
            NSManagedObject * newManagedObject = [ NSEntityDescription insertNewObjectForEntityForName:@"Calciatore" inManagedObjectContext:insertContext ];
            
            [ self mapCalciatore:[ calciatori objectForKey:calciatore ] onEntity:newManagedObject ];
            [ newManagedObject setValue:[ NSNumber numberWithInt:UNSELECTED ] forKey:@"selected" ];
            [ newManagedObject setValue:calciatore forKey:@"id_calciatore" ];

            // Statistiche calciatore
            id statisticheCalciatore = [ statistiche valueForKey:calciatore ];
            id HomeAndAway = [ statisticheCalciatore valueForKey:@"HomeAndAway" ];
            
            [ newManagedObject setValue:[ HomeAndAway valueForKey:@"MV" ] forKey:@"mv" ];
            [ newManagedObject setValue:[ HomeAndAway valueForKey:@"GolFa" ] forKey:@"golfa" ];
            [ newManagedObject setValue:[ HomeAndAway valueForKey:@"GolSu" ] forKey:@"golsu" ];
            [ newManagedObject setValue:[ HomeAndAway valueForKey:@"PI" ] forKey:@"pi" ];

            [ newManagedObject setValue:[[ HomeAndAway valueForKey:@"cartellini" ] description ] forKey:@"cartellini" ];

        }
        
        [ self saveContext:insertContext ];
        
        // [ self loadValues ];

	}
	@catch (NSException * e) {
        NSError * error = [ NSError errorWithDomain:e.name code:100 userInfo:e.userInfo ];
        NSLog(@"Exception: %@", [ error description ]);
	}    
    
    // End core data operation
    [ self endCoreDataOperation:insertContext ];
    
    // [ self performSelectorOnMainThread:@selector(performFetch) withObject:nil waitUntilDone:NO ];
}

- (void) viewDidAppear:(BOOL)animated
{
    [ super viewDidAppear:animated ];

#ifdef NO_REFRESH_DATA
    return;
#endif

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

- (void)viewDidDisappear:(BOOL)animated
{
    [ formazioni release ];
    
    self.managedObjectContext = nil;
    self.fetchedResultsController = nil;
    
    [ super viewDidDisappear:animated ];
}

- (void) viewDidUnload
{
    [ ruoliPanchinari release ];
    [ id_giornata release ];
    [ ngiornata release ];
    
    free( dueTitolari );
    free( dueRiserve );
    free( selectedTitolari );
    free( selectedRiserve );
    
    [ super viewDidUnload ];
}
    
- (void)viewDidLoad
{    
    [ super viewDidLoad ];
    
    dueTitolari = (NSInteger*) malloc(sizeof(NSInteger) * 4);
    dueRiserve = (NSInteger*) malloc(sizeof(NSInteger) * 4);
    selectedTitolari = (NSInteger*) malloc(sizeof(NSInteger) * 4);
    selectedRiserve = (NSInteger*) malloc(sizeof(NSInteger) * 4);
    for ( int i = 0; i < 4; i ++ ) {
        dueTitolari[ i ] = 0;
        dueRiserve[ i ] = 0;
        selectedTitolari[ i ] = 0;
        selectedRiserve[ i ] = 0;
    }

    [ self setBackgroundOfTableView:[ UIImage imageNamed:@"body.png" ]];
    
    [ self performFetch ];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if ( indexPath.section == 0 ) {
        UITableViewCell *cell = [ tableView dequeueReusableCellWithIdentifier:@"FirstCell" ];
        if (cell == nil) {
            cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"FirstCell"] autorelease];
        }
        [ self configureCell:cell atIndexPath:indexPath ];
        return cell;
    }
    else {
        UITableViewCell *cell = [ tableView dequeueReusableCellWithIdentifier:@"Cell" ];
        if (cell == nil) {
            cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"Cell"] autorelease];
        }
        [ self configureCell:cell atIndexPath:indexPath ];
        return cell;
    }
}

- (void)configureCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath 
{
    NSManagedObject * object = [ fetchedResultsController objectAtIndexPath:indexPath ];
    
    int num = 0;
    int tot = 0;
    
    if ( indexPath.section == 0 ) {
        
        // return;
        
        NSString * key = [[ formazioni allKeys ] objectAtIndex:selectedFormazione ];
        NSString * formazione = [ formazioni valueForKey:key ];
        NSArray * components = [ formazione componentsSeparatedByString:@"-" ];

        UILabel * label;
        for ( UIView * subview in cell.contentView.subviews ) {
            switch ([ subview tag ]) {
                case 55:
                    label = (UILabel*) subview;
                    label.text = formazione;
                    break;
                    
                case 50:                    
                    label = (UILabel*) subview;
                    label.text = ngiornata;
                    break;
                    
                // Titolari
                case 500:
                    label = (UILabel*) subview;
                    num = selectedTitolari[0];
                    tot = dueTitolari[0];
                    label.text = [ NSString stringWithFormat:@"%d/%d", num, tot ];
                    break;
                    
                case 501:
                    label = (UILabel*) subview;
                    num = selectedTitolari[1];
                    tot = dueTitolari[1];
                    label.text = [ NSString stringWithFormat:@"%d/%d", num, tot ];
                    break;

                case 502:
                    label = (UILabel*) subview;
                    num = selectedTitolari[2];
                    tot = dueTitolari[2];
                    label.text = [ NSString stringWithFormat:@"%d/%d", num, tot ];
                    break;

                case 503:
                    label = (UILabel*) subview;
                    num = selectedTitolari[3];
                    tot = dueTitolari[3];
                    label.text = [ NSString stringWithFormat:@"%d/%d", num, tot ];
                    break;

                // Riserve
                case 600:
                    label = (UILabel*) subview;
                    num = selectedRiserve[0];
                    tot = dueRiserve[0];
                    num = [[ self calciatoriWithRuolo:@"P" andState:SELECTED_PANCHINARO ] count ];
                    tot = 0;
                    label.text = [ NSString stringWithFormat:@"%d/%d", num, tot ];
                    break;

                case 601:
                    label = (UILabel*) subview;
                    num = selectedRiserve[1];
                    tot = dueRiserve[1];
                    label.text = [ NSString stringWithFormat:@"%d/%d", num, tot ];
                    break;
                    
                case 602:
                    label = (UILabel*) subview;
                    num = selectedRiserve[2];
                    tot = dueRiserve[2];
                    label.text = [ NSString stringWithFormat:@"%d/%d", num, tot ];
                    break;
                    
                case 603:
                    label = (UILabel*) subview;
                    num = selectedRiserve[3];
                    tot = dueRiserve[3];
                    label.text = [ NSString stringWithFormat:@"%d/%d", num, tot ];
                    break;

                    
            }
        }
        
    }
    else {

        UILabel * label;
        OHAttributedLabel * aLabel;
        UIImageView * imageView;
        NSString * cartellini;
        UIImage * image;
        
        int next = [[ object valueForKey:@"selected" ] intValue ];
        if ( next == SELECTED_TITOLARE ) {
            cell.backgroundColor = [ UIColor greenColor ];
        }
        if ( next == SELECTED_PANCHINARO ) {
            cell.backgroundColor = [ UIColor lightGrayColor ];
        }
        if ( next == UNSELECTED ) {
            cell.backgroundColor = [ UIColor whiteColor ];
        }
        
        // NSLog(@"Icona: %@", [ object valueForKey:@"iconatitolarita" ]);
        
        for ( UIView * subview in cell.contentView.subviews ) {
            switch ([ subview tag ]) {
                case 0:
                    label = (UILabel*) subview;
                    label.text = [ object valueForKey:@"calciatore" ];
                    break;
                    
                case 1:
                    @try {
                        aLabel = (OHAttributedLabel*) subview;
                        NSString * string = (NSString*)[ object valueForKey:@"partita" ];
                        NSMutableAttributedString * aString = [[ NSMutableAttributedString alloc ] initWithString:string ];
                        [ aString setFont:[ UIFont systemFontOfSize:12.0 ]];
                        NSRange begin = [ string rangeOfString:@"<strong>" ];
                        NSRange end = [ string rangeOfString:@"</strong>" ];
                        NSRange bold = NSMakeRange(begin.location + begin.length, end.location - (begin.location + begin.length));
                        // NSLog(@"%@ Line: %@", NSStringFromRange(bold), string);
                        [ aString setTextBold:YES range:bold ];
                        [ aString replaceCharactersInRange:end withString:@"" ];
                        [ aString replaceCharactersInRange:begin withString:@"" ];
                        aLabel.attributedText = aString;
                    }
                    @catch (NSException *exception) {
                        NSLog(@"Exception: %@", [ exception description ]);
                    }
                    break;
                    
                case 2:
                    label = (UILabel*) subview;
                    label.text = [ object valueForKey:@"mv" ];
                    break;

                // Cartellini
                case 3:
                    label = (UILabel*) subview;
                    cartellini = [[ object valueForKey:@"cartellini" ] description ];
                    if ( [ cartellini intValue ] != 0 ) {
                        label.text = cartellini;
                    }
                    else {
                        label.text = @"";
                    }
                    break;

                case 4:
                    label = (UILabel*) subview;
                    if ( [[ object valueForKey:@"ruolo" ] isEqualToString:@"P" ] ) {
                        NSString * golsu = [[ object valueForKey:@"golsu" ] description ];
                        if ( [ golsu intValue ] != 0 ) {
                            label.text = golsu;
                        }
                        else {
                            label.text = @"";
                        }
                    }
                    else {
                        NSString * golfa = [[ object valueForKey:@"golfa" ] description ];
                        if ( [ golfa intValue ] != 0 ) {
                            label.text = [[ object valueForKey:@"golfa" ] description ];
                        }
                        else {
                            label.text = @"";
                        }
                    }
                    break;

                // Right icon
                case 5:
                    imageView = (UIImageView*) subview;
                    NSString * stato = [ object valueForKey:@"stato" ];
                    NSString * iconName = [ NSString stringWithFormat:@"freccetta_stato_%@.png", stato ];
                    image = [ UIImage imageNamed:iconName ];
                    [ imageView setImage:image ];
                    break;

                case 6:
                    imageView = (UIImageView*) subview;
                    image = [ UIImage imageNamed:[ object valueForKey:@"iconatitolarita" ]];
                    [ imageView setImage:image ];
                    break;
                    
                case 100:
                    imageView = (UIImageView*) subview;
                    cartellini = [[ object valueForKey:@"cartellini" ] description ];
                    if ( [ cartellini intValue ] != 0 ) {
                        imageView.hidden = NO;
                    }
                    else {
                        imageView.hidden = YES;
                    }
                    break;
                    
                case 200:
                    imageView = (UIImageView*) subview;
                    if ( [[ object valueForKey:@"ruolo" ] isEqualToString:@"P" ] ) {
                        NSString * golsu = [[ object valueForKey:@"golsu" ] description ];
                        if ( [ golsu intValue ] != 0 ) {
                            imageView.hidden = NO;
                        }
                        else {
                            imageView.hidden = YES;
                        }
                    }
                    else {
                        NSString * golfa = [[ object valueForKey:@"golfa" ] description ];
                        if ( [ golfa intValue ] != 0 ) {
                            imageView.hidden = NO;
                        }
                        else {
                            imageView.hidden = YES;
                        }
                    }                    
                    break;
                    
            }
        }
    }
}

#pragma mark -
#pragma mark Table view delegate

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    if ( section == 0 )
        return 0.0;
    return 40.0;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UILabel * label = [[ UILabel alloc ] initWithFrame:CGRectMake(0, 0, 300, 40 ) ];
    
    label.textColor = [ UIColor whiteColor ];
    label.textAlignment = UITextAlignmentCenter;
    label.font = [ UIFont boldSystemFontOfSize:18 ];
    label.backgroundColor = [ UIColor clearColor ];
    
	id <NSFetchedResultsSectionInfo> sectionInfo = [[fetchedResultsController sections] objectAtIndex:section];
    if ( [[ sectionInfo indexTitle ] isEqualToString:@"A" ] ) {
        label.text = @"Attaccanti";
    }
    if ( [[ sectionInfo indexTitle ] isEqualToString:@"D" ] ) {
        label.text = @"Difensori";
    }
    if ( [[ sectionInfo indexTitle ] isEqualToString:@"C" ] ) {
        label.text = @"Centrocampisti";
    }
    if ( [[ sectionInfo indexTitle ] isEqualToString:@"P" ] ) {
        label.text = @"Portieri";
    }
    return [ label autorelease ];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if ( indexPath.section == 0 )
        return 151.0;
    return 30.0;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath 
{
	[ tableView deselectRowAtIndexPath:indexPath animated:YES ];
    
    if ( indexPath.section == 0 ) {
        selectedFormazione ++;
        if ( selectedFormazione >= [[ formazioni allKeys ] count ] )
            selectedFormazione = 0;
        [ self.tableView reloadData ];
        // [ self configureCell:[ self.tableView cellForRowAtIndexPath:indexPath] atIndexPath:indexPath ];
    }
    else {
        NSManagedObject * calciatore = [[ self fetchedResultsController] objectAtIndexPath:indexPath ];
        // NSLog(@"Selected: %@", [ calciatore description ]);
        
        int type = 0;
        if ( [[ calciatore valueForKey:@"ruolo" ] isEqualToString:@"D" ]) { type = 1; }
        else if ( [[ calciatore valueForKey:@"ruolo" ] isEqualToString:@"C" ]) { type = 2; }
        else if ( [[ calciatore valueForKey:@"ruolo" ] isEqualToString:@"A" ]) { type = 3; }
        
        int next = [[ calciatore valueForKey:@"selected" ] intValue ];
        
        if ( next == 0 ) {
            selectedTitolari[ type ] ++;
            next = 1;
        }
        else if ( next == 1 ) {
            selectedTitolari[ type ] --;
            selectedRiserve[ type ] ++;
            next = 2;
        }
        else if ( next == 2 ) {
            selectedRiserve[ type ] --;
            next = 0;
        }
        
        [ calciatore setValue:[ NSNumber numberWithInt:next ] forKey:@"selected" ];
        
        // [ self configureCell:[ self.tableView cellForRowAtIndexPath:indexPath] atIndexPath:indexPath ];
        // NSIndexPath * firstIndex = [ NSIndexPath indexPathForRow:0 inSection:0 ];
        // [ self configureCell:[ self.tableView cellForRowAtIndexPath:firstIndex] atIndexPath:firstIndex ];
        
        [ self.tableView reloadData ];
        
    }
}

@end
