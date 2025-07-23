create table public.locations
(
    id varchar(255) not null primary key,
    city varchar(255) not null,
    code varchar(255) not null constraint location_code_unique_constraint unique,
    country varchar(255) not null,
    name varchar(255) not null
);

create table public.transportations
(
    id varchar(255) not null primary key,
    operating_days integer[] not null,
    type varchar(255) not null,
    destination_location_id varchar(255) not null constraint destination_location_id_foreign_key_constraint references public.locations,
    origin_location_id varchar(255) not null constraint origin_location_id_foreign_key_constraint references public.locations
);

create table public.roles
(
    id varchar(255) not null primary key,
    name varchar(255) not null
);

create table public.users
(
    id varchar(255) not null primary key,
    password varchar(255) not null,
    username varchar(255) not null constraint user_username_unique_constraint unique
);

create table public.users_roles
(
    user_entity_id varchar(255) not null constraint user_entity_id_foreign_key references public.users,
    roles_id varchar(255) not null constraint roles_id_foreign_key references public.roles,
    primary key (user_entity_id, roles_id)
);

insert into public.roles (id, name) values
    ('1', 'ADMIN'),
    ('2', 'AGENCY');

insert into public.users (id, username, password) values
    ('1', 'admin', '$2a$12$H6AnJM8B2jnhJFBFglqxnu9DOvHIBzLf3fVM4xn72rFc/2Gra7uf6'),
    ('2', 'agency', '$2a$12$H6AnJM8B2jnhJFBFglqxnu9DOvHIBzLf3fVM4xn72rFc/2Gra7uf6');

insert into public.users_roles (user_entity_id, roles_id) values
    ('1', '1'),
    ('1', '2'),
    ('2', '2');
