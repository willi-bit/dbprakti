--Wieviele Produkte jeden Typs (Buch, Musik-CD, DVD) sind in der Datenbank erfasst?
select 'Bücher' as Produkt_Typ, count(*) as Anzahl
from book
union all
select 'CD' as Produkt_Typ, count(*) as Anzahl
from cd
union all
select 'DVD' as Produkt_Typ, count(*) as Anzahl
from dvd;

--Nennen Sie die 5 besten Produkte jedes Typs (Buch, Musik-CD, DVD) sortiert nach dem durchschnittlichem Rating.
(select 'Book' as Produkt_Typ, productid from product as p natural join book order by rating limit 5)
union all
(select 'CD' as Produkt_Typ, productid from product natural join cd order by rating limit 5)
union all
(select 'DVD' as Produkt_Typ, productid from product natural join dvd order by rating limit 5);

--Für welche Produkte gibt es im Moment kein Angebot?
select productid from product inner join productcatalog as pc on product.productid=pc.product where pc.available = false;

--Für welche Produkte ist das teuerste Angebot mehr als doppelt so teuer wie das preiswerteste?
select p.productid
from product p
         join productcatalog pc on p.productid = pc.product
group by p.productid
having count(pc.price) > 1 and max(pc.price) > 2*min(pc.price);

--Welche Produkte haben sowohl mindestens eine sehr schlechte (Punktzahl: 1) als auch mindestens eine sehr gute (Punktzahl: 5) Bewertung?
select productid from product p
join review r1 on p.productid=r1.product and r1.stars=1
join review r2 on p.productid=r2.product and r2.stars=5;

--Für wieviele Produkte gibt es gar keine Rezension?
select productid from product where rating=0;

--Nennen Sie alle Rezensenten, die mindestens 10 Rezensionen geschrieben haben.
select username from review group by username having COUNT(*) > 9;

--Geben Sie eine duplikatfreie und alphabetisch sortierte Liste der Namen aller Buchautoren an, die auch an DVDs oder Musik-CDs beteiligt sind.
SELECT *
FROM (
         SELECT Director AS author_id
         FROM DVD
         UNION
         SELECT Creator_ID AS author_id
         FROM DVD_Creator
         UNION
         SELECT Artist_ID AS author_id
         FROM CD_Artist
         UNION
         SELECT Actor_ID AS author_id
         FROM DVD_Actor
     ) AS other
         JOIN Book_Author ON Book_Author.Author_ID = other.author_id
         JOIN Book ON Book.ProductID = Book_Author.Book_ProductID;

--Wie hoch ist die durchschnittliche Anzahl von Liedern einer Musik-CD?
SELECT avg(array_length(string_to_array(cd.titlelist, ','), 1)) from cd;

--Für welche Produkte gibt es ähnliche Produkte in einer anderen Hauptkategorie? Hinweis: Eine Hauptkategorie ist eine Produktkategorie ohne Oberkategorie.
select * from (SELECT DISTINCT p1.productid AS product_name,
                               p2.productid AS similar_product_name,
                               CASE
                                   WHEN b2.productid IS NOT NULL THEN 'book'
                                   WHEN c2.productid IS NOT NULL THEN 'cd'
                                   WHEN d2.productid IS NOT NULL THEN 'dvd'
                                   ELSE 'unknown'
                                   END AS similar_product_category2
               FROM product p1
                        JOIN similarproduct s ON p1.productid = s.product1
                        JOIN product p2 ON s.product2 = p2.productid
                        LEFT JOIN book b2 ON p2.productid = b2.productid
                        LEFT JOIN cd c2 ON p2.productid = c2.productid
                        LEFT JOIN dvd d2 ON p2.productid = d2.productid
               WHERE NOT EXISTS (
                   SELECT 1
                   FROM book b1
                   WHERE p1.productid = b1.productid
               )
                 AND NOT EXISTS (
                   SELECT 1
                   FROM cd c1
                   WHERE p1.productid = c1.productid
               )
                 AND NOT EXISTS (
                   SELECT 1
                   FROM dvd d1
                   WHERE p1.productid = d1.productid
               ))
where similar_product_category2<>'unknown';

--Welche Produkte werden in allen Filialen angeboten?
SELECT p.product
FROM productcatalog p
GROUP BY p.product
HAVING COUNT(DISTINCT p.store) = (SELECT COUNT(*) FROM store);

--In wieviel Prozent der Fälle der Frage 11 gibt es in Leipzig das preiswerteste Angebot?
WITH ProductInAllStores AS (
    SELECT p.product
    FROM productcatalog p
    GROUP BY p.product
    HAVING COUNT(DISTINCT p.store) = (SELECT COUNT(*) FROM store)
),
     CheapestInSpecificStore AS (
         SELECT p.product
         FROM productcatalog p
         WHERE p.store = (SELECT s.storeid FROM store s WHERE s.name = 'Leipzig')
           AND p.price = (
             SELECT MIN(pc.price)
             FROM productcatalog pc
             WHERE pc.product = p.product
         )
     )
SELECT pas.product
FROM ProductInAllStores pas
         JOIN CheapestInSpecificStore css ON pas.product = css.product;