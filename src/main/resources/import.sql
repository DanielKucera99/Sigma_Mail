INSERT INTO user (username, first_name, last_name, birthdate, password, role) VALUES ('jan.jalovec@sigma.com', 'Jan', 'Jalovec', '1997-05-28', 'pass123', 'User');
INSERT INTO user (username, first_name, last_name, birthdate, password, role) VALUES ('petr.malypetr@sigma.com', 'Petr', 'Malypetr', '1986-02-04', 'pass123', 'User');
INSERT INTO user (username, first_name, last_name, birthdate, password, role) VALUES ('josef.novak@sigma.com', 'Josef', 'Novák', '1991-04-10', 'pass123', 'User');
INSERT INTO user (username, first_name, last_name, birthdate, password, role) VALUES ('jiri.duben@sigma.com', 'Jiří', 'Duben', '2000-10-09', 'pass123', 'User');
INSERT INTO user (username, first_name, last_name, birthdate, password, role) VALUES ('stanislav.kos@sigma.com', 'Stanislav', 'Kos', '1970-08-18', 'pass123', 'User');
INSERT INTO user (username, first_name, last_name, birthdate, password, role) VALUES ('jan.ryba@sigma.com', 'Jan', 'Ryba', '1998-07-15', 'pass123', 'Admin');
INSERT INTO user (username, first_name, last_name, birthdate, password, role) VALUES ('jan.jalovec5@sigma.com', 'Jan', 'Jalovec', '1990-04-24', 'Password123', 'User');
Select * from sender;
select * from receiver;
select * from user;
SELECT * from message;
SELECT * from category;
SELECT * from attachment;
SELECT * FROM message_category;



INSERT INTO message (sender, receiver, subject, text, time) VALUES (1,2, 'Test zpravy', 'Test zpravy', CURRENT_TIMESTAMP);
INSERT INTO message (sender, receiver, subject, text, time) VALUES (1,2, 'Test zpravy2', 'Test zpravy2', CURRENT_TIMESTAMP);
INSERT INTO message (sender, receiver, subject, text, time) VALUES (3,4, 'Test zpravy3', 'Test zpravy3', CURRENT_TIMESTAMP);

SET @last_message := LAST_INSERT_ID();

INSERT INTO message_category (message, category) VALUES (@last_message, 1);

SET @last_message := LAST_INSERT_ID();

INSERT INTO message_category (message, category) VALUES (@last_message, 2);

INSERT INTO attachment (message,name,type,size) VALUES (12,'foto.jpg','jpg',10);

INSERT INTO message (sender, receiver, subject, text, time) VALUES (1,2, 'Test konceptu', 'Test', CURRENT_TIMESTAMP);
SET @last_message := LAST_INSERT_ID();
INSERT INTO message_category (message, category) VALUES (@last_message, 3);



INSERT INTO category (name) VALUES ('Send');
INSERT INTO category (name) VALUES ('Received');
INSERT INTO category (name) VALUES ('Concepts');
INSERT INTO category (name) VALUES ('Trash');

select * from category




