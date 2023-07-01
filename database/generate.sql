
CREATE SEQUENCE warehouse.employee_role_worker_role_id1_seq;

CREATE TABLE warehouse.Employee_role (
                worker_role_id1 INTEGER NOT NULL DEFAULT nextval('warehouse.employee_role_worker_role_id1_seq'),
                description VARCHAR NOT NULL,
                CONSTRAINT employee_role_pk PRIMARY KEY (worker_role_id1)
);


ALTER SEQUENCE warehouse.employee_role_worker_role_id1_seq OWNED BY warehouse.Employee_role.worker_role_id1;

CREATE SEQUENCE warehouse.delivery_status_delivery_id_seq;

CREATE TABLE warehouse.delivery_status (
                status_id INTEGER NOT NULL DEFAULT nextval('warehouse.delivery_status_delivery_id_seq'),
                name VARCHAR NOT NULL,
                CONSTRAINT delivery_status_pk PRIMARY KEY (status_id)
);


ALTER SEQUENCE warehouse.delivery_status_delivery_id_seq OWNED BY warehouse.delivery_status.status_id;

CREATE SEQUENCE warehouse.warehouse_warehouse_id_seq;

CREATE TABLE warehouse.Warehouse (
                warehouse_id INTEGER NOT NULL DEFAULT nextval('warehouse.warehouse_warehouse_id_seq'),
                name VARCHAR NOT NULL,
                CONSTRAINT warehouse_pk PRIMARY KEY (warehouse_id)
);


ALTER SEQUENCE warehouse.warehouse_warehouse_id_seq OWNED BY warehouse.Warehouse.warehouse_id;

CREATE SEQUENCE warehouse.user_worker_id_seq;

CREATE TABLE warehouse.Employee (
                employee_id INTEGER NOT NULL DEFAULT nextval('warehouse.user_worker_id_seq'),
                username VARCHAR NOT NULL,
                password VARCHAR NOT NULL,
                warehouse_id INTEGER NOT NULL,
                user_role_id INTEGER NOT NULL,
                CONSTRAINT employee_pk PRIMARY KEY (employee_id)
);


ALTER SEQUENCE warehouse.user_worker_id_seq OWNED BY warehouse.Employee.employee_id;

CREATE SEQUENCE warehouse.authtoken_authtoken_id_seq;

CREATE TABLE warehouse.AuthToken (
                authtoken_id INTEGER NOT NULL DEFAULT nextval('warehouse.authtoken_authtoken_id_seq'),
                employee_id INTEGER NOT NULL,
                expiration_time TIMESTAMP NOT NULL,
                token_value VARCHAR NOT NULL,
                CONSTRAINT authtoken_pk PRIMARY KEY (authtoken_id)
);


ALTER SEQUENCE warehouse.authtoken_authtoken_id_seq OWNED BY warehouse.AuthToken.authtoken_id;

CREATE SEQUENCE warehouse.product_product_id_seq;

CREATE TABLE warehouse.Product (
                product_id INTEGER NOT NULL DEFAULT nextval('warehouse.product_product_id_seq'),
                name VARCHAR NOT NULL,
                description VARCHAR,
                CONSTRAINT product_pk PRIMARY KEY (product_id)
);


ALTER SEQUENCE warehouse.product_product_id_seq OWNED BY warehouse.Product.product_id;

CREATE SEQUENCE warehouse.delivery_delivery_id_seq;

CREATE TABLE warehouse.Delivery (
                delivery_id INTEGER NOT NULL DEFAULT nextval('warehouse.delivery_delivery_id_seq'),
                from_warehouse_id INTEGER NOT NULL,
                to_warehouse_id INTEGER NOT NULL,
                product_id INTEGER NOT NULL,
                quantity INTEGER NOT NULL,
                status_id INTEGER NOT NULL,
                CONSTRAINT delivery_pk PRIMARY KEY (delivery_id)
);


ALTER SEQUENCE warehouse.delivery_delivery_id_seq OWNED BY warehouse.Delivery.delivery_id;

CREATE SEQUENCE warehouse.orderedproduct_order_id_seq;

CREATE TABLE warehouse.OrderedProduct (
                orderedProduct_id INTEGER NOT NULL DEFAULT nextval('warehouse.orderedproduct_order_id_seq'),
                warehouse_id INTEGER NOT NULL,
                product_id INTEGER NOT NULL,
                quantity INTEGER NOT NULL,
                CONSTRAINT orderedproduct_pk PRIMARY KEY (orderedProduct_id)
);


ALTER SEQUENCE warehouse.orderedproduct_order_id_seq OWNED BY warehouse.OrderedProduct.orderedProduct_id;

CREATE TABLE warehouse.StoredProduct (
                product_id INTEGER NOT NULL,
                warehouse_id INTEGER NOT NULL,
                quantity INTEGER NOT NULL,
                CONSTRAINT storedproduct_pk PRIMARY KEY (product_id, warehouse_id)
);


ALTER TABLE warehouse.Employee ADD CONSTRAINT worker_role_user_account_fk
FOREIGN KEY (user_role_id)
REFERENCES warehouse.Employee_role (worker_role_id1)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE warehouse.Delivery ADD CONSTRAINT delivery_status_delivery_fk
FOREIGN KEY (status_id)
REFERENCES warehouse.delivery_status (status_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE warehouse.Delivery ADD CONSTRAINT warehouse_delivery_fk
FOREIGN KEY (to_warehouse_id)
REFERENCES warehouse.Warehouse (warehouse_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE warehouse.Delivery ADD CONSTRAINT warehouse_delivery_fk1
FOREIGN KEY (from_warehouse_id)
REFERENCES warehouse.Warehouse (warehouse_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE warehouse.StoredProduct ADD CONSTRAINT warehouse_stored_fk
FOREIGN KEY (warehouse_id)
REFERENCES warehouse.Warehouse (warehouse_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE warehouse.Employee ADD CONSTRAINT warehouse_user_account_fk
FOREIGN KEY (warehouse_id)
REFERENCES warehouse.Warehouse (warehouse_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE warehouse.OrderedProduct ADD CONSTRAINT warehouse_order_fk
FOREIGN KEY (warehouse_id)
REFERENCES warehouse.Warehouse (warehouse_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE warehouse.AuthToken ADD CONSTRAINT employee_authtoken_fk
FOREIGN KEY (employee_id)
REFERENCES warehouse.Employee (employee_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE warehouse.StoredProduct ADD CONSTRAINT product_stored_fk
FOREIGN KEY (product_id)
REFERENCES warehouse.Product (product_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE warehouse.OrderedProduct ADD CONSTRAINT product_order_fk
FOREIGN KEY (product_id)
REFERENCES warehouse.Product (product_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE warehouse.Delivery ADD CONSTRAINT product_delivery_fk
FOREIGN KEY (product_id)
REFERENCES warehouse.Product (product_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;