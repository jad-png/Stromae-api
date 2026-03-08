# Stromae Video Streaming API - Microservices Architecture

## Overview

Stromae is a complete backend application for a video streaming platform built with microservices architecture. It demonstrates modern Spring Boot and Spring Cloud technologies, including service discovery, API gateway, centralized configuration, and inter-service communication using OpenFeign.

## Project Structure

```
stromae-api/
├── config-service/              # Centralized Configuration Server
├── discovery-service/           # Eureka Service Registry
├── gateway-service/             # Spring Cloud Gateway
├── video-service/               # Video Content Management
├── user-service/                # User Management, Watchlist, History
├── security-service/            # JWT Authentication (Bonus)
├── monitoring-service/          # Monitoring & Metrics (Bonus)
├── docker-compose.yml           # Docker orchestration
└── pom.xml                      # Parent POM
```

## Architecture Overview

### Microservices

1. **Config Service (Port 8888)**
   - Centralized configuration management
   - Uses Git repository for configuration storage
   - Spring Cloud Config Server

2. **Discovery Service (Port 8761)**
   - Eureka server for service discovery
   - All microservices register themselves
   - Enables dynamic service communication

3. **Gateway Service (Port 8080)**
   - Single entry point for all API requests
   - Route configuration and load balancing
   - Spring Cloud Gateway
   - JWT token validation

4. **Video Service (Port 8081)**
   - CRUD operations for video content
   - Search by category, type, title, director
   - MySQL database (stromae_video_db)
   - Entities: Video

5. **User Service (Port 8082)**
   - User management (CRUD)
   - Watchlist management
   - Watch history tracking
   - Viewing statistics
   - MySQL database (stromae_user_db)
   - Entities: User, Watchlist, WatchHistory
   - OpenFeign client to communicate with Video Service

6. **Security Service (Port 8083) - Bonus**
   - JWT token generation and validation
   - Stateless authentication
   - Login endpoint
   - Token validation endpoint

7. **Monitoring Service (Port 8084) - Bonus**
   - Health status monitoring
   - System metrics collection
   - Memory, CPU, and thread metrics
   - Prometheus integration

## Technologies Used

- **Spring Boot 3.1.5**
- **Spring Cloud 2022.0.4**
- **Spring Data JPA** - Database access
- **Spring Cloud Gateway** - API Gateway
- **Spring Cloud Eureka** - Service Discovery
- **Spring Cloud Config** - Centralized Configuration
- **OpenFeign** - Inter-service communication
- **JWT (jjwt)** - Token-based authentication
- **MySQL 8.0** - Relational database
- **Docker & Docker Compose** - Containerization
- **Maven** - Build tool
- **Lombok** - Boilerplate reduction
- **MapStruct** - DTO mapping
- **Spring Security** - Security framework

## Entity Relationships

### Video Service Entities
```
Video
├── id (PK)
├── title
├── description
├── thumbnailUrl
├── trailerUrl (YouTube embed)
├── duration
├── releaseYear
├── type (FILM/SERIE)
├── category (ACTION/COMEDIE/DRAME/SCIENCE_FICTION/THRILLER/HORREUR)
├── rating
├── director
├── cast
├── createdAt
└── updatedAt
```

### User Service Entities
```
User
├── id (PK)
├── username (UNIQUE)
├── email (UNIQUE)
├── password
├── createdAt
└── updatedAt

Watchlist
├── id (PK)
├── userId (FK)
├── videoId (FK to video-service)
├── addedAt
└── UNIQUE(userId, videoId)

WatchHistory
├── id (PK)
├── userId (FK)
├── videoId (FK to video-service)
├── watchedAt
├── progressTime
├── completed
└── UNIQUE(userId, videoId)
```

## API Endpoints

### Video Service (`/api/videos`)

#### Create Video
```http
POST /api/videos
Content-Type: application/json

{
  "title": "Inception",
  "description": "A sci-fi thriller",
  "thumbnailUrl": "https://example.com/inception.jpg",
  "trailerUrl": "https://www.youtube.com/embed/YoHD_XwstME",
  "duration": 8880,
  "releaseYear": 2010,
  "type": "FILM",
  "category": "SCIENCE_FICTION",
  "rating": 8.8,
  "director": "Christopher Nolan",
  "cast": "Leonardo DiCaprio, Marion Cotillard"
}
```

