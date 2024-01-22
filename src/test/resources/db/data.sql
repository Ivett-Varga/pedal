-- Insert some initial data into the `person` table
INSERT INTO person (first_name, last_name)
VALUES ('John', 'Doe');
INSERT INTO person (first_name, last_name)
VALUES ('Jane', 'Roe');

-- Insert some initial data into the `address` table
INSERT INTO address (street, city, state, zip_code, country)
VALUES ('123 Baker St', 'London', 'England', 'NW1 6XE', 'UK');
INSERT INTO address (street, city, state, zip_code, country)
VALUES ('221B Baker St', 'London', 'England', 'NW1 6XE', 'UK');

-- Insert some initial data into the `contact` table
INSERT INTO contact (contact_type, contact_detail, address_id)
VALUES ('phone', '+44 20 7946 0341', 1);
INSERT INTO contact (contact_type, contact_detail, address_id)
VALUES ('email', 'sherlock.holmes@bakerst.com', 2);
