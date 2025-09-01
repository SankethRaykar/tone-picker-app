document.addEventListener("DOMContentLoaded", () => {
    // Get all necessary DOM elements
    const inputText = document.getElementById("inputText");
    const undoBtn = document.getElementById("undoBtn");
    const redoBtn = document.getElementById("redoBtn");
    const resetBtn = document.getElementById("resetBtn");
    const toneButtons = document.querySelectorAll(".tone-grid button");
    const loadingStatus = document.getElementById("loadingStatus");
    const statusText = document.getElementById("statusText");
    const tonePreview = document.getElementById("tonePreview");
    const errorMsg = document.getElementById("errorMsg");

    // State variables for history management
    let history = [{ text: "", tone: "Original" }];
    let historyIndex = 0;

    // Updates the UI state (button disable status, tone preview, and active button)
    function updateUIState() {
        undoBtn.disabled = historyIndex <= 0;
        redoBtn.disabled = historyIndex >= history.length - 1;
        
        const currentTone = history[historyIndex].tone;
        tonePreview.textContent = `Tone: ${currentTone}`;

        // Add 'active' class to the correct button
        toneButtons.forEach(btn => {
            if (btn.title === currentTone) {
                btn.classList.add("active");
            } else {
                btn.classList.remove("active");
            }
        });
    }

    // Handles the API call to the backend
    async function adjustTone(text, x, y, toneTitle) {
        showLoading(true, `Applying "${toneTitle}" tone...`);
        try {
            const response = await fetch("http://localhost:8080/api/tone/adjust", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ text, x: parseInt(x), y: parseInt(y) })
            });

            const data = await response.json();

            // Checks if the API call was successful
            if (response.ok) {
                return data.adjustedText;
            } else {
                throw new Error(data.message || data.error || "Unknown error occurred.");
            }
        } catch (e) {
            showError(e.message);
            return null;
        } finally {
            showLoading(false, "");
        }
    }

    // Show/hide loading status
    function showLoading(show, message) {
        loadingStatus.style.display = show ? "flex" : "none";
        statusText.textContent = message;
        // Disable all buttons to prevent multiple clicks
        toneButtons.forEach(btn => btn.disabled = show);
        resetBtn.disabled = show;
        undoBtn.disabled = show;
        redoBtn.disabled = show;
    }

    // Display error message
    function showError(message) {
        errorMsg.textContent = message;
        setTimeout(() => {
            errorMsg.textContent = "";
        }, 5000);
    }

    // Event listener for all tone buttons
    toneButtons.forEach(btn => {
        btn.addEventListener("click", async () => {
            const text = inputText.value.trim();
            if (!text) {
                showError("Please enter some text before adjusting tone.");
                return;
            }
            errorMsg.textContent = "";

            const x = btn.dataset.x;
            const y = btn.dataset.y;
            const toneTitle = btn.title;

            // Call the API and wait for the result
            const newText = await adjustTone(text, x, y, toneTitle);

            // If a new text is returned, update history
            if (newText) {
                // Remove any "future" history before adding a new item
                history = history.slice(0, historyIndex + 1);
                history.push({ text: newText, tone: toneTitle });
                historyIndex++;
                inputText.value = newText;
            }
            updateUIState();
        });
    });

    // Undo functionality
    undoBtn.addEventListener("click", () => {
        if (historyIndex > 0) {
            historyIndex--;
            inputText.value = history[historyIndex].text;
            updateUIState();
        }
    });

    // Redo functionality
    redoBtn.addEventListener("click", () => {
        if (historyIndex < history.length - 1) {
            historyIndex++;
            inputText.value = history[historyIndex].text;
            updateUIState();
        }
    });

    // Reset button functionality
    resetBtn.addEventListener("click", () => {
        inputText.value = "";
        history = [{ text: "", tone: "Original" }];
        historyIndex = 0;
        updateUIState();
    });

    // Handle user typing
    inputText.addEventListener("input", () => {
        if (history[historyIndex].text !== inputText.value) {
            history = history.slice(0, historyIndex + 1);
            history[historyIndex] = { text: inputText.value, tone: "Original" };
            redoBtn.disabled = true;
        }
    });

    // Initial setup on page load
    updateUIState();
});