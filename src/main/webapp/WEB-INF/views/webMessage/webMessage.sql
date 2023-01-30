show tables;

create table webMessage (
	idx int not null auto_increment primary key,			/* 고유번호 */
	title varchar(100) not null,											/* 메세지 제목 */
	content text not null,														/* 메세지 내용 */
	sendId varchar(20) not null,											/* 보내는 사람 아이디 */
	sendSw char(1) not null,													/* 보낸메세지(s), (휴지통(g)->생략) , 영구삭제(x) */
	sendDate datetime default now(),									/* 메세지 보낸 날짜 */
	receiveId varchar(20) not null,										/* 받는 사람 아이디 */
	receiveSw char(1) not null,												/* 받은메세지(n(ew)), 읽은메세지(r(ead)), 휴지통(g), 영구삭제(x) */
	receiveDate datetime default now()								/* 메세지 받은 날짜 */
);

desc webMessage;
select * from webMessage;

insert into webMessage values (default, '안녕 말숙쓰.', '주말에 시간 됨?', 'hkd1234', 's', default, 'kms1234', 'n', default);
update webMessage set receiveSw = 'r', receiveDate = now() where idx = 1;

insert into webMessage values (default, '방가방가 길동쓰.', 'ㄴㄴ 나 주말에 프로젝트 해야 해.', 'kms1234', 's', default, 'hkd1234', 'n', default);
update webMessage set receiveSw = 'r', receiveDate = now() where idx = 2;
update webMessage set receiveSw = 'g' where idx = 2;
-- 길동입장 : 보낸 메세지 삭제
update webMessage set sendSw = 'x' where idx = 1;
-- 말숙입장 : 휴지통 -> 영구삭제
update webMessage set receiveSw = 'g' where idx = 1;
update webMessage set receiveSw = 'x' where idx = 1;

delete from webMessage where idx = 1;

