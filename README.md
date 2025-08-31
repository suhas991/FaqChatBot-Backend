# 📚 FAQ Chatbot with Spring Boot + LangChain4j + Google Gemini

A **context-aware FAQ Chatbot** built with:

* **Spring Boot (Java 17+)**
* **LangChain4j (Gemini integration)** for GenAI responses
* **H2 in-memory database** for storing chat history and knowledge base
* **REST API** endpoints for chat interaction, session management, and escalation

---

## 🚀 Features

* Chatbot powered by **Google Gemini API** (via LangChain4j).
* **Context-aware conversations** stored per `sessionId`.
* In-memory **H2 database** to persist chat messages & knowledge base.
* **Reset API** to clear conversations for a session.
* **Incident Escalation (planned)** – raise ServiceNow tickets when unresolved.

---

## 🛠️ Tech Stack

* **Backend:** Spring Boot 3.x, Java 17
* **AI Integration:** LangChain4j `google-ai-gemini` module
* **Database:** H2 (in-memory) with Spring Data JPA
* **Build Tool:** Maven

---

## 📂 Project Structure

```
src/main/java/com/genrative/faqchatbot
 ┣ config/
 ┃ ┗ GeminiConfig.java          # Gemini model configuration
 ┣ controller/
 ┃ ┗ ChatController.java        # REST APIs for chat
 ┣ dto/
 ┃ ┣ ChatRequest.java
 ┃ ┗ ChatResponse.java
 ┣ entity/
 ┃ ┣ Message.java               # Stores chat messages
 ┃ ┣ KnowledgeBase.java         # FAQ knowledge base entries
 ┃ ┗ ChatHistory.java           # (Optional aggregated history)
 ┣ repository/
 ┃ ┣ MessageRepository.java
 ┃ ┗ KnowledgeBaseRepository.java
 ┣ service/
 ┃ ┗ ChatService.java           # Chat + session management
 ┗ FaqChatbotApplication.java   # Spring Boot starter
```

---

## ⚙️ Setup & Installation

### 1. Clone Repo

```bash
git clone https://github.com/your-username/faq-chatbot.git
cd faq-chatbot
```

### 2. Add Google Gemini API Key

In `application.properties`:

```properties
spring.application.name=faqchatbot

# H2 Database
spring.datasource.url=jdbc:h2:mem:faqdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true

# Gemini API
GEMINI_API_KEY=your_api_key_here
```

### 3. Build & Run

```bash
mvn clean install
mvn spring-boot:run
```

---

## 📡 API Endpoints

### 1️⃣ Chat with Bot

**POST** `/api/chat`
Request:

```json
{
  "sessionId": "session-123",
  "message": "How do I reset my password?"
}
```

Response:

```json
{
  "reply": "Assistant: To reset your password, please enter your email..."
}
```

---

### 2️⃣ Reset Chat Session

**DELETE** `/api/chat/reset/{sessionId}`
Example:

```
DELETE http://localhost:8080/api/chat/reset/session-123
```

Response:

```json
"Session session-123 cleared."
```

---

### 3️⃣ (Optional) View H2 Console

```
http://localhost:8080/h2-console
```

JDBC URL: `jdbc:h2:mem:faqdb`

---

## 🗺️ Roadmap

* [x] Chat with context using LangChain4j + Gemini
* [x] Session-based chat history in H2
* [x] Reset API to clear sessions
* [ ] Incident escalation → auto-generate ServiceNow tickets with GenAI
* [ ] React + Vite frontend integration
* [ ] User authentication & role-based access control
* [ ] Knowledge base CRUD APIs with admin panel
* [ ] Multi-tenant support (per-organization FAQs)
* [ ] Integration with external APIs (email, Slack, Teams)
* [ ] Analytics dashboard for chat usage & ticket trends

---

## 📜 License

MIT License © 2025
