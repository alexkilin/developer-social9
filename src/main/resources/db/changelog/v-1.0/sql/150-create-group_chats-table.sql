create table group_chats (
   chat_id int8 not null,
    primary key (chat_id)
)

next

alter table group_chats
   add constraint FKn8fkl5522npp79gun310v08gc
   foreign key (chat_id)
   references chats

next