//
//  LoginViewController.h
//  iFantapazz
//
//  Created by Michele Mastrogiovanni on 27/02/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Connector.h"
#import "BaseViewController.h"

@interface LoginViewController : BaseViewController <ConnectorDelegate>
{
    IBOutlet UITextField * username;
    IBOutlet UITextField * password;
}

- (IBAction) login:(id) sender;

@end
