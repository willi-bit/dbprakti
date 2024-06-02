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
WITH MainCategories AS (
    SELECT
        CategoryID,
        Name
    FROM
        Category
    WHERE
        ParentCategory IS NULL
),
     ProductRatings AS (
         SELECT
             r.Product,
             AVG(r.Stars) AS AverageRating
         FROM
             Review r
         GROUP BY
             r.Product
     ),
     RankedProducts AS (
         SELECT
             p.ProductID,
             p.Title,
             mc.Name AS CategoryName,
             pr.AverageRating,
             ROW_NUMBER() OVER (PARTITION BY mc.Name ORDER BY pr.AverageRating DESC) AS Rank
         FROM
             Product p
                 JOIN
             ProductCategories pc ON p.ProductID = pc.Product
                 JOIN
             MainCategories mc ON pc.Category = mc.CategoryID
                 JOIN
             ProductRatings pr ON p.ProductID = pr.Product
     )
SELECT
    ProductID,
    Title,
    CategoryName,
    AverageRating
FROM
    RankedProducts
WHERE
    Rank <= 5
ORDER BY
    CategoryName,
    Rank;

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
SELECT Username
FROM Review
GROUP BY Username
HAVING COUNT(ReviewID) > 10;

--Geben Sie eine duplikatfreie und alphabetisch sortierte Liste der Namen aller Buchautoren an, die auch an DVDs oder Musik-CSs beteiligt sind.
SELECT DISTINCT
    b.Author
FROM
    Book b
WHERE
    EXISTS (
        SELECT 1
        FROM DVD d
        WHERE d.Actors LIKE '%' || b.Author || '%' OR d.Creator = b.Author OR d.Director = b.Author
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
SELECT DISTINCT p1.ProductID, p1.Title AS ProductTitle, c1.Name AS CategoryName, p2.ProductID AS SimilarProductID, p2.Title AS SimilarProductTitle, c2.Name AS SimilarCategoryName
FROM Product p1
         JOIN ProductCategories pc1 ON p1.ProductID = pc1.Product
         JOIN Category c1 ON pc1.Category = c1.CategoryID
         JOIN SimilarProduct sp ON p1.ProductID = sp.Product1
         JOIN Product p2 ON sp.Product2 = p2.ProductID
         JOIN ProductCategories pc2 ON p2.ProductID = pc2.Product
         JOIN Category c2 ON pc2.Category = c2.CategoryID
WHERE c1.CategoryID <> c2.CategoryID;

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
WITH ProductsInAllStores AS (
    SELECT
        p.ProductID,
        p.Title
    FROM
        Product p
            JOIN
        ProductCatalog pc ON p.ProductID = pc.Product
    GROUP BY
        p.ProductID
    HAVING
        COUNT(DISTINCT pc.Store) = (SELECT COUNT(*) FROM Store)
),
     LowestPricePerProduct AS (
         SELECT
             pc.Product,
             MIN(pc.Price) AS LowestPrice
         FROM
             ProductCatalog pc
         GROUP BY
             pc.Product
     )
SELECT
    COUNT(*)
FROM
    ProductsInAllStores pis
        JOIN
    ProductCatalog pc ON pis.ProductID = pc.Product
        JOIN
    Store s ON pc.Store = s.StoreID
        JOIN
    LowestPricePerProduct lpp ON pc.Product = lpp.Product
WHERE
    s.Name = 'Leipzig' AND pc.Price = lpp.LowestPrice;