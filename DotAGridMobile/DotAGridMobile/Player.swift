//
//  Player.swift
//  DotAGridMobile
//
//  Created by apple on 8/6/14.
//  Copyright (c) 2014 apple. All rights reserved.
//

import Foundation

class Player {
    init (money:Int, hero:Hero) {
        self.hero = Hero(hero)
        self.money = money
    }
    var hero:Hero
    var money:Int {
    set {
        if newValue < 0 {
            NSLog("Player is bankrupt")
            money = 0
        } else {
            money = newValue
        }
    }
    get {
        return money
    }
    }
}