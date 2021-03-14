-- Exclui tudo para recriar
drop schema if exists anotacao;
drop user if exists 'anotacao'@'localhost';

source DDL.sql
