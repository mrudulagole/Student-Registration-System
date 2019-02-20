create or replace PACKAGE body stud_reg_sys AS

PROCEDURE show_students(rc OUT SYS_REFCURSOR)
IS
BEGIN
OPEN rc FOR SELECT sid,firstname,lastname,status,gpa,email FROM students;	
END;

PROCEDURE show_courses(rc OUT SYS_REFCURSOR)
IS
BEGIN
  OPEN rc FOR SELECT dept_code,course_no,title FROM courses;
END;


PROCEDURE show_prerequisites(rc OUT SYS_REFCURSOR)
IS
BEGIN
  OPEN rc FOR SELECT dept_code,course_no,pre_dept_code,pre_course_no from prerequisites;
END;

PROCEDURE show_classes(rc OUT SYS_REFCURSOR)
IS
BEGIN
  OPEN rc FOR SELECT classid,dept_code,course_no,sect_no,year,semester,limit,class_size from classes;
END;

PROCEDURE show_enrollments(rc OUT SYS_REFCURSOR)
IS
BEGIN
  OPEN rc FOR SELECT sid, classid, lgrade from enrollments;
END;

PROCEDURE show_logs(rc OUT SYS_REFCURSOR)
IS
BEGIN
  OPEN rc FOR SELECT logid,who,time,table_name,operation,key_value from logs;
END;


PROCEDURE add_student
                   (s_sid students.sid%type,
				    s_firstname students.firstname%type,
					s_lastname students.lastname%type, 
					s_status students.status%type,
					s_gpa students.gpa%type,
					s_email students.email%type)
is
begin
       insert into students
       values (s_sid,s_firstname,s_lastname,s_status,s_gpa,s_email);
end;


---4
PROCEDURE display_stud_data(p_sid students.sid%type,
							flag OUT NUMBER,
							rc OUT SYS_REFCURSOR)
as
    rowcount INT;
begin
	flag :=1;
  SELECT COUNT(*) INTO rowcount FROM students WHERE sid = p_sid;
  IF (rowcount = 0) THEN								
    flag      :=0;
    RETURN;
  ELSE
    OPEN rc FOR select s.sid,s.lastname,s.status,cs.classid,concat(cs.dept_code,cs.course_no) as course_id ,
		c.title,cs.year,cs.semester
		from students s
		inner join enrollments e on s.sid = e.sid
		inner join classes cs on e.classid = cs.classid
		inner join courses c on cs.dept_code=c.dept_code and 
		cs.course_no=c.course_no where e.sid = p_sid;
  END IF;
END;


---5
PROCEDURE prerequisites_data(
    c_dept_code IN Courses.dept_code%type,
    c_course_no IN Courses.course_no%type,
    rc OUT SYS_REFCURSOR)
AS
  CURSOR cur1
  IS
    SELECT pre_dept_code,pre_course_no
    FROM prerequisites
    WHERE dept_code = c_dept_code
    AND course_no   = c_course_no;
prereq_row cur1%ROWTYPE;
BEGIN
  FOR prereq_row IN cur1
  LOOP
    INSERT
    INTO temp_table
     select pre_dept_code,pre_course_no 
	 from prerequisites 
	 where dept_code=c_dept_code and c_course_no=course_no;
    prerequisites_data(prereq_row.pre_dept_code,prereq_row.pre_course_no,rc);
  END LOOP;
  OPEN rc FOR SELECT distinct concat(dept_code ,
  course_no) prereq_course FROM temp_table;
END;

--6

PROCEDURE get_class_student(
    p_classid IN classes.classid%type,
    flag OUT NUMBER,
    rc0 OUT SYS_REFCURSOR,
    rc1 OUT SYS_REFCURSOR)
IS
  count_row INT;
BEGIN
  flag := 1;
  SELECT COUNT(*) INTO count_row FROM classes WHERE classid = p_classid;
  IF (count_row = 0) THEN
    flag := 0;
    RETURN;
  ELSE
    OPEN rc0 FOR SELECT 
	cs.classid,
    c.title,
    cs.semester,
    cs.year FROM classes cs
	inner join courses c on cs.dept_code = c.dept_code AND cs.course_no = c.course_no AND cs.classid = p_classid;
    OPEN rc1 FOR SELECT e.sid,
    s.lastname FROM students s,
	enrollments e where s.sid=e.sid AND e.classid = p_classid;
  END IF;
