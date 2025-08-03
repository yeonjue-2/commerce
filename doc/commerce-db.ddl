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
    user_id     VARCHAR(50)   NOT NULL UNIQUE,
    password    VARCHAR(255),
    email       VARCHAR(255)  NOT NULL UNIQUE,
    role        VARCHAR(50)   NOT NULL DEFAULT 'ROLE_USER',
    provider    VARCHAR(50),
    provider_id VARCHAR(255),
    created_at  DATETIME      NOT NULL,
    updated_at  DATETIME      NOT NULL
);

-- 토큰 테이블
create table refresh_tokens
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT        NOT NULL,
    token         VARCHAR(255)  NOT NULL UNIQUE,
    expiry_date   DATETIME      NOT NULL,
    created_at    DATETIME      NOT NULL,
    updated_at    DATETIME      NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 상품 테이블
create table products
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    amount      INT           NOT NULL,
    stock       INT           NOT NULL,
    created_at  DATETIME      NOT NULL,
    updated_at  DATETIME      NOT NULL
);

create index idx_products_created_at
    on products (created_at);

-- 주문 테이블
create table orders
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id            BIGINT                               NOT NULL,
    product_id         BIGINT                               NOT NULL,
    order_status       ENUM ('INITIAL', 'PAID', 'CANCELED') NOT NULL DEFAULT 'INITIAL',
    total_amount       INT                                  NOT NULL,
    quantity           INT                                  NOT NULL,
    kakao_pay_ready_url VARCHAR(512),
    created_at         DATETIME                             NOT NULL,
    updated_at         DATETIME                             NOT NULL,

    FOREIGN KEY (user_id)    REFERENCES users(id)    ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT
);

-- 결제 테이블
create table payments
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id       BIGINT                               NOT NULL,
    payment_method VARCHAR(30)                          NOT NULL COMMENT 'KAKAOPAY',
    payment_status ENUM ('INITIAL', 'PAID', 'CANCELED') NOT NULL DEFAULT 'INITIAL',
    total_amount   INT                                  NOT NULL,
    pg_token       VARCHAR(255)                         NULL,
    transaction_id VARCHAR(100)                         NOT NULL,
    paid_at        TIMESTAMP                            NULL,
    canceled_at    TIMESTAMP                            NULL,
    fail_reason    VARCHAR(255)                         NULL,
    is_test        BOOLEAN                              NOT NULL DEFAULT FALSE,
    created_at     DATETIME                             NOT NULL,
    updated_at     DATETIME                             NOT NULL,

    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE RESTRICT
);

-- 결제 이력 테이블
create table payment_history
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    payment_id     BIGINT                               NOT NULL,
    order_id       BIGINT                               NOT NULL,
    payment_method VARCHAR(30)                          NOT NULL COMMENT 'KAKAOPAY',
    payment_status ENUM ('INITIAL', 'PAID', 'CANCELED') NOT NULL DEFAULT 'INITIAL',
    total_amount   INT                                  NOT NULL,
    pg_token       VARCHAR(255)                         NULL,
    transaction_id VARCHAR(100)                         NOT NULL,
    paid_at        TIMESTAMP                            NULL,
    canceled_at    TIMESTAMP                            NULL,
    fail_reason    VARCHAR(255)                         NULL,
    is_test        BOOLEAN                              NOT NULL DEFAULT FALSE,
    created_at     DATETIME                             NOT NULL,
    updated_at     DATETIME                             NOT NULL,

    FOREIGN KEY (payment_id) REFERENCES payments(id) ON DELETE CASCADE
);