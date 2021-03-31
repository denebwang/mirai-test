package com.denebw.mirai.testPlugin


import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.permission.AbstractPermitteeId
import net.mamoe.mirai.console.permission.PermissionService.Companion.cancel
import net.mamoe.mirai.console.permission.PermissionService.Companion.permit
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "com.denebw.mirai.testPlugin",
        name = "TestPlugin",
        version = "0.1.0"
    )
) {
    private var liveCheckJob: Job? = null
    private val httpHandler = HttpHandler()
    var startedLive = mutableSetOf<Long>()
    override fun onEnable() {
        LiveCheckConfig.reload()
        LiveRoomData.reload()

        //SendPicCommand.register()
        BiliLiveCheckCmd.register()
        AbstractPermitteeId.AnyContact.permit(BiliLiveCheckCmd.permission)

        liveCheckJob = PluginMain.launch {
            while (true) {
                delay(LiveCheckConfig.interval * 60 * 1000L)
                checkLiveRoom()
            }
        }
    }

    override fun onDisable() {
        //SendPicCommand.unregister()
        BiliLiveCheckCmd.unregister()
        AbstractPermitteeId.AnyContact.cancel(BiliLiveCheckCmd.permission, true)
        if (liveCheckJob != null) {
            liveCheckJob?.cancel()
        }
    }

    suspend fun checkLiveRoom() {
        var liveInfoMap = mutableMapOf<Long, LiveInfo>()

        for (roomId in LiveRoomData.rooms) {
            delay(LiveCheckConfig.requestInterval.toLong())
            val (uid, status, title) = httpHandler.getRoomInfo(roomId)
            when (status) {
                1 -> {
                    if (roomId in startedLive)
                        continue
                    startedLive.add(roomId)
                    delay(LiveCheckConfig.requestInterval.toLong())
                    val (username) = httpHandler.getUserName(uid)
                    liveInfoMap[roomId] = LiveInfo(title, username, roomId)
                }
                0 -> {
                    if (roomId in startedLive)
                        startedLive.remove(roomId)
                }
            }
        }
        for ((roomId, roomInfo) in liveInfoMap) {
            val groups = LiveRoomData.subscriberGroups[roomId] ?: mutableSetOf()
            val users = LiveRoomData.subscriberUsers[roomId] ?: mutableSetOf()
            val message = roomInfo.toMessage()
            Bot.instances.forEach {
                for (groupId in groups) {
                    it.getGroup(groupId)?.sendMessage(message)
                }
                for (userId in users) {
                    it.getFriend(userId)?.sendMessage(message)
                }
            }
        }
    }
}
