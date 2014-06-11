//
//  GridRenderer.m
//  DotAGridMobile
//
//  Created by apple on 11/6/14.
//  Copyright (c) 2014 NUS. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "GridRenderer.h"
#import "CommonShaders.h"
#import "CommonShaders.h"
#import "GenericProgram.h"

@interface GridRenderer () {
    GameLogicManager *logicMan;
    NSDictionary *textures;
    NSNumber *aspectRatio;
}

@end

@implementation GridRenderer
@synthesize gameLogicManager = logicMan;
@synthesize textures = textures;
@synthesize aspectRatio = aspectRatio;
-(void)setRenderReady {
    
}
-(BOOL)getReadyState {
    return NO;
}
-(void)draw {
    
}
-(void) close {
    
}
@end