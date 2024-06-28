CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE Category(
    CategoryID VARCHAR(255) PRIMARY KEY,
    Name VARCHAR(255)
);

ALTER TABLE Category
    ADD COLUMN ParentCategory VARCHAR(255),
    ADD FOREIGN KEY (ParentCategory) REFERENCES Category(CategoryID) ON UPDATE CASCADE,
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
    FOREIGN KEY (Product) REFERENCES Product(ProductID) ON UPDATE CASCADE,
    FOREIGN KEY (Category) REFERENCES Category(CategoryID) ON UPDATE CASCADE
);

CREATE TABLE Person(
    PersonID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    Name VARCHAR(255) UNIQUE
);

CREATE TABLE Book(
    ProductID VARCHAR(255) PRIMARY KEY,
    Pages INT NOT NULl,
    ReleaseDate DATE NOT NULL,
    ISBN VARCHAR(255) UNIQUE NOT NULL,
    Publisher VARCHAR(255)NOT NULL,
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID) ON UPDATE CASCADE
);

CREATE TABLE DVD(
    ProductID VARCHAR(255) PRIMARY KEY,
    Format VARCHAR(255),
    Length INT,
    RegionCode INT,
    Director INT,
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID) ON UPDATE CASCADE,
    FOREIGN KEY (Director) REFERENCES Person(PersonID) ON UPDATE CASCADE
);

CREATE TABLE CD(
    ProductID VARCHAR(255) PRIMARY KEY,
    Label VARCHAR(255) NOT NULL,
    ReleaseDate DATE NOT NULL,
    TitleList TEXT NOT NULL,
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID) ON UPDATE CASCADE
);

CREATE TABLE Book_Author(
    Book_ProductID VARCHAR(255),
    Author_ID INT,
    PRIMARY KEY (Book_ProductID, Author_ID),
    FOREIGN KEY (Book_ProductID) REFERENCES Book(ProductID) ON UPDATE CASCADE,
    FOREIGN KEY (Author_ID) REFERENCES Person(PersonID) ON UPDATE CASCADE
);

CREATE TABLE DVD_Actor(
    DVD_ProductID VARCHAR(255),
    Actor_ID INT,
    PRIMARY KEY (DVD_ProductID, Actor_ID),
    FOREIGN KEY (DVD_ProductID) REFERENCES DVD(ProductID) ON UPDATE CASCADE,
    FOREIGN KEY (Actor_ID) REFERENCES Person(PersonID) ON UPDATE CASCADE
);

CREATE TABLE DVD_Creator(
    DVD_ProductID VARCHAR(255),
    Creator_ID INT,
    PRIMARY KEY (DVD_ProductID, Creator_ID),
    FOREIGN KEY (DVD_ProductID) REFERENCES DVD(ProductID) ON UPDATE CASCADE,
    FOREIGN KEY (Creator_ID) REFERENCES Person(PersonID) ON UPDATE CASCADE
);

CREATE TABLE CD_Artist(
    CD_ProductID VARCHAR(255),
    Artist_ID INT,
    PRIMARY KEY (CD_ProductID, Artist_ID),
    FOREIGN KEY (CD_ProductID) REFERENCES CD(ProductID) ON UPDATE CASCADE,
    FOREIGN KEY (Artist_ID) REFERENCES Person(PersonID) ON UPDATE CASCADE
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
    Price DECIMAL CHECK (Price > 0),
    Available BOOLEAN,
    Condition VARCHAR(255),
    PRIMARY KEY (Store, Product, Condition),
    FOREIGN KEY (Store) REFERENCES Store(StoreID) ON UPDATE CASCADE,
    FOREIGN KEY (Product) REFERENCES Product(ProductID) ON UPDATE CASCADE
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
    FOREIGN KEY (Customer) REFERENCES Customer(CustomerID) ON UPDATE CASCADE
);

CREATE TABLE OrderDetail(
    CustomerOrder VARCHAR(255),
    Product VARCHAR(255),
    Quantity INT,
    PRIMARY KEY (CustomerOrder, Product),
    FOREIGN KEY (CustomerOrder) REFERENCES CustomerOrder(OrderID) ON UPDATE CASCADE,
    FOREIGN KEY (Product) REFERENCES Product(ProductID) ON UPDATE CASCADE
);

CREATE TABLE Review(
    ReviewID VARCHAR(255) PRIMARY KEY,
    Customer VARCHAR(255),
    Product VARCHAR(255),
    Stars INT NOT NULL CHECK (Stars <= 5 AND Stars >= 0),
    Summary TEXT NOT NULL,
    Review TEXT NOT NULL,
    Helpful INT NOT NULL,
    Username VARCHAR(255) NOT NULL,
    FOREIGN KEY (Customer) REFERENCES Customer(CustomerID) ON UPDATE CASCADE,
    FOREIGN KEY (Product) REFERENCES Product(ProductID) ON UPDATE CASCADE
);

CREATE TABLE SimilarProduct(
    Product1 VARCHAR(255),
    Product2 VARCHAR(255),
    PRIMARY KEY (Product1, Product2),
    FOREIGN KEY (Product1) REFERENCES Product(ProductID) ON UPDATE CASCADE,
    FOREIGN KEY (Product2) REFERENCES Product(ProductID) ON UPDATE CASCADE
);

CREATE OR REPLACE FUNCTION update_product_rating()
RETURNS TRIGGER AS 'DECLARE avg_rating FLOAT; BEGIN SELECT AVG(Stars) INTO avg_rating FROM Review WHERE Product = NEW.Product; UPDATE Product SET Rating = avg_rating WHERE ProductID = NEW.Product; RETURN NEW; END; ' LANGUAGE plpgsql;

CREATE TRIGGER update_product_rating_trigger
    AFTER INSERT OR UPDATE OR DELETE ON Review
    FOR EACH ROW EXECUTE FUNCTION update_product_rating();

--Insert Person Uknown for null values
INSERT INTO Person (Name) VALUES ('Unknown');