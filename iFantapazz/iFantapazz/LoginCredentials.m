//
//  LoginCredentials.m
//  iFantapazz
//
//  Created by Michele Mastrogiovanni on 29/02/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "LoginCredentials.h"
#import "Constants.h"

@interface LoginCredentials (PrivateMethods)

- (void) loadPicture;

@end

@implementation LoginCredentials

@synthesize uuid;
@synthesize name;
@synthesize email;
@synthesize status;
@synthesize pictureURL;
@synthesize picture;

- (void) loadPicture
{
    NSAutoreleasePool * pool = [[ NSAutoreleasePool alloc ] init ];
    @try {
        NSString * completeURL = [ NSString stringWithFormat:@"%@/%@", FANTAPAZZ_URL, self.pictureURL ];
        NSURL *url = [ NSURL URLWithString:completeURL ];
        self.picture = [ UIImage imageWithData:[ NSData dataWithContentsOfURL:url ]];
    }
    @catch (NSException *exception) {
        NSLog(@"Cannot load image: %@", [ exception description ]);
    }
    [ pool release ];
}

- (id) initWithDictionary:(NSDictionary*) dict
{
    self = [ super init ];
    if ( self ) {
        self.uuid = [ dict valueForKey:@"uid" ];
        self.name = [ dict valueForKey:@"name" ];
        self.email = [ dict valueForKey:@"mail" ];
        self.status = [ dict valueForKey:@"status" ];
        self.pictureURL = [ dict valueForKey:@"picture" ];
        [ NSThread detachNewThreadSelector:@selector(loadPicture) toTarget:self withObject:nil ];
    }
    return self;
}

- (NSString*) description
{
    NSArray * keys = [ NSArray arrayWithObjects:@"uuid", @"name", @"email", @"status", @"pictureURL", nil ];
    NSArray * objs = [ NSArray arrayWithObjects:self.uuid, self.name, self.email, self.status, self.pictureURL, nil ];
    return [[ NSDictionary dictionaryWithObjects:objs forKeys:keys ] description ];
}

@end
