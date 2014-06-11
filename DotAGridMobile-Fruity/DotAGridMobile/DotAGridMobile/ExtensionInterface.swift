//
//  ExtensionInterface.swift
//  DotAGridMobile
//
//  Created by apple on 11/6/14.
//  Copyright (c) 2014 NUS. All rights reserved.
//

import Foundation
import JavaScriptCore

class ExtensionInterface:JSExport {
    let logicMan:GameLogicManager
    let parent:ExtensionEngine
    init (logicMan: GameLogicManager, delegateFor: ExtensionEngine) {
        self.logicMan = logicMan
        self.parent = delegateFor
    }
}