# Campaign Manager

A full-stack CRUD application for managing advertising campaigns, built with Spring Boot and React.

## Features

- Create, edit, and delete advertising campaigns
- Emerald Account balance management (deducted/refunded on campaign create/delete/edit)
- Keyword typeahead search
- Town selection from predefined dropdown
- Campaign status toggle (On/Off)
- Input validation on both frontend and backend
- In-memory H2 database

## Tech Stack

**Backend:**
- Java 25
- Spring Boot 4.0.6
- Spring Data JPA
- H2 Database (in-memory)
- Lombok
- Maven

**Frontend:**
- React 19
- Vite
- Axios

## Getting Started

### Prerequisites
- Java 25
- Node.js 18+
- Maven

### Running the Backend

```bash
cd backend
./mvnw spring-boot:run
```

Backend starts on `http://localhost:8080`

H2 Console available at `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:campaigndb`
- Username: `sa`
- Password: *(empty)*

### Running the Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend starts on `http://localhost:5173`

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/campaigns` | Get all campaigns |
| GET | `/api/campaigns/{id}` | Get campaign by ID |
| POST | `/api/campaigns` | Create campaign |
| PUT | `/api/campaigns/{id}` | Update campaign |
| DELETE | `/api/campaigns/{id}` | Delete campaign |
| GET | `/api/campaigns/account/emerald-balance` | Get Emerald balance |
| GET | `/api/campaigns/towns` | Get predefined towns |
| GET | `/api/campaigns/keywords` | Get predefined keywords |

## Project Structure

campaign-manager/
├── backend/
│   └── src/main/java/com/example/campaignmanager/
│       ├── config/          # CORS, data seeding, keywords/towns
│       ├── controller/      # REST controllers
│       ├── dto/             # Request/Response DTOs
│       ├── exception/       # Global exception handling
│       ├── model/           # JPA entities
│       ├── repository/      # Spring Data repositories
│       └── service/         # Business logic
└── frontend/
└── src/
├── api/             # Axios API layer
└── components/      # React components

## Default Emerald Account Balance

On startup the application seeds an Emerald Account with a balance of **10 000 zł**.