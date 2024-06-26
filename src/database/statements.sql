select 'Bücher' as Produkt_Typ, count(*) as Anzahl
from book
union all
select 'CD' as Produkt_Typ, count(*) as Anzahl
from cd
union all
select 'DVD' as Produkt_Typ, count(*) as Anzahl
from dvd;