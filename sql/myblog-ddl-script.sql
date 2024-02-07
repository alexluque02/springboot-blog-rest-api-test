-- DROP TABLE posts IF EXISTS;
--/*!40101 SET @saved_cs_client     = @@character_set_client */;
--/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE categories (
    id bigserial NOT NULL,
    name varchar(255) NOT NULL,
    description varchar(512) NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id)
);



CREATE TABLE posts (
  id bigserial NOT NULL,
  content varchar(255) NOT NULL,
  description varchar(255) NOT NULL,
  title varchar(255) NOT NULL,
  category_id int8 NOT NULL,
  CONSTRAINT pk_posts PRIMARY KEY (id),
  CONSTRAINT uk_posts_title UNIQUE (title),
  CONSTRAINT fk_posts_categories FOREIGN KEY (category_id) REFERENCES categories (id)
);

--DROP TABLE IF EXISTS comments;
-- /*!40101 SET @saved_cs_client     = @@character_set_client */;
-- /*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE comments (
  id bigserial NOT NULL,
  body varchar(255) DEFAULT NULL,
  email varchar(255) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  post_id int8 NOT NULL,
  CONSTRAINT pk_comments PRIMARY KEY (id),
  --KEY FKh4c7lvsc298whoyd4w9ta25cr (post_id),
  CONSTRAINT fk_comments_post FOREIGN KEY (post_id) REFERENCES posts (id)
);

--DROP TABLE IF EXISTS users;
--/*!40101 SET @saved_cs_client     = @@character_set_client */;
--/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE users (
  id bigserial NOT NULL,
  email varchar(255) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  password varchar(255) DEFAULT NULL,
  username varchar(255) DEFAULT NULL,
  CONSTRAINT pk_users PRIMARY KEY (id),
  CONSTRAINT uk_users_username UNIQUE (username),
  CONSTRAINT uk_users_email UNIQUE (email)
);

--DROP TABLE IF EXISTS roles;
--/*!40101 SET @saved_cs_client     = @@character_set_client */;
--/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE roles (
  id smallserial NOT NULL,
  name varchar(60) DEFAULT NULL,
  CONSTRAINT pk_roles PRIMARY KEY (id)
);

--DROP TABLE IF EXISTS user_roles;
--/*!40101 SET @saved_cs_client     = @@character_set_client */;
--/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE users_roles (
  user_id int8 NOT NULL,
  role_id int2 NOT NULL,
  CONSTRAINT pk_user_roles PRIMARY KEY (user_id,role_id),
  --KEY FKh8ciramu9cc9q3qcqiv4ue8a6 (role_id),
  CONSTRAINT fk_user_roles_roles FOREIGN KEY (role_id) REFERENCES roles (id),
  CONSTRAINT fk_user_roles_users FOREIGN KEY (user_id) REFERENCES users (id)
);


INSERT INTO roles VALUES (NEXTVAL('roles_id_seq'),'ROLE_ADMIN'),(NEXTVAL('roles_id_seq'),'ROLE_USER');
