//
//  Character.swift
//  DotAGridMobile
//
//  Created by apple on 8/6/14.
//  Copyright (c) 2014 apple. All rights reserved.
//

import Foundation

class Character:Drawable {
    init (character:Character) {
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
    var name:String
    var isAlive:Bool
    // Health Status
    var startingHP:Int {
    set {
        if newValue <= 0 {
            NSLog("startingHP must be positive")
        } else {
            startingHP = newValue
        }
    }
    get {
        return startingHP
    }
    }
    var startingMP:Int {
    set {
        if newValue <= 0 {
            NSLog("startingMP must be positive")
        } else {
            startingMP = newValue
        }
    }
    get {
        return startingMP
    }
    }
    var maxHP:Int {
    set {
        if newValue <= startingHP {
            maxHP = startingHP
        } else {
            maxHP = newValue
        }
    }
    get {
        return maxHP
    }
    }
    var maxMP:Int {
    set {
        if newValue <= startingMP {
            maxMP = startingMP
        } else {
            maxMP = newValue
        }
    }
    get {
        return maxMP
    }
    }
    var currentHP:Int {
    set {
        if newValue <= 0 {
            currentHP = 0
            isAlive = false
        } else {
            currentHP = newValue
        }
    }
    get {
        return currentHP
    }
    }
    var currentMP:Int {
    set {
        if newValue <= 0 {
            currentMP = 0
        } else {
            currentMP = newValue
        }
    }
    get {
        return currentMP
    }
    }
    // Movement
    var startingMovementSpeed:Int {
    set {
        if newValue >= Character.MAX_MOVEMENT_SPEED {
            startingMovementSpeed = Character.MAX_MOVEMENT_SPEED
        } else {
            startingMovementSpeed = newValue
        }
    }
    get {
        return startingMovementSpeed
    }
    }
    var totalMovementSpeed:Int {
    set {
        if newValue >= Character.MAX_MOVEMENT_SPEED {
            totalMovementSpeed = Character.MAX_MOVEMENT_SPEED
        } else {
            totalMovementSpeed = newValue
        }
    }
    get {
        return totalMovementSpeed
    }
    }
    // Physical Attacks
    var startingPhysicalAttack:Double {
    set {
        if newValue <= 0 {
            startingPhysicalAttack = 0
        } else {
            startingPhysicalAttack = newValue
        }
    }
    get {
        return startingPhysicalAttack
    }
    }
    var totalPhysicalAttack:Double {
    set {
        if newValue <= 0 {
            totalPhysicalAttack = 0
        } else {
            totalPhysicalAttack = newValue
        }
    }
    get {
        return totalPhysicalAttack
    }
    }
    var startingPhysicalAttackArea:Int {
    set {
        if newValue <= 1 {
            startingPhysicalAttackArea = 1
        } else {
            startingPhysicalAttackArea = newValue
        }
    }
    get {
        return startingPhysicalAttackArea
    }
    }
    var totalPhysicalAttackArea:Int {
    set {
        if newValue <= 1 {
            totalPhysicalAttackArea = 1
        } else {
            totalPhysicalAttackArea = newValue
        }
    }
    get {
        return totalPhysicalAttackArea
    }
    }
    var startingPhysicalAttackSpeed:Double
    var totalPhysicalAttackSpeed:Double
    // Physical Defense and Magic Resistance
    var startingPhysicalDefense:Double
    var totalPhysicalDefense:Double
    var startingMagicResistance:Double
    var totalMagicResistance:Double
    // Action Point
    var actionPoint:Int {
    set {
        if newValue <= 0 {
            actionPoint = 0
        } else {
            actionPoint = newValue
        }
    }
    get {
        return actionPoint
    }
    }
    var currentActionPoint:Int {
    set {
        if newValue <= 0 {
            currentActionPoint = 0
        } else {
            currentActionPoint = newValue
        }
    }
    get {
        return currentActionPoint
    }
    }
    // Calculation
    var numberOfMovableGrid:Int {
    return Int(currentActionPoint / APUsedInMovingOneGrid)
    }
    var APUsedInMovingOneGrid:Double {
    return Character.MIN_MOVEMENT_CONSUME_AP + (1 - 1.0 * totalMovementSpeed / Character.MAX_MOVEMENT_SPEED) * Character.MOVEMENT_CONSUME_AP
    }
    @final class let MAX_MOVEMENT_SPEED:Int = 522
    @final class let MIN_MOVEMENT_CONSUME_AP:Int = 2
    @final class let MOVEMENT_CONSUME_AP:Int = 20
    
}