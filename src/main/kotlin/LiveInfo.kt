package com.denebw.mirai.testPlugin

import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText

data class LiveInfo(val title: String, val userName: String, val roomId: Long) {
    fun toMessage(): Message {
        val message = PlainText("${userName}正在直播：${title}\n") +
            PlainText("https://live.bilibili.com/$roomId")
        return message
    }
}
