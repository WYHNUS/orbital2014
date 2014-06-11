//
//  Hero.swift
//  DotAGridMobile
//
//  Created by apple on 11/6/14.
//  Copyright (c) 2014 NUS. All rights reserved.
//

import Foundation
class Hero:Character {
    init (hero:Hero){
        super.init(character: hero)
        mainAttribute = hero.mainAttribute
        startingStrength = hero.startingStrength
        startingIntelligence = hero.startingIntelligence
        startingAgility = hero.startingAgility
        strengthGrowth = hero.strengthGrowth
        intelligenceGrowth = hero.intelligenceGrowth
        agilityGrowth = hero.agilityGrowth
        totalAgility = hero.totalAgility
        totalIntelligence = hero.totalIntelligence
        totalStrength = hero.totalStrength
        MPGainPerRound = hero.MPGainPerRound
        HPGainPerRound = hero.HPGainPerRound
        experience = hero.experience
        level = hero.level
        kill = hero.kill
        death = hero.death
        assist = hero.assist
        for item in hero.itemList {
            addItem(item)
        }
    }
    var mainAttribute:String {
    set {
        switch newValue.lowercaseString {
        case "strength":
            fallthrough
        case "agility":
            fallthrough
        case "intelligence":
            self.mainAttribute = newValue.lowercaseString
        default:
            NSLog("Invalid attribute");
        }
    }
    get {
        return self.mainAttribute
    }
    }
    // TODO: why setter?!
    var totalMainAttribute:Double {
    set {
        switch mainAttribute {
        case "strength":
            self.totalMainAttribute = totalStrength
        case "agility":
            self.totalMainAttribute = totalAgility
        case "intelligence":
            self.totalMainAttribute = totalIntelligence
        default:
            NSLog("unsupported attribute");
        }
    }
    get {
        return self.totalMainAttribute
    }
    }
    
    var startingStrength:Int {
    set {
        if newValue <= 0 {
            NSLog("startingStrength must be positive")
        } else {
            self.startingStrength = newValue
        }
    }
    get {
        return self.startingStrength
    }
    }
    
    var startingAgility:Int {
    set {
        if newValue <= 0 {
            NSLog("startingAgility must be positive")
        } else {
            self.startingAgility = newValue
        }
    }
    get {
        return self.startingAgility
    }
    }
    
    var startingIntelligence:Int {
    set {
        if newValue <= 0 {
            NSLog("startingIntelligence must be positive")
        } else {
            self.startingIntelligence = newValue
        }
    }
    get {
        return self.startingIntelligence
    }
    }
    
    var strengthGrowth:Double {
    set {
        if newValue <= 0 {
            NSLog("strengthGrowth must be positive")
        } else {
            self.strengthGrowth = newValue
        }
    }
    get {
        return self.strengthGrowth
    }
    }
    var agilityGrowth:Double {
    set {
        if newValue <= 0 {
            NSLog("agilityGrowth must be positive")
        } else {
            self.agilityGrowth = newValue
        }
    }
    get {
        return self.agilityGrowth
    }
    }
    var intelligenceGrowth:Double {
    set {
        if newValue <= 0 {
            NSLog("intelligenceGrowth must be positive")
        } else {
            self.intelligenceGrowth = newValue
        }
    }
    get {
        return self.intelligenceGrowth
    }
    }
    var totalStrength:Double {
    set {
        if newValue <= Double(self.startingStrength) {
            self.totalStrength = Double(self.startingStrength)
        } else {
            self.totalStrength = newValue
        }
    }
    get {
        return self.totalStrength
    }
    }
    var totalAgility:Double {
    set {
        if newValue <= Double(self.startingAgility) {
            self.totalAgility = Double(self.startingAgility)
        } else {
            self.totalAgility = newValue
        }
    }
    get {
        return self.totalAgility
    }
    }
    var totalIntelligence:Double {
    set {
        if newValue <= Double(self.startingIntelligence) {
            self.totalIntelligence = Double(self.startingIntelligence)
        } else {
            self.totalIntelligence = newValue
        }
    }
    get {
        return self.totalIntelligence
    }
    }
    
    var HPGainPerRound:Double = 0, MPGainPerRound:Double = 0
    
    var experience:Int = 0, level:Int = 0, kill:Int = 0, death:Int = 0, assist:Int = 0
    
    var itemList:Item[] = []
    func addItem(item:Item) -> Item{
        itemList.append(Item(item: item))
        return item
    }
}