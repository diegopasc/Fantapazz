//
//  BaseFetchViewController.m
//  iFantapazz
//
//  Created by Michele Mastrogiovanni on 06/03/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "BaseFetchViewController.h"
#import "AppDelegate.h"

@interface BaseFetchViewController (PrivateMethods)

- (void)configureCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath;

- (NSFetchRequest*) mainFetchRequest;

@end

@implementation BaseFetchViewController

@synthesize managedObjectContext;
@synthesize fetchedResultsController;

- (void)mergeChanges:(NSNotification *)notification
{
    [ self.managedObjectContext performSelectorOnMainThread:@selector(mergeChangesFromContextDidSaveNotification:)	
                                                 withObject:notification
                                              waitUntilDone:YES];	
}

- (void)didReceiveMemoryWarning
{
    self.fetchedResultsController = nil;
    [ super didReceiveMemoryWarning ];
}

- (NSManagedObjectContext*) beginCoreDataOperation
{
    // Create managed context
    NSManagedObjectContext * insertContext = [[ NSManagedObjectContext alloc ] init ];
    [ insertContext setUndoManager:nil ];
    [ insertContext setPersistentStoreCoordinator:self.managedObjectContext.persistentStoreCoordinator ];
    
    //    // Register context with the notification center
    NSNotificationCenter *nc = [ NSNotificationCenter defaultCenter ];
    [ nc addObserver:self 
            selector:@selector(mergeChanges:) 
                name:NSManagedObjectContextDidSaveNotification
              object:insertContext ];
    
    return insertContext;
}

- (void) endCoreDataOperation:(NSManagedObjectContext *)aContext
{
    NSNotificationCenter *nc = [ NSNotificationCenter defaultCenter ];
    
    // De-register context with the notification center
    [ nc removeObserver:self name:NSManagedObjectContextDidSaveNotification object:aContext ];
    
    [ aContext release ];
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.managedObjectContext = ((AppDelegate*)[ UIApplication sharedApplication ].delegate).managedObjectContext;
}

- (void) viewDidUnload
{
    self.managedObjectContext = nil;
    self.fetchedResultsController = nil;
    [ super viewDidUnload ];
}

- (void) performFetch
{
    NSError *error;
	if (![[ self fetchedResultsController ] performFetch:&error ]) {
		// Update to handle the error appropriately.
		NSLog(@"Unresolved error %@, %@", error, [error userInfo]);
		exit(-1);  // Fail
	}
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

#pragma mark - Private methods

- (void)configureCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
    @throw [ NSException exceptionWithName:@"Not implemented" reason:nil userInfo:nil ];
}

- (NSString*) sectionNameKeyPath
{
    return nil;
}

- (NSFetchRequest*) mainFetchRequest
{
    @throw [ NSException exceptionWithName:@"Not implemented" reason:nil userInfo:nil ];
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
	return [[ fetchedResultsController sections ] count ];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
	id <NSFetchedResultsSectionInfo> sectionInfo = [[fetchedResultsController sections] objectAtIndex:section];
	int rows = [ sectionInfo numberOfObjects ];
	return rows;
}

#pragma mark - Core Data methods

- (void) clearAllEntity:(NSString*) entityName inContext:(NSManagedObjectContext *) deleteContext
{
    // Set up the fetched results controller.
    // Create the fetch request for the entity.
    NSFetchRequest * fetchRequest = [[ NSFetchRequest alloc ] init ];
    
    // Edit the entity name as appropriate.
    NSEntityDescription *entity = [ NSEntityDescription entityForName:entityName inManagedObjectContext:deleteContext ];
    [ fetchRequest setEntity:entity ];
    
    [ fetchRequest setSortDescriptors:[ NSArray array ]];
    
    NSError *error = nil;
    
    NSArray * list = [ deleteContext executeFetchRequest:fetchRequest error:& error ];
    
    if ( error != nil ) {
        NSLog(@"Error getting old calciatori: %@, %@", error, [ error userInfo ]);
    }
    
    for ( NSManagedObject * object in list ) {
        NSLog(@"Removing: %@", [ object description ]);
        [ deleteContext deleteObject:object ];
    }
    
}

