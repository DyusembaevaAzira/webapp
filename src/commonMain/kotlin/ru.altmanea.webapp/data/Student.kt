package ru.altmanea.webapp.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.altmanea.webapp.common.ItemId

@Serializable
class Student(
    val firstname: String,
    val surname: String,
    val group: GroupId
){
    fun fullname() =
        "$firstname $surname"
}

typealias StudentId = ItemId

val Student.json
    get() = Json.encodeToString(this)

