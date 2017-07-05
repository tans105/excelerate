create sequence report_manager_seq;

create table report_manager(
report_id bigint DEFAULT nextval('report_manager_seq'::regclass),
query text,
fileName character varying(30),
type character varying(10),
value character varying(30),
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
report_id bigint,
failure_remarks text,
failure_dtm timestamp without time zone,
primary key (id)
);

insert into report_manager values (3,'select * from app_3w_lead','Report 3','d','4','/home/tanmay/Excelerate',null,false,null,true);
insert into report_manager values (2,'select * from app_3w_lead','Report 2','m','4','/home/tanmay/Excelerate',null,false,null,true);
insert into report_manager values (1,'select * from app_3w_lead','Report 1','w','4','/home/tanmay/Excelerate',null,false,null,true);
insert into report_manager values (4,'select * from app_3w_lead','Report 4','w','3','/home/tanmay/Excelerate',null,false,null,true);
insert into report_manager values (5,'select * from app_3w_lead','Report 5','mw','3,2','/home/tanmay/Excelerate',null,false,null,true);

insert into report_manager values (1,'select * from app_3w_lead','3W_Report','mw','4,5','/var/www/gmcms/ReportUtil',null,false,null,true);
insert into report_manager values (2,'select * from app_lead','DirectLogin_Report','w','4','/var/www/gmcms/ReportUtil',null,false,null,true);
insert into report_manager values (3,'select * from app_enq','3M_Report','d','','/var/www/gmcms/ReportUtil',null,false,null,true);