package component.states

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ul
import ru.altmanea.webapp.config.Config
import tanstack.query.core.QueryKey
import tanstack.react.query.useQuery
import tools.fetchText

val statesUnder = FC<Props> {
    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = arrayOf("grades", "under", "lessons", "students").unsafeCast<QueryKey>(),
        queryFn = {
            fetchText(Config.lessonsPath + "under")
        }
    )

    h3 {
        + "Двоешники"
    }

    div {
        if(query.isSuccess){
            val data = Json.decodeFromString<List<String>>(query.data ?: "")

            ul {
                data.map {
                    li {
                        + it
                    }
                }
            }

        }
    }

}
