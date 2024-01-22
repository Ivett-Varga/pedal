-- Check if the database exists
IF DB_ID('PEDAL') IS NOT NULL
    BEGIN
        -- If it exists, set it to SINGLE_USER mode to drop it
        EXEC('ALTER DATABASE PEDAL SET SINGLE_USER WITH ROLLBACK IMMEDIATE');
        -- Drop the database
        EXEC('DROP DATABASE PEDAL');
    END
GO

-- Drop the existing user if it exists
IF EXISTS (SELECT * FROM sys.server_principals WHERE name = N'pedal_admin')
    BEGIN
        DROP LOGIN [pedal_admin];
    END
GO

-- Recreate the database
CREATE DATABASE PEDAL;
GO


ALTER DATABASE PEDAL SET MULTI_USER;
GO

-- Change to the new database context
USE PEDAL;
GO

-- Create the user for the database
CREATE LOGIN pedal_admin WITH PASSWORD = 'Nmd-ldp!9';
CREATE USER pedal_admin FOR LOGIN pedal_admin;
GO

-- Grant appropriate permissions to the user
-- For example, granting control to the user for the database
EXEC sp_addrolemember 'db_owner', 'pedal_admin';
GO

-- Create tables (DDL)
-- Note: Modify the table creation scripts according to your actual schema

-- Create person Table


-- Create address Table
CREATE TABLE address (
                         id INT PRIMARY KEY IDENTITY(1,1),
                         street NVARCHAR(255) NOT NULL,
                         city NVARCHAR(100) NOT NULL,
                         state NVARCHAR(100) NOT NULL,
                         zip_code NVARCHAR(10) NOT NULL,
                         country NVARCHAR(100) NOT NULL
);
GO

CREATE TABLE contact (
                         id INT PRIMARY KEY IDENTITY(1,1),
                         address_id INT,
                         contact_type NVARCHAR(50) NOT NULL,
                         contact_detail NVARCHAR(255) NOT NULL,
                         CONSTRAINT FK_address_contact FOREIGN KEY (address_id)
                             REFERENCES address(id)
);
GO

CREATE TABLE person (
                        id INT PRIMARY KEY IDENTITY(1,1),
                        first_name NVARCHAR(100) NOT NULL,
                        last_name NVARCHAR(100) NOT NULL,
                        permanent_address_id INT,
                        temporary_address_id INT,
                        CONSTRAINT FK_person_permanent_address FOREIGN KEY (permanent_address_id)
                            REFERENCES address(id),
                        CONSTRAINT FK_person_temporary_address FOREIGN KEY (temporary_address_id)
                            REFERENCES address(id)
);
GO

-- Indexes for foreign key columns for better join performance
CREATE INDEX IDX_person_permanent_address ON person(permanent_address_id);
CREATE INDEX IDX_person_temporary_address ON person(temporary_address_id);
CREATE INDEX IDX_contact_address ON contact(address_id);
GO
