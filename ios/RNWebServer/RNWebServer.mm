//
//  RNWebServer.m
//  RNWebServer
//
//  Created by xiaoli on 2025/12/8.
//

#import "RNWebServer.h"
#import <GCDWebServer/GCDWebServer.h>
#import <GCDWebServer/GCDWebServerDataResponse.h>

@interface RNWebServer ()

@property (nonatomic, strong) GCDWebServer *webServer;

@end

@implementation RNWebServer

RCT_EXPORT_MODULE(WebServer)

- (instancetype)init {
    if (self = [super init]) {
      self.webServer = [[GCDWebServer alloc] init];
    }
    return self;
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:(const facebook::react::ObjCTurboModule::InitParams &)params {
  return std::make_shared<facebook::react::NativeWebServerSpecJSI>(params);
}

- (void)start:(NSArray *)handlers resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
    if (self.webServer.isRunning) {
      [self.webServer stop];
    }
    
    for (NSDictionary *handler in handlers) {
      [self.webServer addGETHandlerForBasePath:handler[@"prefix"] directoryPath:handler[@"path"] indexFilename:@"index.html" cacheAge:0 allowRangeRequests:YES];
    }
    
    NSError *error = nil;
    [self.webServer startWithOptions:@{
      GCDWebServerOption_AutomaticallySuspendInBackground: @NO,
      GCDWebServerOption_Port: @(0) // 0表示随机端口
    } error:&error];
    
    if (error) {
      reject(@"start_error", @"Failed to start server", error);
    } else {
      resolve([NSNumber numberWithUnsignedLong:self.webServer.port]);
    }
}

- (void)stop {
    if (self.webServer.isRunning) {
      [self.webServer stop];
    }
}

@end
