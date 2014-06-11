//
//  GameLogicManager.swift
//  DotAGridMobile
//
//  Created by apple on 11/6/14.
//  Copyright (c) 2014 NUS. All rights reserved.
//

import Foundation
class GameLogicManager {
    var extEngine:ExtensionEngine?
    var extensionSource:String = ""
    var useExtensionEngine:Bool {
    set {
        if newValue != self.useExtensionEngine {
            if newValue {
                extEngine = ExtensionEngine(source: self.extensionSource, logicMan: self)
            } else {
                extEngine = nil
            }
        }
    }
    get {
        return self.useExtensionEngine
    }
    }
}