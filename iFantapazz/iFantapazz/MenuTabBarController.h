//
//  MenuTabBarController.h
//  iFantapazz
//
//  Created by Michele Mastrogiovanni on 08/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "PlayerAssignmentController.h"
#import "Connector.h"
#import "MBProgressHUD.h"

@interface MenuTabBarController : UITabBarController <ConnectorDelegate>
{
    BOOL needsReload;
    
    MBProgressHUD * HUD;

    NSDictionary * formazioni;
    NSInteger selectedFormazione;

    NSString * id_giornata;
    NSString * ngiornata;
    NSArray * ruoliPanchinari;
    
    NSMutableArray * players;
}

- (NSArray*) selectedFormatione:(PlayerAssignmentController*) controller;

@property (nonatomic, retain) NSMutableArray * players;

@property (nonatomic, assign) BOOL needsReload;

- (IBAction) send:(id)sender;

@end
