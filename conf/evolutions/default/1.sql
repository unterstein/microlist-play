# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table project (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  user_id                   bigint,
  constraint pk_project primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
  password                  varchar(255),
  email                     varchar(255),
  constraint uq_user_email unique (email),
  constraint pk_user primary key (id))
;

alter table project add constraint fk_project_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_project_user_1 on project (user_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table project;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

