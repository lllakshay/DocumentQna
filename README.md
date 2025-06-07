# DocumentQna
## Overview
DocumentQna is a Spring Boot-based web application that provides intelligent document management and question-answering capabilities. Users can upload documents, manage them efficiently, and ask questions to get relevant answers from the document content. The application features secure user authentication and provides a comprehensive RESTful API for seamless integration.

## Features
- **User Authentication**: Secure user registration and login using JWT tokens
- **Document Upload**: Upload and store documents in various formats
- **Document Management**: Retrieve, update, and delete documents from the system
- **Intelligent QnA**: Ask questions related to uploaded documents and receive contextual answers
- **User Management**: Manage user profiles and authentication states
- **Document Retrieval**: Fetch documents by ID or get all documents for a user
- **Search Functionality**: Search through documents and their content
- **Review System**: Add and retrieve reviews for documents

## Tech Stack
- **Backend**: Spring Boot 3.x
- **Security**: Spring Security with JWT Authentication
- **Database**: MySQL with Spring Data JPA
- **ORM**: Hibernate
- **API Documentation**: Swagger UI via Springfox
- **Testing**: JUnit & Mockito
- **Containerization**: Docker & Docker Compose
- **Build Tool**: Maven

## Installation
### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL Server 8.0+
- Docker & Docker Compose (optional)

### Steps to Set Up the Application
1. **Clone the repository:**
   ```bash
   git clone https://github.com/lllakshay/DocumentQna.git
   cd DocumentQna
   ```

2. **Set up the MySQL database:**
   ```sql
   CREATE DATABASE documentqna;
   CREATE USER 'your_username'@'localhost' IDENTIFIED BY 'your_password';
   GRANT ALL PRIVILEGES ON documentqna.* TO 'your_username'@'localhost';
   FLUSH PRIVILEGES;
   ```

3. **Configure the application:**
   Update the `application.properties` file with your database credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/documentqna
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   ```

4. **Build the project:**
   ```bash
   mvn clean install
   ```

5. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

The application will be available at: `http://localhost:8080`

## Docker Deployment
### Using Docker
1. **Build the Docker image:**
   ```bash
   docker build -t documentqna .
   ```

2. **Run the Docker container:**
   ```bash
   docker run -p 8080:8080 documentqna
   ```

### Using Docker Compose
1. **Start all services:**
   ```bash
   docker-compose up --build
   ```

2. **Run in detached mode:**
   ```bash
   docker-compose up -d
   ```

3. **Stop services:**
   ```bash
   docker-compose down
   ```

## API Documentation
Swagger UI is available at: `http://localhost:8080/swagger-ui.html`

Use this interactive interface to:
- Explore all available APIs
- Test endpoints directly from the browser
- View request/response schemas
- Understand authentication requirements

## Project Structure
```
DocumentQna/
├── src/
│   ├── main/
│   │   ├── java/com/docmanagement/
│   │   │   ├── config/             # Security and Swagger configurations
│   │   │   ├── controller/         # REST API controllers
│   │   │   ├── service/            # Business logic layer
│   │   │   ├── repository/         # Data access layer
│   │   │   ├── model/              # Entity classes
│   │   │   ├── dto/                # Data Transfer Objects
│   │   │   ├── security/           # JWT and security utilities
│   │   │   └── exception/          # Custom exception handlers
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   └── test/
│       └── java/                   # Unit and integration tests
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── README.md
```

## API Endpoints
### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout

### Document Management
- `POST /api/documents` - Upload a new document
- `GET /api/documents` - Get all documents for authenticated user
- `GET /api/documents/{id}` - Get document by ID
- `PUT /api/documents/{id}` - Update document details
- `DELETE /api/documents/{id}` - Delete a document

### Question & Answer
- `POST /api/qna/ask` - Ask a question about a document
- `GET /api/qna/history` - Get QnA history for a user
- `GET /api/qna/{documentId}` - Get all questions for a specific document

## Configuration
### Database Configuration
```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/documentqna
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:password}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```

### JWT Configuration
```properties
# JWT Configuration
jwt.secret=${JWT_SECRET:mySecretKey}
jwt.expiration=${JWT_EXPIRATION:86400000}
```

### File Upload Configuration
```properties
# File Upload Configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

## Testing
### Run Unit Tests
```bash
mvn test
```

### Run Integration Tests
```bash
mvn test -Dtest=**/*IntegrationTest
```

### Generate Test Coverage Report
```bash
mvn jacoco:report
```

## CI/CD Workflow for AWS Deployment
### Prerequisites
1. **AWS Account**: Set up an AWS account with appropriate permissions
2. **IAM User**: Create IAM user with EC2, RDS, and S3 permissions
3. **EC2 Instance**: Set up an EC2 instance for application deployment
4. **RDS MySQL**: Configure MySQL database on AWS RDS
5. **GitHub Repository**: Store your code in a GitHub repository

### Steps to Set Up CI/CD
1. **Create Dockerfile** (if not already created)
2. **Set up GitHub Secrets**:
   - `AWS_ACCESS_KEY_ID`
   - `AWS_SECRET_ACCESS_KEY`
   - `DB_HOST`
   - `DB_USERNAME`
   - `DB_PASSWORD`
   - `JWT_SECRET`

3. **Create GitHub Actions Workflow**:
   Create `.github/workflows/deploy.yml` for automated deployment

4. **Configure AWS Resources**:
   - Set up EC2 instance with Docker
   - Configure RDS MySQL instance
   - Set up security groups and networking

## Environment Variables
```bash
# Database
DB_HOST=localhost
DB_PORT=3306
DB_NAME=documentqna
DB_USERNAME=your_username
DB_PASSWORD=your_password

# JWT
JWT_SECRET=your_jwt_secret_key
JWT_EXPIRATION=86400000

# File Storage
UPLOAD_DIR=/app/uploads
MAX_FILE_SIZE=10MB
```

## Troubleshooting
### Common Issues
1. **Database Connection Error**:
   - Verify MySQL is running
   - Check database credentials in `application.properties`
   - Ensure database exists

2. **JWT Token Issues**:
   - Verify JWT secret is configured
   - Check token expiration settings

3. **File Upload Problems**:
   - Check file size limits
   - Verify upload directory permissions

## Contributing
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact
For any inquiries or feedback, please contact:
- **Email**: surajlakshay@gmail.com
- **GitHub**: [lllakshay](https://github.com/lllakshay)

## Acknowledgments
- Spring Boot community for excellent documentation
- Contributors who helped improve this project
- Open source libraries that made this project possible
