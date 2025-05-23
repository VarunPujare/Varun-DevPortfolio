package hospital_management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
	private Connection connection;
	private Scanner scanner;
Scanner s=new Scanner(System.in);
	public Patient(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}

	public void addpatient() {
		System.out.println("Enter patient name ");
		String name = scanner.next();
		System.out.println("Enter patient Age ");
		int age = scanner.nextInt();
		System.out.println("Enter patient gender ");
		String gender = scanner.next();
		try {
			String query = "INSERT INTO patients(name,age,gender) values(?,?,?)";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, name);
			ps.setInt(2, age);
			ps.setString(3, gender);
			int affectedRows = ps.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("Patient Added Successfully");
			} else {
				System.out.println("Failed to add patient ");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
}

	public void viewPatients() {
		String query = "Select*from patients";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			System.out.println(" Patients: ");
			System.out.println("+-----------+-----------------+----------+-------------+");
			System.out.println("|Patient Id | Name            | Age      | Gender      |");
			System.out.println("+-----------+-----------------+----------+-------------+");

			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String gender = rs.getString("gender");
				System.out.printf("|%-11s|%-17s|%-10s|%-13s|\n",id,name,age,gender);
				System.out.println("+-----------+-----------------+----------+-------------+");

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean getPatientById(int id) {
		String query = "select*from patients Where id=?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public void removePatient() {
		System.out.println("Enter the Patient Id:");
		int pat_id=scanner.nextInt();
		try {
			String query="delete from patients where id=?";
			PreparedStatement preparedStatement=connection.prepareStatement(query);
			preparedStatement.setInt(1, pat_id);
			int affectedRows=preparedStatement.executeUpdate();
			if(affectedRows>0) {
				System.out.println("Patient removed succesfully");
			}else {
				System.out.println("No patient found with the id: "+pat_id);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
