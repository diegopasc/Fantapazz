//
//  SquadreViewController1.m
//  iFantapazz
//
//  Created by Michele Mastrogiovanni on 08/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "SquadreViewController1.h"
#import "NSString+SBJSON.h"
#import "UITableViewController+Additions.h"
#import "MenuTabBarController.h"
#import "Constants.h"
#import "GANTracker.h"

@implementation SquadreViewController1

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        squadre = [[[ NSMutableArray alloc ] init ] retain ];
    }
    return self;
}

- (void) dealloc
{
    [ squadre release ];
    [ super dealloc ];
}

#pragma mark - Actions

- (void) refreshPropertyList
{
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

#pragma mark - Connector delegate

- (void) connector:(Connector*)aConnector success:(NSData*) data withContext:(id) context
{
	@try {
        
        NSMutableArray * newSquadre = [[ NSMutableArray alloc ] init ];
        
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
            
            id _squadre = [ ret valueForKey:@"data" ];
            for ( id _squadra in _squadre ) {
                
                NSLog(@"Squadra: %@", [ _squadra description ]);

                NSDictionary * dict = [ NSDictionary dictionaryWithObjectsAndKeys:[ _squadra valueForKey:@"nome" ], @"name", [[ _squadra valueForKey:@"ID" ] description ], @"uid", nil ];
                [ newSquadre addObject:dict ];
                                
            }
            
            [ squadre release ];
            squadre = [ newSquadre retain ];
            [ self.tableView reloadData ];
            
        }
        
	}
	@catch (NSException * e) {
        NSError * error = [ NSError errorWithDomain:e.name code:100 userInfo:e.userInfo ];
        NSLog(@"Exception: %@", [ error description ]);
	}    
        
    // Hide loading panel
    [ self stopHUDUndeterminate ];
}

- (void) connector:(Connector*)aConnector fail:(NSError*) error withContext:(id) context
{
    // Hide loading panel
    [ self stopHUDUndeterminate ];
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];

    self.title = @"Squadre";
    
    self.clearsSelectionOnViewWillAppear = NO;
    
    [ self setBackgroundOfTableView:[ UIImage imageNamed:@"Background1.png" ]];
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
}

- (void)viewDidAppear:(BOOL)animated
{
    [ super viewDidAppear:animated ];
    
    NSError * error;
    if (![[GANTracker sharedTracker] trackPageview:@"/squadre"
                                         withError:&error]) {
        NSLog(@"error in trackPageview");
    }
    
    [ self refreshPropertyList ];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
}

- (void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [ squadre count ];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] autorelease];
    }
    
    cell.textLabel.text = [[ squadre objectAtIndex:indexPath.row ] valueForKey:@"name" ];
    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    
    return cell;
}

/*
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the specified item to be editable.
    return YES;
}
*/

/*
// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source
        [tableView deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationFade];
    }   
    else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
    }   
}
*/

/*
// Override to support rearranging the table view.
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath
{
}
*/

/*
// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}
*/

#pragma mark - Table view delegate

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    MenuTabBarController * controller = (MenuTabBarController*) segue.destinationViewController;
    controller.needsReload = YES;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSDictionary * squadra = [ squadre objectAtIndex:indexPath.row ];
    
    [[ NSUserDefaults standardUserDefaults ] setObject:[ squadra valueForKey:@"uid" ] forKey:@"fantapazz.login.id_squadra" ];
    
    [[ NSUserDefaults standardUserDefaults ] synchronize ];
    
    // Continue GUI
    [ self performSegueWithIdentifier:@"SquadraChosen" sender:self ];
}

@end
