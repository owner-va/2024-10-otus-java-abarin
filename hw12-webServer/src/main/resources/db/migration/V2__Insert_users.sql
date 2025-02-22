INSERT INTO users (id, name, login, password) VALUES (1, 'Крис Гир', 'admin', '11111');
INSERT INTO users (id, name, login, password) VALUES (2, 'Ая Кэш', 'user2', '11111');
INSERT INTO users (id, name, login, password) VALUES (3, 'Десмин Боргес', 'user3', '11111');
INSERT INTO users (id, name, login, password) VALUES (4, 'Кетер Донохью', 'user4', '11111');
INSERT INTO users (id, name, login, password) VALUES (5, 'Стивен Шнайдер', 'user5', '11111');
INSERT INTO users (id, name, login, password) VALUES (6, 'Джанет Вэрни', 'user6', '11111');
INSERT INTO users (id, name, login, password) VALUES (7, 'Брэндон Смит', 'user7', '11111');
INSERT INTO address (street) VALUES
('ул. Ленина, д. 10'),
('пр. Мира, д. 25'),
('ул. Пушкина, д. 15');

INSERT INTO client (name, address_id) VALUES
('Иванов Иван', 1),
('Петров Петр', 2),
('Сидорова Мария', 3);

INSERT INTO phone (number, client_id) VALUES
('+7-900-123-45-67', 1),
('+7-901-234-56-78', 1),
('+7-902-345-67-89', 2),
('+7-903-456-78-90', 3);