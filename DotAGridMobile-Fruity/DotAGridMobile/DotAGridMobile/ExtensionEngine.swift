//
//  ExtensionEngine.swift
//  DotAGridMobile
//
//  Created by apple on 11/6/14.
//  Copyright (c) 2014 NUS. All rights reserved.
//

import Foundation

import JavaScriptCore

class ExtensionEngine {
    let vm:JSVirtualMachine
    let context:JSContext
    var engineHandler:JSValue?, configHandler:JSValue?
    var interface:ExtensionInterface?
    init (source:String, logicMan:GameLogicManager) {
        vm = JSVirtualMachine()
        context = JSContext(virtualMachine: self.vm)
        interface = ExtensionInterface(logicMan: logicMan, delegateFor: self);
        context.setValue(interface as AnyObject, forKey: "INTERFACE")
        context.evaluateScript(source)
        engineHandler = context.objectForKeyedSubscript("ENGINE")
        configHandler = context.objectForKeyedSubscript("CONFIG")
    }
    deinit {
        
    }
}