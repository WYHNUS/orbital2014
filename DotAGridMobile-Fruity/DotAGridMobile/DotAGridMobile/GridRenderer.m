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
#import "GenericProgram.h"

const static float BOARD_Z_COORD = .01f;
const static float BASE_ZOOM_FACTOR = .5f;
const static int SHADOW_MAP_TEXTURE_DIMENSION = 2048;
const static float LIGHT_MAX_RADIUS = 5;

@interface GridRenderer ()
@end

@implementation GridRenderer {
    GameLogicManager *logicMan;
    NSDictionary *textures;
    NSNumber *aspectRatio;
    int rows, columns, resolution;
    NSArray* terrain;
    NSMutableArray *mapTerrain, *mapTextureCoord, *mapNormalCoord;
    GenericProgram *gridProgram, *mapProgram, *shadowProgram, *shadowObjProgram;
}
@synthesize gameLogicManager = logicMan, textures = textures, aspectRatio = aspectRatio;
-(id)initWithColumns:(NSInteger)noOfColumns rows:(NSInteger)noOfRows terrain:(NSArray *)terrainData {
    self = [super init];
    rows = noOfRows;
    columns = noOfColumns;
    terrain = terrainData;
    if (columns * rows > 5000)
        resolution = 2;
    else if (columns * rows > 1000)
        resolution = 4;
    else if (columns * rows > 200)
        resolution = 8;
    else
        resolution = 16;
    mapTerrain = [NSMutableArray arrayWithCapacity:(rows * resolution + 1) * (columns * resolution + 1) * 4];
    mapTextureCoord = [NSMutableArray arrayWithCapacity:(rows * resolution + 1) * (columns * resolution + 1) * 2];
    mapNormalCoord = [NSMutableArray arrayWithCapacity:(rows * resolution + 1) * (columns * resolution + 1) * 2];
    gridProgram = [[GenericProgram alloc] initWithVertexShaderSource:VS_IDENTITY withFragmentShaderSource:FS_IDENTITY];
    mapProgram = [[GenericProgram alloc] initWithVertexShaderSource:VS_IDENTITY_SPECIAL_LIGHTING withFragmentShaderSource:FS_IDENTITY_SPECIAL_LIGHTING];
    shadowProgram = [[GenericProgram alloc] initWithVertexShaderSource:VS_IDENTITY_SPECIAL_SHADOW withFragmentShaderSource:FS_IDENTITY_SPECIAL_SHADOW];
    shadowObjProgram = [[GenericProgram alloc] initWithVertexShaderSource:VS_IDENTITY_SPECIAL_LIGHTING_UNIFORMNORMAL withFragmentShaderSource:FS_IDENTITY_SPECIAL_LIGHTING_UNIFORMNORMAL];
    [self calculateModel];
    float lightRadius;
    if (rows > columns)
        lightRadius = BASE_ZOOM_FACTOR * LIGHT_MAX_RADIUS * 2 / columns;
    else
        lightRadius = BASE_ZOOM_FACTOR * LIGHT_MAX_RADIUS * 2 / rows;
    return self;
}
-(void) calculateModel {
}
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