END;


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
    status7 OUT NUMBER)
AS
  CURSOR cursor1
  IS
    SELECT SID ,
      FIRSTNAME ,
      LASTNAME ,
      STATUS ,
      GPA ,
      EMAIL
    FROM students
    WHERE sid = p_sid;
  cursor_type cursor1%ROWTYPE;
  CURSOR cursor2
  IS
    SELECT CLASSID ,
      DEPT_CODE ,
      COURSE_NO ,
      SECT_NO ,
      YEAR ,
      SEMESTER ,
      LIMIT ,
      CLASS_SIZE
    FROM classes
    WHERE classid = p_classid;
  cursor_type1 cursor2%ROWTYPE;
  CURSOR cursor3
  IS
    SELECT classid FROM enrollments WHERE sid = p_sid;
  cursor_type2 cursor3%rowtype;
  CURSOR cursor4
  IS
    SELECT pre_course_no,
      pre_dept_code
    FROM prerequisites p,
      classes c
    WHERE c.classid = p_classid
    AND p.dept_code = c.dept_code
    AND p.course_no = c.course_no;
  cursor_type3 cursor4%rowtype;
  CURSOR cursor5
  IS
    SELECT dept_code,
      course_no,
      lgrade
    FROM classes c,
      enrollments e
    WHERE c.classid = e.classid
    AND e.sid       = p_sid;
  cursor_type4 cursor5%rowtype;
  class_limit classes.limit%type;
  class_size classes.class_size%type;
  class_cnt       NUMBER;
  class_reg  NUMBER;
  class_reg2 NUMBER;
  flag            NUMBER;
  flag2           NUMBER;
BEGIN
  status1         :=1;
  status2         :=1;
  status3         :=1;
  status4         :=1;
  status5         :=1;
  status6         :=1;
  status7         :=1;
  class_cnt       := 0;
  class_reg  :=0;
  flag            := 0;
  flag2           := 2;
  class_reg2 :=0;
  OPEN cursor1;
  FETCH cursor1 INTO cursor_type;
  IF (cursor1%notfound) THEN
    status1 :=0;
    RETURN;
  END IF;
  CLOSE cursor1;
  OPEN cursor2;
  FETCH cursor2 INTO cursor_type1;
  IF (cursor2%notfound) THEN
    status2 :=0;
    RETURN;
  END IF;
  CLOSE cursor2;
  SELECT limit,
    class_size
  INTO class_limit,
    class_size
  FROM classes
  WHERE classid = p_classid;
  IF(class_size     = class_limit) THEN
    status3    :=0;
    RETURN;
  END IF;
  FOR cursor_type2 IN cursor3
  LOOP
    IF(cursor_type2.classid = p_classid) THEN
      class_cnt        := 1;
    END IF;
    IF(class_cnt = 1) THEN
      status4   :=0;
      RETURN;
    END IF;
    class_reg := cursor3%rowcount;
  END LOOP;
  IF(class_reg = 2) THEN
    status5        :=0;
  END IF;
  IF(class_reg = 3) THEN
    status6        :=0;
    RETURN;
  END IF;
  flag2 := 0;
  FOR cursor_type3 IN cursor4
  LOOP
    FOR cursor_type4 IN cursor5
    LOOP
      IF(cursor_type3.pre_dept_code = cursor_type4.dept_code AND cursor_type3.pre_course_no = cursor_type4.course_no) THEN
        IF (cursor_type4.lgrade    != 'C' OR cursor_type4.lgrade != ' ') THEN
          flag                 := 1;
        END IF;
      END IF;
    END LOOP;
    IF(flag  = 1) THEN
      flag2 := flag2 + 1;
    END IF;
    flag            := 0;
    class_reg2 := cursor4%ROWCOUNT;
  END LOOP;
  IF(class_reg2 != flag2) THEN
    status7          :=0;
    flag2            := 0;
    RETURN;
  END IF;
  INSERT INTO enrollments VALUES
    (p_sid, p_classid, ' '
    );
END;


