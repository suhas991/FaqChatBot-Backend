# FAQ Chatbot - AI-Powered IT Support

A smart **FAQ chatbot** built with **Spring Boot, MongoDB, and Google Gemini AI**, featuring **role-based agents** for specialized IT support.

---

## 🚀 Features

* **Role-based AI Agents**
  Choose from 4 support roles:

  * *Help Desk*: Password resets, basic troubleshooting
  * *Technical Troubleshooting*: Network & system issues
  * *System Administration*: User/security management, servers
  * *Infrastructure Management*: Capacity planning, disaster recovery

* **Knowledge Base**: Search corporate IT knowledge

* **Conversation Memory**: Keeps chat context across turns

* **Incident Summarization**: Generates ServiceNow-ready tickets

* **Powered by Gemini AI** via **LangChain4Java**

---

## 📋 Requirements

* **Java 17+**
* **Maven 3.8+**
* **MongoDB** (local or cloud)
* **Google AI API Key** (Gemini model)
* *(Optional)* Qdrant/Milvus for semantic search

---

## ⚡ Quick Start

1. **Clone & Setup**

   ```bash
   git clone <your-repo-url>
   cd faq-chatbot
   ```

2. **Configure Database** (`application.yml`)

   ```yaml
   spring:
     data:
       mongodb:
         uri: mongodb://localhost:27017/faqchatbot
   ```

3. **Start Dependencies**

   ```bash
   docker run -p 27017:27017 mongo       # MongoDB
   docker run -p 6333:6333 qdrant/qdrant # Optional: Qdrant
   ```

4. **Run the App**

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

Server: `http://localhost:8080`

---

## 🔗 API Overview

### Chat

* `GET /api/chat` → Health check
* `POST /api/chat` → Chat with AI agent
* `POST /api/chat/reset/{sessionId}` → Reset conversation

### Agents

* `GET /api/agents/roles` → List agent roles
* `GET /api/agents/roles/{role}` → Get role details

### Knowledge Base

* `POST /api/knowledge-base/populate-sample` → Load sample data
* `POST /api/knowledge-base/add` → Add entry
* `GET /api/knowledge-base/search` → Search

---

## 💬 Example Chat Request

```json
{
  "sessionId": "session-123",
  "message": "My computer won't connect to the internet",
  "role": "TECHNICAL_TROUBLESHOOTING",
  "priority": "HIGH"
}
```

---

## 🧪 Testing with Postman

1. `GET http://localhost:8080/api/chat` – Health check
2. `POST http://localhost:8080/api/knowledge-base/populate-sample` – Load data
3. `GET http://localhost:8080/api/agents/roles` – List roles
4. `POST http://localhost:8080/api/chat` – Example chat:

   ```json
   {
     "sessionId": "test-session",
     "message": "I forgot my password",
     "role": "HELP_DESK"
   }
   ```

---

## 🔮 Roadmap

* **Phase 2**: Semantic Search (Qdrant/Milvus, embeddings)
* **Phase 3**: Agent Actions (ServiceNow integration, system diagnostics, escalation)
* **Phase 4**: Frontend (React + Vite, role selector, chat history)

---

## 🏗️ Architecture

```
Frontend (React) → Spring Boot API → MongoDB
                                  ↓
                    Google Gemini AI ← Knowledge Base
                                  ↓
                    Vector DB (Future: Qdrant/Milvus)
```

---

## 🛠️ Tech Stack

* **Backend**: Spring Boot 3.x (Java 17)
* **Database**: MongoDB
* **AI**: Google Gemini (LangChain4Java)
* **Build**: Maven
* **Containerization**: Docker
* **Search**: Text-based (Vector search planned)

---


## 🤝 Contributing

1. Fork the repo
2. Create a branch (`git checkout -b feature/your-feature`)
3. Commit (`git commit -m 'Add feature'`)
4. Push (`git push origin feature/your-feature`)
5. Open a PR

---

## 📄 License
MIT License

**Built with ❤️ using Spring Boot & AI**
