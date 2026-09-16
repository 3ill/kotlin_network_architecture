package org.example.core.config

object WebsocketConfig {
    const val MAX_ATTEMPTS = 5
    const val BASE_URL = "wss://your-server.example.com/socket"
    const val BASE_DELAY = 1_000L
    const val MAX_DELAY = 30_000L
    const val HEARTBEAT_DELAY = 20_000L
}