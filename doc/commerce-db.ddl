-- 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS commerce
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- 사용할 데이터베이스 선택
USE commerce;

-- 사용자 테이블
create table users
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)   NOT NULL,
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 상품 테이블
create table products
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(100)  NOT NULL,
    price     INT           NOT NULL,
    stock     INT           NOT NULL DEFAULT 0,
    image_url VARCHAR(512),
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 주문 테이블
create table orders
(
    id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id            BIGINT                               NOT NULL,
    product_id         BIGINT                               NULL,
    order_status       ENUM ('INITIAL', 'PAID', 'CANCELED') NOT NULL  DEFAULT 'INITIAL',
    total_price        INT                                  NOT NULL  DEFAULT 0,
    quantity           INT                                  NOT NULL,
    kakaopay_ready_url VARCHAR(512),
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id)    REFERENCES users(id)    ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE SET NULL
);

-- 결제 테이블
create table payments
(
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id       BIGINT                               NOT NULL,
    payment_method VARCHAR(30)                          NOT NULL COMMENT 'KAKAOPAY',
    payment_status ENUM ('INITIAL', 'PAID', 'CANCELED') NOT NULL DEFAULT 'INITIAL',
    amount         INT                                  NOT NULL DEFAULT 0,
    pg_token       VARCHAR(255)                         NULL,
    transaction_id VARCHAR(100)                         NOT NULL,
    paid_at        TIMESTAMP                            NULL,
    canceled_at    TIMESTAMP                            NULL,
    fail_reason    VARCHAR(255)                         NULL,
    is_test        BOOLEAN                              NOT NULL DEFAULT FALSE,
    payment_payload_json JSON                           NULL,
    created_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE RESTRICT
);

-- 결제 이력 테이블
create table payment_history
(
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    payment_id     BIGINT                               NOT NULL,
    order_id       BIGINT                               NOT NULL,
    payment_method VARCHAR(30)                          NOT NULL COMMENT 'KAKAOPAY',
    payment_status ENUM ('INITIAL', 'PAID', 'CANCELED') NOT NULL DEFAULT 'INITIAL',
    amount         INT                                  NOT NULL DEFAULT 0,
    pg_token       VARCHAR(255)                         NULL,
    transaction_id VARCHAR(100)                         NOT NULL,
    paid_at        TIMESTAMP                            NULL,
    canceled_at    TIMESTAMP                            NULL,
    fail_reason    VARCHAR(255)                         NULL,
    is_test        BOOLEAN                              NOT NULL DEFAULT FALSE,
    payment_payload_json JSON                           NULL,
    created_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (payment_id) REFERENCES payments(id) ON DELETE CASCADE
);