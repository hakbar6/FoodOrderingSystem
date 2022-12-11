DROP SCHEMA IF EXISTS customer CASCADE;

CREATE SCHEMA customer;

/* buat extension jika uuid tidak ditemukan */
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE "customer".customers
(
    id uuid NOT NULL ,
    username character varying COLLATE pg_catalog."default" NOT NULL ,
    first_name character varying COLLATE pg_catalog."default" NOT NULL ,
    last_name character varying COLLATE pg_catalog."default" NOT NULL ,
    CONSTRAINT customers_pkey PRIMARY KEY (id)
);

DROP MATERIALIZED VIEW IF EXISTS customer.order_customer_m_view;

CREATE MATERIALIZED VIEW customer.order_customer_m_view
TABLESPACE pg_default
AS
    SELECT id,
           username,
           first_name,
           last_name
        FROM customer.customers
WITH DATA;

refresh materialized VIEW customer.order_customer_m_view;
    /*
    Materialized view is physical representation of a query that might include one or more
    tables with rare condition. Materialized view data is stored in disc instead of the query each
    time. So, it will better to use materialized view as cache, instead of query it each time.

    If data in schema is changed, materialized view will refresh to get new data.
    */

DROP function IF EXISTS customer.refresh_order_customer_m_view;

CREATE OR replace function customer.refresh_order_customer_m_view()
    returns trigger
    AS '
    BEGIN
        refresh materialized VIEW customer.order_customer_m_view;
        return null;
    END;
    ' LANGUAGE plpgsql;

DROP trigger IF EXISTS refresh_order_customer_m_view ON customer.customers;

CREATE trigger refresh_order_customer_m_view
    after INSERT OR UPDATE OR DELETE OR truncate
    ON customer.customers FOR each statement
EXECUTE PROCEDURE customer.refresh_order_customer_m_view();