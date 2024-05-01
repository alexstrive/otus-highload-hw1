create table if not exists public.city
(
    id   serial
    constraint city_pk
    primary key,
    name varchar(255) not null
    );

alter table public.city
    owner to postgres;

create table if not exists public."user"
(
    id         serial
    constraint user_pk
    primary key,
    first_name varchar(255) not null,
    last_name  varchar(255),
    email      varchar(255) not null,
    password   varchar(255) not null,
    birthday   date,
    gender     varchar(6),
    city_id    integer
    constraint user__city_fk
    references public.city
    );

comment on column public."user".password is 'hashed';

alter table public."user"
    owner to postgres;

create table if not exists public.interest
(
    id   serial
    constraint interest_pk
    primary key,
    name varchar(255) not null
    );

alter table public.interest
    owner to postgres;

create table if not exists public.user_interest
(
    user_id     integer not null
    constraint user_interest___user_id_fk
    references public."user",
    interest_id integer not null
    constraint user_interest___interest_id_fk
    references public.interest,
    constraint user_interest_pk
    primary key (user_id, interest_id)
    );

alter table public.user_interest
    owner to postgres;

