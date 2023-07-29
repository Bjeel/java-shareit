DROP TABLE IF EXISTS "users" CASCADE;
DROP TABLE IF EXISTS "items" CASCADE;
DROP TABLE IF EXISTS "users_items" CASCADE;
DROP TABLE IF EXISTS "bookings" CASCADE;
DROP TABLE IF EXISTS "requests" CASCADE;
DROP TABLE IF EXISTS "comments" CASCADE;

CREATE TABLE IF NOT EXISTS "users"
(
  "id"    integer GENERATED BY DEFAULT AS IDENTITY UNIQUE PRIMARY KEY,
  "name"  varchar(255),
  "email" varchar(255) UNIQUE
);

CREATE TABLE IF NOT EXISTS "items"
(
  "id"          integer GENERATED BY DEFAULT AS IDENTITY UNIQUE PRIMARY KEY,
  "name"        varchar(255),
  "description" varchar(1500),
  "available"   boolean,
  "owner"       integer,
  "request"     integer
);

CREATE TABLE IF NOT EXISTS "users_items"
(
  "user_id" integer,
  "item_id" integer,

  CONSTRAINT film_genres_key PRIMARY KEY ("user_id", "item_id")
);

CREATE TABLE IF NOT EXISTS "bookings"
(
  "id"         integer GENERATED BY DEFAULT AS IDENTITY UNIQUE PRIMARY KEY,
  "start_time" varchar,
  "end_time"   varchar,
  "item_id"    integer,
  "booker_id"  integer,
  "status"     varchar,

  CONSTRAINT bookings_key PRIMARY KEY ("id")
);


CREATE TABLE IF NOT EXISTS "requests"
(
  "id"          integer GENERATED BY DEFAULT AS IDENTITY UNIQUE PRIMARY KEY,
  "description" varchar(55),
  "requester"   integer,

  CONSTRAINT requests_key PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "comments"
(
  "id"     integer GENERATED BY DEFAULT AS IDENTITY UNIQUE PRIMARY KEY,
  "text"   varchar(1500),
  "item"   integer,
  "author" integer,

  CONSTRAINT comments_key PRIMARY KEY ("id")
);

ALTER TABLE "items"
  ADD FOREIGN KEY ("owner") REFERENCES "users" ("id");

ALTER TABLE "items"
  ADD FOREIGN KEY ("request") REFERENCES "requests" ("id");

ALTER TABLE "users_items"
  ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "users_items"
  ADD FOREIGN KEY ("item_id") REFERENCES "items" ("id");

ALTER TABLE "bookings"
  ADD FOREIGN KEY ("item_id") REFERENCES "items" ("id");

ALTER TABLE "bookings"
  ADD FOREIGN KEY ("booker_id") REFERENCES "users" ("id");

ALTER TABLE "requests"
  ADD FOREIGN KEY ("requester") REFERENCES "users" ("id");

ALTER TABLE "comments"
  ADD FOREIGN KEY ("author") REFERENCES "users" ("id");

ALTER TABLE "comments"
  ADD FOREIGN KEY ("item") REFERENCES "items" ("id");
