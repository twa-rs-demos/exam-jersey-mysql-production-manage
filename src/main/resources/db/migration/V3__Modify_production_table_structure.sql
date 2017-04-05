truncate production;
alter table production add categoryId int not null;
alter table production add foreign key (categoryId) references category (id) on delete no action on update no action;