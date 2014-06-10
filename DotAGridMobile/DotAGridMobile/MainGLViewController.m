//
//  MainGLViewController.m
//  DotAGridMobile
//
//  Created by apple on 11/6/14.
//  Copyright (c) 2014 apple. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "MainGLViewController.h"

@implementation MainGLViewController

-(void) viewDidLoad {
    context = [[EAGLContext alloc] initWithAPI:kEAGLRenderingAPIOpenGLES2];
    context.multiThreaded = YES;
}

@end