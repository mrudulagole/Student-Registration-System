Team members:

Mrudula Gole
Renu Aole


PL/SQL Code Description:

The project is included in stud_reg_sys  package. This package contains following files : 
1.	sequence.sql : In this file we declared one sequence as  log_id_seq  for  logs table.
2.	Triggers.sql : This file creates all the triggers that are mentioned in question 6 and solves some part of question 7 after adding a tuple to purchases table.
3.	Stud_reg_sys_package_specification.sql : This file contains the initialization of all function headers and procedure headers that are useful for the project.
4.	Stud_reg_sys_package_body.sql : This file contains the all the definitions of Project2_headers.
5.	proj2data2018.sql : It contains a script to initialize tables and insert data into tables.


Procedures :

1.	show_students()– This procedure is used to display all tuples that are currently present in the students Table.
2.	show_courses () – This procedure is used to display all tuples that are currently present in the courses Table.
3.	show_prerequisites () -  This procedure is used to display all tuples that are currently present in the prerequisites Table.
4.	show_classes () – This procedure is used to display all tuples that are currently present in the classes Table.
5.	show_enrollments() -  This procedure is used to display all tuples that are currently present in the enrollments Table.
6.	add_student() -  This procedure is used to add students into the students Table.
7.	display_stud_data() -  This procedure display data for a given student  with sid provided as a
parameter, and  lists the sid, lastname, and status of the student as well as all classes the student has taken or is taking. For each class, show classid, dept_code, course_no, title, year, and semester. The dept_code and course_no are displayed together, e.g., CS532.  If the student is not in the students table, “The sid is invalid.” Is reported to the user. If the student 
8.	prerequisites_data() -  This procedure takes dept_code and course_no as parameters and returns all its prerequisite courses  including both direct and indirect prerequisite courses. 
9.	get_class_student() -  This procedure  takes classid  as a parameter and lists the classid, course title, semester and year of the class as well as all the students  who have taken or are taking the class. If the class is not in the classes table, “The cid is invalid.” Is reported.  If no student has taken or is taking the class, “No student is enrolled in the class.” Is reported.
10.	 show_logs () – This procedure is used to display all tuples that are currently present in the logs Table.
11.	add_student() -This procedure is used to insert new  student entry in students table.
12.	display_stud_data()-This procedure is used to display the student data such as sid,lastname,status, classid, dept_code, course_no, title, year, and semester.
13.	prerequisites_data() - This procedure is used to display the courses and its prerequisites.
14.	get_class_student() - This procedure lists the classid, course title, semester and year of the class also the sid and lastname of students who have taken the class. providing classid as a parameter.
15.	enroll_student() –This procedure used to enroll a student to the classes.It will also check all the perquisites condition,minimum required grades obtained by the student and accordingle enrolled a student in enrollments table.
16.	drop_student() - This procedure used to drop a student from the classes.
17.	\delete_student() - This procedure is used  to delete a student from the students table based on sid as a parameter.If entry is not present in table it will display entry is invalid.

Sequences:

1.	Log_id_seq is a sequence for log table. We start the sequence with 100 and increment by 1 each time. Also, the numbers are generated in order.


Triggers:

1.	insert_student: This will trigger when an entry of a student added to students table and add tuple to logs table every time we added a new entry.
2.	delete_student: This will trigger when an entry is deleted from students table and add a tuple into a logs table every time when we delete the entry.
3.	insert_enrollment: This will trigger when an entry of a student added to enrollments table and add tuple to logs table every time we added a new entry.
4.	delete_enrollment:  This will trigger when an entry is deleted from students table and add a tuple into a logs table every time when we delete the entry
5.	increment_class: This will trigger when a student enroll for the course and it will increase the class size in classes table every time when the entry is added in enrollment table.also add a tuple into a logs table every time.
6.	decrement_class : This will trigger when a student drop from the course and decrease the class size in classes table every time when the entry is deleted from enrollments table.Also add a tuple into a logs table every time.
