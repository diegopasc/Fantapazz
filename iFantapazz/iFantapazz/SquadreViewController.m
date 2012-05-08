//
//  SquadreViewController.m
//  iFantapazz
//
//  Created by Michele Mastrogiovanni on 29/02/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "SquadreViewController.h"
#import "Constants.h"
#import "NSString+SBJSON.h"

#import "UITableViewController+Additions.h"

@interface SquadreViewController (PrivateMethods)

- (NSFetchRequest*) mainFetchRequest;

- (void)configureCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath;

@end

@implementation SquadreViewController

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [ super viewDidLoad ];
    
    // Setup background
    [ self setBackgroundOfTableView:[ UIImage imageNamed:@"body.png" ]];
    
    self.title = @"Squadre";
}

- (void) viewDidDisappear:(BOOL)animated
{
    self.fetchedResultsController = nil;
}

- (void) viewDidAppear:(BOOL)animated
{
    [ super viewDidAppear:animated ];

    // Perform core data fetch
    [ self performFetch ];
    
    [ self.tableView reloadData ];

#ifdef NO_REFRESH_DATA
    NSLog(@"Skip");
    return;
#endif
    
    [ self startHUDUndeterminate:@"Loading..." ];
    
    NSString * userID = (NSString*) [[ NSUserDefaults standardUserDefaults ] objectForKey:@"fantapazz.login.uuid" ];
    
    NSString * loginURL = [ NSString stringWithFormat:@"%@/servizi/getSquadreGestibiliByUid?token=%@&uid=%@",
                           FANTAPAZZ_URL,
                           FANTAPAZZ_API_TOKEN,
                           userID ];
    
	NSMutableURLRequest *request = [[[ NSMutableURLRequest alloc ] init ] autorelease ];
	[ request setURL:[ NSURL URLWithString:loginURL ]];
	[ request setHTTPMethod:@"GET" ];
    
    Connector * connector = [[ Connector alloc ] initWithDelegate:self ];
    [ connector startRequest:request withContext:nil ];
    [ connector release ];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

#pragma - Private methods

- (NSFetchRequest*) mainFetchRequest
{
    // Create and configure a fetch request with the Book entity.
	NSFetchRequest * fetchRequest = [[ NSFetchRequest alloc] init ];
	
	NSEntityDescription * entity = [ NSEntityDescription entityForName:@"Squadra" inManagedObjectContext:managedObjectContext ];
	[ fetchRequest setEntity:entity ];
	
	// Create the sort descriptors array.
    NSSortDescriptor *nameSort = [[ NSSortDescriptor alloc ] initWithKey:@"name" ascending:YES ];
    [ fetchRequest setSortDescriptors:[ NSArray arrayWithObject:nameSort ]];
	
	// Predicate selector
    //	NSPredicate * predicate = [ NSPredicate predicateWithFormat:@"name LIKE '%'" ];
    //	[ fetchRequest setPredicate:predicate ];
    
    [ nameSort release ];
    
    return [ fetchRequest autorelease ];
}

#pragma mark - Table delegate

- (void)configureCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
    NSManagedObject * object = [ fetchedResultsController objectAtIndexPath:indexPath ];
    cell.textLabel.text = [ object valueForKey:@"name" ];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] autorelease];
    }
    
    [ self configureCell:cell atIndexPath:indexPath ];
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSManagedObject * object = [ fetchedResultsController objectAtIndexPath:indexPath ];
    
    [[ NSUserDefaults standardUserDefaults ] setObject:[ object valueForKey:@"uid" ] forKey:@"fantapazz.login.id_squadra" ];
    
    [[ NSUserDefaults standardUserDefaults ] synchronize ];
    
    // Continue GUI
    [ self performSegueWithIdentifier:@"SquadraChosen" sender:self ];
}

#pragma mark - Connector Delegate

- (void) connector:(Connector*)aConnector success:(NSData*) data withContext:(id) aContext
{    
    // Begin core data operation
    NSManagedObjectContext * insertContext = [ self beginCoreDataOperation ];
    
    // Clear database
    [ self clearAllEntity:@"Squadra" inContext:insertContext ];
    
	@try {
        NSString * stringData = [[[ NSString alloc] initWithData:data encoding:NSASCIIStringEncoding ] autorelease ];
        
        stringData = [ stringData stringByReplacingOccurrencesOfString:@"\\x3c" withString:@"<" ];
        stringData = [ stringData stringByReplacingOccurrencesOfString:@"\\x3e" withString:@">" ];
        stringData = [ stringData stringByReplacingOccurrencesOfString:@"\\'" withString:@"'" ];
        
		id ret = [ stringData JSONFragmentValue ];
		
#ifdef DEBUG_JSON
		[[ NSString stringWithFormat:@"Response JSON: (class: %@) %@", [[ ret class ] description ], [ ret description ]] limitedLog ];
#endif
        
        if (![[ ret valueForKey:@"status" ] boolValue ]) {
            
            NSLog(@"Message: %@", [ ret valueForKey:@"msg" ] );
            
            UIAlertView *message = [[UIAlertView alloc] initWithTitle:@"Attenzione"
                                                              message:[ ret valueForKey:@"msg" ]
                                                             delegate:nil
                                                    cancelButtonTitle:@"OK"
                                                    otherButtonTitles:nil];
            [ message show ];
            
        }
        else {
            
            NSLog(@"Final %@", [ ret description ]);
            
            id squadre = [ ret valueForKey:@"data" ];
            for ( id squadra in squadre ) {
                
                NSLog(@"Squadra: %@", [ squadra description ]);
                
                NSManagedObject * newManagedObject = [ NSEntityDescription insertNewObjectForEntityForName:@"Squadra" inManagedObjectContext:insertContext ];
                
                [ newManagedObject setValue:[ squadra valueForKey:@"nome" ] forKey:@"name" ];
                [ newManagedObject setValue:[[ squadra valueForKey:@"ID" ] description ] forKey:@"uid" ];
                
            }
         
            [ self saveContext:insertContext ];
            
        }
                
	}
	@catch (NSException * e) {
        NSError * error = [ NSError errorWithDomain:e.name code:100 userInfo:e.userInfo ];
        NSLog(@"Exception: %@", [ error description ]);
	}    
    
    // End core data operation
    [ self endCoreDataOperation:insertContext ];

    // Hide loading panel
    [ self stopHUDUndeterminate ];
}

- (void) connector:(Connector*)aConnector fail:(NSError*) error withContext:(id) aContext
{
    // Hide loading panel
    [ self stopHUDUndeterminate ];
    
    NSLog(@"Fail loading");
}

@end
