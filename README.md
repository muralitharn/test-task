This Spring Boot application demonstrates enterprise-grade features with modern development practices. It implements a robust REST API with JWT-based authentication for secure endpoints, coupled with comprehensive email integration using Spring's MimeMessage for rich HTML content delivery. The system emphasizes cost-efficiency through optimized JSON processing with Jackson's JsonNode for lightweight data handling.

Key Features 🚀
Security & Authentication
JWT implementation with access/refresh token mechanism

Spring Security configuration for role-based endpoint protection

Token validation filter with custom claim verification

Email Integration
MimeMessage-powered email sender with attachment support

HTML template engine integration for dynamic content

Async email delivery configuration

API Documentation
OpenAPI 3/Swagger UI implementation at /swagger-ui.html

Detailed endpoint documentation with request/response schemas

JWT authentication support in Swagger interface

Database & Storage
H2 in-memory database with persistent file storage configuration

SQL database initialization scripts in /resources/db

LOB (Large Object) handling for Excel file storage

Apache POI integration for Excel/Word document generation

System Operations
Spring Actuator endpoints at /actuator for health monitoring

Custom metrics exposure through Actuator

Scheduled tasks with @Scheduled cron expressions

Quartz scheduler integration for job management

Advanced Patterns
Factory design pattern implementation for:

Dynamic response generation (JSON/XML/CSV)

Multiple email strategy handlers

Document export format handlers

Custom exception hierarchy with @ControllerAdvice handling

Global error response standardization


