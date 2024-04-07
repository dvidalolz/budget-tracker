-- Create tables
CREATE TABLE IF NOT EXISTS T_User (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS T_InputType (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    type_name VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES T_User(id) ON DELETE CASCADE,
    UNIQUE (type_name, user_id)
);

CREATE TABLE IF NOT EXISTS T_InputSubType (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    subtype_name VARCHAR(255) NOT NULL,
    input_type_id BIGINT NOT NULL,
    FOREIGN KEY (input_type_id) REFERENCES T_InputType(id) ON DELETE CASCADE,
    UNIQUE (subtype_name, input_type_id)
);

CREATE TABLE IF NOT EXISTS T_Input (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    amount DECIMAL(10, 2) NOT NULL,
    input_date DATE NOT NULL,
    user_id BIGINT NOT NULL,
    input_type_id BIGINT NOT NULL,
    input_subtype_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES T_User(id) ON DELETE CASCADE,
    FOREIGN KEY (input_type_id) REFERENCES T_InputType(id) ON DELETE CASCADE,
    FOREIGN KEY (input_subtype_id) REFERENCES T_InputSubType(id) ON DELETE SET NULL
);














-- CREATE TABLE T_User (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     user_name VARCHAR(255) NOT NULL UNIQUE,
--     password_hash VARCHAR(255) NOT NULL,
--     email VARCHAR(255) NOT NULL UNIQUE
-- );

-- CREATE TABLE T_InputType (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     type_name VARCHAR(255) NOT NULL,
--     user_id BIGINT NOT NULL,
--     FOREIGN KEY fk_inputtype_user (user_id) REFERENCES T_User(id) ON DELETE CASCADE -- delete associated usertypes when user deleted
-- );

-- CREATE TABLE T_InputSubType (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     subtype_name VARCHAR(255) NOT NULL,
--     input_type_id BIGINT NOT NULL,
--     FOREIGN KEY fk_inputsubtype_inputtype (input_type_id) REFERENCES T_InputType(id) ON DELETE CASCADE -- delete associated subtypes when type deleted
-- );

-- CREATE TABLE T_Input (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     amount DECIMAL(10, 2) NOT NULL,
--     input_date DATE NOT NULL,
--     user_id BIGINT NOT NULL,
--     input_type_id BIGINT NOT NULL,
--     input_subtype_id BIGINT,
--     FOREIGN KEY fk_input_user (user_id) REFERENCES T_User(id) ON DELETE CASCADE, -- delete associated input when user deleted
--     FOREIGN KEY fk_input_inputtype (input_type_id) REFERENCES T_InputType(id) ON DELETE CASCADE, -- delete associated inputs when inputtype deleted
--     FOREIGN KEY fk_input_inputsubtype (input_subtype_id) REFERENCES T_InputSubType(id) ON DELETE SET NULL -- set associated input's subtypes to null when subtype deleted
-- );
