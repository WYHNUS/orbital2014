//
//  ControlEvent.m
//  DotAGridMobile
//
//  Created by apple on 11/6/14.
//  Copyright (c) 2014 NUS. All rights reserved.
//

#import "ControlEvent.h"
@interface ControlEvent () {
    NSInteger type;
    EventData *data;
    id<Renderer> emitter;
    NSString *extendedType;
}
@end

@implementation ControlEvent
@synthesize type = type;
@synthesize data = data;
@synthesize extendedType = extendedType;
@synthesize emitter = emitter;
-(id) initWithEvent:(ControlEvent *)event {
    self.type = event.type;
    self.data = event.data ? [[EventData alloc] initWithData:event.data] : nil;
    self.extendedType = event.extendedType ? [NSString stringWithString:event.extendedType] : nil;
    return self;
}
-(id) initWithType:(NSInteger)type eventData:(EventData *)data {
    self = [super init];
    self.type = type;
    self.data = data ? [[EventData alloc] initWithData:data] : [[EventData alloc] init];
    return self;
}
@end

@interface EventData () {
    NSUInteger pointerCount;
    NSArray *x, *y;
    NSNumber *deltaX, *deltaY;
    long startTime, eventTime;
    BOOL wasMultiTouch;
}

@end

@implementation EventData
@synthesize pointerCount = pointerCount;
@synthesize x = x;
@synthesize y = y;
@synthesize deltaX = deltaX;
@synthesize deltaY = deltaY;
@synthesize startTime = startTime;
@synthesize eventTime = eventTime;
@synthesize wasMultiTouch = wasMultiTouch;
-(id)init {
    return [super init];
}
-(id)initWithData:(EventData *)data {
    self = [super init];
    self.pointerCount = data.pointerCount;
    self.startTime = data.startTime;
    self.eventTime = data.eventTime;
    self.wasMultiTouch = self.wasMultiTouch;
    self.deltaX = [NSNumber numberWithFloat:[data.deltaX floatValue]];
    self.deltaY = [NSNumber numberWithFloat:[data.deltaY floatValue]];
    self.x = [[NSArray alloc] initWithArray:data.x copyItems:YES];
    self.y = [[NSArray alloc] initWithArray:data.y copyItems:YES];
    return self;
}
-(id)initWithPointers:(NSUInteger) count {
    self = [super init];
    self.pointerCount = count;
    self.x = [[NSArray alloc] initWithObjects:nil count:self.pointerCount];
    self.y = [[NSArray alloc] initWithObjects:nil count:self.pointerCount];
    return self;
}
@end