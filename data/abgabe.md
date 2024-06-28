# DbGate erlaubt einen Ergebnisexport. Wir nutzen das JSON Format. Jede Spalte ist ein Wert in einem JSON Objekt. Einträge sind als Array dieses Objektes angeordnet.


## Query 1:

---

```
select
  (
    select
      count(*)
    from
      book
  ) as Anzahl_Bücher,
  (
    select
      count(*)
    from
      cd
  ) as Anzahl_CD,
  (
    select
      count(*)
    from
      dvd
  ) as Anzahl_DVD
```
### Result:
[Link zum JSON Ergebnis](query1.json)

<img src="query1.png">

--- 

## Query 2:

---

```
(
  select
      'Buch' as Produkt_Typ,
        productid,
        ROW_NUMBER() OVER (ORDER BY rating) as Rang
  from
    product as p natural
    join book
  order by
    rating
  limit
    5
)
union all
(
  select
    'CD' as Produkt_Typ,
    productid,
    ROW_NUMBER() OVER (ORDER BY rating) as Rang
  from
    product natural
    join cd
  order by
    rating
  limit
    5
)
union all
(
  select
    'DVD' as Produkt_Typ,
    productid,
    ROW_NUMBER() OVER (ORDER BY rating) as Rang
  from
    product natural
    join dvd
  order by
    rating
  limit
    5
)
```
### Result:
[Link zum JSON Ergebnis](Query2.json)

<img src="query2.png">

--- 

## Query 2 ALTERNATIV:
(Für den Fall das alle gleichwertigen Elemente ausgewählt werden)

---

```
WITH Buchelemente AS (
  SELECT
    *,
    RANK() OVER (
      ORDER BY
        rating DESC
    ) AS rankierung
  FROM
    book natural
    join product
),
CDelemente AS (
  SELECT
    *,
    RANK() OVER (
      ORDER BY
        rating DESC
    ) AS rankierung
  FROM
    cd natural
    join product
),
DVDelemente AS (
  SELECT
    *,
    RANK() OVER (
      ORDER BY
        rating DESC
    ) AS rankierung
  FROM
    dvd natural
    join product
) (
  SELECT
    'Buch' as produkt_typ,
    productid,
    rankierung
  FROM
    Buchelemente
  WHERE
    rankierung <= 5
)
union all
(
  SELECT
    'CD' as produkt_typ,
    productid,
    rankierung
  FROM
    CDelemente
  WHERE
    rankierung <= 5
)
union all
(
  SELECT
    'DVD' as produkt_typ,
    productid,
    rankierung
  FROM
    DVDelemente
  WHERE
    rankierung <= 5
);
```
### Result:
[Link zum JSON Ergebnis](Query2-2.json)

Ergebnis sind 963 Zeilen, daher kein Bild

--- 

## Query 3:

---

```
select
  productid
from
  product
  inner join productcatalog as pc on product.productid = pc.product
where
  pc.available = false
```
### Result:
[Link zum JSON Ergebnis](Query3.json)

Kein Bild, da 2071 Zeilen als Ergebnis

--- 
## Query 4:

---

```
select
  pc.product
from
  productcatalog pc
group by
  pc.product
having
  count(pc.price) > 1
  and max(pc.price) > 2*min(pc.price);
```
### Result:
[Link zum JSON Ergebnis](Query4.json)

Leeres Resultat, es gibt nur 112 doppelte Produkteinträge mit unterschiedlichem Preis, keines davon doppelt so viel wie das Minimum.

--- 
## Query 5:

---

```
select
  productid
from
  product p
  join review r1 on p.productid = r1.product
  and r1.stars = 1
  join review r2 on p.productid = r2.product
  and r2.stars = 5
group by
  p.productid
```
### Result:
[Link zum JSON Ergebnis](Query5.json)

Kein Bild, 139 Zeilen

--- 

## Query 6:

---

```
select
  productid
from
  product
where
  rating = 0
```
### Result:
[Link zum JSON Ergebnis](Query6.json)

