DROP TABLE IF EXISTS state_names;
CREATE TABLE state_names (
    id integer,
    name text,
	year integer,
	gender text,
	state text,
	count integer
);

DROP TABLE IF EXISTS national_names;
CREATE TABLE national_names (
    id integer,
    name text,
	year integer,
	gender text,
	count integer
);
