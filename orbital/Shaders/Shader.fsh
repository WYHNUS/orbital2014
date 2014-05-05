//
//  Shader.fsh
//  orbital
//
//  Created by dingxiangfei on 5/5/14.
//  Copyright (c) 2014 nus. All rights reserved.
//

varying lowp vec4 colorVarying;

void main()
{
    gl_FragColor = colorVarying;
}