#### Get Video by ID
```http
GET /api/videos/{id}
```

#### Get All Videos
```http
GET /api/videos
```

#### Update Video
```http
PUT /api/videos/{id}
Content-Type: application/json

{
  "title": "Updated Title",
  "rating": 9.0
}
```

#### Delete Video
```http
DELETE /api/videos/{id}
```

#### Get Videos by Category
```http
GET /api/videos/category/ACTION
```

#### Get Videos by Type
```http
GET /api/videos/type/FILM
```

#### Search by Title
```http
GET /api/videos/search/title?query=Inception
```

#### Search by Director
```http
GET /api/videos/search/director?query=Nolan
```

### User Service (`/api/users`)

#### Create User
```http
POST /api/users
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123"
}
```

#### Get User by ID
```http
GET /api/users/{id}
```

#### Get User by Username
```http
GET /api/users/username/{username}
```

#### Get All Users
```http
GET /api/users
```

#### Update User
```http
PUT /api/users/{id}
Content-Type: application/json

{
  "username": "john_updated",
  "password": "newpassword123"
}
```

#### Delete User
```http
DELETE /api/users/{id}
```

### Watchlist Service (`/api/watchlist`)

#### Add to Watchlist
```http
POST /api/watchlist
Content-Type: application/json

{
  "userId": 1,
  "videoId": 1
}
```

#### Get User's Watchlist
```http
GET /api/watchlist/user/{userId}
```

#### Remove from Watchlist
```http
DELETE /api/watchlist/{userId}/{videoId}
```

#### Check if Video in Watchlist
```http
GET /api/watchlist/{userId}/{videoId}
```

### Watch History Service (`/api/watch-history`)

#### Save Watch History
```http
POST /api/watch-history
Content-Type: application/json

{
  "userId": 1,
  "videoId": 1,
  "progressTime": 3600,
  "completed": false
}
```

#### Get User's Watch History
```http
GET /api/watch-history/user/{userId}
```

#### Get Watch History for Video
```http
GET /api/watch-history/user/{userId}/video/{videoId}
```

#### Get Viewing Statistics
```http
GET /api/watch-history/statistics/user/{userId}
```

### Security Service (`/api/auth`) - Bonus

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "password123"
}

Response:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expiresIn": 86400
}
```

#### Validate Token
```http
POST /api/auth/validate?token={token}
```

#### Get User from Token
```http
GET /api/auth/token/user?token={token}
```

### Monitoring Service (`/api/monitoring`) - Bonus

#### Get Health Status
```http
GET /api/monitoring/health
```

#### Get System Metrics
```http
GET /api/monitoring/metrics
```

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- Docker & Docker Compose
- MySQL 8.0 (if running locally)
- Git

### Local Development Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd stromae-api
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Start MySQL databases (local)**
   ```bash
   # Create databases
   mysql -u root -p < create_databases.sql
   ```

4. **Start services in order**
   ```bash
   # Terminal 1: Config Service
   cd config-service
   mvn spring-boot:run
   
   # Terminal 2: Discovery Service
   cd discovery-service
   mvn spring-boot:run
   
   # Terminal 3: Video Service
   cd video-service
   mvn spring-boot:run
   
   # Terminal 4: User Service
   cd user-service
   mvn spring-boot:run
   
   # Terminal 5: Security Service
   cd security-service
   mvn spring-boot:run
   
   # Terminal 6: Monitoring Service
   cd monitoring-service
   mvn spring-boot:run
   
   # Terminal 7: Gateway Service
   cd gateway-service
   mvn spring-boot:run
   ```

### Docker Compose Deployment

1. **Build and start all services**
   ```bash
   docker-compose up --build
   ```

2. **Verify services are running**
   ```bash
   docker-compose ps
   ```

3. **View logs**
   ```bash
   docker-compose logs -f <service-name>
   ```

4. **Stop services**
   ```bash
   docker-compose down
   ```

## Service Discovery & Health Checks

### Eureka Dashboard
- Access at: `http://localhost:8761`
- All registered services will appear here
- Monitor service status and instance count

