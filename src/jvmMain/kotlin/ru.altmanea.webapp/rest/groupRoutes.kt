package ru.altmanea.webapp.rest

import io.ktor.server.routing.*
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.repo.groupRepo
import ru.altmanea.webapp.repo.teachersRepo

fun Route.groupsRoutes() {
    route(Config.groupsPath){
        repoRoutes(groupRepo)
    }
}
