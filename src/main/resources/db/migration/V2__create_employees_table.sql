CREATE TABLE IF NOT EXISTS employees (
    id              BIGSERIAL       PRIMARY KEY,
    employee_code   VARCHAR(20)     NOT NULL UNIQUE,
    name            VARCHAR(100)    NOT NULL,
    email           VARCHAR(255)    NOT NULL UNIQUE,
    department_id   BIGINT          NOT NULL,
    CONSTRAINT fk_employees_department FOREIGN KEY (department_id) REFERENCES departments(id)
);
