//
//  Item.swift
//  DotAGridMobile
//
//  Created by apple on 11/6/14.
//  Copyright (c) 2014 NUS. All rights reserved.
//

import Foundation

class Item {
    init(item:Item) {
        imageName = item.imageName
        name = item.name
        cost = item.cost
        sellPrice = item.sellPrice
        requiredMPPerUse = item.requiredMPPerUse
        requiredHPPerUse = item.requiredHPPerUse
        reusable = item.reusable
        usableTime = item.usableTime
        addStrength = item.addStrength
        addAgility = item.addAgility
        addIntelligence = item.addIntelligence
        addHP = item.addHP
        addMP = item.addMP
        addHPGainPerRound = item.addHPGainPerRound
        addMPGainPerRound = item.addMPGainPerRound
        addPhysicalDefence = item.addPhysicalDefence
        addMagicResistance = item.addMagicResistance
        addPhysicalAttack = item.addPhysicalAttack
        addPhysicalAttackSpeed = item.addPhysicalAttackSpeed
        addPhysicalAttackArea = item.addPhysicalAttackArea
        addMovementSpeed = item.addMovementSpeed
    }
    var imageName:String?
    var name:String?
    var cost:Int?
    var sellPrice:Int?
    var requiredMPPerUse:Int?
    var requiredHPPerUse:Int?
    var reusable:Bool?
    var usableTime:Int?
    var addStrength:Double?
    var addAgility:Double?
    var addIntelligence:Double?
    var addHP:Int?
    var addMP:Int?
    var addHPGainPerRound:Double?
    var addMPGainPerRound:Double?
    var addPhysicalDefence:Double?
    var addMagicResistance:Double?
    var addPhysicalAttack:Double?
    var addPhysicalAttackSpeed:Double?
    var addPhysicalAttackArea:Int?
    var addMovementSpeed:Int?
}