//
//  BaseViewController.h
//  iFantapazz
//
//  Created by Michele Mastrogiovanni on 02/03/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreData/CoreData.h>
#import "MBProgressHUD.h"

@interface BaseViewController : UITableViewController <MBProgressHUDDelegate>
{
    MBProgressHUD * HUD;
}

- (void) startHUDUndeterminate:(NSString*) message;

- (void) stopHUDUndeterminate;

- (void) showAlert:(NSString*) message;

- (void) showNotification:(NSString*) message withTitle:(NSString*) title;

@end
