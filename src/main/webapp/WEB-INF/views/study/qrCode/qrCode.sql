show tables;

create table qrCode (
	-- idx int not null auto_increment primary key,
	idx char(8) not null,					/* UUID 앞에서 8글자로 지정 */
	qrCode varchar(200) not null,	/* 100글자만 해도 됨 */
	bigo varchar(200)
);

desc qrCode;

select * from qrCode;

drop table qrCode;

select * from qrCode where idx = '';