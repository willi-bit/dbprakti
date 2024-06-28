public class StatementRunner {
    void run() {
        String num1 = """
                select 'Bücher' as Produkt_Typ, count(*) as Anzahl
                from book
                union all
                select 'CD' as Produkt_Typ, count(*) as Anzahl
                from cd
                union all
                select 'DVD' as Produkt_Typ, count(*) as Anzahl
                from dvd
                """;
        String num2 = """
                (select 'Book' as Produkt_Typ ,productid from product as p natural join book order by rating limit 5)
                union all
                (select 'CD' as Produkt_Typ,productid from product natural join cd order by rating limit 5)
                union all
                (select 'DVD' as Produkt_Typ, productid from product natural join dvd order by rating limit 5) 
                """;
        String num3 = """
                select productid from product inner join productcatalog as pc on product.productid=pc.product where pc.available = false
                """;
        String num4 = """
                select p.productid
                from product p
                join productcatalog pc on p.productid = pc.product
                group by p.productid
                having count(pc.price) > 1 and max(pc.price) > min(pc.price);
                """;
        String restOfThePack = """
                select productid from product p
                join review r1 on p.productid=r1.product and r1.stars=1
                join review r2 on p.productid=r2.product and r2.stars=5
                
                select productid from product where rating=0
                
                select username from review group by username having COUNT(*) > 9
                
                select * from (SELECT director AS author_id FROM dvd
                    UNION
                    SELECT creator AS author_id FROM dvd
                    UNION
                    SELECT artist AS author_id FROM cd
                    UNION
                    SELECT unnest(d.actors) as author_id from dvd d
                    ) as andere join book on book.author = andere.author_id
                    
                SELECT avg(array_length(string_to_array(cd.titlelist, ','), 1)) from cd;
                
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
                   \s
                ))
                where similar_product_category2<>'unknown';
                                
                query11:
                SELECT p.product
                FROM productcatalog p
                GROUP BY p.product
                HAVING COUNT(DISTINCT p.store) = (SELECT COUNT(*) FROM store);
                
                query12
                WITH ProductInAllStores AS (
                    SELECT p.product
                    FROM productcatalog p
                    GROUP BY p.product
                    HAVING COUNT(DISTINCT p.store) = (SELECT COUNT(*) FROM store)
                ),
                CheapestInSpecificStore AS (
                    SELECT p.product
                    FROM productcatalog p
                    WHERE p.store ='ee3da6b4-ec11-49db-a03d-34490312aa8c'
                    AND p.price = (
                        SELECT MIN(pc.price)
                        FROM productcatalog pc
                        WHERE pc.product = p.product
                    )
                )
                SELECT pas.product
                FROM ProductInAllStores pas
                JOIN CheapestInSpecificStore css ON pas.product = css.product;
                """;
    }
}
