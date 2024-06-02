ALTER TABLE review
    ADD COLUMN reviewDate VARCHAR(255),
    DROP customer;

COPY review (product, stars, helpful, reviewDate, username, summary, review)
    FROM 'data/review.csv'
    DELIMITER ','
    CSV HEADER;

ALTER TABLE review
    DROP reviewDate;

ALTER TABLE review
    ADD COLUMN customer VARCHAR(255),
    ADD FOREIGN KEY (Customer) REFERENCES Customer(CustomerID);