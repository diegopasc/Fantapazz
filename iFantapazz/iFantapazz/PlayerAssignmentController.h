//
//  PlayerAssignmentController.h
//  iFantapazz
//
//  Created by Michele Mastrogiovanni on 09/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface PlayerAssignmentController : UITableViewController
{
    NSMutableArray * placeDescription;
    NSMutableArray * playerAssignement;
    BOOL needsReload;
}

@property (nonatomic, assign) BOOL needsReload;

@property (nonatomic, retain) NSMutableArray * placeDescription;
@property (nonatomic, retain) NSMutableArray * playerAssignement;

- (void) reloadData;

- (void) selectFormatione:(NSArray*) formazione;

- (void) selectedPlayer:(id) player atIndex:(int) index;

- (void) updateBadge;

@end
