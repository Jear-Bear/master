--question 1
SELECT order_id, order_date, customer_id, order_total, sales_rep_id
FROM oe.orders
ORDER BY customer_id ASC, order_date ASC;

--question 2
SELECT order_id, order_date, customer_id, order_total, sales_rep_id
FROM oe.orders
WHERE order_total > 100000;

--question 3
SELECT order_id, line_item_id, product_id, unit_price, quantity
FROM oe.order_items
WHERE line_item_id = 1;

--question 4
SELECT order_id, order_date, order_total
FROM oe.orders
WHERE order_total BETWEEN 75000 AND 100000;

--question 5
SELECT customer_id, cust_first_name, cust_last_name, nls_territory
FROM oe.customers
WHERE nls_territory = 'AMERICA' 
    OR nls_territory = 'INDIA' 
    OR nls_territory = 'CHINA'
ORDER BY nls_territory ASC, customer_id ASC;

--question 6
SELECT product_id, product_name, product_description
FROM oe.product_information
WHERE product_name LIKE 'Inkjet%';

--question 7
SELECT product_id, supplier_id, catalog_url
FROM oe.product_information
WHERE catalog_url LIKE '%102094%';

--question 8
SELECT order_id, order_date, order_total, customer_id, sales_rep_id
FROM oe.orders
WHERE sales_rep_id IS NULL;

--question 9
SELECT order_id, order_date, order_total, customer_id, sales_rep_id
FROM oe.orders
WHERE sales_rep_id IS NULL
    AND (extract(year from order_date) = '2007' OR order_total > 100000)
ORDER BY order_date;

--question 10
SELECT product_id, product_name, category_id, min_price
FROM oe.product_information
WHERE (min_price >= 1000 OR min_price <= 10)
    AND category_id = 19
ORDER BY min_price DESC;
    
--question 11
SELECT MIN(order_total), MAX(order_total), SUM(order_total), COUNT(order_total), AVG(order_total)
FROM oe.orders;

--question 12
SELECT customer_id, MIN(order_total), MAX(order_total), SUM(order_total), COUNT(order_total), AVG(order_total)
FROM oe.orders
GROUP BY customer_id
ORDER BY sum(order_total) DESC;

--question 13
SELECT product_id, COUNT(product_id)
FROM oe.order_items
GROUP BY product_id
ORDER BY COUNT(product_id) DESC;

--question 14
SELECT account_mgr_id, credit_limit, COUNT(credit_limit)
FROM oe.customers
GROUP BY account_mgr_id, credit_limit
ORDER BY account_mgr_id ASC, credit_limit DESC;

--question 15
select category_id, count(category_id)
FROM oe.product_information
WHERE count(category_id) > 25
GROUP BY category_id, count(category_id)
ORDER BY count(category_id) DESC;

--question 16
SELECT customer_id, MIN(order_date), SUM(order_total)
FROM oe.orders
HAVING extract(year from MIN(order_date)) < '2007'
 AND SUM(order_total) > 150000
GROUP BY customer_id
ORDER BY MIN(order_date) ASC;

