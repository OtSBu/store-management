DROP TABLE IF EXISTS product;

CREATE TABLE product (
    id INT NOT NULL AUTO_INCREMENT,
    product_name VARCHAR(45) NOT NULL,
    description VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    stock_quantity INT NOT NULL,
    category VARCHAR(60) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO product (product_name, description, price, stock_quantity, category)
VALUES
('Lenovo Laptop', 'High-performance laptop', 3499.99, 15, 'Electronics'),
('Coffee Maker', 'Coffee for a energy day', 199.50, 30, 'Home Appliances'),
('Polo T-Shirt', 'Some T-shirt', 89.90, 50, 'Clothing'),
('Samsung Smartphone', 'Some phone with great camera', 2799.00, 20, 'Electronics'),
('Book: Bedtime Stories for Children', 'Illustrated volume with calming bedtime tales', 59.99, 100, 'Books');
