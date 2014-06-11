//
//  ControlEvent.h
//  DotAGridMobile
//
//  Created by apple on 11/6/14.
//  Copyright (c) 2014 NUS. All rights reserved.
//

#import <Foundation/Foundation.h>
@protocol Renderer;

typedef enum {
    TYPE_CLEAR = 0,
    TYPE_DOWN = 1,
    TYPE_DRAG = 2,
    TYPE_CLICK = 4,
    TYPE_INTERPRETED = 8
} kControlEventType;

@interface EventData : NSObject
@property (strong, nonatomic) NSArray* x;
@property (strong, nonatomic) NSArray* y;
@property (nonatomic) NSUInteger pointerCount;
@property (nonatomic) long startTime;
@property (nonatomic) long eventTime;
@property (strong, nonatomic) NSNumber *deltaX;
@property (strong, nonatomic) NSNumber *deltaY;
@property (nonatomic) BOOL wasMultiTouch;

-(id) init;
-(id) initWithData:(EventData*) data;
-(id) initWithPointers:(NSUInteger) count;
@end

@interface ControlEvent : NSObject
@property (nonatomic) NSInteger type;
@property (strong, nonatomic) NSString *extendedType;
@property (nonatomic) id<Renderer> emitter;
@property (strong, nonatomic) EventData *data;

-(id) initWithType:(NSInteger) type eventData:(EventData*) data;
-(id) initWithEvent:(ControlEvent*) event;
@end