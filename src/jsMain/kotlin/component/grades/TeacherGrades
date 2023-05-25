package component.grades

import component.refStyle
import csstype.AlignItems
import csstype.Display
import csstype.FlexDirection
import emotion.react.css
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.style
import react.dom.html.ReactHTML.table
import react.dom.html.ReactHTML.td
import react.dom.html.ReactHTML.ul
import react.router.dom.Link
import ru.altmanea.webapp.common.Item
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.Teacher
import tanstack.query.core.QueryKey
import tanstack.react.query.useQuery
import tools.fetchText

val CTeacherGrades = FC<Props> {
    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = arrayOf("teachers").unsafeCast<QueryKey>(),
        queryFn = {
            fetchText(Config.teachersPath)
        }
    )
    div {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            alignItems = AlignItems.center
        }

        h3 {
            +"Выберете преподавателя для выставления оценок"
        }

        if (query.isSuccess) {
            val teachers = Json.decodeFromString<List<Item<Teacher>>>(query.data ?: "")

            ul {
                teachers.map {
                    li {

                        Link {
                            +it.elem.shortName()
                            to = "teachers/${it.id}"
                        }

                    }
                }
            }
        }
    }
}
