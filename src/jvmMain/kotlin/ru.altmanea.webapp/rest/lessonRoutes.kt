package ru.altmanea.webapp.rest

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import ru.altmanea.webapp.command.AddStudentToLesson
import ru.altmanea.webapp.common.Item
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.Grade
import ru.altmanea.webapp.repo.lessonsRepo
import ru.altmanea.webapp.repo.studentsRepo
import ru.altmanea.webapp.repo.teachersRepo

fun Route.lessonRoutes() {
    route(Config.lessonsPath) {
        repoRoutes(lessonsRepo)

        post(AddStudentToLesson.path) {
            val command = Json.decodeFromString(AddStudentToLesson.serializer(), call.receive())
            val lesson = lessonsRepo.read(listOf(command.lessonId)).getOrNull(0)
                ?: return@post call.respondText(
                    "No lesson with id ${command.lessonId}",
                    status = HttpStatusCode.NotFound
                )
            studentsRepo.read(listOf(command.studentId)).getOrNull(0)
                ?: return@post call.respondText(
                    "No student with id ${command.lessonId}",
                    status = HttpStatusCode.NotFound
                )
            if (lesson.elem.students.find { it.studentId == command.lessonId } != null)
                return@post call.respondText(
                    "Student already in lesson",
                    status = HttpStatusCode.BadRequest
                )
            if (command.version != lesson.version) {
                call.respondText(
                    "Lesson had updated on server",
                    status = HttpStatusCode.BadRequest
                )
            }
            val newLesson = lesson.elem.addStudent(command.studentId)
            if (lessonsRepo.update(Item(newLesson, command.lessonId, command.version))) {
                call.respondText(
                    "Student added correctly",
                    status = HttpStatusCode.OK
                )
            } else {
                call.respondText(
                    "Update error",
                    status = HttpStatusCode.BadRequest
                )
            }
        }
        get("teachers/{teacherId}") {
            val teacher = call.parameters["teacherId"] ?: return@get call.respondText(
                "No teacher id",
                status = HttpStatusCode.NotFound
            )

            val teachers = lessonsRepo.read().filter { it.elem.teacher == teacher }

            if (teachers.isEmpty()) {
                call.respondText("No teacher", status = HttpStatusCode.NotFound)
            } else {
                call.respond(teachers)
            }
        }
        get("states") {
            val lessons = lessonsRepo.read()

            if (lessons.isEmpty()) {
                return@get call.respondText("No lessons", status = HttpStatusCode.NotFound)
            }

            val lessonsWithGrade = lessons.associate {
                it.elem.name to it.elem.students.sumOf { it.grade?.mark ?: 0 } / it.elem.students.size.let {
                    if (it <= 0)
                        1
                    else
                        it
                }
            }

            call.respond(lessonsWithGrade)
        }
        get("statesNoGrade") {
            val lessons = lessonsRepo.read()

            val students = studentsRepo.read()

            if (lessons.isEmpty()) {
                return@get call.respondText("No lessons", status = HttpStatusCode.NotFound)
            }

            val lessonWithGrades = students.associate { student ->
                student.elem.fullname() to lessons.mapNotNull { lesson ->
                    lesson.elem.students.find { it.studentId == student.id }?.let {
                        if(it.grade == null){
                            lesson.elem.name
                        }else {
                            null
                        }
                    }
                }
            }

            call.respond(lessonWithGrades)
        }
        get("under"){
            val lessons = lessonsRepo.read()

            val students = studentsRepo.read()

            if (lessons.isEmpty()) {
                return@get call.respondText("No lessons", status = HttpStatusCode.NotFound)
            }

            if (students.isEmpty()) {
                return@get call.respondText("No students", status = HttpStatusCode.NotFound)
            }

            val studentsNames = students.map { student ->
                lessons.mapNotNull { lesson ->
                    lesson.elem.students.find { it.studentId == student.id }?.let {
                        if(it.grade == Grade.F){
                            student.elem.fullname()
                        }else {
                            null
                        }
                    }
                }
            }.flatten().toSet()

            call.respond(studentsNames)
        }
    }
}
