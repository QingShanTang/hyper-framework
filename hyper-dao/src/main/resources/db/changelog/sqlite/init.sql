CREATE TABLE "t_user" (
"id" integer NOT NULL PRIMARY KEY AUTOINCREMENT,
"username" text(255),
"password" text(255),
"income" real(255),
"if_adult" integer(1),
"created_by" text(20),
"created_date" integer(20),
"last_modified_by" text(20),
"last_modified_date" integer(20)
);
