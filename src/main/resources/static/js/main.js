(function ($) {
    "use strict";
})(jQuery);

// call on startup
document.addEventListener('DOMContentLoaded', function () {
    setDateInputs();
});

function goBack() {
    window.history.back();
}

function setDateInputs() {
    // Select all date type inputs
    const dateInputs = document.querySelectorAll('input[type="date"], input[type="time"]');
    // Loop through each date input
    dateInputs.forEach(input => {
        // Get the value from the data-value attribute
        const dataValue = input.getAttribute('data-value');
        // Set the input's value to the data-value if it exists
        if (dataValue) {
            input.value = dataValue;
        }
    });
}
