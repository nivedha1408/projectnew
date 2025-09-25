// Confirm before deleting a quiz
document.addEventListener("DOMContentLoaded", function () {
    const deleteForms = document.querySelectorAll("form[action*='delete']");
    deleteForms.forEach(form => {
        form.addEventListener("submit", function (e) {
            if (!confirm("Are you sure you want to delete this quiz?")) {
                e.preventDefault();
            }
        });
    });

    // Table search/filter
    const searchInput = document.getElementById("quizSearch");
    if (searchInput) {
        searchInput.addEventListener("keyup", function () {
            const filter = searchInput.value.toLowerCase();
            const rows = document.querySelectorAll("tbody tr");
            rows.forEach(row => {
                const text = row.innerText.toLowerCase();
                row.style.display = text.includes(filter) ? "" : "none";
            });
        });
    }
});
