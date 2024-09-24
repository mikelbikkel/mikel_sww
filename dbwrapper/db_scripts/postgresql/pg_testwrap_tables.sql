drop table if exists app_module;

create table app_module
( id          serial primary key
, ts_insert   timestamp default current_timestamp
, app_module  varchar(100)
, app_name    varchar(100)
, seqno       integer
, tx_date     date
, amount      decimal(9,2)
, isOKorNull  varchar(1)
, isOK        varchar(1) not null default 'N'
, constraint tw_ok_domain check (isOK in ('Y', 'N'))
, constraint tw_okn_domain check (isOKorNull in ('Y', 'N'))
);

drop table if exists test_city;
create table test_city
( name   varchar(50) primary key
, description varchar(100)
);

--
-- Version 1: Functional key and primary key are the same.
--
drop table if exists tv_serie;
create table tv_serie
( name        varchar(50) not null
, description varchar(100)
, producer    varchar(100)
, constraint pk_tv_serie primary key(name)
);

drop table if exists tv_episode;
create table tv_episode
( s_name  varchar(50) not null
, season  integer not null
, episode integer not null
, name        varchar(50) not null
, description varchar(100)
, constraint pk_tv_episode primary key(name, season, episode)
, constraint fk_tv_serie foreign key(s_name) references tv_serie(name) on delete restrict
);

drop view tv_episode_all;
create view tv_episode_all as
select ts.name s_name
     , ts.description s_description
     , ts.producer s_producer
     , ep.season e_season
     , ep.episode e_episode
     , ep.name e_name
     , ep.description e_description
from   tv_episode ep join tv_serie ts on ep.s_name = ts.name;

--
-- Version 2: Separate the primary key from the functional key.
-- Primary key is a sequence.
-- Functional key is implemented as a unique key.
--
-- If there are children, the PK of the parent should be  immutable. Enforce in FK constraints.
--
drop table if exists tv_serie_2;
create table tv_serie_2
( id          serial primary key
, name        varchar(50) not null
, description varchar(100)
, producer    varchar(100)
, constraint uk_tv_serie_2 unique(name)
);

drop table if exists tv_episode_2;
create table tv_episode_2
( id      serial primary key
, s_id    integer not null
, season  integer not null
, episode integer not null
, name        varchar(50) not null
, description varchar(100)
, constraint uk_tv_episode_2 unique(s_id, season, episode)
, constraint fk_tv_serie_2 foreign key(s_id) references tv_serie_2(id)
                           on delete restrict
                           on update restrict
);

drop view tv_episode_all_2;
create view tv_episode_all_2 as
select ep.id          e_id
     , ep.season      e_season
     , ep.episode     e_episode
     , ep.name        e_name
     , ep.description e_description
     , ep.s_id        s_id
     , ts.name        s_name
     , ts.description s_description
     , ts.producer    s_producer
from   tv_episode_2 ep join tv_serie_2 ts on ep.s_id = ts.id;

--
-- Version 3: add field for optimistic locking.
--