### Gateway Health
- Access at: `http://localhost:8080/actuator/health`

### Service-specific Health
- Video Service: `http://localhost:8081/actuator/health`
- User Service: `http://localhost:8082/actuator/health`
- Security Service: `http://localhost:8083/actuator/health`
- Monitoring Service: `http://localhost:8084/actuator/health`

## Configuration Management

### Git Repository Setup

The Config Service reads configurations from a Git repository. To set up:

1. **Create a Git repository** (local or remote)
   ```bash
   mkdir -p /tmp/stromae-config-repo
   cd /tmp/stromae-config-repo
   git init
   ```

2. **Create configuration files**
   - `application.yml` - Shared configuration
   - `video-service.yml` - Video service config
   - `user-service.yml` - User service config
   - `security-service.yml` - Security service config
   - `monitoring-service.yml` - Monitoring config
   - `gateway-service.yml` - Gateway config

3. **Update Config Service configuration**
   Edit `config-service/src/main/resources/application.yml`:
   ```yaml
   spring:
     cloud:
       config:
         server:
           git:
             uri: /tmp/stromae-config-repo
   ```

## Testing with Postman

1. **Import the collection** (Postman collection file should be provided)
2. **Set environment variables**
   - `gatewayUrl`: `http://localhost:8080`
   - `token`: Generated JWT token from login endpoint

3. **Test flow**
   - Login to get JWT token
   - Create users
   - Create videos
   - Add to watchlist
   - Save watch history
   - Get statistics

## Code Quality & Testing

### Running Tests
```bash
mvn test
```

### Code Coverage
```bash
mvn jacoco:report
```

### Build Analysis
```bash
mvn sonar:sonar
```

## Deployment

### Production Considerations

1. **Security**
   - Change default JWT secret
   - Enable HTTPS
   - Set strong database passwords
   - Implement rate limiting

2. **Performance**
   - Cache-Control headers
   - Database indexing
   - Service-to-service timeouts
   - Circuit breakers

3. **Monitoring**
   - Centralized logging (ELK Stack, Splunk)
   - Distributed tracing (Jaeger, Zipkin)
   - Prometheus metrics collection
   - Alert thresholds

4. **Scaling**
   - Kubernetes deployment
   - Horizontal pod autoscaling
   - Load balancing configuration

## Common Issues & Troubleshooting

### Services can't register with Eureka
- Ensure Eureka server is running first
- Check network connectivity between services
- Verify `eureka.client.serviceUrl.defaultZone` configuration

### Database connection errors
- Verify MySQL is running and databases exist
- Check datasource URL and credentials
- Ensure correct MySQL version (8.0+)

### Config Server not loading configurations
- Verify Git repository path exists
- Check file permissions
- Ensure YAML syntax is valid

### Gateway returning 404
- Verify service is registered in Eureka
- Check gateway route configuration
- Ensure service instance is healthy

## Project Statistics

- **Total Microservices**: 7 (5 core + 2 bonus)
- **Total API Endpoints**: 30+
- **Database Tables**: 5
- **Lines of Code**: 3000+
- **Test Coverage Target**: 80%+

## Development Team

This project demonstrates best practices in:
- Microservices architecture
- Spring Boot/Cloud ecosystem
- RESTful API design
- Database design with JPA
- Docker containerization
- Agile development methodology

## License

This project is open source and available under the MIT License.

## Additional Resources

- [Spring Cloud Documentation](https://spring.io/cloud)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Microservices Patterns](https://microservices.io/)
- [JWT Introduction](https://jwt.io/)
- [OpenFeign Documentation](https://github.com/OpenFeign/feign)

## Support & Contact

For issues, questions, or contributions, please refer to the project's GitHub repository.

---

**Last Updated**: March 2, 2026
**Version**: 1.0.0
