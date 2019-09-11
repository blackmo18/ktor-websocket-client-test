package com.proto

import io.ktor.client.HttpClient
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.ws
import io.ktor.http.HttpMethod
import io.ktor.http.cio.websocket.*
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import javax.swing.JOptionPane

class MainApp

@KtorExperimentalAPI
suspend fun main() {
    runBlocking {
        val client = HttpClient {
            install(WebSockets)
        }
        createClientConnection(client, 1)
    }
}


suspend fun CoroutineScope.createClientConnection(client: HttpClient, count: Int) = coroutineScope {
    println("creating client: $count")
    client.ws(
        method = HttpMethod.Get,
        host = "localhost",
        port = 3001,
        path = "/home"
    ) {
        send("Hello server from $count")
        incoming.consumeEach { frame ->
            when (frame) {
                is Frame.Text -> println(frame.readText())
            }
        }
    }
}
