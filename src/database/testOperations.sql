--Wieviele Produkte jeden Typs (Buch, Musik-CD, DVD) sind in der Datenbank erfasst?
SELECT
    'Books' AS ProductType, COUNT(*) AS ProductCount
FROM
    Book
UNION ALL
SELECT
    'CDs' AS ProductType, COUNT(*) AS ProductCount
FROM
    CD
UNION ALL
SELECT
    'DVDs' AS ProductType, COUNT(*) AS ProductCount
FROM
    DVD;

--Nennen Sie die 5 besten Produkte jeder Hauptkategorie sortiert nach dem durchschnittlichem Rating.
WITH ProductRatings AS (
    SELECT
        p.ProductID, p.Title, p.Category, AVG(r.Stars) AS AvgRating
    FROM
        Product p
            LEFT JOIN
        Review r ON p.ProductID = r.Product
    GROUP BY
        p.ProductID
)
SELECT
    pr.ProductID, pr.Title, c.Name AS Category, pr.AvgRating
FROM
    ProductRatings pr
        JOIN
    Category c ON pr.Category = c.CategoryID
WHERE
    c.ParentCategoryID IS NULL
ORDER BY
    pr.AvgRating DESC
LIMIT 5;

--Für welche Produkte gibt es im Moment kein Angebot?
SELECT
    p.ProductID, p.Title
FROM
    Product p
        LEFT JOIN
    ProductCatalog pc ON p.ProductID = pc.Product
WHERE
    pc.Product IS NULL;

--Für welche Produkte ist das teuerste Angebot mehr als doppelt so teuer wie das preiswerteste?
SELECT
    p.ProductID, p.Title
FROM
    Product p
        JOIN
    ProductCatalog pc1 ON p.ProductID = pc1.Product
        JOIN
    ProductCatalog pc2 ON p.ProductID = pc2.Product
GROUP BY
    p.ProductID
HAVING
    MAX(pc1.Price) > 2 * MIN(pc2.Price);

--Welche Produkte haben sowohl mindestens eine sehr schlechte (Punktzahl: 1) als auch mindestens eine sehr gute (Punktzahl: 5) Bewertung?
SELECT
    p.ProductID, p.Title
FROM
    Product p
        JOIN
    Review r1 ON p.ProductID = r1.Product AND r1.Stars = 1
        JOIN
    Review r2 ON p.ProductID = r2.Product AND r2.Stars = 5;

--Für wieviele Produkte gibt es gar keine Rezension?
SELECT
    COUNT(*) AS ProductsWithoutReviews
FROM
    Product p
        LEFT JOIN
    Review r ON p.ProductID = r.Product
WHERE
    r.Product IS NULL;

--Nennen Sie alle Rezensenten, die mehr als 10 Rezensionen geschrieben haben.
SELECT
    c.Name, COUNT(r.ReviewID) AS ReviewCount
FROM
    Customer c
        JOIN
    Review r ON c.CustomerID = r.Customer
GROUP BY
    c.CustomerID
HAVING
    COUNT(r.ReviewID) > 10;

--Geben Sie eine duplikatfreie und alphabetisch sortierte Liste der Namen aller Buchautoren an, die auch an DVDs oder Musik-CSs beteiligt sind.
SELECT DISTINCT
    b.Author
FROM
    Book b
WHERE
    EXISTS (
        SELECT 1
        FROM DVD d
        WHERE d.Creator = b.Author OR d.Director = b.Author
    )
   OR EXISTS (
    SELECT 1
    FROM CD c
    WHERE c.Artist = b.Author
)
ORDER BY
    b.Author;

--Wie hoch ist die durchschnittliche Anzahl von Liedern einer Musik-CD?
SELECT
    AVG(array_length(string_to_array(TitleList, ','), 1)::FLOAT) AS AvgNumberOfSongs
FROM
    CD;

--Für welche Produkte gibt es ähnliche Produkte in einer anderen Hauptkategorie?
SELECT
    sp.Product1, sp.Product2
FROM
    SimilarProduct sp
        JOIN
    Product p1 ON sp.Product1 = p1.ProductID
        JOIN
    Product p2 ON sp.Product2 = p2.ProductID
        JOIN
    Category c1 ON p1.Category = c1.CategoryID
        JOIN
    Category c2 ON p2.Category = c2.CategoryID
WHERE
    c1.ParentCategoryID IS NULL AND c2.ParentCategoryID IS NULL AND c1.CategoryID <> c2.CategoryID;

--Wieviele Kunden haben ein Produkt doppelt gekauft?
SELECT
    COUNT(DISTINCT o.Customer) AS CustomersWithDuplicatePurchases
FROM
    CustomerOrder o
        JOIN
    OrderDetail od ON o.OrderID = od.CustomerOrder
GROUP BY
    o.Customer, od.Product
HAVING
    COUNT(od.Product) > 1;

--Wie hoch ist der durschnittliche Preis eines Warenkorbs, d.h. die durchschnittliche Summe der Preise aller Produkte, die zur gleichen Zeit gekauft wurden?
SELECT
    AVG(t.TotalPrice) AS AvgCartPrice
FROM (
         SELECT
             o.OrderID, SUM(pc.Price * od.Quantity) AS TotalPrice
         FROM
             CustomerOrder o
                 JOIN
             OrderDetail od ON o.OrderID = od.CustomerOrder
                 JOIN
             ProductCatalog pc ON od.Product = pc.Product
         GROUP BY
             o.OrderID
     ) t;

--Welche Produkte werden in allen Fillialen angeboten? Hinweis: Ihre Query muss so formuliert werden, dass sie für eine beliebige Anzahl von Fillialen funktioniert.
SELECT
    p.ProductID, p.Title
FROM
    Product p
        JOIN
    ProductCatalog pc ON p.ProductID = pc.Product
GROUP BY
    p.ProductID
HAVING
    COUNT(DISTINCT pc.Store) = (SELECT COUNT(*) FROM Store);

--In wieviel Prozent der Fälle der Frage 13 gibt es in Leipzig das preiswerteste Angebot?
WITH AllStoresProducts AS (
    SELECT
        p.ProductID, p.Title
    FROM
        Product p
            JOIN
        ProductCatalog pc ON p.ProductID = pc.Product
    GROUP BY
        p.ProductID
    HAVING
        COUNT(DISTINCT pc.Store) = (SELECT COUNT(*) FROM Store)
),
     LeipzigCheapest AS (
         SELECT
             p.ProductID, MIN(pc.Price) AS MinPrice
         FROM
             Product p
                 JOIN
             ProductCatalog pc ON p.ProductID = pc.Product
                 JOIN
             Store s ON pc.Store = s.StoreID
         WHERE
             s.Address LIKE '%Leipzig%'
         GROUP BY
             p.ProductID
     ),
     CheapestOverall AS (
         SELECT
             p.ProductID, MIN(pc.Price) AS MinPrice
         FROM
             Product p
                 JOIN
             ProductCatalog pc ON p.ProductID = pc.Product
         GROUP BY
             p.ProductID
     )
SELECT
    CASE
        WHEN COUNT(DISTINCT a.ProductID) > 0 THEN
            COUNT(DISTINCT lc.ProductID) * 100.0 / COUNT(DISTINCT a.ProductID)
        ELSE
            0
        END AS PercentCheapestInLeipzig
FROM
    LeipzigCheapest lc
        JOIN
    AllStoresProducts a ON lc.ProductID = a.ProductID
        JOIN
    CheapestOverall co ON lc.ProductID = co.ProductID AND lc.MinPrice = co.MinPrice;