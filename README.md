# 🧠 Shodh AI Code

**Shodh AI Code** is an intelligent **online code judging platform** designed to evaluate programming submissions in real time.  
It allows users to participate in coding contests, submit solutions, and view leaderboards — all powered by a scalable, Docker-based backend.

---

## 🚀 Features

### 👨‍💻 For Users
- Participate in **coding contests** with multiple problems  
- Submit solutions in **multiple languages** (Python, C++, Java, etc.)  
- Receive **real-time feedback** (Accepted / Wrong Answer / Time Limit Exceeded)  
- View **leaderboards** updating dynamically per contest  

### ⚙️ For System
- **Spring Boot backend** with REST APIs (`/api/contests`, `/api/submissions`, `/api/leaderboard`)  
- **Isolated Docker container** execution for safe code evaluation  
- **Judge service** runs user code securely with:
  - Time & memory limits  
  - Sandboxed environment (no network, read-only FS)  
  - Auto cleanup after execution  
- **MySQL** or **PostgreSQL** database for contests, users, and submissions  
- **React frontend** for a clean, interactive coding UI  

---

## 🧩 Tech Stack

| Layer | Technology |
|-------|-------------|
| **Frontend** | React.js, TailwindCSS |
| **Backend API** | Spring Boot (Java 21) |
| **Execution Engine** | Python Flask (Dockerized) |
| **Database** | MySQL / PostgreSQL |
| **Containerization** | Docker + Docker Compose |
| **Build Tools** | Maven |
| **Version Control** | Git + GitHub |

---
## 🏗️ Project Structure

shodh-ai-code/
├── backend/
│ ├── src/main/java/com/example/demo/
│ │ ├── controller/ # REST APIs (Contest, Problem, Submission)
│ │ ├── model/ # JPA Entities
│ │ ├── repository/ # Spring Data JPA Repos
│ │ ├── service/ # Business Logic & Code Judging
│ │ ├── config/ # CORS, Web Config
│ │ └── DataLoader.java # Sample data initialization
│ ├── judge/ # Python code judging service
│ ├── executor/ # Code runner Docker microservice
│ ├── pom.xml # Maven configuration
│ └── Dockerfile
│
├── frontend/ # React frontend (contest UI)
├── docker-compose.yml # Combined environment
└── README.md # You are here
---

## 🧠 How It Works

1. **User submits code** for a problem through the frontend.  
2. Backend **stores the submission** and sends it to the **Judge Service**.  
3. Judge service **spins up a Docker container** to execute the code securely.  
4. Code output is compared against **expected test cases**.  
5. Result (Accepted / Wrong Answer / Runtime Error) is stored and **leaderboard updated**.

---

## 🧰 Setup Instructions

### 🐳 Using Docker
```bash
# Clone the repository
git clone https://github.com/aastha-0711/Shodh-ai-code.git
cd Shodh-ai-code

# Build and run
docker-compose up --build

## 🏗️ Project Structure

