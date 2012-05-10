//
//  PlayerAssignmentController.m
//  iFantapazz
//
//  Created by Michele Mastrogiovanni on 09/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "PlayerAssignmentController.h"
#import "CalciatoriViewController1.h"
#import "MenuTabBarController.h"
#import "OHAttributedLabel.h"
#import "UITableViewController+Additions.h"
#import "Constants.h"

@interface PlayerAssignmentController (PrivateMethods)

- (void) clearTable;

- (void) addItemWithRole:(NSString*) role;

- (void) assignPlayer:(id) player atIndex:(int) index;

- (void) clearPlayerAtIndex:(int) index;

@end

@implementation PlayerAssignmentController

@synthesize placeDescription;
@synthesize playerAssignement;
@synthesize needsReload;

#pragma mark - Private methods

- (void) clearTable
{
    [ placeDescription removeAllObjects ];
    [ playerAssignement removeAllObjects ];
}

- (void) addItemWithRole:(NSString*) role 
{
    NSMutableDictionary * property = [ NSMutableDictionary dictionaryWithObjectsAndKeys:role, @"role", @"NO", @"assigned", nil ];
    [ placeDescription addObject:property ];
    [ playerAssignement addObject:[ NSNull null ]];
}

- (void) assignPlayer:(id) player atIndex:(int) index 
{
    if ( [[[ placeDescription objectAtIndex:index ] valueForKey:@"assigned" ] isEqual:@"YES" ] ) {
        id oldPlayer = [ playerAssignement objectAtIndex:index ];
        [ oldPlayer setValue:@"NO" forKey:@"SELECTED" ];
    }

    [ player setValue:@"YES" forKey:@"SELECTED" ];

    [[ placeDescription objectAtIndex:index ] setValue:@"YES" forKey:@"assigned" ];
    [ playerAssignement replaceObjectAtIndex:index withObject:player ];
}

- (void) clearPlayerAtIndex:(int) index
{
    [[ placeDescription objectAtIndex:index ] setValue:@"NO" forKey:@"assigned" ];
    // [ self.tableView reloadData ];
}

#pragma mark - View lifecycle

- (void) selectFormatione:(NSArray*) formazione
{
    [ self clearTable ];
    
    for ( int i = 0; i < [ formazione count ]; i ++ ) {
        
        int type = [[ formazione objectAtIndex:i ] integerValue ];
        switch (type) {
            case 1: [ self addItemWithRole:@"P" ]; break;
            case 2: [ self addItemWithRole:@"D" ]; break;
            case 3: [ self addItemWithRole:@"C" ]; break;
            case 4: [ self addItemWithRole:@"A" ]; break;
            case 0: [ self addItemWithRole:@"?" ]; break;
        }
        
    }
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [ super viewDidLoad ];
    
    needsReload = YES;
    placeDescription = [[[ NSMutableArray alloc ] init ] retain ];
    playerAssignement = [[[ NSMutableArray alloc ] init ] retain ];
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
    
    [ self setBackgroundOfTableView:[ UIImage imageNamed:@"Background1.png" ]];
}

- (void)viewDidUnload
{
    [ playerAssignement release ];
    [ placeDescription release ];
    
    [ super viewDidUnload ];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
}

- (void) reloadData
{
    if ( needsReload ) {
        needsReload = NO;
        MenuTabBarController * tab = (MenuTabBarController*) self.tabBarController;
        NSArray * list = [ tab selectedFormatione:self ];
        [ self selectFormatione:list ];
    }
    [ self.tableView reloadData ];
}

- (void) updateBadge
{
    int tot = [ placeDescription count ];
    int assigned = 0;
    for ( int i = 0; i < tot; i ++ )
        if ( [[[ placeDescription objectAtIndex:i ] valueForKey:@"assigned" ] isEqual:@"YES" ] )
            assigned ++;
    
    self.tabBarItem.badgeValue = [ NSString stringWithFormat:@"%d/%d", assigned, tot ];
}

- (void)viewDidAppear:(BOOL)animated
{
    [ super viewDidAppear:animated ];
    
    [ self reloadData ];

    [ self updateBadge ];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
}

- (void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [ placeDescription count ];
}

