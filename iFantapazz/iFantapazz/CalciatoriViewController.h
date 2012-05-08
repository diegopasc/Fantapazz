//
//  CalciatoriViewController.h
//  iFantapazz
//
//  Created by Michele Mastrogiovanni on 25/02/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreData/CoreData.h>
#import "Connector.h"
#import "BaseFetchViewController.h"

@interface CalciatoriViewController : BaseFetchViewController <ConnectorDelegate>
{
    NSDictionary * formazioni;
    NSInteger selectedFormazione;
    
    NSString * id_giornata;
    NSString * ngiornata;
    NSArray * ruoliPanchinari;
    
    NSInteger * dueTitolari;
    NSInteger * dueRiserve;
    NSInteger * selectedTitolari;
    NSInteger * selectedRiserve;

}

- (IBAction) send:(id) sender;

- (void) validate;

@end
