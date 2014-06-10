//
//  MainGLViewController.h
//  DotAGridMobile
//
//  Created by apple on 11/6/14.
//  Copyright (c) 2014 apple. All rights reserved.
//
#import <OpenGLES/EAGL.h>
#import <OpenGLES/ES2/gl.h>
#import <OpenGLES/ES2/glext.h>
#import <GLKit/GLKit.h>

@interface MainGLViewController : GLKViewController {
    EAGLContext * context;
}

@end