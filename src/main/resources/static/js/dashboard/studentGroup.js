function openDeleteStudentGroupModal(e) {
    document.getElementById("deleteStudentGroupModalInput").value = e.getAttribute('data-id');
    var modalOpenButton = document.getElementById("studentGroupDeleteModalButtonId");
    modalOpenButton.click();
}