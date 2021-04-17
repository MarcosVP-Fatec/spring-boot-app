create schema if not exists anotacao;
use anotacao;
create user if not exists 'user'@'localhost' identified by 'pass123';
grant select, insert, delete, update on anotacao.* to user@'localhost';

create table usr_usuario (
  usr_id    bigint unsigned not null auto_increment,
  usr_nome  varchar(20)     not null,
  usr_senha varchar(50)     not null,
  primary key (usr_id),
  unique key uni_usuario_nome (usr_nome)
);

create table aut_autorizacao (
  aut_id    bigint unsigned not null auto_increment,
  aut_nome  varchar(20)     not null,
  primary key (aut_id),
  unique key uni_aut_nome (aut_nome)
);

create table uau_usuario_autorizacao (
  usr_id bigint unsigned not null,
  aut_id bigint unsigned not null,
  primary key (usr_id, aut_id),
  foreign key aut_usuario_fk (usr_id) references usr_usuario (usr_id) on delete restrict on update cascade,
  foreign key aut_autorizacao_fk (aut_id) references aut_autorizacao (aut_id) on delete restrict on update cascade
);

insert into aut_autorizacao (aut_nome) values ('ROLE_ADMIN');
insert into usr_usuario (usr_nome,usr_senha) values ('Mitces','pwM');
insert into uau_usuario_autorizacao (usr_id, aut_id) values (1,1);

insert into aut_autorizacao (aut_nome) values ('ROLE_NORMAL_HIGH');
insert into aut_autorizacao (aut_nome) values ('ROLE_NORMAL_LOW');
insert into usr_usuario (usr_nome,usr_senha) values ('Valacae','pwV');
insert into uau_usuario_autorizacao (usr_id, aut_id) values (2,2);
insert into uau_usuario_autorizacao (usr_id, aut_id) values (2,3);