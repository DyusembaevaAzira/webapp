package component.lesson

import component.template.EditAddProps
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.select
import react.useRef
import react.useState
import ru.altmanea.webapp.common.Item
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.Lesson
import ru.altmanea.webapp.data.Teacher
import tanstack.query.core.QueryKey
import tanstack.react.query.useQuery
import tools.fetchText
import web.html.HTMLSelectElement
import web.html.InputType

val CLessonAdd = FC<EditAddProps<Lesson>>("LessonNew") { props ->

    var name by useState("")
    val selectRef = useRef<HTMLSelectElement>()

    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = arrayOf("teachers").unsafeCast<QueryKey>(),
        queryFn = {
            fetchText(
                Config.teachersPath
            )
        }
    )

    if (query.isSuccess) {
        val teachers = Json.decodeFromString<List<Item<Teacher>>>(query.data ?: "")
        div {
            input {
                type = InputType.text
                value = name
                onChange = { name = it.target.value }
            }
            select {
                ref = selectRef
                teachers.map {
                    option {
                        +it.elem.shortName()
                        value = it.id
                    }
                }
            }
            button {
                +"✔"
                onClick = {
                    selectRef.current?.value?.let {
                        props.saveElement(Lesson(name, it))
                    }
                }
            }
        }
    }
}
