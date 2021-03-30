package com.denebw.mirai.testPlugin

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

object LiveCheckConfig : AutoSavePluginConfig("config"){
    val cookie: String by value()

}