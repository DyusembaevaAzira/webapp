package component.lesson

import component.grades.CSelectTeacher
import component.template.EditItemProps
import js.core.jso
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.td
import react.useState
import ru.altmanea.webapp.common.Item
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.Lesson
import ru.altmanea.webapp.data.Student
import tanstack.query.core.QueryKey
import tanstack.react.query.useQuery
import tools.fetchText
import web.html.InputType
import kotlin.js.json

val CLessonEditContainer = FC<EditItemProps<Lesson>>("LessonEditContainer") { props ->
    val sk = props.item.elem.students.joinToString(separator = "") { "s" }
    val myQueryKey = arrayOf("LessonEditContainer", sk).unsafeCast<QueryKey>()
    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = myQueryKey,
        queryFn = {
            fetchText(
                "${Config.studentsPath}byId",
                jso {
                    method = "POST"
                    headers = json("Content-Type" to "application/json")
                    body = Json.encodeToString(props.item.elem.students.map { it.studentId })
                }
            )
        }
    )
    if (query.isLoading) ReactHTML.div { +"Loading .." }
    else if (query.isError) ReactHTML.div { +"Error!" }
    else {
        val studentItems =
            Json.decodeFromString<Array<Item<Student>>>(query.data ?: "")

        CLessonEdit {
            item = props.item
            students = studentItems
            saveElement = props.saveElement
        }
    }
}



external interface LessonEditProps : Props {
    var item: Item<Lesson>
    var students: Array<Item<Student>>
    var saveElement: (Lesson) -> Unit
}

val CLessonEdit = FC<LessonEditProps>("LessonEdit") { props ->
    var name by useState(props.item.elem.name)
    div {
        input {
            type = InputType.text
            value = name
            onChange = { name = it.target.value }
        }
        button {
            +"Change Name"
            onClick = {
                props.saveElement(Lesson(name, props.item.elem.teacher, props.item.elem.students))
            }
        }
        CSelectTeacher {
            teacher = props.item.elem.teacher
            save = {}
        }
        CAddStudentToLesson {
            lesson = props.item
        }
        ReactHTML.table {
            props.students.map {
                ReactHTML.tr {
                    td {
                        +it.elem.fullname()
                    }
                }
            }
        }
    }
}



