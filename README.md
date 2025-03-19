============================================
PROJECT OVERVIEW
============================================
This Spring Boot application demonstrates enterprise-grade features with modern development practices. It implements a secure REST API with JWT authentication, email integration, and efficient document processing capabilities while emphasizing cost optimization through JSON node utilization.

============================================
KEY FEATURES
============================================
SECURITY:
- JWT authentication with access/refresh tokens
- Role-based authorization using Spring Security
- Custom token validation filters

EMAIL SYSTEM:
- MIME message implementation for HTML emails
- Attachment support for documents/files
- Asynchronous email delivery configuration

API DOCUMENTATION:
- OpenAPI 3/Swagger UI available at /swagger-ui.html
- Integrated JWT support in Swagger interface
- Detailed request/response schema documentation

DATABASE:
- H2 database with file-based persistence
- LOB storage for Excel/Word documents
- SQL initialization scripts in /resources/db

FILE PROCESSING:
- Apache POI integration for Excel/Word generation
- Database storage of generated files as BLOBs
- Batch processing capabilities

SYSTEM OPERATIONS:
- Spring Actuator endpoints (/actuator/*)
- Custom metrics monitoring (email stats, API response times)
- Scheduled tasks with cron expressions

DESIGN PATTERNS:
- Factory pattern implementation for:
  * Multiple response formats (JSON/XML/CSV)
  * Document export handlers
  * Email strategy variations
- Custom exception hierarchy with global handling
- Standardized error response structure

============================================
TECHNOLOGY STACK
============================================
- Spring Boot 3.x, Spring Security, Spring Data JPA
- H2 Database (Embedded & Server modes)
- Apache POI 5.x, Jackson Databind
- Spring Actuator, Micrometer
- OpenAPI 3, Swagger UI
- Maven Build System

============================================
SETUP INSTRUCTIONS
============================================
1. Configure application.properties:
spring.mail.host=your-smtp-host
jwt.secret=your-256-bit-secret

2. Run application:
mvn spring-boot:run

3. Access endpoints:
Swagger UI: http://localhost:8080/swagger-ui.html
H2 Console: http://localhost:8080/h2-console
Actuator: http://localhost:8080/actuator

4. Authentication:
POST http://localhost:8080/api/auth/login
Body: {"username":"user","password":"pass"}

============================================
DOCUMENT PROCESSING
============================================
- Excel generation using XSSFWorkbook
- Word documents with XWPFDocument
- File storage in database BLOB columns
- Batch operations with Spring Batch

============================================
MONITORING
============================================
Custom actuator endpoints:
- /actuator/emailmetrics: Email delivery statistics
- /actuator/apimetrics: API response metrics
- /actuator/scheduledtasks: Cron job statuses

Note: Replace placeholder values (your-smtp-host, your-256-bit-secret) with actual configuration values before running.
