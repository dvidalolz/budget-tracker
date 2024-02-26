INSERT INTO T_User (user_name, password_hash, email) VALUES
('JohnDoe', 'hash1', 'john.doe@example.com'),
('JaneDoe', 'hash2', 'jane.doe@example.com'),
('FooBar', 'hash3', 'foo.bar@example.com'),
('AliceWonder', 'hash4', 'alice.wonder@example.com'),
('BobBuilder', 'hash5', 'bob.builder@example.com'),
('CharlieDay', 'hash6', 'charlie.day@example.com'),
('EveEve', 'hash7', 'eve.eve@example.com');


INSERT INTO T_InputType (type_name, user_id) VALUES
('Salary', (SELECT id FROM T_User WHERE user_name = 'JohnDoe')),
('Investment', (SELECT id FROM T_User WHERE user_name = 'JaneDoe')),
('Savings', (SELECT id FROM T_User WHERE user_name = 'FooBar')),
('Freelance', (SELECT id FROM T_User WHERE user_name = 'AliceWonder')),
('Dividends', (SELECT id FROM T_User WHERE user_name = 'BobBuilder')),
('Rental', (SELECT id FROM T_User WHERE user_name = 'CharlieDay')),
('Pension', (SELECT id FROM T_User WHERE user_name = 'EveEve'));


INSERT INTO T_InputSubType (subtype_name, input_type_id) VALUES
('Monthly Salary', (SELECT id FROM T_InputType WHERE type_name = 'Salary')),
('Quarterly Dividends', (SELECT id FROM T_InputType WHERE type_name = 'Dividends')),
('Stocks', (SELECT id FROM T_InputType WHERE type_name = 'Investment')),
('Apartment Rental', (SELECT id FROM T_InputType WHERE type_name = 'Rental')),
('Government Bonds', (SELECT id FROM T_InputType WHERE type_name = 'Savings')),
('Consulting', (SELECT id FROM T_InputType WHERE type_name = 'Freelance')),
('Senior Pension', (SELECT id FROM T_InputType WHERE type_name = 'Pension')),
('Online Sales', (SELECT id FROM T_InputType WHERE type_name = 'Freelance')),
('Book Royalties', (SELECT id FROM T_InputType WHERE type_name = 'Dividends'));


INSERT INTO T_Input (amount, input_date, user_id, input_type_id, input_subtype_id) VALUES
(3200.00, '2024-01-01', (SELECT id FROM T_User WHERE user_name = 'JohnDoe'), (SELECT id FROM T_InputType WHERE type_name = 'Salary'), (SELECT id FROM T_InputSubType WHERE subtype_name = 'Monthly Salary')),
(1500.00, '2024-01-15', (SELECT id FROM T_User WHERE user_name = 'JaneDoe'), (SELECT id FROM T_InputType WHERE type_name = 'Investment'), (SELECT id FROM T_InputSubType WHERE subtype_name = 'Stocks')),
(700.00, '2024-02-01', (SELECT id FROM T_User WHERE user_name = 'FooBar'), (SELECT id FROM T_InputType WHERE type_name = 'Savings'), (SELECT id FROM T_InputSubType WHERE subtype_name = 'Government Bonds')),
(4500.00, '2024-02-10', (SELECT id FROM T_User WHERE user_name = 'AliceWonder'), (SELECT id FROM T_InputType WHERE type_name = 'Freelance'), (SELECT id FROM T_InputSubType WHERE subtype_name = 'Consulting')),
(1200.00, '2024-02-20', (SELECT id FROM T_User WHERE user_name = 'BobBuilder'), (SELECT id FROM T_InputType WHERE type_name = 'Dividends'), (SELECT id FROM T_InputSubType WHERE subtype_name = 'Quarterly Dividends')),
(2500.00, '2024-03-01', (SELECT id FROM T_User WHERE user_name = 'CharlieDay'), (SELECT id FROM T_InputType WHERE type_name = 'Rental'), (SELECT id FROM T_InputSubType WHERE subtype_name = 'Apartment Rental')),
(980.00, '2024-03-15', (SELECT id FROM T_User WHERE user_name = 'EveEve'), (SELECT id FROM T_InputType WHERE type_name = 'Pension'), (SELECT id FROM T_InputSubType WHERE subtype_name = 'Senior Pension')),
(2100.00, '2024-03-25', (SELECT id FROM T_User WHERE user_name = 'AliceWonder'), (SELECT id FROM T_InputType WHERE type_name = 'Freelance'), (SELECT id FROM T_InputSubType WHERE subtype_name = 'Online Sales')),
(500.00, '2024-04-05', (SELECT id FROM T_User WHERE user_name = 'BobBuilder'), (SELECT id FROM T_InputType WHERE type_name = 'Dividends'), (SELECT id FROM T_InputSubType WHERE subtype_name = 'Book Royalties'));
