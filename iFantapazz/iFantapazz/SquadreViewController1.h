//
//  SquadreViewController1.h
//  iFantapazz
//
//  Created by Michele Mastrogiovanni on 08/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BaseViewController.h"
#import "Connector.h"

@interface SquadreViewController1 : BaseViewController <UITableViewDataSource, UITableViewDelegate, ConnectorDelegate>
{
    NSMutableArray * squadre;
}

@end
