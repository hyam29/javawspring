show tables;

create table guest2 (
	idx int not null auto_increment primary key, /* 고유번호 */
	name varchar(20) not null, /* 방문자 성명 */
	email varchar(60), /* 이메일 주소 */
	homePage varchar(60), /* 홈페이지 주소 */
	visitDate datetime default now(), /* 방문일자 */
	hostIp varchar(50) not null, /* 방문자 IP */
	content text not null	/* 방문소감(내용) */
);

desc guest2;
drop table guest2;

insert into guest2 values (default, '관리자', 'yam@abc.net', '입력사항 없음', default, '192.168.50.190', '방명록 서비스를 표시합니다.');

select * from guest2;