package component.teacher

import component.template.ElementInListProps
import react.FC
import react.dom.html.ReactHTML
import ru.altmanea.webapp.data.Teacher

val TeacherInList = FC<ElementInListProps<Teacher>> { props ->
    ReactHTML.span {
        + "${props.element.secondName} ${props.element.firstName[0]}."
    }
}
