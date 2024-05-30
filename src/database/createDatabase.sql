CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE Category(
    CategoryID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    Name VARCHAR(255),
    ParentCategoryID UUID
);

ALTER TABLE Category
    ADD CONSTRAINT FKParentCategory
    FOREIGN KEY (ParentCategoryID) REFERENCES Category(CategoryID);
ALTER TABLE Category
    ADD CONSTRAINT uniqueNameParentCombination UNIQUE(Name, ParentCategoryID);

CREATE TABLE Product(
    ProductID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    Title VARCHAR(255),
    Rating FLOAT,
    Rank INT,
    ProductNR VARCHAR(255) UNIQUE,
    Picture VARCHAR(255),
    Category UUID,
    FOREIGN KEY (Category) REFERENCES Category(CategoryID)
);

CREATE TABLE Book(
    ProductID UUID PRIMARY KEY,
    Author VARCHAR(255),
    Pages INT,
    ReleaseDate DATE,
    ISBN VARCHAR(255) UNIQUE,
    Publisher VARCHAR(255),
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
);

CREATE TABLE DVD(
    ProductID UUID PRIMARY KEY,
    Format VARCHAR(255),
    Length INT,
    RegionCode INT,
    Actors TEXT,
    Creator VARCHAR(255),
    Director VARCHAR(255),
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
);

CREATE TABLE CD(
    ProductID UUID PRIMARY KEY,
    Artist VARCHAR(255),
    Label VARCHAR(255),
    ReleaseDate DATE,
    TitleList TEXT,
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
);

CREATE TABLE Store(
    StoreID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    Name VARCHAR(255),
    Address TEXT
);

CREATE TABLE ProductCatalog(
    Store UUID,
    Product UUID,
    Price DECIMAL(2,2),
    Available BOOLEAN,
    Condition VARCHAR(255),
    PRIMARY KEY (Store, Product),
    FOREIGN KEY (Store) REFERENCES Store(StoreID),
    FOREIGN KEY (Product) REFERENCES Product(ProductID)
);

CREATE TABLE Customer(
    CustomerID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    Name VARCHAR(255),
    Address TEXT,
    BankAccount VARCHAR(255)
);

CREATE TABLE CustomerOrder(
    OrderID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    Customer UUID,
    Date DATE,
    FOREIGN KEY (Customer) REFERENCES Customer(CustomerID)
);

CREATE TABLE OrderDetail(
    CustomerOrder UUID,
    Product UUID,
    Quantity INT,
    PRIMARY KEY (CustomerOrder, Product),
    FOREIGN KEY (CustomerOrder) REFERENCES CustomerOrder(OrderID),
    FOREIGN KEY (Product) REFERENCES Product(ProductID)
);

CREATE TABLE Review(
    ReviewID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    Customer UUID,
    Product UUID,
    Stars INT,
    Summary TEXT,
    Review TEXT,
    Helpful INT,
    Username VARCHAR(255),
    FOREIGN KEY (Customer) REFERENCES Customer(CustomerID),
    FOREIGN KEY (Product) REFERENCES Product(ProductID)
);

CREATE TABLE SimilarProduct(
    Product1 UUID,
    Product2 UUID,
    PRIMARY KEY (Product1, Product2),
    FOREIGN KEY (Product1) REFERENCES Product(ProductID),
    FOREIGN KEY (Product2) REFERENCES Product(ProductID)
);