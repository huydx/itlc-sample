create table advertisers (
  id INTEGER PRIMARY KEY,
  url VARCHAR(256),
  landing_page VARCHAR(256),
  name VARCHAR(256)
);

create table advertiser_block (
  id INTEGER PRIMARY KEY,
  advertiser_id INTEGER,
  url VARCHAR(256)
);