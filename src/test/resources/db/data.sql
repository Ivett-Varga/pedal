-- Insert data into the 'address' table
INSERT INTO address (street, city, state, zip_code, country) VALUES ('123 Main St', 'Springfield', 'StateName', '12345', 'USA');
DECLARE @Address1ID INT = SCOPE_IDENTITY();
-- PRINT 'Address recorded, id: ' + CAST(@Address1ID AS VARCHAR);

INSERT INTO address (street, city, state, zip_code, country) VALUES ('456 Elm St', 'Shelbyville', 'StateName', '67890', 'USA');
DECLARE @Address2ID INT = SCOPE_IDENTITY();
-- PRINT 'Address recorded, id: ' + CAST(@Address2ID AS VARCHAR);

-- Insert data into the 'person' table with references to the 'address' table
INSERT INTO person (first_name, last_name, permanent_address_id, temporary_address_id) VALUES ('John', 'Doe', @Address1ID, @Address2ID);
INSERT INTO person (first_name, last_name, permanent_address_id, temporary_address_id) VALUES ('Jane', 'Eyre', @Address1ID, @Address2ID);


-- Insert data into the 'contact' table with references to the 'address' table
INSERT INTO contact (address_id, contact_type, contact_detail) VALUES (@Address1ID, 'email', 'john.doe@email.com');
INSERT INTO contact (address_id, contact_type, contact_detail) VALUES (@Address2ID, 'email', 'jane.eyre@email.com');
