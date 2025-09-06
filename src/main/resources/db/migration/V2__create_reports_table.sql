create table reports (
  id varchar(255) primary key,
  reporter_id varchar(255) not null,
  reported_content_type varchar(50) not null,
  reported_content_id varchar(255) not null,
  reason varchar(50) not null,
  status varchar(50) not null default 'PENDING',
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
