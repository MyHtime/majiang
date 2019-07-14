create table notification
(
	id bigint auto_increment,
	notifier bigint not null,
	receiver bigint not null,
	outer_id bigint not null,
	type int not null,
	gmt_create bigint not null,
	status int default 0 not null,
	constraint notification_pk
		primary key (id)
);

comment on table notification is '通知';

comment on column notification.notifier is '发起通知的人';

comment on column notification.receiver is '接收通知的人';

comment on column notification.outer_id is '类似于通知的内容ID（问题，点赞，回复，评论id）';

comment on column notification.type is '通知类型';

comment on column notification.type is '状态（未读-0； 已读-1）';

