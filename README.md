# DocumentQna

---

## Overview

DocumentQna is a **Spring Boot**-based application designed to manage documents and facilitate question-answering (QnA) functionalities. It provides RESTful APIs for user authentication, document ingestion, retrieval, and QnA services.

---

## Features

- **User Authentication:** Secure user registration and login using JWT tokens.
- **Document Management:** Upload, retrieve, and manage documents efficiently.
- **QnA Module:** Ask questions related to uploaded documents and receive relevant answers.
- **Modular Architecture:** Separation of concerns with distinct layers for controllers, services, and repositories.
- **Swagger Integration:** Interactive API documentation for easy testing and integration.
- **Docker Support:** Containerized application setup using Docker and Docker Compose.

---

## Technologies Used

- Java 17
- Spring Boot 3.x
- Spring Security
- Spring Data JPA
- Hibernate
- MySQL
- Swagger (Springfox)
- Docker & Docker Compose
- JUnit & Mockito

---

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL Server
- Docker & Docker Compose (optional for containerization)

---

### Installation

1. **Clone the repository:**

   ```bash
   git clone https://github.com/lllakshay/DocumentQna.git
   cd DocumentQna


## Configure the Database

Update the application.properties file with your MySQL credentials:

spring.datasource.url=jdbc:mysql://localhost:3306/documentqna
spring.datasource.username=your_username
spring.datasource.password=your_password

---

## Build the Project

```bash
mvn clean install

---

## Run the Application

mvn spring-boot:run

The application will start on: 

```bash
http://localhost:8080

---

## Docker Deployment (Optional)

### Build the Docker Image
```bash
docker build -t documentqna .

### Run the Docker Container
```bash
docker run -p 8080:8080 documentqna

### Using Docker Compose

If you have a docker-compose.yml file, start the services with:
```bash
docker-compose up --build

---

## API Documentation

Swagger UI is available at:
```bash
http://localhost:8080/swagger-ui.html

Use this interactive interface to test and explore the available APIs easily.

---

## Project Structure
```bash
DocumentQna/
├── src/
│   ├── main/
│   │   ├── java/com/docmanagement/
│   │   │   ├── config/             # Security and Swagger configurations
│   │   │   ├── controller/         # REST controllers
│   │   │   ├── service/            # Service interfaces and implementations
│   │   │   ├── repository/         # JPA repositories
│   │   │   └── model/              # Entity classes
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
├── pom.xml
└── README.md

---

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request for any enhancements or bug fixes.

---

## License

This project is licensed under the MIT License. See the LICENSE file for details.

---

## Contact

For any inquiries or feedback, please contact:  
surajlakshay@gmail.com
