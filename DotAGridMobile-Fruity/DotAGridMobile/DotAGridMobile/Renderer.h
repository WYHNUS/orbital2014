//
//  Renderer.h
//  DotAGridMobile
//
//  Created by apple on 11/6/14.
//  Copyright (c) 2014 NUS. All rights reserved.
//
#import <DotAGridMobileLogic-Swift.h>
#import "Closeable.h"
#import "ControlEvent.h"
@class GameLogicManager;

@protocol Renderer <Closeable>

@required
@property (strong, atomic) GameLogicManager *gameLogicManager;
@property (strong, atomic) NSDictionary *textures;
@property (strong, atomic) NSNumber *aspectRatio;-(void) setRenderReady;
-(BOOL) getReadyState;
-(void) draw;

@optional
-(BOOL) passEvent:(ControlEvent*) e;
-(void) setModel:(NSArray*) model view:(NSArray*) view projection:(NSArray*) projection;
@end