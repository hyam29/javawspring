show tables;

desc user;

create table user2 (
	mid varchar(5) not null,
	nickName varchar(20) not null,
	job varchar(10) not null
);

desc user2;

insert into user value (default, 'bbbbbb', '비맨', 33, '청주');
insert into user2 value ('bbbbbb', '비다', '회사원');