- (void)configureCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath 
{
    BOOL empty = NO;
    
    id description = [ placeDescription objectAtIndex:indexPath.row ];
    id object = [ playerAssignement objectAtIndex:indexPath.row ];
    if ( [[ description valueForKey:@"assigned" ] isEqual:@"NO" ]) {
        object = [ NSDictionary dictionary ];
        empty = YES;
    }
    
    UILabel * label;
    OHAttributedLabel * aLabel;
    UIImageView * imageView;
    NSString * cartellini;
    UIImage * image;
    
    for ( UIView * subview in cell.contentView.subviews ) {
        switch ([ subview tag ]) {
            case 0:
                label = (UILabel*) subview;
                label.text = [ object valueForKey:@"Calciatore" ];
                break;
                
            case 1:
                @try {
                    aLabel = (OHAttributedLabel*) subview;
                    if ( empty ) {
                        aLabel.attributedText = [[[ NSAttributedString alloc ] init ] autorelease ];
                        break;
                    }
                    NSString * string = (NSString*)[ object valueForKey:@"partita" ];
                    NSMutableAttributedString * aString = [[ NSMutableAttributedString alloc ] initWithString:string ];
                    [ aString setFont:[ UIFont systemFontOfSize:12.0 ]];
                    NSRange begin = [ string rangeOfString:@"<strong>" ];
                    NSRange end = [ string rangeOfString:@"</strong>" ];
                    NSRange bold = NSMakeRange(begin.location + begin.length, end.location - (begin.location + begin.length));
                    // NSLog(@"%@ Line: %@", NSStringFromRange(bold), string);
                    [ aString setTextBold:YES range:bold ];
                    [ aString replaceCharactersInRange:end withString:@"" ];
                    [ aString replaceCharactersInRange:begin withString:@"" ];
                    aLabel.attributedText = aString;
                }
                @catch (NSException *exception) {
                    NSLog(@"Exception: %@", [ exception description ]);
                }
                break;
                
            case 2:
                label = (UILabel*) subview;
                label.text = [ object valueForKey:@"mv" ];
                break;
                
                // Cartellini
            case 3:
                label = (UILabel*) subview;
                cartellini = [[ object valueForKey:@"cartellini" ] description ];
                if ( [ cartellini intValue ] != 0 ) {
                    label.text = cartellini;
                }
                else {
                    label.text = @"";
                }
                break;
                
            case 4:
                label = (UILabel*) subview;
                if ( [[ object valueForKey:@"Ruolo" ] isEqualToString:@"P" ] ) {
                    NSString * golsu = [[ object valueForKey:@"golsu" ] description ];
                    if ( [ golsu intValue ] != 0 ) {
                        label.text = golsu;
                    }
                    else {
                        label.text = @"";
                    }
                }
                else {
                    NSString * golfa = [[ object valueForKey:@"golfa" ] description ];
                    if ( [ golfa intValue ] != 0 ) {
                        label.text = [[ object valueForKey:@"golfa" ] description ];
                    }
                    else {
                        label.text = @"";
                    }
                }
                break;
                
                // Right icon
            case 5:
                imageView = (UIImageView*) subview;
                NSString * stato = [ object valueForKey:@"Stato" ];
                NSString * iconName = [ NSString stringWithFormat:@"freccetta_stato_%@.png", stato ];
                image = [ UIImage imageNamed:iconName ];
                [ imageView setImage:image ];
                break;
                
            case 6:
                imageView = (UIImageView*) subview;
                image = [ UIImage imageNamed:[ object valueForKey:@"iconatitolarita" ]];
                [ imageView setImage:image ];
                break;
                
            case 100:
                imageView = (UIImageView*) subview;
                cartellini = [[ object valueForKey:@"cartellini" ] description ];
                if ( [ cartellini intValue ] != 0 ) {
                    imageView.hidden = NO;
                }
                else {
                    imageView.hidden = YES;
                }
                break;
                
            case 200:
                imageView = (UIImageView*) subview;
                if ( [[ object valueForKey:@"Ruolo" ] isEqualToString:@"P" ] ) {
                    NSString * golsu = [[ object valueForKey:@"golsu" ] description ];
                    if ( [ golsu intValue ] != 0 ) {
                        imageView.hidden = NO;
                    }
                    else {
                        imageView.hidden = YES;
                    }
                }
                else {
                    NSString * golfa = [[ object valueForKey:@"golfa" ] description ];
                    if ( [ golfa intValue ] != 0 ) {
                        imageView.hidden = NO;
                    }
                    else {
                        imageView.hidden = YES;
                    }
                }                    
                break;
                
        }
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] autorelease];
    }
    
    id assignment = [ placeDescription objectAtIndex:indexPath.row ];

    if ( [[ assignment valueForKey:@"role" ] isEqual:@"P" ] ) {
        cell.backgroundColor = COLOR_PORTIERE;
    }
    if ( [[ assignment valueForKey:@"role" ] isEqual:@"D" ] ) {
        cell.backgroundColor = COLOR_DIFENSORE;
    }
    if ( [[ assignment valueForKey:@"role" ] isEqual:@"C" ] ) {
        cell.backgroundColor = COLOR_CENTROCAMPISTA;
    }
    if ( [[ assignment valueForKey:@"role" ] isEqual:@"A" ] ) {
        cell.backgroundColor = COLOR_ATTACCANTE;
    }
    if ( [[ assignment valueForKey:@"role" ] isEqual:@"?" ] ) {
        id object = [ playerAssignement objectAtIndex:indexPath.row ];
        if ( [[ assignment valueForKey:@"assigned" ] isEqual:@"NO" ]) {
            cell.backgroundColor = COLOR_ALTRO;
        }
        else {
            if ( [[ object valueForKey:@"Ruolo" ] isEqual:@"P" ] ) {
                cell.backgroundColor = COLOR_PORTIERE;
            }
            if ( [[ object valueForKey:@"Ruolo" ] isEqual:@"D" ] ) {
                cell.backgroundColor = COLOR_DIFENSORE;
            }
            if ( [[ object valueForKey:@"Ruolo" ] isEqual:@"C" ] ) {
                cell.backgroundColor = COLOR_CENTROCAMPISTA;
            }
            if ( [[ object valueForKey:@"Ruolo" ] isEqual:@"A" ] ) {
                cell.backgroundColor = COLOR_ATTACCANTE;
            }
        }
    }
    
    // cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    
    [ self configureCell:cell atIndexPath:indexPath ];
    
    return cell;
}

