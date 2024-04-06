-- Drop tables in reverse order of creation to respect foreign key constraints
DROP TABLE IF EXISTS T_Input;
DROP TABLE IF EXISTS T_InputSubType;
DROP TABLE IF EXISTS T_InputType;
DROP TABLE IF EXISTS T_User;

-- Create tables
CREATE TABLE IF NOT EXISTS T_User (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_name VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS T_InputType (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  type_name VARCHAR(255) NOT NULL,
  user_id BIGINT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES T_User(id) ON DELETE CASCADE,
  UNIQUE (type_name, user_id)
);

CREATE TABLE IF NOT EXISTS T_InputSubType (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  subtype_name VARCHAR(255) NOT NULL,
  input_type_id BIGINT NOT NULL,
  FOREIGN KEY (input_type_id) REFERENCES T_InputType(id) ON DELETE CASCADE,
  UNIQUE (subtype_name, input_type_id)
);

CREATE TABLE IF NOT EXISTS T_Input (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  amount DECIMAL(10, 2) NOT NULL,
  input_date DATE NOT NULL,
  user_id BIGINT NOT NULL,
  input_type_id BIGINT NOT NULL,
  input_subtype_id BIGINT,
  FOREIGN KEY (user_id) REFERENCES T_User(id) ON DELETE CASCADE,
  FOREIGN KEY (input_type_id) REFERENCES T_InputType(id) ON DELETE CASCADE,
  FOREIGN KEY (input_subtype_id) REFERENCES T_InputSubType(id) ON DELETE SET NULL
);