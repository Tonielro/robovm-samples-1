//
//  ViewController.m
//  SampleSDKDemoObjC
//

#import "ViewController.h"
#import <SampleSDK/SampleSDK.h>

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // testing RoboVM framework
    SampleSDK* sdk = SampleSDKInstance();
    NSLog(@"RoboVM version %@", [sdk roboVmVersion]);

    Calculator* calc = [sdk newCalculator];
    [calc add:123];
    [calc sub:23];
    NSLog(@"%d", [calc result]);
    
    [[sdk webServer] start];
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
