document.addEventListener('DOMContentLoaded', () => {
    const picker = document.querySelector('[data-tag-picker]');
    if (!picker) return;

    const addButton = picker.querySelector('[data-add-tag]');
    const menu = picker.querySelector('[data-tag-menu]');
    const select = picker.querySelector('[data-tag-select]');
    const selectedTags = picker.querySelector('[data-selected-tags]');
    const selectedValues = new Set();

    fetch('/api/events')
        .then(response => response.json())
        .then(response => {
            const events = Array.isArray(response) ? response : response.value || [];
            const tags = [...new Set(events
                .flatMap(event => (event.dietaryTags || '').split(','))
                .map(tag => tag.trim())
                .filter(Boolean))].sort();

            tags.forEach(tag => {
                const option = document.createElement('option');
                option.value = tag;
                option.textContent = tag;
                select.appendChild(option);
            });
        })
        .catch(() => {
            select.disabled = true;
        });

    addButton.addEventListener('click', () => {
        menu.hidden = !menu.hidden;
        if (!menu.hidden) select.focus();
    });

    select.addEventListener('change', () => {
        const value = select.value;
        if (!value || selectedValues.has(value)) return;

        selectedValues.add(value);
        const pill = document.createElement('label');
        pill.className = 'tag-option';
        const input = document.createElement('input');
        input.type = 'checkbox';
        input.name = 'dietaryTags';
        input.value = value;
        input.checked = true;

        const text = document.createElement('span');
        text.append(document.createTextNode(value + ' '));

        const removeButton = document.createElement('button');
        removeButton.type = 'button';
        removeButton.className = 'remove-tag';
        removeButton.setAttribute('aria-label', `Remove ${value}`);
        removeButton.textContent = 'x';
        text.appendChild(removeButton);
        pill.append(input, text);

        removeButton.addEventListener('click', () => {
            selectedValues.delete(value);
            pill.remove();
        });
        selectedTags.appendChild(pill);
        select.value = '';
        menu.hidden = true;
    });
});