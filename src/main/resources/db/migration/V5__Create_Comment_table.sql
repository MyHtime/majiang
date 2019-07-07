create table comment
(
	id integer auto_increment,
	parent_id integer not null,
	type int not null,
	commentator integer not null,
	gmt_create bigint not null,
	gmt_modify bigint not null,
	like_count bigint default 0,
	content varchar(255),
	constraint comment_pk
		primary key (id)
);

comment on table comment is '评论';

comment on column comment.parent_id is '父类ID（一级回复对应questionid，二级回复对应commentid）';

comment on column comment.type is '父类类型（一级回复，二级回复）';

comment on column comment.commentator is '评论人';

comment on column comment.gmt_create is '创建时间';

comment on column comment.gmt_modify is '更新时间';

comment on column comment.like_count is '点赞数';

comment on column comment.content is '评论内容';

