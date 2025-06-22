function openDeleteGroupModal(e) {
    document.getElementById("deleteGroupModalInput").value = e.getAttribute('data-id');
    var modalOpenButton = document.getElementById("groupDeleteModalButtonId");
    modalOpenButton.click();
}

