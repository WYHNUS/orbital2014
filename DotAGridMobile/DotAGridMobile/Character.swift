//
//  Character.swift
//  DotAGridMobile
//
//  Created by apple on 8/6/14.
//  Copyright (c) 2014 apple. All rights reserved.
//

import Foundation
let MAX_MOVEMENT_SPEED:Int = 522
let MIN_MOVEMENT_CONSUME_AP:Int = 2
let MOVEMENT_CONSUME_AP:Int = 20

class Character:Drawable {
    init() {
        super.init()
    }
    init (character:Character) {
        super.init()
        imageName = character.imageName
        name = character.name
        isAlive = character.isAlive
        startingHP = character.startingHP
        startingMP = character.startingMP
        maxHP = character.maxHP
        maxMP = character.maxMP
        currentHP = character.currentHP
        currentMP = character.currentMP
        startingMovementSpeed = character.startingMovementSpeed
        totalMovementSpeed = character.totalMovementSpeed
        startingPhysicalAttack = character.startingPhysicalAttack
        totalPhysicalAttack = character.totalPhysicalAttack
        startingPhysicalAttackArea = character.startingPhysicalAttackArea
        totalPhysicalAttackArea = character.totalPhysicalAttackArea
        startingPhysicalAttackSpeed = character.startingPhysicalAttackSpeed
        totalPhysicalAttackSpeed = character.totalPhysicalAttackSpeed
        actionPoint = character.actionPoint
        currentActionPoint = character.actionPoint
    }
    var name:String?
    var isAlive:Bool?
    // Health Status
    var startingHP:Int? {
    set {
        if newValue <= 0 {
            NSLog("startingHP must be positive")
        } else {
            self.startingHP = newValue
        }
    }
    get {
        return self.startingHP
    }
    }
    var startingMP:Int? {
    set {
        if newValue <= 0 {
            NSLog("startingMP must be positive")
        } else {
            self.startingMP = newValue
        }
    }
    get {
        return self.startingMP
    }
    }
    var maxHP:Int? {
    set {
        if newValue <= startingHP {
            self.maxHP = startingHP
        } else {
            self.maxHP = newValue
        }
    }
    get {
        return self.maxHP
    }
    }
    var maxMP:Int? {
    set {
        if newValue <= startingMP {
            self.maxMP = startingMP
        } else {
            self.maxMP = newValue
        }
    }
    get {
        return self.maxMP
    }
    }
    var currentHP:Int? {
    set {
        if newValue <= 0 {
            self.currentHP = 0
            self.isAlive = false
        } else {
            self.currentHP = newValue
        }
    }
    get {
        return self.currentHP
    }
    }
    var currentMP:Int? {
    set {
        if newValue <= 0 {
            self.currentMP = 0
        } else {
            self.currentMP = newValue
        }
    }
    get {
        return self.currentMP
    }
    }
    // Movement
    var startingMovementSpeed:Int? {
    set {
        if newValue >= MAX_MOVEMENT_SPEED {
            self.startingMovementSpeed = MAX_MOVEMENT_SPEED
        } else {
            self.startingMovementSpeed = newValue
        }
    }
    get {
        return self.startingMovementSpeed
    }
    }
    var totalMovementSpeed:Int? {
    set {
        if newValue >= MAX_MOVEMENT_SPEED {
            self.totalMovementSpeed = MAX_MOVEMENT_SPEED
        } else {
            self.totalMovementSpeed = newValue
        }
    }
    get {
        return self.totalMovementSpeed
    }
    }
    // Physical Attacks
    var startingPhysicalAttack:Double? {
    set {
        if newValue <= 0 {
            self.startingPhysicalAttack = 0
        } else {
            self.startingPhysicalAttack = newValue
        }
    }
    get {
        return self.startingPhysicalAttack
    }
    }
    var totalPhysicalAttack:Double? {
    set {
        if newValue <= 0 {
            self.totalPhysicalAttack = 0
        } else {
            self.totalPhysicalAttack = newValue
        }
    }
    get {
        return self.totalPhysicalAttack
    }
    }
    var startingPhysicalAttackArea:Int? {
    set {
        if newValue <= 1 {
            self.startingPhysicalAttackArea = 1
        } else {
            self.startingPhysicalAttackArea = newValue
        }
    }
    get {
        return self.startingPhysicalAttackArea
    }
    }
    var totalPhysicalAttackArea:Int? {
    set {
        if newValue <= 1 {
            self.totalPhysicalAttackArea = 1
        } else {
            self.totalPhysicalAttackArea = newValue
        }
    }
    get {
        return self.totalPhysicalAttackArea
    }
    }
    var startingPhysicalAttackSpeed:Double?
    var totalPhysicalAttackSpeed:Double?
    // Physical Defense and Magic Resistance
    var startingPhysicalDefense:Double?
    var totalPhysicalDefense:Double?
    var startingMagicResistance:Double?
    var totalMagicResistance:Double?
    // Action Point
    var actionPoint:Int? {
    set {
        if newValue <= 0 {
            self.actionPoint = 0
        } else {
            self.actionPoint = newValue
        }
    }
    get {
        return self.actionPoint
    }
    }
    var currentActionPoint:Int? {
    set {
        if newValue <= 0 {
            self.currentActionPoint = 0
        } else {
            self.currentActionPoint = newValue
        }
    }
    get {
        return self.currentActionPoint
    }
    }
    // Calculation
    var numberOfMovableGrid:Int {
    return Int(self.currentActionPoint!) / Int(self.APUsedInMovingOneGrid)
    }
    var APUsedInMovingOneGrid:Double {
    return Double(MIN_MOVEMENT_CONSUME_AP) + Double(1.0 - 1.0 * Double(self.totalMovementSpeed!) / Double(MAX_MOVEMENT_SPEED)) * Double(MOVEMENT_CONSUME_AP)
    }
    
}