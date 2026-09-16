package org.example.websocket


interface WebSocketManager {
    suspend fun connect()

    suspend fun connectOnce()

    suspend fun connectWithReconnect()


}