
# 🤖 AI Tone Picker

This is a full-stack web application that allows users to adjust the tone of their text using the Mistral AI API. The tool features a dynamic, interactive user interface, a robust backend for secure API communication, and core functionalities like undo/redo and live loading states.

---

## 📁 Project Structure
       
```
.
├── backend/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/tonepicker/backend/
│   │       │       ├── TonePickerBackendApplication.java
│   │       │       ├── controller/
│   │       │       │   └── ToneController.java
│   │       │       ├── model/
│   │       │       │   ├── ToneRequest.java
│   │       │       │   └── ToneResponse.java
│   │       │       └── service/
│   │       │           └── MistralService.java
│   │       └── resources/
│   │           └── application.properties
│   ├── pom.xml
│   └── .gitignore
├── frontend/
│   ├── index.html
│   ├── style.css
│   └── script.js
└── README.md
       
```
---
## ✅ Technologies Used

**Frontend:**  
- HTML  
- CSS  
- Vanilla JavaScript  

**Backend:**  
- Java 17+  
- Spring Boot  
- Spring WebFlux  
- Maven  

**API:**  
- Mistral AI  

**Tools:**  
- Git  
- IntelliJ / VS Code  

---

## 📌 Features

- **Dynamic Tone Picker:** An intuitive 3x3 grid for adjusting text tone along two axes: Formality and Style.  
- **Undo/Redo Functionality:** Tracks changes to text history, allowing users to revert or re-apply tone adjustments.  
- **Responsive & Interactive UI:** Fast-loading interface with seamless animations and visual feedback.  
- **Robust Error Handling:** Handles empty text inputs, network issues, and API failures gracefully.

---

## 🛠️ Technical Architecture

**Frontend:**  
- Lightweight and responsive, built with Vanilla JavaScript, HTML, and CSS.  
- Handles state management for text history, undo/redo, and live tone previews.  

**Backend:**  
- Spring Boot REST API acts as a secure proxy to the Mistral AI API.  
- Manages API keys, request caching, and error handling to prevent exposing credentials.  

---

## 🗂 State Management

- Text revisions are tracked in a `history` array with a `historyIndex`.  
- Undo/redo adjusts the `historyIndex` and updates the text area.  
- Tone adjustments are applied to the current text and stored in history for accurate state tracking.

---

## ⚠ Error Handling & Edge Cases

- **Empty Text:** Frontend blocks requests if no text is entered.  
- **API Failures:** Backend returns 502 with a descriptive message; frontend displays it.  
- **Concurrent Actions:** Interactive buttons are disabled while an API call is in progress.  
- **Invalid Inputs:** Edge cases like large text or out-of-bounds tone coordinates are validated.

---

## 🚀 How to Run Locally

### Prerequisites
- JDK 17 or higher  
- Maven  
- Mistral AI API Key  

### 1. Backend Setup
1. Navigate to `backend/`.  
2. Create `src/main/resources/application.properties` with:

spring.application.name=tone-picker-backend
server.port=8080
spring.main.allow-bean-definition-overriding=true
mistral.api.key=${MISTRAL_API_KEY}



3. Run the Spring Boot app:

```bash
./mvnw spring-boot:run
---
### 2.Frontend Setup
Navigate to frontend/.

Open index.html in your browser.

The frontend will connect to the backend at http://localhost:8080.

---
## 🌐 Deployment & Video
Deployed App Link: Insert your Vercel/Render link here

Video Recording: Insert your demo video link here
---
##📝 Notes
Frontend and backend are completely separated for modularity.

Undo/redo and tone previews are fully functional.

Loading indicators provide clear feedback while the API processes requests.

The project follows best practices for code quality, organization, and UX.

