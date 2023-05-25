package component.group

import component.template.ElementInListProps
import react.FC
import react.dom.html.ReactHTML.span
import ru.altmanea.webapp.data.Group

val CGroupInList = FC<ElementInListProps<Group>> { props ->
    span {
        + props.element.name
    }
}
