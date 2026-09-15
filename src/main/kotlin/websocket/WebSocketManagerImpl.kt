package org.example.websocket

import io.ktor.client.HttpClient

class WebSocketManagerImpl(private val client: HttpClient): WebSocketManager {
    override suspend fun connect() {
        TODO("Not yet implemented")
    }
}