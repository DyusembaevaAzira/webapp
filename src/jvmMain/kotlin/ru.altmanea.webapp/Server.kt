package ru.altmanea.webapp

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import kotlinx.coroutines.delay
import ru.altmanea.webapp.repo.createTestData
import ru.altmanea.webapp.rest.groupsRoutes
import ru.altmanea.webapp.rest.lessonRoutes
import ru.altmanea.webapp.rest.studentRoutes
import ru.altmanea.webapp.rest.teachersRoute


fun main() {
    embeddedServer(
        Netty,
        port = 8080,
        host = "127.0.0.1",
        watchPaths = listOf("classes")
    ) {
        main()
    }.start(wait = true)
}

fun Application.main(isTest: Boolean = true) {
    config(isTest)
    static()
    rest()
    if (isTest) logRoute()
}

fun Application.config(isTest: Boolean) {
    install(ContentNegotiation) {
        json()
    }
    install(Authentication) {
        basic("auth-basic") {
            realm = "Access to the '/basic' path"
            validate { credentials ->
                if (credentials.name == "admin" && credentials.password == "admin") {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }

        }
    }
    if (isTest) {
        createTestData()
        install(createApplicationPlugin("DelayEmulator") {
            onCall {
                delay(1000L)
            }
        })
    }
}

fun Application.rest() {
    routing {
        studentRoutes()
        lessonRoutes()
        teachersRoute()
        groupsRoutes()
    }
}
