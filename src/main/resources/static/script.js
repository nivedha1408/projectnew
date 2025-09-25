function addQuestion() {
    alert("Dynamic question addition can be implemented here with JS DOM manipulation");
    // Example: clone a hidden template and append to form
}

/**
 * Add a new option to a question dynamically
 * @param qIndex - index of question
 */
function addOption(qIndex) {
    alert("Dynamic option addition for question " + (qIndex + 1));
}

/**
 * Quiz countdown timer
 * @param duration in seconds
 * @param display element
 */
function startTimer(duration, display) {
    let timer = duration, minutes, seconds;
    let countdown = setInterval(function () {
        minutes = parseInt(timer / 60, 10);
        seconds = parseInt(timer % 60, 10);

        minutes = minutes < 10 ? "0" + minutes : minutes;
        seconds = seconds < 10 ? "0" + seconds : seconds;

        display.textContent = minutes + ":" + seconds;

        if (--timer < 0) {
            clearInterval(countdown);
            alert("Time is up! Submitting quiz...");
            document.getElementById("quizForm").submit();
        }
    }, 1000);
}
