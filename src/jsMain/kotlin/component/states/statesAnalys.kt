package component.states

import component.loading
import csstype.ClassName
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.table
import react.dom.html.ReactHTML.td
import react.dom.html.ReactHTML.thead
import react.dom.html.ReactHTML.tr
import ru.altmanea.webapp.command.LessonWithGrades
import ru.altmanea.webapp.config.Config
import tanstack.query.core.QueryKey
import tanstack.react.query.useQuery
import tools.fetchText

val CStatesAnalys = FC<Props> {
    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = arrayOf("grades", "analys", "lessons", "students").unsafeCast<QueryKey>(),
        queryFn = {
            fetchText(Config.studentsPath + "states/analys")
        }
    )
    if (query.isLoading) {
        ReactHTML.div {
            ReactHTML.style {
                +loading
            }
            className = ClassName("ring")
            +"Loading"
        }
    }
    if (query.isSuccess) {
        val data = Json.decodeFromString<Map<String, List<LessonWithGrades>>>(query.data ?: "")

        table {
            thead {
                tr {
                    td {
                        + ""
                    }
                    data.map { it.value }.flatten().map { it.lesson }.toSet().map {
                        td {
                            + it
                        }
                    }
                }
            }
            data.map { student ->
                tr {
                    td {
                        +student.key
                    }
                    student.value.map {
                        td {
                            +"${it.grade?.mark ?: "-"}"
                        }
                    }
                }
            }
        }
    }
}
