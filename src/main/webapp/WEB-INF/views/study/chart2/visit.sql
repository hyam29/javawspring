create table visit (
  visitDate datetime not null default now(),
  visitCount			int  not null default 1
);

drop table visit;
delete from visit;

select now();
select date(now());

insert into visit values (date(now()),default);
insert into visit values ('2023-02-20',8);
insert into visit values ('2023-02-18',5);
insert into visit values ('2023-02-15',10);
insert into visit values ('2023-02-16',12);
insert into visit values ('2023-02-14',5);
insert into visit values ('2023-02-13',3);
insert into visit values ('2023-02-11',15);

select * from visit;
select substring(visitDate,1,10) as visitDate, visitCount from visit order by visitDate desc limit 7;
