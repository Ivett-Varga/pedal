# pedal
PeDaL application is a Spring Boot application for personal data and location mangement

Project Description:

The objective of the project was to develop a Spring Boot-based application capable of managing and maintaining records of persons, addresses, and contacts. The application utilizes a database management system that encompasses three primary tables: Persons, Addresses, and Contacts. Each person can have a maximum of two addresses (permanent and temporary), and multiple contacts (such as email, phone, etc.) can be associated with each address.

Key Functions of the Application:

Data Retrieval: The application allows for querying existing data of persons, addresses, and contacts.
Data Registration: New records can be added to the persons, addresses, and contacts tables.
Data Modification: The data of existing records can be modified.
Data Deletion: Unwanted records can be removed.
Error Handling: The application handles various errors that may occur during database operations.
Development Environment and Technologies:

The application was developed in Java 17, leveraging the benefits offered by the Spring Boot framework.
MS SQL Server 2019 was utilized for data storage and management.
DDL (Data Definition Language) script is provided for the creation of the database structure. 
DML (Data Manipulation Language) script is also included for the loading of initial data.
Testing:

The project includes writing various test cases that ensure the correct functioning of the application's features. Integration and unit tests were conducted to verify the correct operation of API endpoints and the correctness of application logic.
