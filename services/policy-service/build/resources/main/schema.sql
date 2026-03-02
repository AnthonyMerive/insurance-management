CREATE TABLE IF NOT EXISTS policies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT,
    type VARCHAR(50),
    extra_data CLOB
);