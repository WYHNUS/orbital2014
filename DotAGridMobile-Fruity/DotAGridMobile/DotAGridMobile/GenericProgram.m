//
//  GenericProgram.m
//  DotAGridMobile
//
//  Created by apple on 11/6/14.
//  Copyright (c) 2014 NUS. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "GenericProgram.h"

@interface GenericProgram() {
    GLuint programId, vs, fs;
}
@end

@implementation GenericProgram
@synthesize programId = programId;

-(id) initWithVertexShaderSource:(const char *)vsSrc withFragmentShaderSource:(const char *)fsSrc {
    self = [super init];
    vs = glCreateShader(GL_VERTEX_SHADER);
    glShaderSource(vs, 1, &vsSrc, 0);
    glCompileShader(vs);
    char errString[65536];
    GLsizei errStringLen = 0;
    glGetShaderInfoLog(vs, 65535, &errStringLen, errString);
    if (errStringLen > 0)
        NSLog(@"Vertex Shader error:\n%@", [NSString stringWithUTF8String:errString]);
    fs = glCreateShader(GL_FRAGMENT_SHADER);
    glShaderSource(fs, 1, &fsSrc, 0);
    glCompileShader(fs);
    glGetShaderInfoLog(fs, 65535, &errStringLen, errString);
    if (errStringLen > 0)
        NSLog(@"Fragment Shader error:\n%@", [NSString stringWithUTF8String:errString]);
    programId = glCreateProgram();
    glAttachShader(programId, vs);
    glAttachShader(programId, fs);
    glLinkProgram(programId);
    glGetProgramInfoLog(programId, 65535, &errStringLen, errString);
    if (errStringLen > 0)
        NSLog(@"Program Link error:\n%@", [NSString stringWithUTF8String:errString]);
    return self;
}

-(void)close {
    glDeleteProgram(programId);
    glDeleteShader(vs);
    glDeleteShader(fs);
}
@end