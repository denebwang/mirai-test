package com.denebw.mirai.testPlugin


import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.ConsoleCommandSender
import net.mamoe.mirai.console.command.ConsoleCommandSender.bot
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.util.scopeWith
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.message.data.Image

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "com.denebw.mirai.testPlugin",
        name = "TestPlugin",
        version = "0.1.0"
    )
) {
//    val PERMISSION_EXECUTE_1 by lazy {
//        PermissionService.INSTANCE.register(permissionId("command.manage"), "manage")
//    }
    private var liveCheckJob: Job? = null
    val httpHandler = HttpHandler()
    var startedLive = mutableSetOf<Long>()
    override fun onEnable() {
//        MySetting.reload() // 从数据库自动读取配置实例
//        MyPluginData.reload()
//
//        logger.info { "Hi: ${MySetting.name}" } // 输出一条日志.
//        logger.info("Hi: ${MySetting.name}") // 输出一条日志. 与上面一条相同, 但更推荐上面一条.
//        logger.verbose("Hi: ${MySetting.name}") // 多种日志级别可选

        // 请不要使用 println, System.out.println 等标准输出方式. 请总是使用 logger.

        //MySimpleCommand.register() // 注册指令
        //MyCompositeCommand.register()
        SendPicCommand.register()
//        PERMISSION_EXECUTE_1 // 初始化, 注册权


        liveCheckJob = PluginMain.launch{
            while(true)
            {
                checkLiveRoom()
                delay(5 * 60 * 1000L)
            }
        }
    }

    override fun onDisable() {
        SendPicCommand.unregister()
        //MySimpleCommand.unregister() // 取消注册指令
        //MyCompositeCommand.unregister()
        if(liveCheckJob!=null)
        {
            liveCheckJob?.cancel()
        }
    }

    suspend fun checkLiveRoom()
    {
        var newLive = mutableSetOf<Long>()
        var liveInfoMap = mutableMapOf<Long,LiveInfo>()

        for (roomId in LiveRoomData.rooms )
        {
            if (roomId in startedLive)
                continue
            val (uid,status,title) = httpHandler.getRoomInfo(roomId)
            if(status==1)
            {
                val (username) = httpHandler.getUserName(uid)
                newLive.add(roomId)
                liveInfoMap[roomId] = LiveInfo(title,username)
            }
        }
        for (roomId in newLive)
        {
            val groups =LiveRoomData.subscriberGroups[roomId]?: mutableSetOf()
            for (groupId in  groups)
            {
                //咋发送啊
            }
        }
    }
}
