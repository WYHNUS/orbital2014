//
//  GenericProgram.h
//  DotAGridMobile
//
//  Created by apple on 11/6/14.
//  Copyright (c) 2014 NUS. All rights reserved.
//
#import <OpenGLES/ES2/gl.h>
#import <OpenGLES/ES2/glext.h>
#import "Closeable.h"
@interface GenericProgram : NSObject <Closeable>
    -(id) initWithVertexShaderSource:(const char*)vsSrc withFragmentShaderSource:(const char *)fsSrc;
    @property (nonatomic, readonly) GLuint programId;
@end