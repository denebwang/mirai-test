package com.denebw.mirai.testPlugin

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object LiveRoomData : AutoSavePluginData("LiveRoom") {
    var rooms: MutableSet<Long> by value()//直播间id
    val subscriberGroups: MutableMap<Long, MutableSet<Long>> by value()//直播间-订阅的id（群号）
    val subscriberUsers: MutableMap<Long, MutableSet<Long>> by value()//直播间-订阅的id（qq）
}