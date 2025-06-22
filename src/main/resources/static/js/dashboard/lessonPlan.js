function openDeleteLessonPlanButton(e) {
    document.getElementById("deleteLessonPlanModalInput").value = e.getAttribute('data-id');
    var modalOpenButton = document.getElementById("lessonPlanDeleteModalButtonId");
    modalOpenButton.click();
}

function accordion(a){
    var acc = document.getElementsByClassName("accordion");
    var i;

    for (i = 0; i < acc.length; i++) {
        acc[i].addEventListener("click", function() {
            this.classList.toggle("active");
            var panel = this.nextElementSibling;
            if (panel.style.maxHeight) {
                panel.style.maxHeight = null;
            } else {
                panel.style.maxHeight = panel.scrollHeight + "px";
            }
        });
    }
}
