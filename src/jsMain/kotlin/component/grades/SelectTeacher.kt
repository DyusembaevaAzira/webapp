package component.grades

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.select
import react.useRef
import ru.altmanea.webapp.common.Item
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.Teacher
import ru.altmanea.webapp.data.TeacherId
import tanstack.query.core.QueryKey
import tanstack.react.query.useQuery
import tools.fetchText
import web.html.HTMLSelectElement

external interface SelectTeacher : Props {
    var teacher: TeacherId
    var save: (Teacher) -> Unit
}

val CSelectTeacher = FC<SelectTeacher> { props ->
    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = arrayOf("teachers").unsafeCast<QueryKey>(),
        queryFn = {
            fetchText(
                Config.teachersPath
            )
        }
    )
    val selectRef = useRef<HTMLSelectElement>()

    if (query.isSuccess) {
        val teachers = Json.decodeFromString<List<Item<Teacher>>>(query.data ?: "")

        select {
            ref = selectRef
            defaultValue = props.teacher
            teachers.map {
                option {
                    + it.elem.shortName()
                    value = it.id
                }
            }
        }
    }
}
