//
//  CalcialtoriViewController1.h
//  iFantapazz
//
//  Created by Michele Mastrogiovanni on 08/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MenuTabBarController.h"
#import "PlayerAssignmentController.h"

@interface CalciatoriViewController1 : UITableViewController
{
    PlayerAssignmentController * delegate;
    int index;
    NSString * role;
    NSArray * players;
}

@property (nonatomic, retain) NSString * role;
@property (nonatomic, retain) NSArray * players;
@property (nonatomic, retain) PlayerAssignmentController * delegate;
@property (nonatomic, assign) int index;

@end
