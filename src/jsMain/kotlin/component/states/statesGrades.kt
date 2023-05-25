package component.states

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.table
import react.dom.html.ReactHTML.td
import react.dom.html.ReactHTML.thead
import react.dom.html.ReactHTML.tr
import react.dom.html.ReactHTML.ul
import ru.altmanea.webapp.config.Config
import tanstack.query.core.QueryKey
import tanstack.react.query.useQuery
import tools.fetchText

val CGradesStates = FC<Props> {
    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = arrayOf("grades", "grades", "lessons", "students").unsafeCast<QueryKey>(),
        queryFn = {
            fetchText(Config.lessonsPath + "statesNoGrade")
        }
    )

    div {
        h3 {
            +"Студенты без оценок"
        }
        if (query.isSuccess) {
            val data = Json.decodeFromString<Map<String, List<String>>>(query.data ?: "")

            table {
                thead {
                    tr {
                        td {
                            +"Ученик"
                        }
                        td {
                            +"Предметы"
                        }
                    }
                }
                data.map {
                    if (it.value.isNotEmpty()) {
                        tr {
                            td {
                                +it.key
                            }
                            td {
                                div {
                                    ul {
                                        it.value.map {
                                            li {
                                                +it
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }

    }
}
