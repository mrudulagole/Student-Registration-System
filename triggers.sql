--this file is used to create all triggers
--whenever there is an insert into students and enrollment
--the respective changes are inserted into the logs table
--whenever we update enrollment table(update or delete)trigger accordingly increases or decrement class size

Drop trigger insert_student;
Drop trigger delete_student;
Drop trigger insert_enrollment;
Drop trigger delete_enrollment;
Drop trigger increment_class;
Drop trigger decrement_class;


create or replace trigger insert_student
after insert on students
for each row
declare 
sid_id enrollments.sid%type;
begin
	insert into logs values (LOG_ID_SEQUENCE.nextval,user, sysdate, 'Students','insert',sid_id);
	dbms_output.put_line('in log_insert_student triger');
end;
/

create or replace trigger delete_student
after delete on students
for each row
declare 
sid_id enrollments.sid%type;
begin
sid_id:=:old.sid;
	insert into logs values (LOG_ID_SEQUENCE.nextval,user, sysdate, 'Students','delete',sid_id);
	delete from enrollments where sid = sid_id;
	
	dbms_output.Put_line('in delete enroll triger');

end;
/
show errors

create or replace trigger insert_enrollment
after insert on enrollments
for each row
declare 
sid_id enrollments.sid%type;
class_id enrollments.classid%type;
begin
	insert into logs values (LOG_ID_SEQUENCE.nextval,user, sysdate, 'enrollments','insert',sid_id);
	dbms_output.put_line('in delete enroll triger');
	
end;
/


create or replace trigger delete_enrollment
after delete on enrollments
for each row
declare 
sid_id enrollments.sid%type;
class_id enrollments.classid%type;
begin

	insert into logs values (LOG_ID_SEQUENCE.nextval,user, sysdate, 'enrollments','delete',sid_id);
	delete from enrollments where sid = sid_id and classid = class_id;
	dbms_output.Put_line('\nin delete enroll triger');

end;
/
show errors


create or replace trigger increment_class
after insert on enrollments
for each row
declare 
class_id classes.classid%type;
begin
class_id:=:new.classid;
	insert into logs values (LOG_ID_SEQUENCE.nextval,user, sysdate,'Enrollments','insert',class_id);
	update classes set class_size = class_size+1 where classid = class_id;
	

end;
/


create or replace trigger decrement_class
after delete on enrollments
for each row
declare 
class_id classes.classid%type;
begin
	class_id:=:old.classid;
	insert into logs values (log_ID_SEQUENCE.nextval,user, sysdate, 'Enrollments','delete',class_id);
	update classes set class_size = class_size-1 where classid = class_id;
	

end;
/
show error;
