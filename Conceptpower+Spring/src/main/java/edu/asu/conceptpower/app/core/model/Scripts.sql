create table concepttype(type_id varchar(255) primary key,supertype_id varchar(255), type_name varchar(255), description varchar(255) not null, matches varchar(255), creator_id varchar(255) not null, modified varchar(255));