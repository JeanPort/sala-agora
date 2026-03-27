
create table tb_room(
	id bigint not null auto_increment,
    name varchar(200) not null,
    seats int not null,
    active tinyint not null,
    primary key (id)
)Engine=innoDB;