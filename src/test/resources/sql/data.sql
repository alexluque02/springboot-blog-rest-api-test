delete from comments;
delete from posts;
delete from categories;
delete from users_roles;
delete from roles;
delete from users;

insert into categories (id, name, description) values (1, 'Teal', 'Vestibulum quam sapien, varius ut, blandit non, interdum in, ante. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Duis faucibus accumsan odio.');
insert into categories (id, name, description) values (2, 'Fuscia', 'Quisque arcu libero, rutrum ac, lobortis vel, dapibus at, diam. Nam tristique tortor eu pede.');

insert into posts (id, title, description, content, category_id) values (1, 'Monster Walks, The', 'Nullam molestie nibh in lectus.', 'Donec semper sapien a libero.', 1);
insert into posts (id, title, description, content, category_id) values (2, 'Leave It to Beaver', 'In blandit ultrices enim.', 'Pellentesque ultrices mattis odio.', 2);

insert into comments (id, body, email, name, post_id) values (1, 'Ut tellus.', 'manolo.manolo@gmail.com', 'Lén', 1);
insert into comments (id, body, email, name, post_id) values (2, 'Proin at turpis a pede posuere nonummy.', 'manolo.manolo@gmail.com', 'Anaël', 2);
insert into comments (id, body, email, name, post_id) values (3, 'Morbi vel lectus in quam fringilla rhoncus.', 'skibidytoilet69@icq.com', 'Dà', 2);
insert into comments (id, body, email, name, post_id) values (4, 'Donec ut mauris eget massa tempor convallis.', 'skibidytoilet69@icq.com', 'Bérangère', 1);


insert into roles (id,name) values (1,'USER');
insert into roles (id,name) values (2,'ADMIN');

insert into users (id, email, name, password, username) values (1, 'manolo.manolo@gmail.com', 'Manolo', 'uN1~{O)+''}', 'manolo32');
insert into users (id, email, name, password, username) values (2, 'skibidytoilet69@icq.com', 'Skibidy', 'aH5_V1Oar1', 'skybidyToilet34');

insert into users_roles (role_id, user_id) values (1,1);
insert into users_roles (role_id, user_id) values (2,2);