document.addEventListener('DOMContentLoaded', function () {
    const dropdownList = document.querySelectorAll('.multi-select-dropdown');
    dropdownList.forEach(dropdown => {
        const checkboxes = dropdown.querySelectorAll('input[type="checkbox"]');
        const selectedItems = dropdown.querySelector('.selected-items');
        const dropdownToggle = dropdown.querySelector('.dropdown-toggle');
        const selectForBindingTagId = dropdown.getAttribute('data-select-for-binding-tag-id');
        const dropdownMenu = dropdown.querySelector('.dropdown-menu');

        let selectedOrder = [];
        let firstTimeInUse = true;

        function updateSelectedItems() {
            // set selected values in case updates
            if (firstTimeInUse) {
                for (const child of selectedItems.children) {
                    const value = child.getAttribute("data-value");
                    selectedOrder.push(value);
                    checkboxes.forEach(cb => {
                        if (cb.value === value) {
                            cb.checked = true;
                        }
                    });
                }
                firstTimeInUse = false;
            }

            selectedItems.innerHTML = '';
            if (selectedOrder.length === 0) {
                selectedItems.textContent = 'Select options';
                return;
            }

            const selectedItemsTagForBinding = document.getElementById(selectForBindingTagId);
            if (selectedItemsTagForBinding != null) {
                selectedItemsTagForBinding.replaceChildren()
            }

            selectedOrder.forEach(value => {
                const checkbox = Array.from(checkboxes).find(cb => cb.value === value);
                if (checkbox && checkbox.checked) {
                    // to view
                    const badge = document.createElement('span');
                    badge.className = 'badge';
                    badge.value = checkbox.value;
                    badge.textContent = checkbox.parentElement.textContent;
                    // remove icon
                    const removeIcon = document.createElement('span');
                    removeIcon.className = 'remove-item';
                    removeIcon.innerHTML = '&times;';
                    removeIcon.onclick = (event) => {
                        console.log("remove")
                        event.stopPropagation();
                        const checkbox = Array.from(checkboxes).find(cb => cb.value === value);
                        if (checkbox) {
                            checkbox.checked = false;
                        }
                        selectedOrder = selectedOrder.filter(item => item !== value);
                        updateSelectedItems();
                        // Ensure dropdown stays closed
                        dropdownMenu.classList.remove('show');
                    };
                    badge.appendChild(removeIcon);
                    selectedItems.appendChild(badge);
                    // to binding
                    if (selectedItemsTagForBinding != null) {
                        const option = document.createElement('option');
                        option.value = checkbox.value;
                        option.textContent = checkbox.parentElement.textContent;
                        option.selected = true;
                        selectedItemsTagForBinding.appendChild(option);
                    }
                }
            });
        }

        dropdownToggle.addEventListener('click', () => {
            // dropdownMenu.classList.toggle('show');
            dropdownMenu.classList.add('show');
        });

        checkboxes.forEach(checkbox => {
            checkbox.addEventListener('change', () => {
                if (checkbox.checked) {
                    selectedOrder.push(checkbox.value);
                } else {
                    selectedOrder = selectedOrder.filter(value => value !== checkbox.value);
                }
                updateSelectedItems();
            });
        });

        // Close dropdown when clicking outside
        document.addEventListener('click', (event) => {
            if (!dropdownToggle.contains(event.target) && !dropdownMenu.contains(event.target)) {
                dropdownMenu.classList.remove('show');
            }
        });

        updateSelectedItems(); // Initialize with default state
    });
});