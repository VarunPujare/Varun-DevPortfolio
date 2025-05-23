package hospital_management;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HospitalManagementSystem {

	private static final String url="jdbc:mysql://localhost:3306/hospital";

	private static final String username="root";
	private static final String password="varun454530";

public static void main(String[] args) {
	Scanner sc=new Scanner(System.in);
	try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		
	}
	catch(ClassNotFoundException e) {
		e.printStackTrace();
	}
	
	try {
		Connection connection=DriverManager.getConnection(url,username,password);
		Patient patient=new Patient(connection,sc);
		Doctor doctor=new Doctor(connection);
		while(true) {
			System.out.println("HOSPITAL MANAGEMENT SYSTEM");
			System.out.println("1.Add patient");
			System.out.println("2.View Patients");
			System.out.println("3.View Doctors ");
			System.out.println("4.Book Appointment");
			System.out.println("5.Remove Patient");
			System.out.println("6.Exit");
			
			System.out.println("Enter your choice");
			int choice=sc.nextInt();
			switch(choice) {
			case 1:
				patient.addpatient();
				System.out.println();
				break;
			case 2:
				patient.viewPatients();
				System.out.println();
				break;
			
			case 3:
				doctor.viewDoctors();
				System.out.println();
				break;
			case 4:
				bookAppointment(patient,doctor,connection,sc);
				System.out.println();
				break;
			case 5:
				patient.removePatient();
				System.out.println();
				break;
			case 6:
				return;
				default:
					System.out.println("Enter valid choice!!!");
					System.out.println();
			}
			}
	}
	catch(SQLException e) {
		e.printStackTrace();
	}
}
public static void bookAppointment(Patient patient,Doctor doctor,Connection connection,Scanner scanner) {
	System.out.println("Enter patient id:");
	Scanner sc=new Scanner(System.in);
	int patient_id=sc.nextInt();
	System.out.println("Enter Doctor id:");
	int doctor_id=sc.nextInt();
	System.out.println("Enter appointment date(YYYY-MM-DD):");
	String appointmentDate=sc.next();
	if(patient.getPatientById(patient_id)&& doctor.getDoctorById(doctor_id))
	{
	if(checkDoctorAvailability(doctor_id,appointmentDate,connection))	{
		String appointmentQuery="insert into appointments(patient_id,doctor_id,appointment_date)values(?,?,?)";
		try {
			PreparedStatement ps=connection.prepareStatement(appointmentQuery);
			ps.setInt(1,patient_id);
			ps.setInt(2,doctor_id);
			ps.setString(3,appointmentDate);
			int affectedrows=ps.executeUpdate();
			if(affectedrows>0) {
				System.out.println("Appointment booked");
			}else {
				System.out.println("Failde to book appointment!");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	else {
		System.out.println("doctor not available on these date");
	}
	
	}else {
		System.out.println("Either patient or doctor does  not exists");
	}
}
public static boolean checkDoctorAvailability(int doctorId,String appointmentDate,Connection connection) {
	String query="Select count(*)from appointments where doctor_id=? AND appointment_date=?";
	try {
		PreparedStatement ps=connection.prepareStatement(query);
		ps.setInt(1, doctorId);
		ps.setString(2, appointmentDate);
		ResultSet rs=ps.executeQuery();
		if(rs.next()) {
			int count=rs.getInt(1);
			if(count==0) {
				return true;
			}
			else {
				return false;
			}
		}
	}catch(SQLException e) {
		e.printStackTrace();
	}
	return false;
}
}
