//
//  CalcialtoriViewController1.m
//  iFantapazz
//
//  Created by Michele Mastrogiovanni on 08/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "CalciatoriViewController1.h"
#import "OHAttributedLabel.h"
#import "Constants.h"

@implementation CalciatoriViewController1

@synthesize role, players, delegate, index;

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.clearsSelectionOnViewWillAppear = NO;
}

- (void) viewDidUnload
{
    [super viewDidUnload];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
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
    return [ players count ];
}

- (void)configureCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath 
{
    id object = [ players objectAtIndex:indexPath.row ];
    
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
                    NSString * string = (NSString*)[ object valueForKey:@"partita" ];
                    NSMutableAttributedString * aString = [[ NSMutableAttributedString alloc ] initWithString:string ];
                    // [ aString setFont:[ UIFont systemFontOfSize:12.0 ]];
                    NSRange begin = [ string rangeOfString:@"<strong>" ];
                    NSRange end = [ string rangeOfString:@"</strong>" ];
                    // NSRange bold = NSMakeRange(begin.location + begin.length, end.location - (begin.location + begin.length));
                    // NSLog(@"%@ Line: %@", NSStringFromRange(bold), string);
                    // [ aString setTextBold:YES range:bold ];
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
    if ( [[ object valueForKey:@"Ruolo" ] isEqual:@"?" ] ) {
        cell.backgroundColor = COLOR_ALTRO;
    }
    if ( [[ object valueForKey:@"SELECTED" ] isEqual:@"YES" ] ) {
        cell.backgroundColor = COLOR_DISABLED;
    }
    
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] autorelease];
    }
        
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

#pragma mark - Table view delegate

- (NSIndexPath *)tableView:(UITableView *)tableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    id object = [ players objectAtIndex:indexPath.row ];
    if ( [[ object valueForKey:@"SELECTED" ] isEqual:@"YES" ] ) {
        return nil;
    }
    return indexPath;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [ self.delegate selectedPlayer:[ players objectAtIndex:indexPath.row ] atIndex:index ];
    
    [ self.navigationController popViewControllerAnimated:YES ];
    // [[ self presentingViewController ] dismissViewControllerAnimated:YES completion:nil ];
}

@end
