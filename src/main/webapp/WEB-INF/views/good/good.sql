create table good (
	idx int not null auto_increment primary key,	/* 좋아요 고유번호 */
	part varchar(20) not null,										/* 게시판(table) 카테고리 */	
	partIdx int not null,													/* 게시판(table)의 고유번호 */
	mid varchar(20) not null,											/* 게시글 조회하는 사용자의 아이디 */
	goodSw char(1) default 'Y'										/* 좋아요(Y), 좋아요취소(N) 체크 */
);

desc good;

drop table good;

select * from good;