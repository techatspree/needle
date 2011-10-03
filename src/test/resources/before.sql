
create table USER_TABLE (
	id int primary key,
	name varchar(256) not null
);


create table ADDRESS_TABLE (
	id int primary key,
	zip varchar(256),
	street varchar(256),
	user_id int not null,
	foreign key (user_id) references USER_TABLE(id)
);



--

/***
 *
 *


ssssssssssss

 *
 *
 *
 */



INSERT INTO USER_TABLE (ID,NAME) VALUES (1,'Test');
INSERT INTO USER_TABLE (ID,NAME) VALUES (2,'Test');
INSERT INTO USER_TABLE (ID,NAME) VALUES (3,'Test');
INSERT INTO USER_TABLE (ID,NAME) VALUES (4,'Test');


INSERT INTO ADDRESS_TABLE (ID,user_id) VALUES (1,1);
INSERT INTO ADDRESS_TABLE (ID,user_id) VALUES (2,1);
INSERT INTO ADDRESS_TABLE (ID,user_id) VALUES (3,2);
INSERT INTO ADDRESS_TABLE (ID,user_id) VALUES (4,3);


