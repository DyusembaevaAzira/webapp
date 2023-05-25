package ru.altmanea.webapp.repo

import ru.altmanea.webapp.common.Item
import ru.altmanea.webapp.data.*

val studentsRepo = ListRepo<Student>()
val lessonsRepo = ListRepo<Lesson>()
val teachersRepo = ListRepo<Teacher>()
val groupRepo = ListRepo<Group>()

fun createTestData() {
    listOf("20z", "20m").map {
        groupRepo.create(Group(it))
    }
    val groupFirst = groupRepo.read().findLast { it.elem.name == "20z" }
    check(groupFirst != null)
    val groupSecond = groupRepo.read().findLast { it.elem.name == "20m" }
    check(groupSecond != null)

    listOf(
        Student("Sheldon", "Cooper", groupFirst.id),
        Student("Leonard", "Hofstadter", groupFirst.id),
        Student("Howard", "Wolowitz", groupSecond.id),
        Student("Penny", "Hofstadter", groupSecond.id),
    ).apply {
        map {
            studentsRepo.create(it)
        }
    }

    listOf(
        Teacher(
            "Ivan",
            "Ivanov"
        ),
        Teacher(
            "Alex",
            "Popov"
        )
    ).apply {
        map {
            teachersRepo.create(it)
        }
    }
    val ivan = teachersRepo.read().findLast { it.elem.firstName == "Ivan" }
    check(ivan != null)
    val alex = teachersRepo.read().findLast { it.elem.firstName == "Alex" }
    check(alex != null)

        listOf(
            Lesson("Math", ivan.id),
            Lesson("Phys", ivan.id),
            Lesson("Story", alex.id),
        ).apply {
            map {
                lessonsRepo.create(it)
            }
        }


    val students = studentsRepo.read()
    val lessons = lessonsRepo.read()
    val sheldon = students.findLast { it.elem.firstname == "Sheldon" }
    check(sheldon != null)
    val leonard = students.findLast { it.elem.firstname == "Leonard" }
    check(leonard != null)
    val math = lessons.findLast { it.elem.name == "Math" }
    check(math != null)
    val newMath = Lesson(
        math.elem.name,
        math.elem.teacher,
        arrayOf(
            GradeInfo(sheldon.id, Grade.A),
            GradeInfo(leonard.id, Grade.B)
        )
    )

    lessonsRepo.update(Item(newMath, math.id, math.version))
}
