show tables;

create table member2 (
	idx int not null auto_increment,						/* 회원 고유번호 */
	mid varchar(20)	not null,										/* 회원 아이디(중복불허) */
	pwd varchar(100) not null,									/* 비밀번호(SHA256 암호화 처리) */
	nickName varchar(20) not null,							/* 별명(필수입력) */
	name varchar(20) not null,									/* 회원 성명 */
	gender varchar(5) default '여자',						/* 성별 */
	birthday datetime default now(),						/* 생일 */
	tel varchar(15),														/* 전화번호(010-1234-5678) */
	address varchar(100),												/* 회원주소(상품 배달 시 기본주소로 사용) */
	email varchar(50) not null,									/* 이메일(아이디/비밀번호 분실시 사용) - 형식체크 필수 */
	homePage varchar(50) not null,							/* 홈페이지(블로그) 주소 */
	job varchar(20),														/* 회원 직업 */
	hobby varchar(100),													/* 회원 취미(2개 이상은 '/' 로 구분처리)*/
	photo varchar(100) default 'noimage.jpg',		/* 회원 사진 */	
	content text,																/* 회원 자기 소개 */
	userInfor char(6) default '공개',						/* 회원정보 공개여부(공개/비공개) */ 
	userDel char(2) default 'NO',								/* 회원 탈퇴 신청 여부 (OK: 탈퇴신청한회원, NO: 가입중인회원 */
	point int default 100,											/* 회원 누적 포인트 (가입시는 기본 100p, 방문 시 마다 1회 10p 부여, 1일 max 50p) */
	level int default 4, 												/* 회원등급(0: 관리자, 1: 운영자, 2: 우수회원, 3:정회원, 4:준회원) */
	visitCnt int default 0,											/* 방문횟수 */
	startDate datetime default now(),						/* 최초 가입일 */
	lastDate datetime default now(),						/* 마지막 접속일 */
	todayCnt int default 0,											/* 오늘 방문한 횟수 */
	primary key(idx, mid)												/* 주키: idx(고유번호), mid(아이디) */
);

drop table member2;

desc member2;
select * from member2;

/* 더미 데이터 */
insert into member2 values (default, 'admin', '1234', '관리해용', '관리자', default, default, '010-1234-1234', '충북 청주시 흥덕구', 'yam@abc.com', 'http://yam.h20@blog.com', '프리랜서', '등산/바둑', default, '관리자예요.', default, default, default, 0, default, default, default, default);

delete from member2 where idx = 1;

select * from member2 where mid = 'admin';