/*
 // Override to support conditional editing of the table view.
 - (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
 {
 // Return NO if you do not want the specified item to be editable.
 return YES;
 }
 */

/*
 // Override to support editing the table view.
 - (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
 {
 if (editingStyle == UITableViewCellEditingStyleDelete) {
 // Delete the row from the data source
 [tableView deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationFade];
 }   
 else if (editingStyle == UITableViewCellEditingStyleInsert) {
 // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
 }   
 }
 */

/*
 // Override to support rearranging the table view.
 - (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath
 {
 }
 */

/*
 // Override to support conditional rearranging of the table view.
 - (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
 {
 // Return NO if you do not want the item to be re-orderable.
 return YES;
 }
 */

- (void) selectedPlayer:(id) aPlayer atIndex:(int) index
{
    [ self assignPlayer:aPlayer atIndex:index ];
}

#pragma mark - Table view delegate

- (void) prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    NSIndexPath * indexPath = [ self.tableView indexPathForSelectedRow ];
    
    MenuTabBarController * menuController = (MenuTabBarController*) self.tabBarController;
    
    CalciatoriViewController1 * controller = (CalciatoriViewController1*) segue.destinationViewController;
    
    id assignment = [ placeDescription objectAtIndex:indexPath.row ];
    
    NSString * role = [ assignment valueForKey:@"role" ];
    
    controller.role = role;
    controller.delegate = self;
    controller.index = indexPath.row;
    
    if ( [ role isEqual:@"?" ] ) {
        
        NSMutableArray * list = [[[ NSMutableArray alloc ] initWithCapacity:[ menuController.players count ]] autorelease ];
        
        [ list addObjectsFromArray:[ menuController.players filteredArrayUsingPredicate:[ NSPredicate predicateWithFormat:@"Ruolo = 'P'" ]]];
        
        [ list addObjectsFromArray:[ menuController.players filteredArrayUsingPredicate:[ NSPredicate predicateWithFormat:@"Ruolo = 'D'" ]]];
        
        [ list addObjectsFromArray:[ menuController.players filteredArrayUsingPredicate:[ NSPredicate predicateWithFormat:@"Ruolo = 'C'" ]]];
        
        [ list addObjectsFromArray:[ menuController.players filteredArrayUsingPredicate:[ NSPredicate predicateWithFormat:@"Ruolo = 'A'" ]]];

        controller.players = list;
        
    }
    else {
        NSPredicate *bPredicate = [ NSPredicate predicateWithFormat:@"Ruolo = %@", role ];
        controller.players = [ menuController.players filteredArrayUsingPredicate:bPredicate ];
    }
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [ self performSegueWithIdentifier:@"SelectPlayer" sender:self ];
}

@end