--8
PROCEDURE drop_student(
    p_sid     IN enrollments.sid%type,
    p_classid IN enrollments.classid%type,
    status1 OUT NUMBER,
    status2 OUT NUMBER,
    status3 OUT NUMBER,
    status4 OUT NUMBER,
    status5 OUT NUMBER,
    status6 OUT NUMBER)
AS
  CURSOR cursor1
  IS
    SELECT * FROM students WHERE sid = p_sid;
  cursor_type cursor1%ROWTYPE;
  CURSOR cursor2
  IS
    SELECT * FROM classes WHERE classid = p_classid;
  cursor_type1 cursor2%ROWTYPE;
  CURSOR cursor3
  IS
    SELECT classid FROM enrollments WHERE sid = p_sid;
  cursor_type2 cursor3%rowtype;
  CURSOR cursor4
  IS
    SELECT p.dept_code,
      p.course_no
    FROM prerequisites p,
      classes c
    WHERE pre_course_no = c.course_no
    AND pre_dept_code   = c.dept_code
    AND c.classid       = p_classid;
  cursor_type3 cursor4%rowtype;
  CURSOR cursor5
  IS
    SELECT dept_code,
      course_no
    FROM classes c,
      enrollments e
    WHERE c.classid = e.classid
    AND e.sid       = p_sid;
  cursor_type4 cursor5%rowtype;
  CURSOR cursor6
  IS
    SELECT sid FROM enrollments WHERE classid = p_classid;
  cursor_type5 cursor6%rowtype;
  class_cnt      NUMBER;
  std_cnt        NUMBER;
  flag           NUMBER;
  register_class NUMBER := 0;
  register_std   NUMBER := 0;
BEGIN
  class_cnt := 0;
  flag      := 0;
  status1   := 1;
  status2   := 1;
  status3   := 1;
  status4   := 1;
  status5   := 1;
  status6   := 1;
  OPEN cursor1;
  FETCH cursor1 INTO cursor_type;
  IF (cursor1%notfound) THEN
    status1 :=0; 
    RETURN;
  END IF;
  CLOSE cursor1;
  OPEN cursor2;
  FETCH cursor2 INTO cursor_type1;
  IF (cursor2%notfound) THEN
    status2 :=0; 
    RETURN;
  END IF;
  CLOSE cursor2;
  FOR cursor_type2 IN cursor3
  LOOP
    IF(cursor_type2.classid = p_classid) THEN
      class_cnt        := 1;
    END IF;
    register_class := cursor3%rowcount;
  END LOOP;
  IF(class_cnt = 0) THEN
    status3   :=0; 
    RETURN;
  END IF;
  FOR cursor_type5 IN cursor6
  LOOP
    IF(cursor_type5.sid = p_sid) THEN
      std_cnt      := 1;
    END IF;
    register_std := cursor6%rowcount;
  END LOOP;
  FOR cursor_type3 IN cursor4
  LOOP
    FOR cursor_type4 IN cursor5
    LOOP
      IF(cursor_type3.dept_code = cursor_type4.dept_code AND cursor_type3.course_no = cursor_type4.course_no) THEN
        flag               := 1;
      END IF;
    END LOOP;
  END LOOP;
  IF(flag    = 1) THEN
    status4 :=0; 
    RETURN;
  END IF;
  DELETE FROM enrollments WHERE sid = p_sid AND classid = p_classid;
  IF(class_cnt= 1 AND register_class =1) THEN
    status5  :=0; 
  END IF;
  IF(std_cnt = 1 AND register_std =1) THEN
    status6 :=0; 
  END IF;
  class_cnt := 0;
  std_cnt   := 0;
END;

--9
PROCEDURE delete_student(
    p_sid IN students.sid%type,
    status OUT NUMBER)
AS
  CURSOR cursorC1
  IS
    SELECT * FROM students WHERE sid = p_sid;
  studentrow cursorC1%ROWTYPE;
BEGIN
  status :=1;
  OPEN cursorC1;
  FETCH cursorC1 INTO studentrow;
  IF (cursorC1%notfound) THEN
    status :=0; 
    RETURN;
  END IF;
  CLOSE cursorC1;
  DELETE FROM students WHERE sid = p_sid;
END;

END stud_reg_sys;
/
show errors;

