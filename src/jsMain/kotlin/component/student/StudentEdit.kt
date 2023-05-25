package component.student

import component.template.EditItemProps
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.select
import react.dom.html.ReactHTML.span
import react.useRef
import react.useState
import ru.altmanea.webapp.common.Item
import ru.altmanea.webapp.data.Group
import ru.altmanea.webapp.data.GroupId
import ru.altmanea.webapp.data.Student
import tanstack.query.core.QueryKey
import tanstack.react.query.useQuery
import tools.fetchText
import web.html.HTMLSelectElement
import web.html.InputType

val CStudentEdit = FC<EditItemProps<Student>>("StudentEdit") { props ->
    var firstname by useState(props.item.elem.firstname)
    var surname by useState(props.item.elem.surname)
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
        selectGroup {
            group = props.item.elem.group
        }
    }
    button {
        +"✓"
        onClick = {
            props.saveElement(Student(firstname, surname, props.item.elem.group))
        }
    }
}

external interface SelectGroupProps : Props {
    var group: GroupId
}

val selectGroup = FC<SelectGroupProps> { props ->
    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = arrayOf("groups").unsafeCast<QueryKey>(),
        queryFn = {
            fetchText("groups/")
        }
    )
    val selectRef = useRef<HTMLSelectElement>()

    if (query.isSuccess) {
        val groups = Json.decodeFromString<List<Item<Group>>>(query.data ?: "")
        select {
            ref = selectRef
            defaultValue = props.group
            groups.map {
                option {
                    +it.elem.name
                    value = it.id
                }
            }
        }
    }
}
