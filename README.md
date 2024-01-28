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
