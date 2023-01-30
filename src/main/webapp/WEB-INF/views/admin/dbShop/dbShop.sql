show tables;

/* 대분류 */
create table categoryMain (
	categoryMainCode char(1) not null,							/* 대분류코드(A,B,C, ...) 영대문 1자 */ 
	categoryMainName varchar(20) not null,					/* 대분류명(회사명) 삼성, LG, 현대 등 */
	primary key(categoryMainCode),
	unique key(categoryMainName)
);

desc categoryMain;

/* 중분류 */
create table categoryMiddle (
	categoryMainCode char(1) not null,							/* 대분류코드를 외래키로 지정 */ 
	categoryMiddleCode char(2) not null,						/* 중분류코드(01,02,03, ...) 숫자(문자형식 int XXX) 2자리 */ 
	categoryMiddleName varchar(20) not null,				/* 중분류명(제품분류명) 전자제품, 의류, 신발류, 차종 등 */
	primary key(categoryMiddleCode),
	/*unique key(categoryMiddleName)*/
	foreign key(categoryMainCode) references categoryMain(categoryMainCode)
);
/* 
	unique key(categoryMiddleName) 생략?
	삼성에도, LG에도 전자제품이 있다면 중복이 되므로 생략해야함
	설계 시 잘 생각하고 테이블 생성하기
*/

/* 소분류 */
create table categorySub (
	categoryMainCode char(1) not null,							/* 대분류코드를 외래키로 지정 */ 
	categoryMiddleCode char(2) not null,						/* 중분류코드를 외래키로 지정 */ 
	categorySubCode char(3) not null,								/* 소분류코드(001, 002, 003, ...) 숫자(문자형식) 3자리 */ 
	categorySubName varchar(20) not null,						/* 소분류명(상품구분) (전자제품에서의) 냉장고, 에어컨, TV 등 */
	primary key(categorySubCode),
	/*unique key(categorySubName) 상동 이유로 생략 */
	foreign key(categoryMainCode) references categoryMain(categoryMainCode),
	foreign key(categoryMiddleCode) references categoryMiddle(categoryMiddleCode)
);

/* 세분류(상품 테이블) */
create table dbProduct (
	idx int not null,																/* 상품 고유번호 */
	categoryMainCode char(1) not null,							/* 대분류코드를 외래키로 지정 */ 
	categoryMiddleCode char(2) not null,						/* 중분류코드를 외래키로 지정 */ 
	categorySubCode char(3) not null,								/* 소분류코드를 외래키로 지정 */
	productCode varchar(20) not null,								/* 상품고유코드(대분류코드+중분류코드+소분류코드+고유번호(랜덤)) */
	productName varchar(50) not null,								/* 상품명(상품모델명) 세분류 (관리자는 이것만으로 상품 알 수 있음) */
	detail varchar(100) not null,										/* 상품명으로 소비자는 모르니까 상품의 간단설명(초기화면출력에 필요) */
	mainPrice int not null,													/* 상품의 기본가격 */
	fSName varchar(100) not null,										/* 상품의 기본사진(여기선 1장만 처리, 상품사진이 여러개라면 fSName 1,2,3 으로 여러 필드 만들어야 함) */
	content text not null,													/* 상품의 상세설명 - ckeditor를 이용한 이미지 처리 */
	primary key(idx, productCode),
	unique key(productName),												/* 상품명은 중복XXX -> unique key */
	foreign key(categoryMainCode) references categoryMain(categoryMainCode),
	foreign key(categoryMiddleCode) references categoryMiddle(categoryMiddleCode),
	foreign key(categorySubCode) references categorySub(categorySubCode)
);

/* 상품 옵션 */
create table dbOption (
	idx int not null auto_increment,			/* 옵션 고유번호 */
	productIdx int not null,							/* product테이블(상품)의 고유번호 - 외래키 지정 필요 */
	optionName varchar(50) not null,			/* 옵션 이름 */
	optionPrice int not null default 0,		/* 옵션 가격 */
	primary key(idx),
	foreign key(productIdx) references dbProduct(idx)
);

drop table categoryMain;
drop table categoryMiddle;
drop table categorySub;
drop table dbProduct;
drop table dbOption;

desc categoryMain;
desc categoryMiddle;
desc categorySub;
desc dbProduct;
desc dbOption;

select * from categoryMain;
select * from categoryMiddle;
select * from categorySub;
select * from dbProduct;
select * from dbOption;