Kein Bild, da 795 Einträge.


--- 
## Query 7:

---

```
select
  username
from
  review
group by
  username
having
  COUNT(*) > 9
```
### Result:
[Link zum JSON Ergebnis](Query7.json)

<img src="query7.png">

(Guest ist kein individueller Nutzer, bei uns aktuell trotzdem mitgezählt)

--- 
## Query 8:

---

```
select
  name
from
  (
    select
      ba.author_id
    from
      (
        SELECT
          director AS id1
        FROM
          dvd
        UNION
        SELECT
          creator_id AS id1
        FROM
          dvd_creator
        UNION
        SELECT
          artist_id AS id1
        FROM
          cd_artist
        UNION
        SELECT
          actor_id as id1
        from
          dvd_actor
      ) as andere
      join book_author ba on ba.author_id = andere.id1
    group by
      ba.author_id
  ) as pid
  join person on person.personid = pid.author_id;
```
### Result:
[Link zum JSON Ergebnis](Query8.json)

<img src="query8.png">

--- 
## Query 9:

---

```
SELECT
  avg(
    array_length(string_to_array(cd.titlelist, ','), 1)
  )
from
  cd;
```
### Result:
[Link zum JSON Ergebnis](Query9.json)

<img src="query9.png">

--- 
## Query 10:

---

```
WITH RECURSIVE CategoryHierarchy AS (
  SELECT
    categoryid AS maincategoryid,
    categoryid AS subcategoryid
  FROM
    category
  WHERE
    parentcategory IS NULL
  UNION ALL
  SELECT
    ch.maincategoryid,
    c.categoryid
  FROM
    CategoryHierarchy ch
    JOIN category c ON ch.subcategoryid = c.parentcategory
),
ProductMainCategory AS (
  SELECT
    pc.product,
    ch.maincategoryid
  FROM
    productcategories pc
    JOIN CategoryHierarchy ch ON pc.category = ch.subcategoryid
)
SELECT
  pm1.product AS product1_id,
  pm2.product AS product2_id,
  pm1.maincategoryid AS product1_maincategory,
  pm2.maincategoryid AS product2_maincategory
FROM
  ProductMainCategory pm1
  JOIN ProductMainCategory pm2 ON pm1.product <> pm2.product
  AND pm1.maincategoryid <> pm2.maincategoryid
  JOIN similarproduct sp ON pm1.product = sp.product1
  AND pm2.product = sp.product2
GROUP BY
  pm1.product,
  pm2.product,
  pm1.maincategoryid,
  pm2.maincategoryid
ORDER BY
  product1_id,
  product2_id;
```
### Result:
[Link zum JSON Ergebnis](Query10.json)

Kein Bild, da 1366 Zeilen

--- 
## Query 11:

---

```
SELECT
  p.product
FROM
  productcatalog p
GROUP BY
  p.product
HAVING
  COUNT(DISTINCT p.store) = (
    SELECT
      COUNT(*)
    FROM
      store
  );
```
### Result:
[Link zum JSON Ergebnis](Query11.json)

Kein Bild, da 269 Zeilen

--- 
## Query 12:

---

```
WITH ProductInAllStores AS (
  SELECT
    p.product
  FROM
    productcatalog p
  GROUP BY
    p.product
  HAVING
    COUNT(DISTINCT p.store) = (
      SELECT
        COUNT(*)
      FROM
        store
    )
),
CheapestInSpecificStore AS (
  SELECT
    p.product
  FROM
    productcatalog p
  WHERE
    p.store = (
      select
        storeid
      from
        store
      where
        name = 'Leipzig'
    )
    AND p.price = (
      SELECT
        MIN(pc.price)
      FROM
        productcatalog pc
      WHERE
        pc.product = p.product
    )
)
SELECT
  pas.product
FROM
  ProductInAllStores pas
  JOIN CheapestInSpecificStore css ON pas.product = css.product;
```
### Result:
[Link zum JSON Ergebnis](Query12.json)

Kein Bild, da 56 Zeilen