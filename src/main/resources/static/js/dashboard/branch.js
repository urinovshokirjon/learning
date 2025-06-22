function openDeleteBranchButton(e) {
    document.getElementById("deleteBranchModalInput").value = e.getAttribute('data-id');
    var modalOpenButton = document.getElementById("branchDeleteModalButtonId");
    modalOpenButton.click();
}