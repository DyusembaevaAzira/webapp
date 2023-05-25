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
import react.dom.html.ReactHTML.tr
import ru.altmanea.webapp.config.Config
import tanstack.query.core.QueryKey
import tanstack.react.query.useQuery
import tools.fetchText

val CStatesLesson = FC<Props> {
    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = arrayOf("grades", "lessons", "students").unsafeCast<QueryKey>(),
        queryFn = {
            fetchText(Config.lessonsPath + "states")
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
        val data = Json.decodeFromString<Map<String, Int>>(query.data ?: "")

        table {
            ReactHTML.thead {
                ReactHTML.tr {
                    td {
                        +"Предмет"
                    }
                    td {
                        +"Средняя оценка"
                    }
                }
                data.map {
                    tr {
                        td {
                            +it.key
                        }
                        td {
                            +it.value.toString()
                        }
                    }
                }

            }
        }
    }
}
