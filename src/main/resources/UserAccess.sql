sudo -u postgres psql -d postgres_data_source

CREATE TABLE gr_conf_access_user (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    enabled BOOLEAN NOT NULL,
    account_non_expired BOOLEAN NOT NULL,
    credentials_non_expired BOOLEAN NOT NULL,
    account_non_locked BOOLEAN NOT NULL
);

--CREATE TABLE gr_conf_user_authorities (
--    user_id BIGINT NOT NULL,
--    authority VARCHAR(50) NOT NULL,
--    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES gr_conf_access_user(id)
--);
--INSERT INTO gr_conf_user_authorities (user_id, authority) VALUES
--((SELECT id FROM gr_conf_user_access WHERE username = 'admin'), 'ROLE_ADMIN'),
--((SELECT id FROM gr_conf_user_access WHERE username = 'user'), 'ROLE_USER');


CREATE TABLE gr_conf_access_roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE gr_conf_access_user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES gr_conf_access_user(id),
    CONSTRAINT fk_role_id FOREIGN KEY (role_id) REFERENCES gr_conf_access_roles(id)
);

\dt
\dp
GRANT ALL ON TABLE gr_conf_access_user TO postgres_data_source;
--GRANT ALL ON TABLE gr_conf_user_authorities TO postgres_data_source;
GRANT ALL ON TABLE gr_conf_access_roles TO postgres_data_source;
GRANT ALL ON TABLE gr_conf_access_user_roles TO postgres_data_source;
\dp

INSERT INTO gr_conf_access_user (username, password, enabled, account_non_expired, credentials_non_expired, account_non_locked) VALUES
('admin', '$2a$10$vJaSzy8EhH/HAB1UDaE42uFKu54JQlXqagqqz8pyGi7ZrAEmf.Opy', true, true, true, true),
('user' , '$2a$10$S1eEZm5TRtev/WwUKQtm0uP5d/.g6ld14nTDuW3PAA.jLHiuutZWK', true, true, true, true);

	
INSERT INTO gr_conf_access_roles (name) VALUES
('ROLE_ADMIN'),
('ROLE_USER');

INSERT INTO gr_conf_access_user_roles (user_id, role_id) VALUES
((SELECT id FROM gr_conf_access_user WHERE username = 'admin'), (SELECT id FROM gr_conf_access_roles WHERE name = 'ROLE_ADMIN')),
((SELECT id FROM gr_conf_access_user WHERE username = 'user'), (SELECT id FROM gr_conf_access_roles WHERE name = 'ROLE_USER'));

---------------------------------------------
select * from gr_conf_access_user;
 id | username |                           password                           | enabled | account_non_expired | credentials_non_expired | account_non_locked
----+----------+--------------------------------------------------------------+---------+---------------------+-------------------------+--------------------
  1 | admin    | $2a$10$vJaSzy8EhH/HAB1UDaE42uFKu54JQlXqagqqz8pyGi7ZrAEmf.Opy | t       | t                   | t                       | t
  2 | user     | $2a$10$S1eEZm5TRtev/WwUKQtm0uP5d/.g6ld14nTDuW3PAA.jLHiuutZWK | t       | t                   | t                       | t
(2 lignes)
---------------------------------------------
select * from gr_conf_access_roles;
 id |    name
----+------------
  1 | ROLE_ADMIN
  2 | ROLE_USER
(2 lignes)
---------------------------------------------
select * from gr_conf_access_user_roles ;
 user_id | role_id
---------+---------
       1 |       1
       2 |       2
(2 lignes)
---------------------------------------------