function openDeleteLessonPlanItemButton(e) {
    document.getElementById("deleteLessonPlanItemModalInput").value = e.getAttribute('data-id');
    var modalOpenButton = document.getElementById("lessonPlanItemDeleteModalButtonId");
    modalOpenButton.click();
}
