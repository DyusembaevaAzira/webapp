package component.lesson

import component.CGrade
import component.grades.GradeInfoFull
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.span
import react.dom.html.ReactHTML.ul


external interface GradeEditProps : Props {
    var students: Array<GradeInfoFull>
    var changeStudents: (Array<GradeInfoFull>) -> Unit
}

val CGradeEdit = FC<GradeEditProps>("GradeEdit") { props ->
    ul {
        props.students.map { itemGradePair ->
            li {
                div {
                    span {
                        +itemGradePair.itemStudent.elem.fullname()
                    }
                    CGrade {
                        init = itemGradePair.grade
                        change = { newGrade ->
                            val newStudents = props.students.map {
                                if (it.itemStudent.id == itemGradePair.itemStudent.id)
                                    it.newGrade(newGrade)
                                else
                                    it
                            }.toTypedArray()
                            props.changeStudents(newStudents)
                        }
                    }
                }
            }
        }
    }
}
