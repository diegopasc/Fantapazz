//
//  UITableViewController+Additions.m
//  iCalcio
//
//  Created by i4mob Team on 29/01/10.
//  Copyright 2010 i4mob. All rights reserved.
//

#import "UITableViewController+Additions.h"


@implementation UITableViewController (Additions)

- (void) setBackgroundOfTableView:(UIImage*)image
{
	self.tableView.backgroundColor = [ UIColor clearColor ];
	
	UIView * view = self.navigationController.view;
    
    view.backgroundColor = [ UIColor colorWithPatternImage:image ];
	
//    // Override point for customization after app launch    
//	UIWindow * window = view.window;
//	
//	// Remove view
//	[ self.view removeFromSuperview ];
//	
//	// Add subview
//	UIView *backgroundView = [[ UIView alloc] initWithFrame:window.frame ];
//	backgroundView.backgroundColor = [UIColor colorWithPatternImage:image ];
//	[ window addSubview:backgroundView ];
//	[ backgroundView release ];
//	
//	// Add table view
//	[ window addSubview:view ];
}

@end
