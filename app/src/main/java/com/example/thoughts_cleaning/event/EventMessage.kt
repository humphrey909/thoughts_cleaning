package com.example.thoughts_cleaning.event

import java.io.Serializable
import java.math.BigInteger

/**
 * Created by SeoKang on 2021-06-07.
 */
data class EventMessage(val title: String, val body: String) : Serializable {
    var eventType: String = ""
    var channelId: Int = 0
    var channelName: String = ""
    var isShowBadge: Boolean = true
    var pId: String = "_"
    var certNo: String = ""
    var guardianUserId: Int = 0
    var category: Int = 0
    var imgUrl: String = ""
    var suniCode: String = ""
    var roomId: Int = 0


    /**
     * For EventBus
     */
    constructor(eventType: String, pId: String, title: String, body: String) : this(title, body) {
        this.eventType = eventType
        this.pId = pId
    }


    /**
     * For EventBus
     * 보호자-사용자 연결
     */
    constructor(eventType: String, pId: String, title: String, body: String, certNo: String, guardianUserId: Int) : this(title, body) {
        this.eventType = eventType
        this.pId = pId
        this.certNo = certNo
        this.guardianUserId = guardianUserId
    }

    /**
     * For EventBus
     * 20240718 humphrey
     * 순이다방 입장 데이터
     */
    constructor(eventType: String, pId: String, title: String, body: String, category: Int) : this(title, body) {
        this.eventType = eventType
        this.pId = pId
        this.category = category
    }

    /**
     * For EventBus
     * 20241008 humphrey
     * 영상 다방 입장 데이터
     */
    constructor(eventType: String, pId: String, title: String, body: String, roomId:Int , imgUrl: String, suniCode:String) : this(title, body) {
        this.eventType = eventType
        this.pId = pId
        this.roomId = roomId
        this.imgUrl = imgUrl
        this.suniCode = suniCode
    }

    /**
     * For EventBus
     * 20241008 humphrey
     * 영상 다방 입장 데이터
     */
    constructor(eventType: String, pId: String, title: String, body: String, imgUrl: String, suniCode:String) : this(title, body) {
        this.eventType = eventType
        this.pId = pId
        this.imgUrl = imgUrl
        this.suniCode = suniCode
    }

    /**
     * For showNotification
     */
    constructor(channelId: Int, channelName:String, superData: EventMessage) : this(superData.title, superData.body) {
        this.channelId = channelId
        this.channelName = channelName
        this.eventType = superData.eventType
        this.certNo = superData.certNo
        this.guardianUserId = superData.guardianUserId
        this.category = superData.category
        this.roomId = superData.roomId
        this.imgUrl = superData.imgUrl
    }

    /**
     * For ForegroundService
     */
    constructor(channelId: Int, channelName:String, title:String, body: String) : this(title, body) {
        this.channelId = channelId
        this.channelName = channelName
        this.isShowBadge = false
    }
}
