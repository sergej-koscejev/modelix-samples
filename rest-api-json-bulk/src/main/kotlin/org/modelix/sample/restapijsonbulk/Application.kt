package org.modelix.sample.restapijsonbulk

import BulkAccessWrapper
import com.typesafe.config.ConfigFactory
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.locations.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.routing.*
import org.modelix.sample.restapijsonbulk.models.apis.BulkApi

suspend fun main() {
    val config = ConfigFactory.load()
    val host = config.getString("mps.json-bulk-model-access.host")
    val port = config.getInt("mps.json-bulk-model-access.port")
    val models = config.getStringList("mps.json-bulk-model-access.models")

    // initialize the object which handles the connection to the
    // mps-json-bulk-access plugin running in our MPS instance
    BulkAccessWrapper.initialize(host, port, models)

    // start the embedded server to serve the API
    val deploymentPort = config.getInt("ktor.deployment.port")
    embeddedServer(Netty, port = deploymentPort) {
        install(CORS) {
            anyHost()
        }
        install(DefaultHeaders)
        install(ContentNegotiation) { gson() }
        install(AutoHeadResponse)
        install(Locations)
        install(Routing) {
            // the BulkApi provides the routs defined in our openapi specification
            BulkApi(BulkAccessWrapper.loadRoots, BulkAccessWrapper.resolve)
        }
    }.start(wait = true)
}