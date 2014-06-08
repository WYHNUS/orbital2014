//
//  ExtensionEngine.swift
//  DotAGridMobile
//
//  Created by apple on 8/6/14.
//  Copyright (c) 2014 apple. All rights reserved.
//

import Foundation
import JavaScriptCore

class ExtensionEngine {
    let vm:JSVirtualMachine = JSVirtualMachine()
    let context:JSContext = JSContext(vm)
    let engineHandler:JSValue, configHandler:JSValue
    let interface:ExtensionInterface
    init (source:String, logicMan:GameLogicManager) {
        interface = ExtensionInterface();
        context["changeProperty"] = {
            (name:String, value:Int) -> Int in
            return 0
        }
        context.executeScript(source)
        engineHandler = context["engine"]
        configHandler = context["config"]
    }
    deinit {
        
    }
}