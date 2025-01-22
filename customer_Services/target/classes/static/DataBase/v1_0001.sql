-- for Ads cost

--CREATE TABLE ads_cost_tabel (
--	adscost_id int4 NOT NULL,
--	ads_prices int8 NULL,
--	interset_rate int4 NULL,
--	"type" varchar(255) DEFAULT 'OTHERS'::character varying NULL,
--	CONSTRAINT ads_cost_tabel_pkey PRIMARY KEY (adscost_id),
--	CONSTRAINT ads_cost_tabel_type_check CHECK (((type)::text = ANY ((ARRAY['ENTERTAINMENT'::character varying, 'EDUCATION'::character varying, 'POLITICS'::character varying, 'BUSINESS'::character varying, 'PERSONAL'::character varying, 'OTHERS'::character varying])::text[])))
--);

INSERT INTO ads_cost_tabel (adscost_id,ads_prices,interset_rate,"type") VALUES
	 (1,1000,2,'EDUCATION'),
	 (2,2000,4,'BUSINESS'),
	 (3,3000,7,'ENTERTAINMENT'),
	 (4,4000,12,'POLITICS'),
	 (5,5000,5,'OTHERS'),
	 (6,6000,7,'PERSONAL');


--CREATE TABLE customer (
--	customer_id int4 NOT NULL,
--	address varchar(255) NULL,
--	cash_valut int4 NULL,
--	creation_date timestamp(6) NULL,
--	customer_name varchar(255) NULL,
--	email varchar(255) NULL,
--	is_active bool NOT NULL,
--	last_updated_date timestamp(6) NULL,
--	"password" varchar(255) NULL,
--	"role" varchar(255) NULL
--);

--CREATE TABLE customer_role (
--	role_id uuid NOT NULL,
--	roles varchar(255) NULL
--);

INSERT INTO customer_role (role_id,roles) VALUES
	 ('a1153c38-96ab-4024-b1fe-a48b8e2853e0'::uuid,'USER'),
	 ('4364a6be-eb75-4cf0-8948-2dc0fc9a0632'::uuid,'SUPERUSER'),
	 ('66efd1ed-be09-419e-b517-8b64a3f91535'::uuid,'ADMIN');

--CREATE TABLE order_ads_tabel (
--	order_id uuid NOT NULL,
--	ads_id int4 NOT NULL
--);

--CREATE TABLE orders_placed (
--	order_no uuid NOT NULL,
--	order_status bool NULL,
--	creation_date timestamp(6) NULL,
--	customer_id int4 NULL,
--	customer_name varchar(255) NULL,
--	total_days int4 NULL,
--	is_updatedto_superuser bool NULL,
--	order_cancellation bool NOT NULL,
--	total_cost int4 NULL
--);

