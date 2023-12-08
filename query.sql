-- tao csdl
create database ts00500_dinh_pham_le_hoang_asm
go
-- chon csdl
use ts00500_dinh_pham_le_hoang_asm
go
insert into users(username , pass , rol) values
    ('zimlewis' , 'memaybeo123' , N'Giảng viên'),
    ('memay' , '456' , N'Cán bộ đào tạo'),
    ('conmemaybeo' , '9876' , N'Giảng viên');
go
insert into students(student_id , student_name , email , phone_number , gender , address , image) values
    ('TS00500' , N'Đinh Phạm Lê Hoàng' , 'hoangdplts00500' , '0355361405' , 0 , N'69/420 Đường Thầy Bạch, Quận Sáu Thiện Nhân, Thành phố Bất Chiến' , 'hoang.png'),
    ('TS00318' , N'Phạm Thị Thúy Quỳnh' , 'quynhttpts00318' , '0826097488' , 1 , N'123 Hai Ba Trung','quynh.jpg'),
    ('TS00316' , N'Võ Thị Ngọc Ánh' , 'anhvtnts00316' , '0969696969' , 1 , N'345 Bà Triểu' , 'anh.jpg'),
    ('TS00400' , N'Trần Lân Giác' , 'giactlts00400' , '0942042042' , 0 , N'345 Mạy Mè' , 'giac.jpg')
go
insert into students(student_id , student_name , email , phone_number , gender , address , image) values ('TS01500' , N'Le Hoang Beo' , 'memaybeo' , '0355361405' , 0 , N'69/420 Đường Thầy Bạch, Quận Sáu Thiện Nhân, Thành phố Bất Chiến' , 'hoang.png'),
    ('TS01500' , N'Le Hoang Beo' , 'memaybeo' , '0355361405' , 0 , N'69/420 Đường Thầy Bạch, Quận Sáu Thiện Nhân, Thành phố Bất Chiến' , 'hoang.png'),
    ('TS01501' , N'Le Hoang Gay' , 'memaybeo' , '123' , 0 , N'69/420 Đường Thầy Bạch, Quận Sáu Thiện Nhân, Thành phố Bất Chiến' , 'hoang.png')

go
insert into grade(student_id , english , it , ce) values
    ('TS00500' , 8 , 10 , 5),
    ('TS00318' , 7 , 8 , 9),
    ('TS00316' , 8 , 8 , 8),
    ('TS00400' , 10 , 10 , 10)

go
insert into grade(student_id , english , it , ce) values ('TS00500' , 8 , 10 , 5);
update students set student_name = N'Me May Rat Beo' where student_id = 'TS01500'
update students set student_name = N'Ba May Gay' where student_id = 'TS01501'

update students set gender = 1 where gender = 0;
update students set gender = 0 where student_id = 'TS00316' or student_id = 'TS00318';

update grade set ce = 10 where grade.student_id = 'TS00500';
update grade set english = 10, ce = 10, it = 10 where student_id = 'TS00402';

delete from students where student_id = 'TS01500' or student_id = 'TS01501'

go

select * from users;
select * from students;
select * from grade;


go

select b.student_id, a.student_name, b.it, b.ce, b.english , (b.it + b.ce + b.english) / 3 as average from students a join grade b on a.student_id = b.student_id order by average desc

go
-- test, select students who have average grade above 8.0
select 
a.student_name as name, 
(b.ce + b.english + b.it) / 3 as avg 
from students a 
join grade b 
on a.student_id = b.student_id 
where (b.ce + b.english + b.it) / 3 > 8
select * from grade;
go
select * from users where username = 'zimlewis' and password = 'memaybeo123' 
go
-- Debug stuff---------
delete from grade;
drop table grade;
drop table students;
drop table users;
-----------------------

create login zimlewis with password = 'Daylazim123';