- (void) saveContext:(NSManagedObjectContext*) aContext
{    
    NSError * error = nil;
    if ( ! [ aContext save:&error ] ) {
        NSLog(@"Unresolved error %@, %@", error, [ error userInfo ]);
        [ aContext reset ];
    }
}

#pragma mark -
#pragma mark Fetched results controller

/**
 Returns the fetched results controller. Creates and configures the controller if necessary.
 */
- (NSFetchedResultsController *)fetchedResultsController 
{
    if (fetchedResultsController != nil)
        return fetchedResultsController;
	
	// Create and initialize the fetch results controller.
	NSFetchedResultsController *aFetchedResultsController = [[NSFetchedResultsController alloc] initWithFetchRequest:[ self mainFetchRequest ] 
																								managedObjectContext:managedObjectContext 
																								  sectionNameKeyPath:[ self sectionNameKeyPath ]
																										   cacheName:nil ] ; // @"Root"];
	self.fetchedResultsController = aFetchedResultsController;
	fetchedResultsController.delegate = self;
	
	// Memory management.
	[ aFetchedResultsController release ];
	
	return fetchedResultsController;
}    

/**
 Delegate methods of NSFetchedResultsController to respond to additions, removals and so on.
 */
- (void)controllerWillChangeContent:(NSFetchedResultsController *)controller 
{
	[ self.tableView beginUpdates ];
}

- (void)controller:(NSFetchedResultsController *)controller didChangeObject:(id)anObject atIndexPath:(NSIndexPath *)viewIndexPath forChangeType:(NSFetchedResultsChangeType)type newIndexPath:(NSIndexPath *)newViewIndexPath {
    
	NSIndexPath * indexPath = viewIndexPath;
	NSIndexPath * newIndexPath = newViewIndexPath;
	
	switch(type) {
			
		case NSFetchedResultsChangeInsert:
			[self.tableView insertRowsAtIndexPaths:[NSArray arrayWithObject:newIndexPath] withRowAnimation:UITableViewRowAnimationFade];
			break;
			
		case NSFetchedResultsChangeDelete:
			[self.tableView deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationFade];
			break;
			
		case NSFetchedResultsChangeUpdate:
			[ self configureCell:[ self.tableView cellForRowAtIndexPath:indexPath] atIndexPath:viewIndexPath ];
			break;
			
		case NSFetchedResultsChangeMove:
            //			if ( anObject != editingPot )
            //				[self.tableView deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationFade];
			
			// Reloading the section inserts a new row and ensures that titles are updated appropriately.
			[self.tableView reloadSections:[NSIndexSet indexSetWithIndex:newIndexPath.section] withRowAnimation:UITableViewRowAnimationFade];
			break;
	}
}


- (void)controller:(NSFetchedResultsController *)controller didChangeSection:(id <NSFetchedResultsSectionInfo>)sectionInfo atIndex:(NSUInteger)sectionIndex forChangeType:(NSFetchedResultsChangeType)type {
	
	switch(type) {
			
		case NSFetchedResultsChangeInsert:
			[ self.tableView insertSections:[NSIndexSet indexSetWithIndex:sectionIndex] withRowAnimation:UITableViewRowAnimationFade];
			break;
			
		case NSFetchedResultsChangeDelete:
			[ self.tableView deleteSections:[NSIndexSet indexSetWithIndex:sectionIndex] withRowAnimation:UITableViewRowAnimationFade];
			break;
	}
}


- (void)controllerDidChangeContent:(NSFetchedResultsController *)controller 
{
	// The fetch controller has sent all current change notifications, so tell the table view to process all updates.
	[ self.tableView endUpdates ];
}

@end
