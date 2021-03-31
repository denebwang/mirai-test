package com.denebw.mirai.testPlugin


import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.toMessageChain

object BiliLiveCheckCmd : CompositeCommand(
    PluginMain, "live", "直播",
    description = "直播间订阅相关"
) {
    @SubCommand("订阅", "subscribe")
    suspend fun CommandSender.subscribe(roomId: Long) {
        when (subject) {
            is Group -> {
                if (roomId !in LiveRoomData.rooms) {
                    LiveRoomData.subscriberGroups[roomId] = mutableSetOf(subject?.id ?: return)
                    LiveRoomData.rooms.add(roomId)
                } else LiveRoomData.subscriberGroups[roomId]?.add(subject?.id ?: return)

                PluginMain.logger.info("Added new subscriber group ${subject?.id}")
            }
            is User -> {
                if (roomId !in LiveRoomData.rooms) {
                    LiveRoomData.subscriberUsers[roomId] = mutableSetOf(subject?.id ?: return)
                    LiveRoomData.rooms.add(roomId)
                } else {
                    LiveRoomData.subscriberUsers[roomId]?.add(subject?.id ?: return)
                }
                PluginMain.logger.info("Added new subscriber user ${subject?.id}")

            }
            else -> {
                PluginMain.logger.error("Incorrect subject: ${subject.toString()}")
            }
        }
        sendMessage("添加订阅成功")
    }

    @SubCommand("订阅列表", "list")
    suspend fun CommandSender.list() {
        when (subject) {
            is Group -> {
                val httpHandler = HttpHandler()
                var message = PlainText("当前订阅列表：\n").toMessageChain()
                LiveRoomData.subscriberGroups.forEach {
                    if (subject?.id in it.value) {
                        val (uid, _, _) = httpHandler.getRoomInfo(it.key)
                        val (username) = httpHandler.getUserName(uid)
                        message += PlainText(username + "\n")
                    }
                }
                sendMessage(message)
            }
            is User -> {
                val httpHandler = HttpHandler()
                var message = PlainText("当前订阅列表：\n").toMessageChain()
                LiveRoomData.subscriberUsers.forEach {
                    if (subject?.id in it.value) {
                        val (uid, _, _) = httpHandler.getRoomInfo(it.key)
                        val (username) = httpHandler.getUserName(uid)
                        message += PlainText(username + "\n")
                    }
                }
                sendMessage(message)

            }
            else -> {
                PluginMain.logger.error("Incorrect subject: ${subject.toString()}")
            }
        }
    }

    @SubCommand("取消订阅", "unsubscribe")
    suspend fun CommandSender.unsubscribe(roomId: Long) {
        if (roomId !in LiveRoomData.rooms) {
            sendMessage("该直播间未被订阅")
            return
        }
        when (subject) {
            is Group -> {
                LiveRoomData.rooms.remove(roomId)
                LiveRoomData.subscriberGroups[roomId]?.remove(subject?.id ?: return)
                PluginMain.logger.info("Added new subscriber group ${subject?.id}")
            }
            is User -> {
                LiveRoomData.rooms.remove(roomId)
                LiveRoomData.subscriberUsers[roomId]?.remove(subject?.id ?: return)
                PluginMain.logger.info("Added new subscriber user ${subject?.id}")

            }
            else -> {
                PluginMain.logger.error("Incorrect subject: ${subject.toString()}")
            }
        }
        sendMessage("移除成功")
    }
}