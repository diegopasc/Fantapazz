//
//  Connector.h
//  iFantapazz
//
//  Created by Michele Mastrogiovanni on 29/02/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

@class Connector;

@protocol ConnectorDelegate 

- (void) connector:(Connector*)aConnector success:(NSData*) data withContext:(id) context;

- (void) connector:(Connector*)aConnector fail:(NSError*) error withContext:(id) context;

@end

@interface Connector : NSObject <NSURLConnectionDataDelegate>
{
    id delegate;
    NSURLConnection * connection;
    NSMutableData * data;
    id context;
}

- (id) initWithDelegate:(id) delegate;

- (void) startRequest:(NSURLRequest*) request withContext:(id) context;

@end
