//
//  File.swift
//  DotAGridMobile
//
//  Created by apple on 8/6/14.
//  Copyright (c) 2014 apple. All rights reserved.
//

import Foundation

// ported from edu.nus.comp.dotagrid.logic.Hero
class Hero:Character {
    init (hero:Hero){
        super.init(hero)
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
            mainAttribute = newValue.lowercaseString
        default:
            NSLog("Invalid attribute");
        }
    }
    get {
        return mainAttribute
    }
    }
    // TODO: why setter?!
    var totalMainAttribute:Double {
    set {
        switch mainAttribute {
            case "strength":
                totalMainAttribute = totalStrength
            case "agility":
                totalMainAttribute = totalAgility
            case "intelligence":
                totalMainAttribute = totalIntelligence
        }
    }
    get {
        return totalMainAttribute
    }
    }
    
    var startingStrength:Int {
    set {
        if newValue <= 0 {
            NSLog("startingStrength must be positive")
        } else {
            startingStrength = newValue
        }
    }
    get {
        return startingStrength
    }
    }
    
    var startingAgility:Int {
    set {
        if newValue <= 0 {
            NSLog("startingAgility must be positive")
        } else {
            startingAgility = newValue
        }
    }
    get {
        return startingAgility
    }
    }
    
    var startingIntelligence:Int {
    set {
        if newValue <= 0 {
            NSLog("startingIntelligence must be positive")
        } else {
            startingIntelligence = newValue
        }
    }
    get {
        return startingIntelligence
    }
    }
    
    var strengthGrowth:Double {
    set {
        if newValue <= 0 {
            NSLog("strengthGrowth must be positive")
        } else {
            strengthGrowth = newValue
        }
    }
    get {
        return strengthGrowth
    }
    }
    var agilityGrowth:Double {
    set {
        if newValue <= 0 {
            NSLog("agilityGrowth must be positive")
        } else {
            agilityGrowth = newValue
        }
    }
    get {
        return agilityGrowth
    }
    }
    var intelligenceGrowth:Double {
    set {
        if newValue <= 0 {
            NSLog("intelligenceGrowth must be positive")
        } else {
            intelligenceGrowth = newValue
        }
    }
    get {
        return intelligenceGrowth
    }
    }
    var totalStrength:Double {
    set {
        if newValue <= startingStrength {
            totalStrength = startingStrength
        } else {
            totalStrength = newValue
        }
    }
    get {
        return totalStrength
    }
    }
    var totalAgility:Double {
    set {
        if newValue <= startingAgility {
            totalAgility = startingAgility
        } else {
            totalAgility = newValue
        }
    }
    get {
        return totalAgility
    }
    }
    var totalIntelligence:Double {
    set {
        if newValue <= startingIntelligence {
            totalIntelligence = startingIntelligence
        } else {
            totalIntelligence = newValue
        }
    }
    get {
        return totalIntelligence
    }
    }
    
    var HPGainPerRound:Double, MPGainPerRound:Double
    
    var experience:Int, level:Int, kill:Int, death:Int, assist:Int
    
    var itemList:Item[]
    func addItem(item:Item) -> Item{
        itemList.append(Item(item))
        return item
    }
}