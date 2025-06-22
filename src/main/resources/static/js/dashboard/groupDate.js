document.addEventListener('DOMContentLoaded', function () {
    const startDateInput = document.getElementById('startDate');

    function formatDate(inputDate) {
        if (!inputDate) return '';
        const [month, day, year] = inputDate.split('/');
        return `${year.padStart(4, '20')}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`;
    }

    const startDateValue = startDateInput.getAttribute('data-date');
    if (startDateValue) {
        startDateInput.value = formatDate(startDateValue);
    }

    startDateInput.addEventListener('change', function () {
        const value = startDateInput.value;
        const [month, day, year] = value.split('/');
        if (month && day && year) {
            const formattedDate = `${year.padStart(4, '20')}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`;
            startDateInput.setAttribute('data-date', formattedDate);
        }
    });
});
document.addEventListener('DOMContentLoaded', function () {
    const startDateInput = document.getElementById('startDate');

    startDateInput.addEventListener('change', function () {
        const value = startDateInput.value;
        const dateParts = value.split('/');
        if (dateParts.length === 3) {
            const month = dateParts[0].padStart(2, '0');
            const day = dateParts[1].padStart(2, '0');
            const year = dateParts[2].padStart(4, '20');

            if (month.length === 2 && day.length === 2 && year.length === 4) {
                const formattedDate = `${year}-${month}-${day}`;
                startDateInput.value = formattedDate;
            } else {
                alert('Sana formati noto\'g\'ri. Iltimos, mm/dd/yyyy formatida kiriting.');
            }
        }
    });
});


document.addEventListener("DOMContentLoaded", function () {
    // Barcha checkboxlarni olayapmiz va ularni ustidan o'tib chiqamiz
    document.querySelectorAll('.form-check-input').forEach(function (checkbox) {
        // Har bir checkboxga `change` event qo'shamiz
        checkbox.addEventListener('change', function () {
            // Mos keluvchi vaqt inputini olish
            const timeInput = this.closest('.form-check').querySelector('input[type="time"]');
            // Checkbox checked bo'lsa vaqt inputini faollashtirish, aks holda o'chirish
            timeInput.disabled = !this.checked;
        });

        // Sahifa yuklanganda har bir checkboxni tekshirib inputni holatini moslaymiz
        if (!checkbox.checked) {
            const timeInput = checkbox.closest('.form-check').querySelector('input[type="time"]');
            timeInput.disabled = true;
        }
    });
});


