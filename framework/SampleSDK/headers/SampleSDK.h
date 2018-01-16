//
//  SampleSDK.h
//  SampleSDK
//
// Defines objc api for SampleSDK implementation in RoboVM
//

#import <Foundation/Foundation.h>

//
// Calculator API -- everything is exposed as protocols
//
NS_SWIFT_NAME(Calculator)
@protocol Calculator
-(int)reset;
-(int)add:(int) i;
-(int)sub:(int) i;
-(int)result;
@end

// and typedef NSObject<Calculator> to Calculator which makes to make code better readable
typedef NSObject<Calculator> Calculator;

//
// WebServer API
//
NS_SWIFT_NAME(WebServer)
@protocol WebServer
-(void)start;
@end
typedef NSObject<WebServer> WebServer;



//
// SampleSDK of framework. it is entry point to framework.
//
NS_SWIFT_NAME(SampleSDK)
@protocol SampleSDK
-(Calculator *) newCalculator;
-(void) sayHello;
-(NSString*) roboVmVersion;
-(WebServer *) webServer;
@end
typedef NSObject<SampleSDK> SampleSDK;

//
// static function that returns instance of Framework's main class. On first access it also instantiate RoboVM
//
static SampleSDK* SampleSDKInstance() {
    extern SampleSDK* rvmInstantiateFramework(const char *className);
    return rvmInstantiateFramework("org.robovm.samples.framework.SampleSDKImpl");
}
