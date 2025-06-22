function openDeleteProfileButton(e) {
    document.getElementById("deleteProfileModalInput").value = e.getAttribute('data-id');
    var modalOpenButton = document.getElementById("profileDeleteModalButtonId");
    modalOpenButton.click();
    console.log(modalOpenButton);
}