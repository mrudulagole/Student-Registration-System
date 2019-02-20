CREATE or replace PACKAGE stud_reg_sys
AS 
TYPE rc IS REF CURSOR;
--2
PROCEDURE show_students(rc OUT SYS_REFCURSOR);     

PROCEDURE show_courses(rc OUT SYS_REFCURSOR);

PROCEDURE show_prerequisites(rc OUT SYS_REFCURSOR);

PROCEDURE show_classes(rc OUT SYS_REFCURSOR);
	
PROCEDURE show_enrollments(rc OUT SYS_REFCURSOR);
	
PROCEDURE show_logs(rc OUT SYS_REFCURSOR);
--3
PROCEDURE add_student
                   (s_sid students.sid%type,
				    s_firstname students.firstname%type,
					s_lastname students.lastname%type, 
					s_status students.status%type,
					s_gpa students.gpa%type,
					s_email students.email%type);
--4					
PROCEDURE display_stud_data(p_sid students.sid%type,
							flag OUT NUMBER,
							rc OUT SYS_REFCURSOR);
							
--5							
PROCEDURE prerequisites_data(
    c_dept_code IN Courses.dept_code%type,
    c_course_no IN Courses.course_no%type,
    rc OUT SYS_REFCURSOR);
	
--6	
PROCEDURE get_class_student(
    p_classid IN classes.classid%type,
    flag OUT NUMBER,
    rc0 OUT SYS_REFCURSOR,
    rc1 OUT SYS_REFCURSOR);
					

--7

PROCEDURE enroll_student(
    p_sid     IN enrollments.sid%type,
    p_classid IN enrollments.classid%type,
    status1 OUT NUMBER,
    status2 OUT NUMBER,
    status3 OUT NUMBER,
    status4 OUT NUMBER,
    status5 OUT NUMBER,
    status6 OUT NUMBER,
    status7 OUT NUMBER);
		
--8

PROCEDURE drop_student(
    p_sid     IN enrollments.sid%type,
    p_classid IN enrollments.classid%type,
    status1 OUT NUMBER,
    status2 OUT NUMBER,
    status3 OUT NUMBER,
    status4 OUT NUMBER,
    status5 OUT NUMBER,
    status6 OUT NUMBER);

--9
PROCEDURE delete_student(
    p_sid IN students.sid%type,
    status OUT NUMBER);	
					
end stud_reg_sys;
/
show errors;