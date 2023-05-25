package ru.altmanea.webapp.rest

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import ru.altmanea.webapp.command.LessonWithGrades
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.Grade
import ru.altmanea.webapp.data.GradeInfo
import ru.altmanea.webapp.repo.lessonsRepo
import ru.altmanea.webapp.repo.studentsRepo

fun Route.studentRoutes() {
    route(Config.studentsPath) {
        repoRoutes(studentsRepo)
        get("ByStartName/{startName}") {
            val startName =
                call.parameters["startName"] ?: return@get call.respondText(
                    "Missing or malformed startName",
                    status = HttpStatusCode.BadRequest
                )
            val students = studentsRepo.read().filter {
                it.elem.firstname.startsWith(startName)
            }
            if (students.isEmpty()) {
                call.respondText("No students found", status = HttpStatusCode.NotFound)
            } else {
                call.respond(students)
            }
        }
        get("states/analys") {
            val students = studentsRepo.read().associateBy { it.id }

            val grades = lessonsRepo.read()

            val gradesWithStudent = students.map { student ->
                student.value.elem.fullname() to grades.map { lesson ->
                    LessonWithGrades(
                        lesson.elem.name,
                        lesson.elem.students.find { it.studentId == student.key }?.grade
                    )
                }
            }.toMap()

            if (gradesWithStudent.isEmpty()) {
                call.respondText("No student", status = HttpStatusCode.NotFound)
            } else {
                call.respond(gradesWithStudent)
            }
        }
    }
}

