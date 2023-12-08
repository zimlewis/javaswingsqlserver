create database ts00500_dinh_pham_le_hoang_asm
go
-- chon csdl
use ts00500_dinh_pham_le_hoang_asm;
go
create table users (
    username varchar(50) not null,
    pass varchar(50) not null,
    rol nvarchar(50) not null,
    primary key (username)
)
go
create table students (
    student_id varchar(50) not null,
    student_name nvarchar(50) not null,
    email nvarchar(50) not null,
    phone_number varchar(10) not null,
    gender bit not null,
    adr nvarchar(100) not null,
    img varbinary(max) default null,
    primary key (student_id)
)
go
create table grade (
    id int identity(1,1),
    student_id varchar(50) not null,
    english int not null,
    it int not null,
    ce int not null,
    primary key (id),
    foreign key (student_id) references students (student_id),
    constraint chk_grade check ((english >= 0 and english <= 10) and (it >= 0 and it <= 10) and (ce >= 0 and ce <= 10))
)
go
ALTER SERVER ROLE sysadmin ADD MEMBER zimlewis;