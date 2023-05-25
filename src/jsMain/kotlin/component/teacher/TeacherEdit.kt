package component.teacher

import component.styles
import component.template.EditItemProps
import js.core.jso
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ol
import react.dom.html.ReactHTML.style
import react.useState
import ru.altmanea.webapp.common.Item
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.Lesson
import ru.altmanea.webapp.data.LessonId
import ru.altmanea.webapp.data.Teacher
import tanstack.query.core.QueryKey
import tanstack.react.query.useQuery
import tools.fetchText
import web.html.InputType
import kotlin.js.json

val TeacherEdit = FC<EditItemProps<Teacher>> { props ->
    var firstName by useState(props.item.elem.firstName)
    var secondName by useState(props.item.elem.secondName)

    div {
        style {
            +styles
        }
        label {
            form = "firstName"
            +"Имя"
        }
        input {
            id = "firstName"
            type = InputType.text
            value = firstName
            onChange = { firstName = it.target.value }
        }
        label {
            form = "secondName"
            +"Фамилия"
        }
        input {
            id = "secondName"
            type = InputType.text
            value = secondName
            onChange = { secondName = it.target.value }
        }
    }
    /*CTeacherLessons {
        lessons = props.item.elem.
    }*/
}

external interface TeacherLessonProps : Props {
    var lessons: Array<LessonId>
}

val CTeacherLessons = FC<TeacherLessonProps> { props ->
    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = arrayOf("lessons", props.lessons.joinToString { it }).unsafeCast<QueryKey>(),
        queryFn = {
            fetchText(Config.lessonsPath + "byId",
                jso {
                    method = "POST"
                    headers = json("Content-Type" to "application/json")
                    body = Json.encodeToString(props.lessons)
                }
            )
        }
    )
    h3 {
        +"Уроки преподавателя"
    }

    if (query.isLoading) {
        div {
            +"Loading"
        }
    } else if (query.isSuccess) {
        val data = try {
            Json.decodeFromString<List<Item<Lesson>>>(query.data ?: "")
        } catch (e: Exception) {
            emptyList()
        }

        div {
            ol {
                data.map {
                    li {
                        +it.elem.name
                    }
                }
            }

        }
    }
}
