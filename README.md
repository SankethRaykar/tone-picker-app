AI-Tone-Picker/
├── backend/
│   ├── src/
│   ├── pom.xml
│   └── .gitignore
├── frontend/
│   ├── index.html
│   ├── style.css
│   └── script.js
└── README.md





AI Tone Picker
This is a full-stack web application that allows users to adjust the tone of their text using the Mistral AI API. The tool features a dynamic, interactive user interface, a robust backend for secure API communication, and core functionalities like undo/redo and live loading states.

Table of Contents
Features

Technical Architecture

How to Run the Application Locally

Deployed Application & Video

Features
Dynamic Tone Picker: An intuitive 3x3 grid for adjusting text tone along two axes: Formality and Style.

Undo/Redo Functionality: Tracks changes to text history, allowing users to revert or re-apply tone adjustments.

Responsive & Interactive UI: The application's design adapts to various screen sizes and includes seamless animations and visual feedback for a polished user experience.

Robust Error Handling: The app gracefully handles network issues, empty text inputs, and API failures.

Technical Architecture
The application is built on a client-server architecture with a clear separation of concerns, which was a key design decision to ensure scalability and security.

Frontend: The client-side is a lightweight, responsive application built with Vanilla JavaScript, HTML, and CSS. This approach was chosen to demonstrate a strong command of core web technologies without relying on a large framework. The result is a fast-loading and highly performant user interface.

Backend: The server-side is a simple REST API developed using Java and the Spring Boot framework. Spring Boot was selected for its ease of use, security features, and speed of development. The backend's primary role is to act as a secure proxy to the Mistral AI API, preventing the API key from being exposed on the client.

State Management
The state of the application, particularly the text and its revision history, is managed entirely on the frontend. The core of this functionality relies on a history array and an integer historyIndex.

When a new tone is applied, the text is added to the history array, and the historyIndex is updated.

The undo/redo functionality works by simply incrementing or decrementing the historyIndex and setting the textarea's value to the text at that position in the array. This approach is efficient and provides a reliable way to manage the user's workflow.

Error Handling & Edge Cases
The application is designed to be resilient and provide clear feedback to the user when something goes wrong.

Empty Text: The application checks if the text area is empty before making an API call. If it is, a clear error message is displayed on the frontend, and no request is sent.

API Failures: The backend includes a try-catch block for API calls to Mistral. If the API returns an error or is unreachable, the backend sends a 502 (Bad Gateway) response to the frontend with a descriptive message. The frontend then displays this message to the user.

Concurrent Actions: All interactive buttons are disabled while an API call is in progress to prevent the user from making duplicate or conflicting requests.

How to Run the Application Locally
Prerequisites
JDK 17 or higher

Maven

A Mistral AI API Key

1. Backend Setup
Navigate to the backend/ directory.

Create an application.properties file inside src/main/resources.

Add your Mistral AI key to the file:

mistral.api.key=YOUR_API_KEY_HERE

Run the Spring Boot application using Maven:

./mvnw spring-boot:run

2. Frontend Setup
Navigate to the frontend/ directory.

Open the index.html file in your web browser.

The frontend will automatically connect to the backend running on http://localhost:8080.

Deployed Application & Video
Deployed App Link: [Insert your Vercel/Render link here]

Video Recording: [Insert your YouTube/Google Drive link here]
