//
//  AppDelegate.h
//  iFantapazz
//
//  Created by Michele Mastrogiovanni on 24/02/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <CoreData/CoreData.h>
#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property (readonly, strong, nonatomic) NSManagedObjectContext *managedObjectContext;
@property (readonly, strong, nonatomic) NSManagedObjectModel *managedObjectModel;
@property (readonly, strong, nonatomic) NSPersistentStoreCoordinator *persistentStoreCoordinator;

- (void)saveContext;

@end
