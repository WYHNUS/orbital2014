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
        self.hero = Hero(hero: hero)
        self.money = money
    }
    var hero:Hero
    var money:Int {
    set {
        if newValue < 0 {
            NSLog("Player is bankrupt")
            self.money = 0
        } else {
            self.money = newValue
        }
    }
    get {
        return self.money
    }
    }
}