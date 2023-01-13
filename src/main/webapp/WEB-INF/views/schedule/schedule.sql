show tables;

create table schedule2 (
	idx int not null auto_increment primary key,
	mid varchar(20) not null,
	sDate datetime not null,											/* 일정 등록한 날짜 */
	part varchar(20) not null,										/* 모임(1), 업무(2), 학습(3), 여행(4), 기타(5) */
	content text not null													/* 일정 상세 내역 */
);

desc schedule2;

insert into schedule2 values (default, 'hkd1234', '2023-01-13', '모임', '가족회의, 장소:집, 시간: 18');
insert into schedule2 values (default, 'hkd1234', '2023-01-13', '모임', '동창모임, 장소:충대중문, 시간: 12');
insert into schedule2 values (default, 'hkd1234', '2023-01-13', '학습', '프로젝트 계획서 작성');
insert into schedule2 values (default, 'hkd1234', '2023-01-14', '업무', '계획서 제출(20까지)');
insert into schedule2 values (default, 'hkd1234', '2023-01-15', '학습', '스터디, 장소:집, 시간: 18');
insert into schedule2 values (default, 'hkd1234', '2023-01-15', '기타', '빨래');
insert into schedule2 values (default, 'hkd1234', '2023-01-20', '학습', '스터디, 장소:집, 시간: 18');
insert into schedule2 values (default, 'hkd1234', '2023-01-21', '기타', '병원가기');
insert into schedule2 values (default, 'hkd1234', '2023-01-21', '모임', '친구모임, 장소:성안길, 시간: 16');
insert into schedule2 values (default, 'hkd1234', '2023-01-22', '여행', '가족여행, 장소:부산, 시간: 08');
insert into schedule2 values (default, 'hkd1234', '2023-02-22', '학습', '프로젝트 점검');
insert into schedule2 values (default, 'kms1234', '2023-01-23', '기타', '집안 청소');
insert into schedule2 values (default, 'kms1234', '2023-01-23', '학습', '프로젝트시작, 장소:집, 시간: 19');

select * from schedule2;

drop table schedule2;

select * from schedule2 where mid='hkd1234' order by sDate;
select * from schedule2 where mid='hkd1234' and sDate='2023-1' order by sDate; -- XXX
select * from schedule2 where mid='hkd1234' and sDate='2023-01' order by sDate; -- XXX
select * from schedule2 where mid='hkd1234' and sDate ='2023-01-13' order by sDate; -- OOO (년,월,일 모두 작성하면 인식 가능)
select * from schedule2 where mid='hkd1234' and substring(sDate,1,7) ='2023-01' order by sDate; -- OOO (문자 변경함수 작성하여 처리 (DB에서만 0이 아닌 1부터 문자열 시작)
select * from schedule2 where mid='hkd1234' and date_format(sDate, '%Y-%m') ='2023-1' order by sDate; -- XXX
select * from schedule2 where mid='hkd1234' and date_format(sDate, '%Y-%m') ='2023-01' order by sDate; -- OOO
select * from schedule2 where mid='hkd1234' and date_format(sDate, '%Y-%m') ='2023-01' group by substring(sDate,1,7) order by sDate;
select sDate,count(*) from schedule2 where mid='hkd1234' and date_format(sDate, '%Y-%m') ='2023-01' group by substring(sDate,1,7) order by sDate;
select sDate,part from schedule2 where mid='hkd1234' and date_format(sDate, '%Y-%m') ='2023-01' order by sDate, part;
select sDate,part from schedule2 where mid='hkd1234' and date_format(sDate, '%Y-%m') ='2023-01' order by sDate, part;

select * from schedule2 where mid='hkd1234' and date_format(sDate, '%Y-%m-%d')='2023-1-13';
