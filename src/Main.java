import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
	static Scanner scan = new Scanner(System.in);

	public static void main(String[] args) {
		// USE TRY AND CATCH WITH A DO WHILE LOOP TO CAPTURE DATA.
		// Connect to the poised database, via the jdbc:mysql:channel on localhost (this PC)
		// Use username "otheruser", password "swordfish".
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/poisedpms?allowPublicKeyRetrieval=true&useSSL=false",
					"otheruser", "swordfish");
			Statement statement = connection.createStatement();

			// Declare true for do loop to run
			boolean menuPrompt = true;

			do {
				System.out.println("\nWelcome to the Poised project management system. What would you like to do? \n"
						+ "1. Enter a New Project\n" + "2. Enter a New Architect\n" + "3. Enter a New Contractor\n"
						+ "4. Enter a New Customer\n" + "5. Update data\n" + "6. Delete data\n" + "7. Search data\n"
						+ "0. Exit");

				// Scan the answer given
				String choice = scan.nextLine();

				// Switch statement to execute method according to answer given
				switch (choice) {

				case "0":
					// End the while loop
					menuPrompt = false;
					System.out.println("\nGoodbye");
					break;
				case "1":
					Project.enterNewProject(statement);
					break;
				case "2":
					Architect.enterNewArchitect(statement);
					break;
				case "3":
					Contractor.enterNewContractor(statement);
					break;
				case "4":
					Customer.enterNewCustomer(statement);
					break;
				case "5":
					updateDatabase(statement);
					break;
				case "6":
					deleteData(statement);
					break;
				case "7":
					searchData(statement);
					break;

				// Default if user entered a wrong character
				default:
					System.out.println("\nEnter a number available in the prompt:");
					break;
				}

			} while (menuPrompt == true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		scan.close();
	}

	// method to update database
	public static void updateDatabase(Statement statement) throws SQLException {
		System.out.println(
				"\nWhich data do you want to update? \n1. Project by Project Number\n2. Project by Project Name \n3. Architect \n4. Contractor \n5. Customer \n0. Back to Main Screen");
		String itemUpdate = scan.nextLine();

		switch (itemUpdate) {
		case "0":
			break;
		case "1":
			Project.updateProjectNumber(statement);
			break;
		case "2":
			Project.updateProjectName(statement);
			break;
		case "3":
			Architect.updateArchitect(statement);
			break;
		case "4":
			Contractor.updateContractor(statement);
			break;
		case "5":
			Customer.updateCustomer(statement);
			break;

		default:
			System.out.println("\nData was not updated");
			break;
		}

	}

	// method to delete from database
	public static void deleteData(Statement statement) throws SQLException {
		System.out.println(
				"\nFrom which database do you want to delete? \n1. Projects \n2. Architect \n3. Contractor \n4. Customer \n0. Back to Main Screen");
		String itemUpdate = scan.nextLine();

		switch (itemUpdate) {

		case "0":
			break;
		case "1":
			Project.deleteProject(statement);
			break;
		case "2":
			Architect.deleteArchitect(statement);
			break;
		case "3":
			Contractor.deleteContractor(statement);
			break;
		case "4":
			Customer.deleteCustomer(statement);
			break;

		default:
			System.out.println("\nData was not deleted");
			break;
		}

	}

	// method to search database(completion and due dates, projectName and number)
	public static void searchData(Statement statement) throws SQLException {
		// USE TRY AND CATCH DO WHILE LOOP UNTILL ALL THE INPUTS ARE CAPTURED CORRECT
		boolean continueInput = true;
		do {
			try {

//				Ask user which item they want to search for 
				System.out.println(
						"\nHow would you like to search?: \n1. Project by Project Number \n2. Project by Project Name \n3. All Projects"
								+ "\n4. Architect by Name \n5. All Architects \n6. Contractor by Name \n7. All Contractors "
								+ "\n8. Customer by Name \n9. All Customers \n10. Finalised Projects \n11. Incomplete Projects "
								+ "\n12. Projects past due date \n0. Back to Main Screen");
				String itemUpdate = scan.nextLine();

				switch (itemUpdate) {

				case "0":
					// Back to Main Screen
					break;

				case "1":
					// Search by project Number
					System.out.print("Enter Project Number:");
					int projectNumber = scan.nextInt();
					scan.nextLine();
					ResultSet projectByNumber = statement.executeQuery(
							"SELECT project.projectNum, project.projectName, project.buildingType, project.address, project.erfNum, project.totalFee, project.amountPaid, project.deadlineDate, project.completionDate, project.finalised, project.architect, project.contractor, project.customer, project.engineer, project.projectManager FROM project INNER JOIN architect ON project.architect = architect.name INNER JOIN contractor ON project.contractor = contractor.name INNER JOIN customer ON project.customer = customer.name WHERE projectNum= "+ projectNumber + ";");
					displayProjectResults(projectByNumber);
					break;

				case "2":
					// Search by project Name
					System.out.print("Enter Project Name:");
					String projectName = scan.nextLine();
					ResultSet projectByName = statement.executeQuery(
							"SELECT project.projectNum, project.projectName, project.buildingType, project.address, project.erfNum, project.totalFee, project.amountPaid, project.deadlineDate, project.completionDate, project.finalised, project.architect, project.contractor, project.customer, project.engineer, project.projectManager FROM project INNER JOIN architect ON project.architect = architect.name INNER JOIN contractor ON project.contractor = contractor.name INNER JOIN customer ON project.customer = customer.name WHERE projectName='" + projectName + "';");
					displayProjectResults(projectByName);

					break;

				case "3":
					// Confirm if user wants to see all the Projects
					System.out.println("\nAre you sure you want to show all the Projects? \n1. YES \n2. NO");
					int confirmSeeProjects = scan.nextInt();
					scan.nextLine();
					if (confirmSeeProjects == 1) {
						ResultSet showAllProject = statement.executeQuery("SELECT * FROM project");
						displayProjectResults(showAllProject);
					}
					break;

				case "4":
					// User enters Architect Name
					System.out.print("\nPlease enter Architect Name:");
					String architectName = scan.nextLine();
					ResultSet resultShowArchitect = statement
							.executeQuery("SELECT * FROM architect WHERE name = '" + architectName + "'");
					displayContactDetailsResults(resultShowArchitect);
					break;

				case "5":
					// Confirm if user wants to see all the Architects
					System.out.println("\nAre you sure you want to show all the Architects? \n1. YES \n2. NO");
					int confirmSeeArchitect = scan.nextInt();
					scan.nextLine();
					if (confirmSeeArchitect == 1) {
						ResultSet showAllArchitect = statement.executeQuery("SELECT * FROM architect");
						displayContactDetailsResults(showAllArchitect);
					}
					break;

				case "6":
					// User enters Contractor Name
					System.out.print("\nPlease enter Contractor Name:");
					String contractorName = scan.nextLine();
					ResultSet resultShowContractor = statement
							.executeQuery("SELECT * FROM contractor WHERE name = '" + contractorName + "'");
					displayContactDetailsResults(resultShowContractor);
					break;

				case "7":
					// Confirm if user wants to see all the Contractors
					System.out.println("\nAre you sure you want to show all the Contractors? \n1. YES \n2. NO");
					int confirmSeeContractor = scan.nextInt();
					scan.nextLine();
					if (confirmSeeContractor == 1) {
						ResultSet showAllContractor = statement.executeQuery("SELECT * FROM contractor");
						displayContactDetailsResults(showAllContractor);
					}
					break;

				case "8":
					// User enters Customer Name
					System.out.print("\nPlease enter Customer Name:");
					String CustomerName = scan.nextLine();
					ResultSet resultShowCustomer = statement
							.executeQuery("SELECT * FROM customer WHERE name = '" + CustomerName + "'");
					displayContactDetailsResults(resultShowCustomer);
					break;

				case "9":
					// Confirm if user wants to see all the Customers
					System.out.println("\nAre you sure you want to show all the Customers? \n1. YES \n2. NO");
					int confirmSeeCustomer = scan.nextInt();
					scan.nextLine();
					if (confirmSeeCustomer == 1) {
						ResultSet showAllCustomer = statement.executeQuery("SELECT * FROM customer");
						displayContactDetailsResults(showAllCustomer);
					}
					break;

				case "10":
					// Show all Finalized projects
					boolean projectFinalised = true;
					ResultSet showProjectByFinalised = statement
							.executeQuery("SELECT * FROM project WHERE finalised =" + projectFinalised);
					displayProjectResults(showProjectByFinalised);
					break;

				case "11":
					// Show all Not finalized projects
					boolean projectNotFinalised = false;
					ResultSet showProjectByNotFinalised = statement
							.executeQuery("SELECT * FROM project WHERE finalised =" + projectNotFinalised);
					displayProjectResults(showProjectByNotFinalised);
					break;

				case "12":
					// All project that is past due date
					ResultSet showProjectPastDue = statement.executeQuery(
							"SELECT * FROM project WHERE deadlineDate < DATE(NOW()) AND finalised = FALSE");
					displayProjectResults(showProjectPastDue);
					break;

				// Default if user entered a wrong character
				default:
					System.out.println("\nInvalid number input\n");
					break;
				}
				// STOP THE WHILE LOOP
				continueInput = false;
			} catch (InputMismatchException e) {
				System.out.println("\nIncorrect format input entered, try again.");
				scan.nextLine();
			}
		} while (continueInput);

	}

	public static void displayProjectResults(ResultSet results) throws SQLException {
		// Display Fields
		System.out.println(
				"ProjectNumber, ProjectName, BuildingType, Address, ErfNumber, TotalFee, AmountPaid, Deadline, CompletionDate, Finalised, Architect, Contractor, Customer, Engineer, Project Manager");
		// Display selected data rows
		while (results.next()) {
			System.out.println(results.getInt("projectNum") + ", " + results.getString("projectName") + ", "
					+ results.getString("buildingType") + ", " + results.getString("address") + ", "
					+ results.getInt("erfNum") + ", " + results.getDouble("totalFee") + ", "
					+ results.getDouble("amountPaid") + ", " + results.getDate("deadlineDate") + ", "
					+ results.getDate("completionDate") + ", " + results.getBoolean("finalised") + ", "
					+ results.getString("architect") + ", " + results.getString("contractor") + ", "
					+ results.getString("customer") + ", " + results.getString("engineer") + ", "
					+ results.getString("projectManager"));
		}	
	}

	public static void displayContactDetailsResults(ResultSet results) throws SQLException {
		// Display Fields
		System.out.println("Id, Name, Telephone Number, Email, Address");
		// Display selected data rows
		while (results.next()) {
			System.out.println(results.getInt("Id") + ", " + results.getString("Name") + ", " + results.getInt("Telephone") + ", "
					+ results.getString("Email") + ", " + results.getString("Address"));
		}
	}

}
