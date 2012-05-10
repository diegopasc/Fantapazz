//
//  FormazioneViewController.h
//  iFantapazz
//
//  Created by Michele Mastrogiovanni on 10/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface FormazioneViewController : UITableViewController
{
    NSDictionary * formazioni;
    NSInteger selectedFormazione;
}

@property (nonatomic, retain) NSDictionary * formazioni;
@property (nonatomic, assign) NSInteger selectedFormazione;

@end
