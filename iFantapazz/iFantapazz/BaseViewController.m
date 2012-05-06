//
//  BaseViewController.m
//  iFantapazz
//
//  Created by Michele Mastrogiovanni on 02/03/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "BaseViewController.h"

@implementation BaseViewController

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

- (void) showAlert:(NSString*) message
{
    UIAlertView *msg = [[UIAlertView alloc] initWithTitle:@"Attenzione"
                                                  message:message
                                                 delegate:nil
                                        cancelButtonTitle:@"OK"
                                        otherButtonTitles:nil ];
    [ msg show ];
}

- (void) showNotification:(NSString*) message withTitle:(NSString*) title
{
    UIAlertView *msg = [[UIAlertView alloc] initWithTitle:title
                                                  message:message
                                                 delegate:nil
                                        cancelButtonTitle:@"OK"
                                        otherButtonTitles:nil ];
    [ msg show ];
}

#pragma mark - View lifecycle

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

@end
