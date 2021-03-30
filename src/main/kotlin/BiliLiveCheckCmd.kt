package com.denebw.mirai.testPlugin

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand

object BiliLiveCheckCmd : CompositeCommand (
    PluginMain, "live","直播",
    description = "直播间订阅相关"
    ){
    @SubCommand("订阅","subscribe")
    suspend fun CommandSender.subscribe(roomId:Long)
    {

    }

    @SubCommand("订阅列表","list")
    suspend fun CommandSender.list()
    {

    }

    @SubCommand("取消订阅", "unsubscribe")
    suspend fun CommandSender.unsubscribe(roomId:Long)
    {

    }
}