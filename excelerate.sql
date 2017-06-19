create sequence report_manager_seq;

create table report_manager(
report_id bigint DEFAULT nextval('report_manager_seq'::regclass),
query text,
fileName character varying(30),
duration character varying(10),
download_location text,
last_generated_on timestamp without time zone,
is_failing boolean,
last_failure_dtm timestamp without time zone,
active boolean,
primary key (report_id)
);

create sequence report_failure_archive_seq;

create table report_failure_archive(
id bigint DEFAULT nextval('report_failure_archive_seq'::regclass),
failure_remarks text,
failure_dtm timestamp without time zone,
primary key (id)
);

insert into report_manager values (1,'select * from app_enq','3MLeads','d','/home/tanmay/Excelerate',null,false,null,true);
insert into report_manager values (2,'select * from app_lead','DirectLoginLeads','d','/home/tanmay/Excelerate',null,false,null,true);
insert into report_manager values (3,'select * from app_3w_lead','3WLeads','d','/home/tanmay/Excelerate',null,false,null,true);