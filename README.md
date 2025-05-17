# Fraud Protection Backend

A robust backend system for fraud protection and monitoring, built with Spring Boot and Jmix framework.

## 🚀 Features

- Entity management and CRUD operations
- Multi-language support with translation service
- Security and authentication
- Message queue integration
- RESTful API endpoints
- Database integration
- Docker support

## 🛠️ Technology Stack

- Java 17+
- Spring Boot
- Jmix Framework
- PostgreSQL
- Docker
- Gradle
- JUnit 5
- Mockito

## 📋 Prerequisites

- JDK 17 or higher
- Docker and Docker Compose
- Gradle
- Node.js (for frontend development)

## 🚀 Getting Started

### Local Development Setup

1. Clone the repository:
```bash
git clone https://github.com/OtabekEshpulatov/fraud-protection-backend
cd fraud-protection-backend
```

2. Start the required services using Docker Compose:
```bash
docker-compose up -d
```

3. Build and run the application:
```bash
./gradlew bootRun
```

### Docker Deployment

1. Build the Docker image:
```bash
docker build -t fraud-protection-backend .
```

2. Run the container:
```bash
docker run -p 8080:8080 fraud-protection-backend
```

## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── otabekjan/
│   │           └── fraud_protection/
│   │               ├── config/         # Configuration classes
│   │               ├── controller/     # REST controllers
│   │               ├── dto/           # Data Transfer Objects
│   │               ├── entity/        # JPA entities
│   │               ├── enums/         # Enumeration classes
│   │               ├── exceptions/    # Custom exceptions
│   │               ├── listener/      # Event listeners
│   │               ├── mq/           # Message queue handlers
│   │               ├── security/      # Security configuration
│   │               ├── service/       # Business logic services
│   │               └── view/          # View components
│   └── resources/
│       └── application.yml
└── test/
    └── java/
        └── com/
            └── otabekjan/
                └── fraud_protection/
                    └── service/       # Unit tests
```

## 🔧 Configuration

The application can be configured through `application.yml`. Key configuration parameters include:

- Database connection settings
- Security configurations
- Message queue settings
- Logging levels
- Cache configurations

## 🧪 Testing

Run the test suite:

```bash
./gradlew test
```

## 📚 API Documentation

The API documentation is available at `/swagger-ui.html` when running the application.

## 🔐 Security

The application implements several security features:

- JWT-based authentication
- Role-based access control
- Request rate limiting
- Input validation
- SQL injection prevention

## 🌐 Internationalization

The system supports multiple languages through the `TranslateService`. Translations are stored in the database and can be managed through the admin interface.

## 📦 Dependencies

Key dependencies include:

- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- Jmix Framework
- PostgreSQL Driver
- Lombok
- JUnit 5
- Mockito

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- Otabek Eshpulatov - Initial work

## 🙏 Acknowledgments

- Jmix Framework team
- Spring Boot team
- All contributors and maintainers 