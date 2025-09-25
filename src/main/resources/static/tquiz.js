document.addEventListener("DOMContentLoaded", function () {
    // Timer countdown (example: 5 minutes = 300 seconds)
    let timerElement = document.getElementById("quizTimer");
    if (timerElement) {
        let timeLeft = parseInt(timerElement.dataset.time); // in seconds
        const interval = setInterval(() => {
            let minutes = Math.floor(timeLeft / 60);
            let seconds = timeLeft % 60;
            timerElement.textContent = `⏳ Time Left: ${minutes}:${seconds < 10 ? "0" + seconds : seconds}`;
            timeLeft--;

            if (timeLeft < 0) {
                clearInterval(interval);
                alert("⏰ Time’s up! Submitting your quiz.");
                document.getElementById("quizForm").submit();
            }
        }, 1000);
    }

    // Form validation – ensure all questions answered
    const form = document.getElementById("quizForm");
    if (form) {
        form.addEventListener("submit", function (e) {
            const questions = document.querySelectorAll(".question");
            let unanswered = [];
            questions.forEach((q, index) => {
                const checked = q.querySelector("input[type='radio']:checked");
                if (!checked) {
                    unanswered.push(index + 1);
                }
            });

            if (unanswered.length > 0) {
                e.preventDefault();
                alert("⚠ Please answer all questions before submitting.\nUnanswered: " + unanswered.join(", "));
            }
        });
    }
});
