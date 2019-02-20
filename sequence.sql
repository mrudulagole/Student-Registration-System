drop table temp_table;
create table temp_table(dept_code varchar2(10),course_no number(3,0));
drop sequence LOG_ID_SEQUENCE;

CREATE SEQUENCE LOG_ID_SEQUENCE
	increment by 1
	START WITH 100
	MAXVALUE 999
	MINVALUE 1
	NOCYCLE
	NOCACHE;