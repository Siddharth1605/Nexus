CREATE TABLE riders (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(10) UNIQUE NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE orders (
    id UUID PRIMARY KEY,
    food_name VARCHAR(100),
    restaurant_lat DOUBLE PRECISION,
    restaurant_lng DOUBLE PRECISION,
    restaurant_h3 VARCHAR(32),
    status VARCHAR(30),
    created_at TIMESTAMP
);

CREATE TABLE assignments (
    id UUID PRIMARY KEY,
    rider_id UUID NOT NULL,
    order_id UUID NOT NULL,
    status VARCHAR(30),
    attempt_number INT,
    assigned_at TIMESTAMP,

    CONSTRAINT fk_assignment_rider
        FOREIGN KEY (rider_id)
        REFERENCES riders(id),

    CONSTRAINT fk_assignment_order
        FOREIGN KEY (order_id)
        REFERENCES orders(id)
);