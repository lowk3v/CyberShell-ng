CREATE TABLE Users (
	id integer PRIMARY KEY AUTOINCREMENT,
	username varchar,
	password varchar,
	email varchar
);

CREATE TABLE Projects (
	id integer PRIMARY KEY AUTOINCREMENT,
	name varchar,
	description text
);

CREATE TABLE "Targets " (
	id integer PRIMARY KEY AUTOINCREMENT,
	name varchar,
	link text,
	project_id integer,
	password varchar,
	db_connection varchar,
	description text
);

CREATE TABLE Binaries (
	id integer PRIMARY KEY AUTOINCREMENT,
	name varchar,
	binary text
);
