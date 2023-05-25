package component.grades

import component.lesson.CGradeEdit
import component.loading
import component.styleDetails
import csstype.ClassName
import js.core.get
import js.core.jso
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.body
import react.dom.html.ReactHTML.details
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.style
import react.dom.html.ReactHTML.summary
import react.router.useParams
import ru.altmanea.webapp.common.Item
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.Grade
import ru.altmanea.webapp.data.GradeInfo
import ru.altmanea.webapp.data.Lesson
import ru.altmanea.webapp.data.Student
import tanstack.query.core.QueryKey
import tanstack.react.query.useMutation
import tanstack.react.query.useQuery
import tanstack.react.query.useQueryClient
import tools.HTTPResult
import tools.fetch
import tools.fetchText
import kotlin.js.json

val CGradesContainer = FC<Props> {
    val params = useParams()["teacherId"] ?: ""

    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = arrayOf("lessons", params).unsafeCast<QueryKey>(),
        queryFn = {
            fetchText(Config.lessonsPath + Config.teachersPath + params)
        }
    )

    if (query.isLoading) {
        div {
            ReactHTML.style {
                +loading
            }
            className = ClassName("ring")
            +"Loading"
        }
    }
    if (query.isSuccess) {
        val lessons = Json.decodeFromString<List<Item<Lesson>>>(query.data ?: "")

            lessons.map {
                details {
                    summary { +it.elem.name }
                    div {
                        GradesInfo {
                            lesson = it
                            studentsId = it.elem.students
                        }
                    }
                }
            }
    }
}

external interface GradesInfoProps : Props {
    var lesson: Item<Lesson>
    var studentsId: Array<GradeInfo>
}

class GradeInfoFull(
    val itemStudent: Item<Student>,
    val grade: Grade?
) {
    fun newGrade(grade: Grade?) =
        GradeInfoFull(itemStudent, grade)
}

val GradesInfo = FC<GradesInfoProps> { props ->
    val queryClient = useQueryClient()
    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = arrayOf("lessons", props.studentsId.joinToString { it.studentId }).unsafeCast<QueryKey>(),
        queryFn = {
            fetchText(
                "${Config.studentsPath}byId",
                jso {
                    method = "POST"
                    headers = json("Content-Type" to "application/json")
                    body = Json.encodeToString(props.studentsId.map { it.studentId })
                }
            )
        }
    )

    val updateMutation = useMutation<HTTPResult, Any, Item<Lesson>, Any>(
        mutationFn = { item: Item<Lesson> ->
            fetch(
                Config.lessonsPath,
                jso {
                    method = "PUT"
                    headers = json(
                        "Content-Type" to "application/json"
                    )
                    body = Json.encodeToString(item)
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(arrayOf("lessons").unsafeCast<QueryKey>())
            }
        }
    )

    if (query.isSuccess) {
        val students = Json.decodeFromString<List<Item<Student>>>(query.data ?: "")
            .associateBy { it.id }
        val studentGrades = props.studentsId.mapNotNull { pair ->
            students[pair.studentId]?.let {
                GradeInfoFull(it, pair.grade)
            }
        }.toTypedArray()

        CGradeEdit {
            this.students = studentGrades
            changeStudents = {
                updateMutation.mutateAsync(
                    Item(
                        Lesson(
                            props.lesson.elem.name,
                            props.lesson.elem.teacher,
                            it.map { GradeInfo(it.itemStudent.id, it.grade) }.toTypedArray()
                        ),
                        props.lesson.id,
                        props.lesson.version
                    ), null
                )
            }
        }
    }
}
