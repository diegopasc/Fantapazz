//
//  MenuTabBarController.h
//  iFantapazz
//
//  Created by Michele Mastrogiovanni on 08/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Connector.h"
#import "MBProgressHUD.h"

@interface MenuTabBarController : UITabBarController <ConnectorDelegate>
{
    MBProgressHUD * HUD;

    NSDictionary * formazioni;
    NSInteger selectedFormazione;

    NSString * id_giornata;
    NSString * ngiornata;
    NSArray * ruoliPanchinari;
    
    NSMutableArray * players;
    
    NSInteger * dueTitolari;
    NSInteger * dueRiserve;
    NSInteger * selectedTitolari;
    NSInteger * selectedRiserve;
}

- (void) loadData;

@property (nonatomic, retain) NSMutableArray * players;

@end
