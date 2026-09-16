package org.example.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class WebsocketMessageDto(val type: String, val channel: String?) {
}

@Serializable
data class HeartbeatDto(val type: String) {}