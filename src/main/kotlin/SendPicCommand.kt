package com.denebw.mirai.testPlugin

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage
import java.io.File

object SendPicCommand : CompositeCommand(
    PluginMain, "sendPic",
    "发图",
    description = "发送一张图片"
) {
    @SubCommand("1")
    suspend fun CommandSender.cmd1() {
        PluginMain.logger.info("发送一张图片")
        if (subject === null)
            return
        PluginMain.logger.info(subject?.toString())
        val image: Image = File("data/TestPlugin/1.jpg").uploadAsImage(subject?: return)
        val receipt = sendMessage(image)
        GlobalScope.launch {
            receipt?.recallIn(10 * 1000)
        }
    }
}