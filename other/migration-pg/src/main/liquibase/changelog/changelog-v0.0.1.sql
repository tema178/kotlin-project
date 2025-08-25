--liquibase formatted sql

--changeset tema:1 labels:v0.0.1

CREATE TABLE "statuses" (
	"id" text primary key constraint id_length_ctr check (length("id") < 64),
	"name" text not null constraint name_length_ctr check (length(name) < 1024),
	"type" text constraint type_length_ctr check (length(type) < 1024),
	"status" text constraint status_length_ctr check (length(status) < 1024),
	"updated_by_id" text not null constraint updated_by_id_length_ctr check (length(id) < 64),
    "updated_at" bigint not null,
	"lock" text not null constraint lock_length_ctr check (length(id) < 64),
    UNIQUE (name, type)
);

CREATE INDEX updated_by_id_idx on "statuses" using hash ("updated_by_id");


