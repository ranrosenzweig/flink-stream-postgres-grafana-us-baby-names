FROM postgres:latest

ADD entrypoint.sh /usr/local/bin/
ADD create_tables.sql /create_tables.sql

ENTRYPOINT entrypoint.sh
