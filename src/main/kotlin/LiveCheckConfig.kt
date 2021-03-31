package com.denebw.mirai.testPlugin

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object LiveCheckConfig : AutoSavePluginConfig("config") {
    val cookie: String by value()

    @ValueDescription("直播检测间隔，单位秒")
    var interval: Int by value(5)

    @ValueDescription("网络请求间隔，单位毫秒")
    var requestInterval: Int by value(100)

}