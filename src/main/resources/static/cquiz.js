// create-quiz.js
document.addEventListener('DOMContentLoaded', () => {
    const addQuestionBtn = document.getElementById('addQuestionBtn');
    if (addQuestionBtn) addQuestionBtn.addEventListener('click', addQuestion);

    // Ensure initial renumber (in case server-side rendering produced non-zero indices)
    renumberAll();
});

/** Get current count of question blocks */
function getQuestionCount() {
    return document.querySelectorAll('#questionsContainer .question-block').length;
}

/** Add a new question (clones template and appends) */
function addQuestion() {
    const container = document.getElementById('questionsContainer');
    const template = document.getElementById('question-template').innerHTML;
    const newIndex = getQuestionCount();
    const html = template.replace(/__Q_INDEX__/g, newIndex).replace(/__DISPLAY_INDEX__/g, newIndex + 1);
    container.insertAdjacentHTML('beforeend', html);
    renumberAll();
}

/** Add a new option for a specific question index */
function addOptionForQuestion(qIndex) {
    // find question block (data-index may be string)
    const qBlock = document.querySelector(`#questionsContainer .question-block[data-index="${qIndex}"]`);
    if (!qBlock) {
        console.error('Question block not found for index', qIndex);
        return;
    }
    const optionsDiv = qBlock.querySelector('.options');
    const optCount = optionsDiv.querySelectorAll('.option-block').length;
    const optionHtml =
        `<div class="option-block">
            <label>Option ${optCount + 1}</label>
            <input type="text" class="option-text" name="questions[${qIndex}].options[${optCount}].text" required />
            <input type="hidden" class="option-correct-hidden" name="questions[${qIndex}].options[${optCount}].correct" value="false" />
            <label><input type="checkbox" class="option-correct-checkbox" name="questions[${qIndex}].options[${optCount}].correct" value="true" /> Correct</label>
            <button type="button" class="remove-option" onclick="removeOption(this)">Remove Option</button>
         </div>`;
    optionsDiv.insertAdjacentHTML('beforeend', optionHtml);
    // No need to renumber since appended at end; but keep consistent
    renumberOptions(qBlock);
}

/** Remove an option (button inside option-block) */
function removeOption(btn) {
    const optionBlock = btn.closest('.option-block');
    if (!optionBlock) return;
    const optionsDiv = optionBlock.parentElement;
    optionBlock.remove();
    const qBlock = optionsDiv.closest('.question-block');
    renumberOptions(qBlock);
}

/** Remove a question (button inside question-block). Then renumber everything. */
function removeQuestion(btn) {
    const qBlock = btn.closest('.question-block');
    if (!qBlock) return;
    qBlock.remove();
    renumberAll();
}

/** Renumber all questions and their nested option names & data-index attributes */
function renumberAll() {
    const qBlocks = document.querySelectorAll('#questionsContainer .question-block');
    qBlocks.forEach((qBlock, qIndex) => {
        // set data-index
        qBlock.setAttribute('data-index', qIndex);

        // update header display
        const h3 = qBlock.querySelector('h3');
        if (h3) h3.textContent = `Question ${qIndex + 1}`;

        // update question text input name
        const qText = qBlock.querySelector('.question-text');
        if (qText) qText.setAttribute('name', `questions[${qIndex}].text`);

        // renumber options inside this question
        renumberOptions(qBlock);
    });
}

/** Renumber option names inside a given question block */
function renumberOptions(qBlock) {
    const qIndex = Array.from(document.querySelectorAll('#questionsContainer .question-block')).indexOf(qBlock);
    const options = qBlock.querySelectorAll('.option-block');
    options.forEach((optBlock, optIndex) => {
        // option label
        const label = optBlock.querySelector('label');
        if (label) label.textContent = `Option ${optIndex + 1}`;

        // option text input
        const optText = optBlock.querySelector('.option-text');
        if (optText) optText.setAttribute('name', `questions[${qIndex}].options[${optIndex}].text`);

        // hidden correct input (false)
        let hiddenCorrect = optBlock.querySelector('.option-correct-hidden');
        if (!hiddenCorrect) {
            hiddenCorrect = document.createElement('input');
            hiddenCorrect.type = 'hidden';
            hiddenCorrect.className = 'option-correct-hidden';
            optBlock.appendChild(hiddenCorrect);
        }
        hiddenCorrect.setAttribute('name', `questions[${qIndex}].options[${optIndex}].correct`);
        hiddenCorrect.setAttribute('value', 'false');

        // checkbox correct
        let checkbox = optBlock.querySelector('.option-correct-checkbox');
        if (!checkbox) {
            const chk = document.createElement('input');
            chk.type = 'checkbox';
            chk.className = 'option-correct-checkbox';
            optBlock.appendChild(chk);
            checkbox = chk;
        }
        checkbox.setAttribute('name', `questions[${qIndex}].options[${optIndex}].correct`);
        checkbox.setAttribute('value', 'true');

        // option id (if present)
        const optId = optBlock.querySelector('.option-id');
        if (optId) {
            optId.setAttribute('name', `questions[${qIndex}].options[${optIndex}].id`);
        }
    });
}
