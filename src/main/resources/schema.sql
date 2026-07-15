create table tags
(
    id   int auto_increment
        primary key,
    name varchar(255) not null,
    constraint tags_name_uindex
        unique (name)
);

create table users
(
    id           int auto_increment
        primary key,
    first_name   varchar(32)            not null,
    last_name    varchar(32)            not null,
    email        varchar(255)           not null,
    username     varchar(255)           not null,
    password     varchar(255)           not null,
    phone_number varchar(15)            null,
    role         enum ('USER', 'ADMIN') not null,
    blocked      tinyint(1)             null,
    created_at   date                   not null,
    constraint users_email_uindex
        unique (email),
    constraint users_password_uindex
        unique (password)
);

create table posts
(
    id         int auto_increment
        primary key,
    title      varchar(255) not null,
    content    varchar(255) not null,
    author_id  int          not null,
    created_at date         not null,
    updated_at date         not null,
    constraint author_id
        foreign key (author_id) references users (id)
);

create table comments
(
    id         int auto_increment
        primary key,
    content    varchar(255) not null,
    author_id  int          not null,
    post_id    int          not null,
    created_at date         not null,
    updated_at date         not null,
    constraint comments_posts_id_fk
        foreign key (post_id) references posts (id),
    constraint comments_users_id_fk
        foreign key (author_id) references users (id)
);

create table post_likes
(
    id      int auto_increment
        primary key,
    post_id int not null,
    user_id int not null,
    constraint post_likes_post_id_uindex
        unique (post_id),
    constraint post_likes_user_id_uindex
        unique (user_id),
    constraint post_likes_posts_id_fk
        foreign key (post_id) references posts (id),
    constraint post_likes_users_id_fk
        foreign key (user_id) references users (id)
);

create table post_tags
(
    post_id int not null,
    tag_id  int not null,
    primary key (post_id, tag_id),
    constraint post_tags_posts_id_fk
        foreign key (post_id) references posts (id),
    constraint post_tags_tags_id_fk
        foreign key (tag_id) references tags (id)
);



