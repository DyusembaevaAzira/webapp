package component.student

import component.template.EditAddProps
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.select
import react.dom.html.ReactHTML.span
import react.useRef
import react.useState
import ru.altmanea.webapp.common.Item
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.Group
import ru.altmanea.webapp.data.Student
import tanstack.query.core.QueryKey
import tanstack.react.query.useQuery
import tools.fetchText
import web.html.HTMLSelectElement
import web.html.InputType

val CStudentAdd = FC<EditAddProps<Student>>("StudentAdd") { props ->
    var firstname by useState("")
    var surname by useState("")
    val selectRef = useRef<HTMLSelectElement>()

    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = arrayOf("groups").unsafeCast<QueryKey>(),
        queryFn = {
            fetchText(
                Config.groupsPath
            )
        }
    )

    span {
        input {
            type = InputType.text
            value = firstname
            onChange = { firstname = it.target.value }
        }
        input {
            type = InputType.text
            value = surname
            onChange = { surname = it.target.value }
        }
    }
    if(query.isSuccess){
        val groups = Json.decodeFromString<List<Item<Group>>>(query.data ?: "")

        select {
            ref = selectRef
            groups.map {
                option {
                    + it.elem.name
                    value = it.id
                }
            }
        }
    }
    button {
        +"✓"
        onClick = {
            selectRef.current?.value?.let {
                props.saveElement(Student(firstname, surname, it))
            }
        }
    }
}
