#!/bin/bash
shopt -s extglob

psql postgres <<EOF
	select pg_terminate_backend(pid) from pg_stat_activity where datname='kyhtanil';
	drop database kyhtanil;
	create database kyhtanil;
EOF
psql kyhtanil <<EOF
	alter default privileges grant select,insert,update on tables to kyhtanil;
	alter default privileges grant usage,select,update on sequences to kyhtanil;
EOF
cat server/sql/!(content.sql) server/sql/content.sql | psql kyhtanil || exit 1
