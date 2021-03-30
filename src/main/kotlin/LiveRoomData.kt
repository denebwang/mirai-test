package com.denebw.mirai.testPlugin

import net.mamoe.mirai.console.data.AutoSavePluginData

object LiveRoomData:AutoSavePluginData("LiveRoom") {
    var rooms:MutableSet<Long> = mutableSetOf()//直播间id
    val subscriberGroups:MutableMap<Long, MutableSet<Long>> = mutableMapOf()//直播间-订阅的id（群号）
    val subscriberUsers:MutableMap<Long, MutableSet<Long>> = mutableMapOf()//直播间-订阅的id（qq）
}