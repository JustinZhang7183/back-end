CREATE TABLE users  (
    `username` varchar(50) NOT NULL COMMENT 'username',
    `password` varchar(500) NOT NULL COMMENT 'password',
    `enabled` bit(1) NOT NULL COMMENT 'is enabled',
    PRIMARY KEY (`username`)
);

create table authorities (
	`username` varchar(50) not null,
	`authority` varchar(50) not null,
    UNIQUE INDEX `ix_auth_username`(`username`, `authority`) USING BTREE
);