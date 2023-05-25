import component.grades.CGradesContainer
import component.grades.CTeacherGrades
import component.group.CEditGroup
import component.group.CGroupAdd
import component.group.CGroupInList
import component.lesson.CLessonAdd
import component.lesson.CLessonEditContainer
import component.lesson.CLessonInList
import component.refStyle
import component.states.*
import component.student.CStudentAdd
import component.student.CStudentEdit
import component.student.CStudentInList
import component.styleDetails
import component.styles
import component.teacher.TeacherAdd
import component.teacher.TeacherEdit
import component.teacher.TeacherInList
import component.template.RestContainerChildProps
import component.template.restContainer
import component.template.restList
import csstype.Color
import csstype.LineHeight
import csstype.px
import emotion.react.css
import react.FC
import react.Props
import react.create
import react.createContext
import react.dom.client.createRoot
import react.dom.html.ReactHTML.body
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.style
import react.dom.html.ReactHTML.ul
import react.router.Route
import react.router.Routes
import react.router.dom.HashRouter
import react.router.dom.Link
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.Group
import ru.altmanea.webapp.data.Lesson
import ru.altmanea.webapp.data.Student
import ru.altmanea.webapp.data.Teacher
import tanstack.query.core.QueryClient
import tanstack.query.core.QueryKey
import tanstack.react.query.QueryClientProvider
import tanstack.react.query.devtools.ReactQueryDevtools
import web.dom.document

val invalidateRepoKey = createContext<QueryKey>()

fun main() {
    val container = document.getElementById("root")!!
    createRoot(container).render(CRoutes.create())
}

val CRoutes = FC<Props> {
    body {
        css {
            fontFamily = csstype.FontFamily.sansSerif
            color = Color("#000000")
            letterSpacing = 1.px
        }
        style {
            +(refStyle + styleDetails)
        }

        QueryClientProvider {
            client = QueryClient()
            HashRouter {
                Routes {
                    Route {
                        path = ""
                        element = mainContainer.create()
                    }
                    Route {
                        path = "info"
                        element = app.create()
                    }
                    Route {
                        path = "states"
                        element = statesContainer.create()
                    }
                    Route {
                        path = "states/analys"
                        element = CStatesAnalys.create()
                    }
                    Route {
                        path = "states/Sub"
                        element = CStatesLesson.create()
                    }
                    Route {
                        path = "states/NoGrade"
                        element = CGradesStates.create()
                    }
                    Route {
                        path = "states/FStudent"
                        element = statesUnder.create()
                    }
                    Route {
                        path = "grades"
                        element = CTeacherGrades.create()
                    }
                    Route {
                        path = "grades/teachers/:teacherId"
                        element = CGradesContainer.create()
                    }
                    Route {
                        path = "info/lessons"
                        val list: FC<RestContainerChildProps<Lesson>> =
                            restList(
                                CLessonInList,
                                CLessonAdd,
                                CLessonEditContainer
                            )
                        element = restContainer(
                            Config.lessonsPath,
                            list,
                            "lessons"
                        ).create()
                    }
                    Route {
                        path = "info/students"
                        val list: FC<RestContainerChildProps<Student>> =
                            restList(
                                CStudentInList,
                                CStudentAdd,
                                CStudentEdit
                            )
                        element = restContainer(
                            Config.studentsPath,
                            list,
                            "students"
                        ).create()
                    }
                    Route {
                        path = "info/teachers"
                        val list: FC<RestContainerChildProps<Teacher>> =
                            restList(
                                TeacherInList,
                                TeacherAdd,
                                TeacherEdit
                            )
                        element = restContainer(
                            Config.teachersPath,
                            list,
                            "teachers"
                        ).create()
                    }
                    Route {
                        path = "info/groups"
                        val list: FC<RestContainerChildProps<Group>> =
                            restList(
                                CGroupInList,
                                CGroupAdd,
                                CEditGroup
                            )
                        element = restContainer(
                            Config.groupsPath,
                            list,
                            "groups"
                        ).create()
                    }
                }
            }
            ReactQueryDevtools { }
        }
    }
}

val mainContainer = FC<Props>("MainContainer"){
    h2 {
        +"Журнал оценок"
    }
    listOf("Справочники" to "info", "Выставление оценок" to "grades", "Отчеты" to "states").map {
        li {
            Link {
                + it.first
                to = it.second
            }
        }
    }
}

val app = FC<Props>("App") {
    h2 {
        + "Справочники"
    }
    body {
        style {
            +styles
        }

        ul {
            listOf("Students" to "Студенты", "Lessons" to "Уроки", "Teachers" to "Преподаватели", "Groups" to "Группы").map { tag ->
                li {
                    Link {
                        +tag.second
                        to = tag.first.lowercase()
                    }
                }
            }
        }
    }
}
