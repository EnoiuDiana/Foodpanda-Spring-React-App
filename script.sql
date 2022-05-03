create table address
(
    id        bigint auto_increment
        primary key,
    city      varchar(255) not null,
    street    varchar(255) not null,
    street_no int          not null
);

create table delivery_zone
(
    id   bigint auto_increment
        primary key,
    name varchar(255) not null,
    constraint UK_ss6fqqek7kf2owkosw5ccnk1y
        unique (name)
);

create table restaurant
(
    id         bigint auto_increment
        primary key,
    name       varchar(255) not null,
    address_id bigint       not null,
    constraint UK_i6u3x7opncroyhd755ejknses
        unique (name),
    constraint FK96q13p1ptpewvus590a8o83xt
        foreign key (address_id) references address (id)
);

create table menu
(
    id            bigint auto_increment
        primary key,
    restaurant_id bigint null,
    constraint UK_f8ucgppk8hu6hra30opglnux3
        unique (restaurant_id),
    constraint FKblwdtxevpl4mrds8a12q0ohu6
        foreign key (restaurant_id) references restaurant (id)
);

create table menu_item
(
    id          bigint auto_increment
        primary key,
    description varchar(255) not null,
    name        varchar(255) not null,
    price       float        not null,
    menu_id     bigint       not null,
    category    varchar(255) not null,
    constraint UK_aaw4j0c1b37xh7ntmavh5utpp
        unique (name),
    constraint FKcdkmv42yhn6udah6ug8rsymfl
        foreign key (menu_id) references menu (id)
);

create table restaurant_delivery_zone
(
    restaurant_id    bigint not null,
    delivery_zone_id bigint not null,
    constraint FKbj2rfjaherldagcetxe55xmse
        foreign key (delivery_zone_id) references delivery_zone (id),
    constraint FKuqtooo3pbm8opml8o4ofi8kn
        foreign key (restaurant_id) references restaurant (id)
);

create table user
(
    id         bigint auto_increment
        primary key,
    email      varchar(255) not null,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    password   varchar(255) not null,
    constraint UK_ob8kqyqqgmefl0aco34akdtpe
        unique (email)
);

create table admin
(
    id            bigint not null
        primary key,
    restaurant_id bigint null,
    constraint UK_8j4f39vvylo34wofb1dc4mdy5
        unique (restaurant_id),
    constraint FK1ja8rua032fgnk9jmq7du3b3a
        foreign key (id) references user (id),
    constraint FKiiyxksvn6gl7i1ywyisqy7rkc
        foreign key (restaurant_id) references restaurant (id)
);

create table customer
(
    id bigint not null
        primary key,
    constraint FKg2o3t8h0g17smtr9jgypagdtv
        foreign key (id) references user (id)
);

create table cart_item
(
    id          bigint auto_increment
        primary key,
    customer_id bigint not null,
    menuitem_id bigint not null,
    constraint FKfy7fubprxqguyp4km04eogy66
        foreign key (customer_id) references customer (id),
    constraint FKqno19mt0mkf4syfl70rti2nr8
        foreign key (menuitem_id) references menu_item (id)
);

create table placed_order
(
    id                bigint auto_increment
        primary key,
    status            varchar(255) not null,
    customer_ord_id   bigint       not null,
    restaurant_ord_id bigint       not null,
    constraint FK55ruecr6s6bsj3bgpv5094hmn
        foreign key (restaurant_ord_id) references restaurant (id),
    constraint FKh3rnilgyb29m2j449cmib4sey
        foreign key (customer_ord_id) references customer (id)
);

create table order_item
(
    id              bigint auto_increment
        primary key,
    menuitem_ord_id bigint not null,
    order_id        bigint not null,
    constraint FKkt9njeicfn4wtdkk7tcijn9vc
        foreign key (menuitem_ord_id) references menu_item (id),
    constraint FKmukqc0xe8hnvw5j252mimgp8o
        foreign key (order_id) references placed_order (id)
);


