package component.group

import component.template.EditAddProps
import react.FC
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.label
import react.useState
import ru.altmanea.webapp.data.Group
import web.html.InputType

val CGroupAdd = FC<EditAddProps<Group>>("AddGroup"){ props ->
    var name by useState("")
    div {
        label {
            form = "groupName"
            +"Group name"
        }
        ReactHTML.input {
            type = InputType.text
            id = "groupName"
            value = name
            onChange = { name = it.target.value }
        }
        ReactHTML.button {
            +"Сохранить"
            onClick = {
                props.saveElement(Group(name))
            }
        }
    }
}
