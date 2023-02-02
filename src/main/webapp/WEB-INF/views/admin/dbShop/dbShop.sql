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
	productCode varchar(20) not null,								/* 상품고유코드(대분류코드+중분류코드+소분류코드+고유번호(랜덤)) ex) A 05 002 5 */
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

/* ================ 상품 주문 시작시에 사용하는 테이블들 (나에게 맞게 설계 다시 해야 함) ==================== */

/* 장바구니 테이블 */
create table dbCart (
  idx   int not null auto_increment,			/* 장바구니 고유번호 */
  cartDate datetime default now(),				/* 장바구니에 상품을 담은 날짜 */
  mid   varchar(20) not null,							/* 장바구니를 사용한 사용자의 아이디 - 로그인한 회원 아이디이다. */
  productIdx  int not null,								/* 장바구니에 구입한 상품의 고유번호 */
  productName varchar(50) not null,				/* 장바구니에 담은 구입한 상품명 */
  mainPrice   int not null,								/* 메인상품의 기본 가격 */
  thumbImg		varchar(100) not null,			/* 서버에 저장된 상품의 메인 이미지 */
  optionIdx	  varchar(50)	 not null,			/* 옵션의 고유번호리스트(여러개가 될수 있기에 문자열 배열로 처리한다.) */
  optionName  varchar(100) not null,			/* 옵션명 리스트(배열처리) */
  optionPrice varchar(100) not null,			/* 옵션가격 리스트(배열처리) */
  optionNum		varchar(50)  not null,			/* 옵션수량 리스트(배열처리) */
  totalPrice  int not null,								/* 구매한 모든 항목(상품과 옵션포함)에 따른 총 가격 */
  primary key(idx, mid),
  foreign key(productIdx) references dbProduct(idx) on update cascade on delete restrict
  /* foreign key(mid) references member(mid) on update cascade on delete cascade */		/* 문자 외래키(mid)는 버전에 따라 오류발생 */
);
drop table dbCart;
desc dbCart;
delete from dbCart;
select * from dbCart;

/* 주문 테이블 -  */
create table dbOrder (
  idx         int not null auto_increment, /* 고유번호 */
  orderIdx    varchar(15) not null,   /* 주문 고유번호(새롭게 만들어 주어야 한다.) */
  mid         varchar(20) not null,   /* 주문자 ID */
  productIdx  int not null,           /* 상품 고유번호 */
  orderDate   datetime default now(), /* 실제 주문을 한 날짜 */
  productName varchar(50) not null,   /* 상품명 */
  mainPrice   int not null,				    /* 메인 상품 가격 */
  thumbImg    varchar(100) not null,   /* 썸네일(서버에 저장된 메인상품 이미지) */
  optionName  varchar(100) not null,  /* 옵션명    리스트 -배열로 넘어온다- */
  optionPrice varchar(100) not null,  /* 옵션가격  리스트 -배열로 넘어온다- */
  optionNum   varchar(50)  not null,  /* 옵션수량  리스트 -배열로 넘어온다- */
  totalPrice  int not null,					  /* 구매한 상품 항목(상품과 옵션포함)에 따른 총 가격 */
  /* cartIdx     int not null,	*/		/* 카트(장바구니)의 고유번호 */ 
  primary key(idx, orderIdx),
  /* foreign key(mid) references member(mid), */		/* 문자인경우 외래키 고려~~~ */
  foreign key(productIdx) references dbProduct(idx)  on update cascade on delete cascade
);
drop table dbOrder;
desc dbOrder;
delete from dbOrder;
select * from dbOrder;

/* 배송테이블 */
create table dbBaesong (
  idx     int not null auto_increment,
  oIdx    int not null,								/* 주문테이블의 고유번호를 외래키로 지정함 */
  orderIdx    varchar(15) not null,   /* 주문 고유번호 */
  orderTotalPrice int     not null,   /* 주문한 모든 상품의 총 가격 */
  mid         varchar(20) not null,   /* 회원 아이디 */
  name				varchar(20) not null,   /* 배송지 받는사람 이름 */
  address     varchar(100) not null,  /* 배송지 (우편번호)주소 */
  tel					varchar(15),						/* 받는사람 전화번호 */
  message     varchar(100),						/* 배송시 요청사항 */
  payment			varchar(10)  not null,	/* 결재도구 */
  payMethod   varchar(50)  not null,  /* 결재도구에 따른 방법(카드번호) */
  orderStatus varchar(10)  not null default '결제완료', /* 주문순서(결제완료->배송중->배송완료->구매완료) */
  primary key(idx),
  foreign key(oIdx) references dbOrder(idx) on update cascade on delete cascade
);

-- payment, payMethod는 따로 사용할 것이므로 필요없지만 필요한 사람들을 위해 일단 넣어 둠

/* SHOW CREATE TABLE dbOrder; */
desc dbBaesong;
drop table dbBaesong;
delete from dbBaesong;
select * from dbBaesong;




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