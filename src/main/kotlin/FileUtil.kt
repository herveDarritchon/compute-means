package fr.behaska.notejustine

import java.io.File
import java.net.URL
import java.util.*

class FileUtil {

    fun getNoteByStudent(fileName: String): Map<String, Double> {
        val fullFileName = getResourceAsText(fileName)?.file
        println("File: $fullFileName")
        if (fullFileName == null) {
            println("File not found")
            return mapOf()
        }
        val notes = readFileAsLinesUsingUseLines(fullFileName)
        println("$fileName notes: $notes")

        return notes.map {
            val item = it.split(";")
            if (item.size != 2) {
                println("Invalid line: $it")
                return@map null
            }
            val studentId = sanitize(item[0])
            val note = item[1].toDoubleOrNull()
            if (note == null) {
                println("Invalid note: ${item[1]}")
                return@map null
            }
            studentId to note
        }
            .filterNotNull()
            .filter { !it.first.startsWith("2023") }
            .toMap()
    }

    fun meanByStudent(
        exams: List<Map<String, Double>>,
    ): Map<String, Double> {
        val notesGroupedByStudent = exams.map { it.entries }
            .flatten()
            .groupBy { it.key }
            .mapValues { it.value.map { it.value } }

        val notes = notesGroupedByStudent.map { student ->
            if (student.value.size != exams.size) {
                println("Missing notes for student ${student.key}")
                return@map student.key to 0.0
            }
            val mean = computeMean(student.value)
            student.key to String.format(Locale.US, "%.2f", mean).toDouble()
        }.sortedByDescending { it.second }.toMap()
        return notes
    }

    private fun computeMean(notes: List<Double>): Double {
        return notes.average()
    }

    fun getResourceAsText(fileName: String): URL? =
        Thread.currentThread().contextClassLoader.getResource(fileName)

    fun readFileAsLinesUsingUseLines(fileName: String): List<String> =
        File(fileName).useLines { it.toList() }

    fun writeFileFromLines(fileName: String, lines: List<String>): Unit =
        File(fileName).writeText(lines.joinToString("\n"))

    fun createResultList(notes: Map<String, Double>): List<String> {
        return notes.entries
            .map { "${it.key},${it.value}" }
            .mapIndexed() { idx, note -> "${idx + 1},$note" }

    }

    fun sanitize(studentId: String): String {
        return studentId.filter { it.isLetterOrDigit() }

    }
}