# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table project (
  id                        bigint auto_increment not null,
  name                      varbinary(255),
  user_id                   bigint,
  constraint pk_project primary key (id))
;

create table task (
  id                        bigint auto_increment not null,
  title                     varbinary(255),
  description               varbinary(255),
  finished                  tinyint(1) default 0,
  due_date                  datetime,
  project_id                bigint,
  constraint pk_task primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
  password                  varbinary(255),
  email                     varchar(255),
  constraint uq_user_email unique (email),
  constraint pk_user primary key (id))
;

alter table project add constraint fk_project_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_project_user_1 on project (user_id);
alter table task add constraint fk_task_project_2 foreign key (project_id) references project (id) on delete restrict on update restrict;
create index ix_task_project_2 on task (project_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table project;

drop table task;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

