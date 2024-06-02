CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE Category(
    CategoryID VARCHAR(255) PRIMARY KEY,
    Name VARCHAR(255)
);

ALTER TABLE Category
    ADD COLUMN ParentCategory VARCHAR(255),
    ADD FOREIGN KEY (ParentCategory) REFERENCES Category(CategoryID),
    ADD CONSTRAINT UniqueNameParentCombination UNIQUE (Name, ParentCategory);

CREATE TABLE Product(
    ProductID VARCHAR(255) PRIMARY KEY,
    Title VARCHAR(255) NOT NULL,
    Rating FLOAT NOT NULL,
    Rank INT NOT NULL,
    Picture VARCHAR(255)
);

CREATE TABLE ProductCategories(
    Product VARCHAR(255),
    Category VARCHAR(255),
    PRIMARY KEY (Product, Category),
    FOREIGN KEY (Product) REFERENCES Product(ProductID),
    FOREIGN KEY (Category) REFERENCES Category(CategoryID)
);

CREATE TABLE Book(
    ProductID VARCHAR(255) PRIMARY KEY,
    Author VARCHAR(255) NOT NULL,
    Pages INT NOT NULl,
    ReleaseDate DATE NOT NULL,
    ISBN VARCHAR(255) UNIQUE NOT NULL,
    Publisher VARCHAR(255)NOT NULL,
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
);

CREATE TABLE DVD(
    ProductID VARCHAR(255) PRIMARY KEY,
    Format VARCHAR(255),
    Length INT,
    RegionCode INT,
    Actors TEXT,
    Creator VARCHAR(255),
    Director VARCHAR(255),
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
);

CREATE TABLE CD(
    ProductID VARCHAR(255) PRIMARY KEY,
    Artist VARCHAR(255)  NOT NULL,
    Label VARCHAR(255) NOT NULL,
    ReleaseDate DATE NOT NULL,
    TitleList TEXT NOT NULL,
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
);

CREATE TABLE Store(
    StoreID VARCHAR(255) PRIMARY KEY,
    Name VARCHAR(255),
    Address TEXT,
    CONSTRAINT uniqueNameAddressCombination UNIQUE(Name, Address)
);

CREATE TABLE ProductCatalog(
    Store VARCHAR(255),
    Product VARCHAR(255),
    Price DECIMAL,
    Available BOOLEAN,
    Condition VARCHAR(255),
    PRIMARY KEY (Store, Product),
    FOREIGN KEY (Store) REFERENCES Store(StoreID),
    FOREIGN KEY (Product) REFERENCES Product(ProductID)
);

CREATE TABLE Customer(
    CustomerID VARCHAR(255) PRIMARY KEY,
    Name VARCHAR(255),
    Address TEXT,
    BankAccount VARCHAR(255)
);

CREATE TABLE CustomerOrder(
    OrderID VARCHAR(255) PRIMARY KEY,
    Customer VARCHAR(255),
    Date DATE,
    FOREIGN KEY (Customer) REFERENCES Customer(CustomerID)
);

CREATE TABLE OrderDetail(
    CustomerOrder VARCHAR(255),
    Product VARCHAR(255),
    Quantity INT,
    PRIMARY KEY (CustomerOrder, Product),
    FOREIGN KEY (CustomerOrder) REFERENCES CustomerOrder(OrderID),
    FOREIGN KEY (Product) REFERENCES Product(ProductID)
);

CREATE TABLE Review(
    ReviewID VARCHAR(255) PRIMARY KEY,
    Customer VARCHAR(255),
    Product VARCHAR(255),
    Stars INT NOT NULL,
    Summary TEXT NOT NULL,
    Review TEXT NOT NULL,
    Helpful INT NOT NULL,
    Username VARCHAR(255) NOT NULL,
    FOREIGN KEY (Customer) REFERENCES Customer(CustomerID),
    FOREIGN KEY (Product) REFERENCES Product(ProductID)
);

CREATE TABLE SimilarProduct(
    Product1 VARCHAR(255),
    Product2 VARCHAR(255),
    PRIMARY KEY (Product1, Product2),
    FOREIGN KEY (Product1) REFERENCES Product(ProductID),
    FOREIGN KEY (Product2) REFERENCES Product(ProductID)
);

CREATE OR REPLACE FUNCTION update_product_rating()
RETURNS TRIGGER AS 'DECLARE avg_rating FLOAT; BEGIN SELECT AVG(Stars) INTO avg_rating FROM Review WHERE Product = NEW.Product; UPDATE Product SET Rating = avg_rating WHERE ProductID = NEW.Product; RETURN NEW; END; ' LANGUAGE plpgsql;

CREATE TRIGGER update_product_rating_trigger
    AFTER INSERT OR UPDATE OR DELETE ON Review
    FOR EACH ROW EXECUTE FUNCTION update_product_rating();