create table user
(
	id int auto_increment,
	account_id varchar(100),
	name VARCHAR(50),
	token char(36),
	gmt_create bigint,
	gmt_modify bigint,
	constraint user_pk
		primary key (id)
);

comment on column user.account_id is 'ACCOUNT_ID(eg:github...)
';

comment on column user.token is 'token 自定义cookie方式。登录成功，key、value存入数据库';

comment on column user.gmt_create is '创建时间';

comment on column user.gmt_modify is '修改时间';

