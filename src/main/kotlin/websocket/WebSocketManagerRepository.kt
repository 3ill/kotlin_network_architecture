package org.example.websocket


interface WebSocketManagerRepository {
    suspend fun connect()

    suspend fun connectOnce()

    suspend fun connectWithReconnect()


}