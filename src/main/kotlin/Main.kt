package fr.behaska.notejustine

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val fileUtil = FileUtil()
    val cc1NoteByStudent = fileUtil.getNoteByStudent("CC1-2024-25.csv")
    val cc2NoteByStudent = fileUtil.getNoteByStudent("CC2-2024-25.csv")
    val anatEmbryoNoteByStudent = fileUtil.getNoteByStudent("colle-anat-embryo-nov-2024.csv")

    val notes = fileUtil.meanByStudent(listOf(cc1NoteByStudent, cc2NoteByStudent, anatEmbryoNoteByStudent))

    val results = fileUtil.createResultList(notes)

    fileUtil.writeFileFromLines("results.csv", results)
}
