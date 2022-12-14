CREATE TABLE public.clients
(
    id           int8         NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    address      varchar(255) NOT NULL,
    full_name    varchar(255) NOT NULL,
    phone_number varchar(255) NOT NULL,
    CONSTRAINT clients_pkey PRIMARY KEY (id)
);

CREATE TABLE public.pizzas
(
    id          int8         NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    ingredients varchar(255) NOT NULL,
    "name"      varchar(255) NOT NULL,
    spicy       bool NULL,
    vegetarian  bool NULL,
    CONSTRAINT pizzas_pkey PRIMARY KEY (id)
);

CREATE TABLE public.orders
(
    id        int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    status    varchar(255) NULL,
    client_id int8 NOT NULL,
    CONSTRAINT orders_pkey PRIMARY KEY (id)
);

CREATE TABLE public.pizza_order
(
    order_id int8 NOT NULL,
    pizza_id int8 NOT NULL
);

INSERT INTO public.clients (address, full_name, phone_number)
VALUES ('1119 Hauk Crossing', 'Ora Humbey', '375-754-8278');

INSERT INTO public.pizzas (ingredients, name, spicy, vegetarian)
VALUES ('Tomatoes, garlic, oregano, and extra virgin olive oil', 'Neapolitan', false, true);