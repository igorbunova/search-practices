create table song (
    id bigint not null,
    duration_ms int,
    name text,
    artist_display_name text,
    album_display_name text,
    isrc text,
    constraint song_pk primary key(id)
);

create table translation (
    song_id bigint not null,
    language varchar(10) not null,
    field varchar(30) not null,
    value text not null,
    constraint translation_pk primary key(song_id, language, field),
    constraint song_id_fk foreign key(song_id) references song(id)
);

\copy song from './song.csv' delimiter ';' csv header;
\copy translation from './song_name_tr.csv' delimiter ';' csv header;

alter table song add column modify_dt timestamp not null default now();
alter table translation add column modify_dt timestamp not null default now();

