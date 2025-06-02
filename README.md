# DocumentQna



Document Management & Q&A Application
Project Structure
document-management/
├── src/
│   ├── main/
│   │   ├── java/com/docmanagement/
│   │   │   ├── DocumentManagementApplication.java
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── SwaggerConfig.java
│   │   │   │   └── AsyncConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── DocumentController.java
│   │   │   │   ├── UserController.java
│   │   │   │   └── QAController.java
│   │   │   ├── service/
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── DocumentService.java
│   │   │   │   ├── UserService.java
│   │   │   │   ├── QAService.java
│   │   │   │   └── impl/
│   │   │   │       ├── AuthServiceImpl.java
│   │   │   │       ├── DocumentServiceImpl.java
│   │   │   │       ├── UserServiceImpl.java
│   │   │   │       └── QAServiceImpl.java
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   └── DocumentRepository.java
│   │   │   ├── entity/
│   │   │   │   ├── User.java
│   │   │   │   ├── Document.java
│   │   │   │   └── Role.java
│   │   │   ├── dto/
│   │   │   │   ├── AuthRequest.java
│   │   │   │   ├── AuthResponse.java
│   │   │   │   ├── DocumentDto.java
│   │   │   │   ├── UserDto.java
│   │   │   │   ├── QARequest.java
│   │   │   │   └── QAResponse.java
│   │   │   ├── security/
│   │   │   │   ├── JwtAuthenticationEntryPoint.java
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   └── JwtTokenProvider.java
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   └── DocumentProcessingException.java
│   │   │   └── util/
│   │   │       ├── DocumentProcessor.java
│   │   │       └── TextExtractor.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── data.sql
│   └── test/
│       └── java/com/docmanagement/
│           ├── controller/
│           ├── service/
│           └── repository/
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── README.md
