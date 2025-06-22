function openDeleteSubjectModal(e) {
    document.getElementById("deleteSubjectModalInput").value = e.getAttribute('data-id');
    var modalOpenButton = document.getElementById("subjectDeleteModalButtonId");
    modalOpenButton.click();
}
