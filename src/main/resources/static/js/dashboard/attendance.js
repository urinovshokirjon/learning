function toggleHomeworkSelect(checkbox) {
    var studentId = checkbox.getAttribute('data-id');
    var selectInput = document.getElementById('homework_select_input' + studentId);
    if (checkbox.checked) {
        selectInput.style.display = 'inline';
    } else {
        selectInput.style.display = 'none';
    }
}

document.addEventListener("DOMContentLoaded", function () {
    // Get the list of dates and checkboxes
    const dates = Array.from(document.querySelectorAll('input[type="date"]'));
    const checkboxes = Array.from(document.querySelectorAll('.attendance-checkbox'));

    // Function to initialize checkbox states
    function initializeCheckboxes() {
        let today = new Date().toISOString().split('T')[0];
        let initialDate = dates[0]?.value;

        // Enable the first date's checkboxes and disable the rest
        dates.forEach(date => {
            const checkboxesForDate = checkboxes.filter(cb => cb.id.includes(date.value));
            if (date.value === initialDate) {
                date.removeAttribute('disabled');
                checkboxesForDate.forEach(cb => {
                    cb.disabled = false;
                    cb.style.display = 'inline'; // Show checkboxes for enabled dates
                });
            } else {
                date.setAttribute('disabled', 'true');
                checkboxesForDate.forEach(cb => {
                    cb.disabled = true;
                    cb.style.display = 'none'; // Hide checkboxes for disabled dates
                });
            }
        });

        // Add event listener for date change
        dates.forEach(date => {
            date.addEventListener('change', function () {
                const selectedDate = this.value;
                dates.forEach(date => {
                    const checkboxesForDate = checkboxes.filter(cb => cb.id.includes(date.value));
                    if (date.value === selectedDate) {
                        date.removeAttribute('disabled');
                        checkboxesForDate.forEach(cb => {
                            cb.disabled = false;
                            cb.style.display = 'inline'; // Show checkboxes for enabled dates
                        });
                    } else {
                        date.setAttribute('disabled', 'true');
                        checkboxesForDate.forEach(cb => {
                            cb.disabled = true;
                            cb.style.display = 'none'; // Hide checkboxes for disabled dates
                        });
                    }
                });
            });
        });
    }

    initializeCheckboxes();
});