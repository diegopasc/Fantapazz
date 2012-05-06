//
//  LoginCredentials.h
//  iFantapazz
//
//  Created by Michele Mastrogiovanni on 29/02/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface LoginCredentials : NSObject
{
    NSString * uuid;
    NSString * name;
    NSString * email;
    NSString * status;
    NSString * pictureURL;
    UIImage * picture;
}

- (id) initWithDictionary:(NSDictionary*) dict;

@property (nonatomic, retain) NSString * uuid;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSString * email;
@property (nonatomic, retain) NSString * status;
@property (nonatomic, retain) NSString * pictureURL;
@property (nonatomic, retain) UIImage * picture;

@end
