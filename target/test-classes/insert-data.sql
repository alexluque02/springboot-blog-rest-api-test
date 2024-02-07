insert into categories (id, name, description) values (0, 'Teal', 'Vestibulum quam sapien, varius ut, blandit non, interdum in, ante. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Duis faucibus accumsan odio.');
insert into categories (id, name, description) values (1, 'Fuscia', 'Quisque arcu libero, rutrum ac, lobortis vel, dapibus at, diam. Nam tristique tortor eu pede.');
insert into categories (id, name, description) values (2, 'Indigo', 'Nunc nisl. Duis bibendum, felis sed interdum venenatis, turpis enim blandit mi, in porttitor pede justo eu massa.');
insert into categories (id, name, description) values (3, 'Turquoise', 'In est risus, auctor sed, tristique in, tempus sit amet, sem. Fusce consequat.');
insert into categories (id, name, description) values (4, 'Crimson', 'Proin at turpis a pede posuere nonummy. Integer non velit.');
insert into categories (id, name, description) values (5, 'Crimson2', 'Proin at turpis a pede posuere nonummy. Integer non velit.');


insert into posts (id, title, description, content, category_id) values (1, 'Monster Walks, The', 'Nullam molestie nibh in lectus.', 'Donec semper sapien a libero.', 0);
insert into posts (id, title, description, content, category_id) values (2, 'Leave It to Beaver', 'In blandit ultrices enim.', 'Pellentesque ultrices mattis odio.', 0);
insert into posts (id, title, description, content, category_id) values (3, 'Wah-Wah', 'Proin leo odio, porttitor id, consequat in, consequat ut, nulla.', 'Suspendisse ornare consequat lectus.', 0);
insert into posts (id, title, description, content, category_id) values (4, 'Protocol', 'Curabitur at ipsum ac tellus semper interdum.', 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit.', 5);
insert into posts (id, title, description, content, category_id) values (5, 'Irreconcilable Differences', 'Etiam vel augue.', 'Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Donec pharetra, magna vestibulum aliquet ultrices, erat tortor sollicitudin mi, sit amet lobortis sapien sapien non mi.', 1);
insert into posts (id, title, description, content, category_id) values (6, 'Sokkotanssi', 'Morbi quis tortor id nulla ultrices aliquet.', 'Morbi sem mauris, laoreet ut, rhoncus aliquet, pulvinar sed, nisl.', 3);
insert into posts (id, title, description, content, category_id) values (7, 'California Conquest', 'Duis bibendum.', 'In blandit ultrices enim.', 5);
insert into posts (id, title, description, content, category_id) values (8, 'Frankenstein Unbound', 'Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Donec pharetra, magna vestibulum aliquet ultrices, erat tortor sollicitudin mi, sit amet lobortis sapien sapien non mi.', 'Donec vitae nisi.', 5);
insert into posts (id, title, description, content, category_id) values (9, 'Jesus of Montreal (Jésus de Montréal)', 'Praesent blandit lacinia erat.', 'Duis aliquam convallis nunc.', 3);
insert into posts (id, title, description, content, category_id) values (10, 'Young Törless, The (Junge Törless, Der)', 'Integer a nibh.', 'Proin interdum mauris non ligula pellentesque ultrices.', 3);
insert into posts (id, title, description, content, category_id) values (11, 'comment for test', 'Integer a nibh.', 'Proin interdum mauris non ligula pellentesque ultrices.', 3);

insert into comments (id, body, email, name, post_id) values (1, 'Ut tellus.', 'tpetteford0@linkedin.com', 'Lén', 1);
insert into comments (id, body, email, name, post_id) values (2, 'Proin at turpis a pede posuere nonummy.', 'tpetteford0@linkedin.com', 'Anaël', 1);
insert into comments (id, body, email, name, post_id) values (3, 'Morbi vel lectus in quam fringilla rhoncus.', 'tpetteford0@linkedin.com', 'Dà', 1);
insert into comments (id, body, email, name, post_id) values (4, 'Donec ut mauris eget massa tempor convallis.', 'sdebeneditti1@icq.com', 'Bérangère', 2);
insert into comments (id, body, email, name, post_id) values (5, 'Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Donec pharetra, magna vestibulum aliquet ultrices, erat tortor sollicitudin mi, sit amet lobortis sapien sapien non mi.', 'gboobyer4@amazon.co.jp', 'Kuí', 10);
insert into comments (id, body, email, name, post_id) values (6, 'Nulla suscipit ligula in lacus.', 'sdebeneditti1@icq.com', 'Görel', 2);
insert into comments (id, body, email, name, post_id) values (7, 'Phasellus sit amet erat.', 'sdebeneditti1@icq.com', 'Judicaël', 2);
insert into comments (id, body, email, name, post_id) values (8, 'Nulla mollis molestie lorem.', 'jdelisle2@mysql.com', 'Danièle', 3);
insert into comments (id, body, email, name, post_id) values (9, 'Nulla tempus.', 'jdelisle2@mysql.com', 'Maïté', 3);
insert into comments (id, body, email, name, post_id) values (10, 'Nulla neque libero, convallis eget, eleifend luctus, ultricies eu, nibh.', 'jdelisle2@mysql.com', 'Tán', 3);
insert into comments (id, body, email, name, post_id) values (11, 'Vivamus metus arcu, adipiscing molestie, hendrerit at, vulputate vitae, nisl.', 'jdelisle2@mysql.com', 'Måns', 4);
insert into comments (id, body, email, name, post_id) values (12, 'Nulla tempus.', 'jdelisle2@mysql.com', 'Amélie', 4);
insert into comments (id, body, email, name, post_id) values (13, 'Integer ac neque.', 'jdelisle2@mysql.com', 'Lucrèce', 4);
insert into comments (id, body, email, name, post_id) values (14, 'Morbi quis tortor id nulla ultrices aliquet.', 'aspellecy3@google.nl', 'Chloé', 5);
insert into comments (id, body, email, name, post_id) values (15, 'Cras pellentesque volutpat dui.', 'aspellecy3@google.nl', 'Léonie', 5);
insert into comments (id, body, email, name, post_id) values (16, 'Donec ut mauris eget massa tempor convallis.', 'aspellecy3@google.nl', 'Séréna', 5);
insert into comments (id, body, email, name, post_id) values (17, 'Aliquam sit amet diam in magna bibendum imperdiet.', 'aspellecy3@google.nl', 'Véronique', 6);
insert into comments (id, body, email, name, post_id) values (18, 'Aliquam sit amet diam in magna bibendum imperdiet.', 'aspellecy3@google.nl', 'André', 6);
insert into comments (id, body, email, name, post_id) values (19, 'Quisque erat eros, viverra eget, congue eget, semper rutrum, nulla.', 'amatushevich4@nifty.com', 'Naéva', 6);
insert into comments (id, body, email, name, post_id) values (20, 'Phasellus in felis.', 'amatushevich4@nifty.com', 'Sòng', 7);
insert into comments (id, body, email, name, post_id) values (21, 'Donec ut dolor.', 'amatushevich4@nifty.com', 'Angèle', 7);
insert into comments (id, body, email, name, post_id) values (22, 'In est risus, auctor sed, tristique in, tempus sit amet, sem.', 'amatushevich4@nifty.com', 'Mélys', 7);
insert into comments (id, body, email, name, post_id) values (23, 'Vivamus metus arcu, adipiscing molestie, hendrerit at, vulputate vitae, nisl.', 'amatushevich4@nifty.comu', 'Rébecca', 8);
insert into comments (id, body, email, name, post_id) values (24, 'Praesent id massa id nisl venenatis lacinia.', 'amatushevich4@nifty.com', 'Léonie', 8);
insert into comments (id, body, email, name, post_id) values (25, 'Maecenas pulvinar lobortis est.', 'amatushevich4@nifty.com', 'Bérengère', 8);
insert into comments (id, body, email, name, post_id) values (26, 'In blandit ultrices enim.', 'amatushevich4@nifty.com', 'Hélène', 9);
insert into comments (id, body, email, name, post_id) values (27, 'Integer aliquet, massa id lobortis convallis, tortor risus dapibus augue, vel accumsan tellus nisi eu orci.', 'mallsobrook5@pbs.org', 'Clélia', 9);
insert into comments (id, body, email, name, post_id) values (28, 'In hac habitasse platea dictumst.', 'mallsobrook5@pbs.org', 'Andréa', 9);
insert into comments (id, body, email, name, post_id) values (29, 'Suspendisse potenti.', 'mallsobrook5@pbs.org', 'Intéressant', 10);
insert into comments (id, body, email, name, post_id) values (30, 'Mauris enim leo, rhoncus sed, vestibulum sit amet, cursus id, turpis.', 'mallsobrook5@pbs.org', 'Audréanne', 10);

insert into roles (id,name) values (1,'ROLE_USER');
insert into roles (id,name) values (2,'ROLE_ADMIN');

insert into users (id, email, name, password, username) values (1, 'tpetteford0@linkedin.com', 'Tomi', '$2a$12$V2STGXRVuoOEqKtAtKZJ3ePwcVAb/GZ7y4NTKhrlZ1MJy6AWiLyXe', 'tvenneur0'); --uN1~{O)+''}
insert into users (id, email, name, password, username) values (2, 'sdebeneditti1@icq.com', 'Silva', '$2a$12$Jzkwh1vpWmnA905RHLbGJ.TY8TB8sCYn75XDNKStUkJKeZPSlosSS', 'sbrane1'); --aH5_V1Oar1
insert into users (id, email, name, password, username) values (3, 'jdelisle2@mysql.com', 'Janene', '$2a$12$vIP0edhI8.lSeDQQCa5.OO516gl0FdQuAQOB9C8UJc/o0mXMHc/JC', 'jjosuweit2');--iK0>q/VzG$ePJ|
insert into users (id, email, name, password, username) values (4, 'aspellecy3@google.nl', 'Aileen', '$2a$12$n6MCi4VWqdJ9uSRo/2FfMON1oPjvVGD2hkmOdp/WIO8czw2PUwS/W', 'amindenhall3');--bZ0sf6&d
insert into users (id, email, name, password, username) values (5, 'amatushevich4@nifty.com', 'Arne', '$2a$12$WsKcjmxqEjaH2c6lSLaL5.zmFvy6jx63Btm1NYAcXbb5bsBnrgLji', 'ahuet4'); --zE5#8$x7"mk>
insert into users (id, email, name, password, username) values (6, 'mallsobrook5@pbs.org', 'Marchall', '$2a$12$kfYReQVCRSH8CUP8UFX.Qeh.p56KJBqxwamOZf.Evji066665ltPq', 'mantonnikov5'); --vM1+)1e_2=
insert into users (id, email, name, password, username) values (7, 'robertorebolledo151@gmail.com', 'Roberto', '$2a$12$owdta.5AgpfxHOR02hpNZuikw2dwypcoKCZFVjqRcpfjpLerAn4nm', 'krobert151'); --tiburonMolon123
insert into users (id, email, name, password, username) values (8, 'robertorebolledo152@gmail.com', 'Roberto2', '$2a$12$owdta.5AgpfxHOR02hpNZuikw2dwypcoKCZFVjqRcpfjpLerAn4nm', 'krobert152');



insert into users_roles (role_id, user_id) values (1,1);
insert into users_roles (role_id, user_id) values (1,2);
insert into users_roles (role_id, user_id) values (1,3);
insert into users_roles (role_id, user_id) values (1,4);
insert into users_roles (role_id, user_id) values (2,5);
insert into users_roles (role_id, user_id) values (2,6);
insert into users_roles (role_id, user_id) values (2,7);
insert into users_roles (role_id, user_id) values (1,8);
