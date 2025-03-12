INSERT INTO client (name) VALUES
('Иванов Иван'),
('Петров Петр'),
('Сидорова Мария');

INSERT INTO address (client_id, street) VALUES
(1, 'ул. Ленина, д. 10'),
(2, 'пр. Мира, д. 25'),
(3, 'ул. Пушкина, д. 15');

INSERT INTO phone (number, client_id) VALUES
('+7-900-123-45-67', 1),
('+7-901-234-56-78', 1),
('+7-902-345-67-89', 2),
('+7-903-456-78-90', 3);
