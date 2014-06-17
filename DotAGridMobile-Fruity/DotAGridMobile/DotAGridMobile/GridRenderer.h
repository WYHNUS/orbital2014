//
//  GridRenderer.h
//  DotAGridMobile
//
//  Created by apple on 11/6/14.
//  Copyright (c) 2014 NUS. All rights reserved.
//
#import "Renderer.h"
@interface GridRenderer : NSObject <Renderer>
-(id) initWithColumns:(NSInteger) columns rows:(NSInteger)rows terrain:(NSArray*) terrain;
@end