package com.denebw.mirai.testPlugin

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import com.google.gson.Gson
import com.google.gson.JsonParser

class HttpHandler {
    private val checkRoomUrl: String = "https://api.live.bilibili.com/room/v1/Room/get_info"
    private val checkUserUrl: String = "http://api.bilibili.cn/userinfo"
    private val gson = Gson()
    private val client = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .build()

    fun getRoomInfo(roomId: Long): RoomResponseData {
        val checkRoomRequest = HttpRequest.newBuilder()
            .uri(URI("$checkRoomUrl?id=$roomId"))
            //.header("cookie", LiveCheckConfig.cookie)
            .timeout(Duration.ofSeconds(10))
            .GET()
            .build()
        val response = client.send(checkRoomRequest, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() == 200) {
            val objects = JsonParser.parseString(response.body()).asJsonObject.getAsJsonObject("data")
            val started: Int = gson.fromJson(objects.get("live_status"), Int::class.java)
            val uid: Long = gson.fromJson(objects.get("uid"), Long::class.java)
            val title =gson.fromJson(objects.get("title"),String::class.java)
            return RoomResponseData(uid, started, title)
        } else {
            PluginMain.logger.error("Connection failed!")
        }
        return RoomResponseData(0, 0)
    }

    fun getUserName(uid:Long):UserResponseData
    {
        val getUserInfoRequest = HttpRequest.newBuilder()
        .uri(URI("$checkUserUrl?mid=$uid"))
        .timeout(Duration.ofSeconds(10))
        .GET()
        .build()
        val response = client.send(getUserInfoRequest, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() == 200) {
            val objects = JsonParser.parseString(response.body()).asJsonObject.getAsJsonObject("data")
            val name = gson.fromJson(objects.get("name"), String::class.java)
            return UserResponseData(name)
        } else {
            PluginMain.logger.error("Connection failed!")
        }
        return UserResponseData("")
    }
}