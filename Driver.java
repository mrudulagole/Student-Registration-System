import java.io.DataInputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import oracle.jdbc.OracleTypes;
import oracle.jdbc.internal.OracleCallableStatement;
import oracle.jdbc.pool.OracleDataSource;

public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	      int choice;
	      int table_choice=0;
	      
	      Scanner input = new Scanner(System.in);
	     // Scanner reader = new Scanner(System.in);
	      DataInputStream reader=new DataInputStream(System.in);
	      try
			{
			      //Connection to Oracle server
			      OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
			     ds.setURL("jdbc:oracle:thin:@castor.cc.binghamton.edu:1521:ACAD111");
			      Connection conn = ds.getConnection("mgole1", "Kolhapur22091995");
			     //Class.forName("oracle.jdbc.driver.OracleDriver");
			     //Connection conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/xe","SYSTEM","Mrudula");
			      System.out.println("Connection Established");
			     
			      
			      while(true) {
			    	  
			    	  displayMainMenu();   //to display the main menu
			    	  System.out.println("ENTER YOUR CHOICE :  ");
			    	  choice = input.nextInt();
			    	  
			    	  switch(choice) {
					  case 1: System.out.println("\n");
					  		System.out.println("---------------Select table to be displayed------------");
					  		System.out.println("1.  STUDENTS\n2.  COURSES\n3.  PREREQUISITES\n4.  CLASSES\n5.  ENROLLMENTS\n6.  LOGS");
					  		System.out.println("-------------------------------------------------------\n");
					  		System.out.println("YOUR CHOICE :  ");
					  		table_choice = Integer.parseInt(reader.readLine());
					  		
					  		switch(table_choice) {
					  		case 1: displayStudents(conn);
					  				break;
					  								  				
					  		case 2: displayCourses(conn);
					  				break;
					  				
					  		case 3:displayPrerequisites(conn);
					  				break;
					  				
					  		case 4:displayClasses(conn);
					  				break;
					  				
					  		case 5:displayEnrollments(conn);
					  				break;

					  		case 6:displayLogs(conn);
					  				break;
					  				
					  		default:
							    System.out.println("Invalid choice !!");
					  			break;
					  		}
					  		break;
					  case 2: System.out.println("Please enter the SID:\n");
							String sid_addStud = reader.readLine();
							System.out.println("Please enter the First name:\n");
							String firstname_addStud = reader.readLine();
							System.out.println("Please enter the Last name:\n");
							String lastname_addStud = reader.readLine();
							System.out.println("Please enter the Status:\n");
							String status_addStud = reader.readLine();
							System.out.println("Please enter the GPA:\n");
							String gpa_addStud = reader.readLine();
							System.out.println("Please enter the Email:\n");
							String email_addStud =reader.readLine();
							addStudent(conn, sid_addStud, firstname_addStud, lastname_addStud, status_addStud, gpa_addStud, email_addStud);
							System.out.println("Student Added\n");
							break;
					  case 3: 
						  	System.out.println("Please enter the SID:");
						  	String sid_findStudClasses = reader.readLine();
						  	findStudClasses(conn,sid_findStudClasses);
							break;
					  case 4:
						    System.out.println("Please enter the dept_code: ");
							String dept_code_getPrerequisites = reader.readLine();
							System.out.println("Please enter the course_no: ");
							String course_no_getPrerequisites = reader.readLine();
							getPrerequisites(conn, dept_code_getPrerequisites, course_no_getPrerequisites);
							break;
							
					  case 5:System.out.println("Please enter class id: ");
							String classid_findClassDetails = reader.readLine();
							findClassDetails(conn, classid_findClassDetails);
							break;
					  case 6:
						  	System.out.println("Please enter the sid: ");
							String sid_enrollStudent = reader.readLine();
							System.out.println("Please enter the classid: ");
							String classid_enrollStudent = reader.readLine();
							enrollStudent(conn, sid_enrollStudent, classid_enrollStudent);
							break;
					  case 7: 
						  System.out.println("Please enter the sid: ");
							String sid_dropStudent = reader.readLine();
							System.out.println("Please enter the classid: ");
							String classid_dropStudent = reader.readLine();
							dropStudent(conn, sid_dropStudent, classid_dropStudent);
							break;
					  case 8:
						  	System.out.println("Please enter the sid: ");
							String sid_deleteStudent = reader.readLine();
							int status = deleteStudent(conn, sid_deleteStudent);
							if(status == 0) {
								System.out.println("The sid is invalid");
								continue;
							}
							else if(status==1) {
								System.out.println("Student deleted");
								continue;
							}
							else {
								continue;
							}
					  case 9:
						  	conn.close();
						  	conn =null;
						  	System.out.println("Exiting...");
						  	System.exit(1);
						  	break;
					  default:
						  conn.close();
						  conn=null;
						  break;
			    	  }
			      }
			      
			} catch (SQLException ex) { ex.printStackTrace();
			}
		    catch (Exception e) {e.printStackTrace();
		    }  
		    }
	public static void displayMainMenu() {
		
		System.out.println("==================================MENU=======================================");
		System.out.println("1.  DISPLAY TABLES");
		System.out.println("2.  ADD STUDENT");
		System.out.println("3.  FIND CLASSES TAKEN BY A STUDENT");
		System.out.println("4.  FIND PREREQUISITES OF A COURSE");
		System.out.println("5.  FIND STUDENTS ENROLLED IN A CLASS");
		System.out.println("6.  ENROLL A STUDENT");
		System.out.println("7.  DROP A STUDENT FROM A CLASS");
		System.out.println("8.  DELETE A STUDENT");
		System.out.println("9.  EXIT");
		System.out.println("=============================================================================");
		System.out.println();
		
	}
	
	public static void displayStudents(Connection conn)throws Exception,Exception{
		//CallableStatement stmt = conn.prepareCall("BEGIN stud_reg_sys.show_students(?); END;");
		CallableStatement stmt = conn.prepareCall("BEGIN stud_reg_sys.show_students(?); END;");
		stmt.registerOutParameter(1, OracleTypes.CURSOR); 
		//CallableStatement stmt = conn.prepareCall("select * from mrudula.students");
		stmt.execute();
		ResultSet rs = ((OracleCallableStatement) stmt).getCursor(1);
		System.out.println("Sid\tFIRSTNAME\tLASTNAME\tSTATUS\t\tGPA\t\tEMAIL");
		System.out.println("-----------------------------------------------------------------------------------------");
		while (rs.next()) {
			System.out.println(
					rs.getString("sid") + "\t" + rs.getString("firstname") + "\t\t" + rs.getString("lastname") + "\t\t"
							+ rs.getString("status") + "\t\t" + rs.getString("gpa") + "\t\t" + rs.getString("email"));
		}
		System.out.println("\n");
		stmt.close();
	}
	
	public static void displayCourses(Connection conn)throws Exception,Exception{
		//CallableStatement stmt = conn.prepareCall("BEGIN stud_reg_sys.show_courses(?); END;");
		CallableStatement stmt = conn.prepareCall("BEGIN stud_reg_sys.show_courses(?); END;");
		stmt.registerOutParameter(1, OracleTypes.CURSOR); 
		stmt.execute();
		ResultSet rs = ((OracleCallableStatement) stmt).getCursor(1);
		System.out.println("DEPT_CODE\tCOURSE_NO\tTITLE");
		System.out.println("----------------------------------------------------------");
		while (rs.next()) {
			System.out.println(
					rs.getString("DEPT_CODE") + "\t\t" + rs.getString("COURSE_NO") + "\t\t" + rs.getString("TITLE"));
		}
		System.out.println("\n");
		stmt.close();
	}
	public static void displayPrerequisites(Connection conn)throws Exception,Exception{
		//CallableStatement stmt = conn.prepareCall("BEGIN stud_reg_sys.show_prerequisites(?); END;");
		CallableStatement stmt = conn.prepareCall("BEGIN stud_reg_sys.show_prerequisites(?); END;");
		stmt.registerOutParameter(1, OracleTypes.CURSOR); 
		stmt.execute();
		ResultSet rs = ((OracleCallableStatement) stmt).getCursor(1);
		System.out.println("DEPT_CODE\tCOURSE_NO\tPRE_DEPT_CODE\tPRE_COURSE_NO");
		System.out.println("---------------------------------------------------------------------");
		while (rs.next()) {
			System.out.println(rs.getString("DEPT_CODE") + "\t\t" + rs.getString("COURSE_NO") + "\t\t"
					+ rs.getString("PRE_DEPT_CODE") + "\t\t" + rs.getString("PRE_COURSE_NO"));
		}
		System.out.println("\n");
		stmt.close();
	}
	
	public static void displayClasses(Connection conn)throws Exception,Exception{
		//CallableStatement stmt = conn.prepareCall("BEGIN stud_reg_sys.show_classes(?); END;");
		CallableStatement stmt = conn.prepareCall("BEGIN stud_reg_sys.show_classes(?); END;");
		stmt.registerOutParameter(1, OracleTypes.CURSOR); 
		stmt.execute();
		ResultSet rs = ((OracleCallableStatement) stmt).getCursor(1);
		System.out.println("CLASSID\tDEPT_CODE\tCOURSE_NO\tSECT_NO\t\tYEAR\tSEMESTER\tLIMIT\tCLASS_SIZE");
		System.out.println("---------------------------------------------------------------------------------------------------------");
		while (rs.next()) {
			System.out.println(
					rs.getString("classid") + "\t" + rs.getString("dept_code") + "\t\t" + rs.getString("course_no") + "\t\t"
					+ rs.getString("sect_no") + "\t\t" + rs.getString("year") + "\t" + rs.getString("semester") + "\t\t" 
					+ rs.getString("limit") + "\t\t" + rs.getString("class_size"));
		}
		System.out.println("\n");
		stmt.close();
	}
	
	public static void displayEnrollments(Connection conn)throws Exception,Exception{
		//CallableStatement stmt = conn.prepareCall("BEGIN stud_reg_sys.show_enrollments(?); END;");
		CallableStatement stmt = conn.prepareCall("BEGIN stud_reg_sys.show_enrollments(?); END;");
		stmt.registerOutParameter(1, OracleTypes.CURSOR); 
		stmt.execute();
		ResultSet rs = ((OracleCallableStatement) stmt).getCursor(1);
		System.out.println("SID\tCLASSID\tLGRADE");
		System.out.println("-------------------------------------------------------------------");
		while (rs.next()) {
			System.out.println(rs.getString("SID") + "\t" + rs.getString("CLASSID") 
					+ "\t"+ rs.getString("LGRADE"));
		}
		System.out.println("\n");
		stmt.close();
	}
	
	public static void displayLogs(Connection conn)throws Exception,Exception{
		//CallableStatement stmt = conn.prepareCall("BEGIN stud_reg_sys.show_logs(?); END;");
		CallableStatement stmt = conn.prepareCall("BEGIN stud_reg_sys.show_logs(?); END;");
		stmt.registerOutParameter(1, OracleTypes.CURSOR); 
		stmt.execute();
		ResultSet rs = ((OracleCallableStatement) stmt).getCursor(1);
		System.out.println("LOGID\tWHO\t\tTIME\t\tTABLE_NAME\tOPERATION\tKEY_VALUE");
		System.out.println("-------------------------------------------------------------------");
		while (rs.next()) {
			System.out.println(rs.getString("LOGID") + "\t" + rs.getString("WHO") + "\t" 
					+ rs.getString("TIME") + "\t" + rs.getString("TABLE_NAME") + "\t" 
					+ rs.getString("OPERATION") + "\t\t"+ rs.getString("KEY_VALUE"));
		}
		System.out.println("\n");
		stmt.close();
	}
	
	public static void addStudent(Connection conn,String sid,String firstname,String lastname,String status,String gpa,String email ) {
		try {
			//CallableStatement stmt = conn.prepareCall("BEGIN stud_reg_sys.add_student(?,?,?,?,?,?); END;");
			CallableStatement stmt = conn.prepareCall("BEGIN stud_reg_sys.add_student(?,?,?,?,?,?); END;");
			stmt.setString(1, sid); 
			stmt.setString(2, firstname);
			stmt.setString(3, lastname);
			stmt.setString(4, status);
			stmt.setString(5, gpa);
			stmt.setString(6, email);
			stmt.execute();
			stmt.close();
			stmt = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void findStudClasses(Connection conn, String sid) throws Exception,Exception{
		try {
			CallableStatement stmt = conn.prepareCall("BEGIN stud_reg_sys.display_stud_data(?,?,?); END;");
			stmt.setString(1, sid);
			stmt.registerOutParameter(2, java.sql.Types.NUMERIC);
			stmt.registerOutParameter(3, OracleTypes.CURSOR);
			stmt.execute();
			int num = stmt.getInt(2);
			if (num == 0) {
				System.out.println("The sid is invalid.");
			}
			else {
				ResultSet rs = ((OracleCallableStatement) stmt).getCursor(3);
				if (!rs.next()) { // if rs.next() returns false
					// then there are no rows.
					System.out.println("The student has not taken any course.");
				}
				else{
				System.out.println("SID\tLASTNAME\tSTATUS\tCLASSID\tDEPT_CODE-COURSE_NO\tTITLE\t\t\tYEAR\t\tSEMESTER");
				System.out.println("------------------------------------------------------------------------------------------------------------------");
				while (rs.next()) {
					System.out.println(rs.getString("sid")+"\t"+rs.getString("lastname") + "\t\t" 
					+ rs.getString("status") + "\t" + rs.getString("classid") + "\t" + rs.getString("course_id")
					+ "\t\t\t" + rs.getString("title") + "\t\t" + rs.getInt("year")+"\t\t"+rs.getString("semester") );
				}
				}
				System.out.println("\n");
				rs.close();
				rs = null;
			}
			stmt.close();
			stmt = null;
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void getPrerequisites(Connection conn,String dept_code, String prereq_course_no) {
		try {			
			CallableStatement stmt = conn.prepareCall("BEGIN stud_reg_sys.prerequisites_data(?,?,?); END;");
			stmt.setString(1, dept_code);
			stmt.setString(2,prereq_course_no);
			stmt.registerOutParameter(3, OracleTypes.CURSOR);
			stmt.execute();

			ResultSet rs = ((OracleCallableStatement) stmt).getCursor(3);
			System.out.println("Prerequisites are as follows: ");
			System.out.println("DEPT_CODE-COURSE_NO");
			System.out.println("----------------------");
			while (rs.next()) {
				System.out.println(rs.getString("prereq_course"));
				//System.out.println(rs.getString("dept_code") + rs.getInt("course_no"));
			}
			System.out.println("\n");
			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void findClassDetails(Connection conn, String classid){
		try {
			//Statement pstmnt = conn.createStatement();
			CallableStatement stmt = conn.prepareCall("BEGIN stud_reg_sys.get_class_student(?,?,?,?); END;");
			
			stmt.setString(1, classid);
			stmt.registerOutParameter(2, java.sql.Types.NUMERIC);
			stmt.registerOutParameter(3, OracleTypes.CURSOR);
			stmt.registerOutParameter(4, OracleTypes.CURSOR);
			stmt.execute();
			int vflag = stmt.getInt(2);
			if (vflag == 0) {
				System.out.println("The Class id is invalid");

			} else {

				ResultSet rs = ((OracleCallableStatement) stmt).getCursor(3);
				System.out.println("CLASSID\tTITLE\t\t\tSEMESTER\tYEAR");
				System.out.println("------------------------------------------------------");
				while (rs.next()) {
					System.out.println(rs.getString("classid") + "\t" + rs.getString("title") + "\t"
							+ rs.getString("semester") + "\t\t" + rs.getString("year"));
				}
				rs = ((OracleCallableStatement) stmt).getCursor(4);
					
				int count=0;
				while (rs.next()) {
					if(count==0) {
						System.out.println("\nStudents who have taken the class");
						System.out.println("SID\tLASTNAME");
						System.out.println("------------------------------");
					}
						
					System.out.println(rs.getString("sid") + "\t" + rs.getString("lastname"));
					count++;
				}
				System.out.println("\n");
				if (count==0) { 
					// then there are no rows.
					System.out.println("No student is enrolled in the class");
				}

				rs.close();
				rs = null;
			}
			stmt.close();
			stmt = null;
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void enrollStudent(Connection conn,String sid, String classid)throws Exception,Exception{
	  try {
		CallableStatement stmt = conn.prepareCall("BEGIN stud_reg_sys.enroll_student(?,?,?,?,?,?,?,?,?); END;");
		stmt.setString(1, sid);
		stmt.setString(2, classid);
		stmt.registerOutParameter(3, java.sql.Types.NUMERIC);
		stmt.registerOutParameter(4, java.sql.Types.NUMERIC);
		stmt.registerOutParameter(5, java.sql.Types.NUMERIC);
		stmt.registerOutParameter(6, java.sql.Types.NUMERIC);
		stmt.registerOutParameter(7, java.sql.Types.NUMERIC);
		stmt.registerOutParameter(8, java.sql.Types.NUMERIC);
		stmt.registerOutParameter(9, java.sql.Types.NUMERIC);
		stmt.execute();
		int status = stmt.getInt(3);
		int status2 = stmt.getInt(4);
		int status3 = stmt.getInt(5);
		int status4 = stmt.getInt(6);
		int status5 = stmt.getInt(7);
		int status6 = stmt.getInt(8);
		int status7 = stmt.getInt(9);

		if (status == 0) {
			System.out.println("The Student id is invalid");
		} 
		else if (status2 == 0) {
			System.out.println("The Class id is invalid");
		}
		else if(status3 == 0){
			System.out.println("The class is closed.");
		}
		else if(status4 == 0){
			System.out.println("The student is already in the class");
		}
		else if(status5 == 0){
			System.out.println("You are overloaded.");
		}
		else if(status6 == 0){
			System.out.println("Students cannot be enrolled in more than three classes in the same semester.");
		}
		else if(status7 == 0){
			System.out.println("Prerequisite courses have not been completed");
		}
		else {
			System.out.println("Student Enrolled");
		}
		System.out.println("\n");
		stmt.close();
		stmt = null;
	  }catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void dropStudent(Connection conn, String sid,String classid)throws Exception,Exception {
		try {
			CallableStatement stmt = conn.prepareCall("BEGIN stud_reg_sys.drop_student(?,?,?,?,?,?,?,?); END;");
			stmt.setString(1, sid);
			stmt.setString(2, classid);
			stmt.registerOutParameter(3, java.sql.Types.NUMERIC);
			stmt.registerOutParameter(4, java.sql.Types.NUMERIC);
			stmt.registerOutParameter(5, java.sql.Types.NUMERIC);
			stmt.registerOutParameter(6, java.sql.Types.NUMERIC);
			stmt.registerOutParameter(7, java.sql.Types.NUMERIC);
			stmt.registerOutParameter(8, java.sql.Types.NUMERIC);
			stmt.execute();
			int status = stmt.getInt(3);
			int status2 = stmt.getInt(4);
			int status3 = stmt.getInt(5);
			int status4 = stmt.getInt(6);
			int status5 = stmt.getInt(7);
			int status6 = stmt.getInt(8);
	
			if (status == 0) {
				System.out.println("The sid is invalid");
			} 
			else if (status2 == 0) {
				System.out.println("The classid is invalid.");
			}
			else if(status3 == 0){
				System.out.println("The student is not enrolled in the class.");
			}
			else if(status4 == 0){
				System.out.println("The drop is not permitted because another class uses it as a prerequisite.");
			}
			
			else{
				System.out.println("\nStudent dropped from the class");
			}
			
			if(status5 == 0){
				System.out.println("This student is not enrolled in any classes.");
			}
			if(status6 == 0){
				System.out.println("The class now has no students.");
			}
			
			System.out.println("\n");
			stmt.close();
			stmt = null;
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static int deleteStudent(Connection conn, String sid)throws Exception,Exception {
		try {
			CallableStatement stmt = conn.prepareCall("BEGIN stud_reg_sys.delete_student(?,?); END;");
			stmt.setString(1, sid);
			stmt.registerOutParameter(2, java.sql.Types.NUMERIC);
			stmt.execute();
			int status = stmt.getInt(2);

			if (status == 0) {
				stmt.close();
				stmt = null;
				return status;
			}
			else{
				stmt.close();
				stmt = null;
				return 1;
			}	
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return -99;
	}
	}// end Class Main