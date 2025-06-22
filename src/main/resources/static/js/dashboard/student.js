function openDeleteButton(e) {
    document.getElementById("deleteStudentGroupModalInput").value = e.getAttribute('data-id');
    document.getElementById("deleteStudentModalIdInput").value = e.getAttribute('data-student-id');
    var modalOpenButton = document.getElementById("studentDeleteModalButtonId");
    modalOpenButton.click();
}

function openStudentDeleteModal(e) {
    document.getElementById("deleteStudentModalInput").value = e.getAttribute('data-id');
    var modalOpenButton = document.getElementById("studentDeleteModalButtonId");
    modalOpenButton.click();
}

function openUpdateButton(e) {
    document.getElementById("updateStudentModalIdInput").value = e.getAttribute('data-student-id');
    document.getElementById('statusSelect').value = e.getAttribute('data-status');
    document.getElementById('messageLabel').value = e.getAttribute('data-message');
    var modalOpenButton = document.getElementById("studentUpdateModalButtonId");
    modalOpenButton.click();
}