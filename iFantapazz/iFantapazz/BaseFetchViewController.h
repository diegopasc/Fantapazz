//
//  BaseFetchViewController.h
//  iFantapazz
//
//  Created by Michele Mastrogiovanni on 06/03/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "BaseViewController.h"

@interface BaseFetchViewController : BaseViewController <UITableViewDataSource, UITableViewDelegate, NSFetchedResultsControllerDelegate>
{
    NSFetchedResultsController *fetchedResultsController;
    NSManagedObjectContext *managedObjectContext;	    
}

@property (nonatomic, retain) NSManagedObjectContext *managedObjectContext;
@property (nonatomic, retain) NSFetchedResultsController *fetchedResultsController;

- (NSManagedObjectContext*) beginCoreDataOperation;

- (void) performFetch;

- (void) endCoreDataOperation:(NSManagedObjectContext*) aContext;

- (NSFetchRequest*) mainFetchRequest;

- (NSString*) sectionNameKeyPath;

- (void) saveContext:(NSManagedObjectContext*) aContext;

- (void) clearAllEntity:(NSString*) entityName inContext:(NSManagedObjectContext *) deleteContext;

@end
