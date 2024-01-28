# PeDaL
PeDaL application is a Spring Boot application for personal data and location mangement

# Overview
Pedal is a Java-based application designed for managing personal data. 
It utilizes Spring Boot, JPA/Hibernate, and a SQL Server database, focusing on clean architecture and well-defined APIs.

# Design Considerations
Adhering to the project's specification of utilizing three database tables, a singular base Contact class is employed rather than extending it with ContactEmail, ContactPhone, etc. 
This approach avoids the generation of additional tables in the database by Hibernate JPA, aligning with the project's specification.

# Features
RESTful API for managing personal data.
Robust data layer with JPA/Hibernate.
Comprehensive testing suite ensuring reliability and stability.

# Setup and Configuration
Refer to the application.properties file for database and server configurations. 
Use Maven commands (mvn clean install, mvn test) for building and testing.

# Recent Updates
Enhanced testing coverage with additional tests and resolved issues for greater stability.
All changes were merged into the master branch.

# Suggested Future Improvements
Test Coverage: Expanding tests to include edge cases and error handling.
Exception Handling: Refining global exception handling with specific or custom exceptions.
API Documentation: Utilization of tools like Swagger for clearer API documentation.
Security: Implementation of security measures, such as JWT, for API access.
Database Interaction: Optimization of database operations for efficiency.
Logging: Reviewing and adjusting logging levels where necessary.
Code Comments: Increasing comments in complex code sections for better understanding.
Performance Testing: Adding tests to assess application performance under load.
Front-end Integration: If applicable, integrating a basic front-end application.
