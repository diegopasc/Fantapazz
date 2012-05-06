//
//  Connector.m
//  iFantapazz
//
//  Created by Michele Mastrogiovanni on 29/02/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "Connector.h"
#import "JSON.h"
#import "NSString+SBJSON.h"

@implementation Connector

- (id) initWithDelegate:(id) aDelegate
{
    self = [ super init ];
    if ( self ) {
        data = [[ NSMutableData alloc ] init ];
        delegate = [ aDelegate retain ];
    }
    return self;
}

- (BOOL)connectionShouldUseCredentialStorage:(NSURLConnection *)connection
{
    return NO;
}

- (void)connection:(NSURLConnection *)connection willSendRequestForAuthenticationChallenge:(NSURLAuthenticationChallenge *)challenge {
    NSURLCredential * credential = [ NSURLCredential credentialWithUser:@"micmastr" password:@"apriti80" persistence:NSURLCredentialPersistencePermanent ];
    [[ challenge sender ] useCredential:credential forAuthenticationChallenge:challenge ];
}

- (void) dealloc
{
    [ data release ];
    [ delegate release ];
    [ super dealloc ];
}

- (void) startRequest:(NSURLRequest*) request withContext:(id) aContext
{
    context = [ aContext retain ];
    connection = [[ NSURLConnection alloc ] initWithRequest:request delegate:self ];
}

- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error
{
    [ delegate connector:self fail:error withContext:context ];
    [ context release ];
    context = nil;
}

- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response
{
    [ data setLength:0 ];
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)aData
{
    [ data appendData:aData ];
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection
{
    [ delegate connector:self success:data withContext:context ];
    [ context release ];
    context = nil;
}

@end
