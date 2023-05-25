package component.group

import component.template.EditItemProps
import react.FC
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import react.useState
import ru.altmanea.webapp.data.Group
import web.html.InputType

val CEditGroup = FC<EditItemProps<Group>>("EditGroup") { props ->
    var name by useState(props.item.elem.name)
    div {
        label {
            form = "groupName"
            +"Group name"
        }
        input {
            type = InputType.text
            id = "groupName"
            value = name
            onChange = { name = it.target.value }
        }
        button {
            +"Сохранить"
            onClick = {
                props.saveElement(Group(name))
            }
        }
    }
}
