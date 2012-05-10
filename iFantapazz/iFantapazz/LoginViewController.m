//
//  LoginViewController.m
//  iFantapazz
//
//  Created by Michele Mastrogiovanni on 27/02/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "LoginViewController.h"
#import "LoginCredentials.h"
#import "NSString+SBJSON.h"
#import "Constants.h"
#import "SquadreViewController1.h"

#import "UITableViewController+Additions.h"

@implementation LoginViewController

//-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
//    
//    SquadreViewController * controller = (SquadreViewController*) [ segue destinationViewController ];
//    
//}

- (void) connector:(Connector*)aConnector success:(NSData*) data withContext:(id) aContext
{
    [ self stopHUDUndeterminate ];
    
	@try {
        NSString * stringData = [[[ NSString alloc] initWithData:data encoding:NSASCIIStringEncoding ] autorelease ];
        
		id ret = [ stringData JSONFragmentValue ];
		
#ifdef DEBUG_JSON
		[[ NSString stringWithFormat:@"Response JSON: (class: %@) %@", [[ ret class ] description ], [ ret description ]] limitedLog ];
#endif
		
        if ( ! [ ret valueForKey:@"status" ] ) {
            
            [ self showAlert:[ ret valueForKey:@"msg" ]];
            
        }
        else {
            
            NSDictionary * dict = [ ret valueForKey:@"data" ];
            
            LoginCredentials * credentials = [[[ LoginCredentials alloc ] initWithDictionary:dict ] autorelease ];
            
            NSLog(@"Credentials: %@", [ credentials description ]);
            
            // Save last successfull login 
            [[ NSUserDefaults standardUserDefaults ] setObject:username.text forKey:@"fantapazz.login.username" ];
            [[ NSUserDefaults standardUserDefaults ] setObject:password.text forKey:@"fantapazz.login.password" ];
            [[ NSUserDefaults standardUserDefaults ] setObject:credentials.uuid forKey:@"fantapazz.login.uuid" ];
            [[ NSUserDefaults standardUserDefaults ] synchronize ];
            
            // Continue GUI
            [ self performSegueWithIdentifier:@"LoginPerformed" sender:self ];
            
        }
	}
	@catch (NSException * e) {
        NSError * error = [ NSError errorWithDomain:e.name code:100 userInfo:e.userInfo ];
        NSLog(@"Exception: %@", [ error description ]);
        [ self showAlert:@"Problemi di rete, riprova!" ];
	}    
}

- (void) connector:(Connector*)aConnector fail:(NSError*) error withContext:(id) aContext
{
    [ self stopHUDUndeterminate ];
    [ self showAlert:@"Problemi di rete, riprova!" ];
    NSLog(@"Fail: %@", [ error description ]);
}

- (IBAction) login:(id) sender
{    
#ifdef NO_REFRESH_DATA
    
    // Continue GUI
    [ self performSegueWithIdentifier:@"LoginPerformed" sender:self ];
    return;
    
#endif

    [ self startHUDUndeterminate:@"Logging..." ];

    NSString * loginURL = [ NSString stringWithFormat:@"%@/servizi/login?token=%@&user=%@&pass=%@",
                           FANTAPAZZ_URL,
                           FANTAPAZZ_API_TOKEN,
                           username.text,
                           password.text ];
    
    NSMutableURLRequest *request = [[[ NSMutableURLRequest alloc ] init ] autorelease ];
    [ request setURL:[ NSURL URLWithString:loginURL ]];
    [ request setHTTPMethod:@"GET" ];
        
    Connector * connector = [[ Connector alloc ] initWithDelegate:self ];
    [ connector startRequest:request withContext:nil ];
    [ connector release ];
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
        
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.title = @"iFantapazz";
    
    [ self setBackgroundOfTableView:[ UIImage imageNamed:@"Background1.png" ]];

    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
 
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated 
{
	[ super viewWillAppear:animated ];
	// [ username becomeFirstResponder ];
}

- (void)viewDidAppear:(BOOL)animated
{
    [ super viewDidAppear:animated ];
    
    username.clearButtonMode = UITextFieldViewModeAlways;
    password.clearButtonMode = UITextFieldViewModeAlways;
    password.secureTextEntry = YES;
    
    NSString * _username = [[ NSUserDefaults standardUserDefaults ] stringForKey:@"fantapazz.login.username" ];
    NSString * _password = [[ NSUserDefaults standardUserDefaults ] stringForKey:@"fantapazz.login.password" ];
    if ( _username != nil ) {
        username.text = _username;
    }
    if ( _password != nil ) {
        password.text = _password;
    }
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

#pragma mark UITextField Delegate methods

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    
	if ( textField == username ) {
		[ password becomeFirstResponder ];
	}
	if (textField == password) {
        [ password resignFirstResponder ];
	}
	return YES;
}

@end
