import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;

public class Architect {
	// insert Architect method
	public static void enterNewArchitect(Statement statement) throws SQLException {
		// USE TRY AND CATCH WITH A DO WHILE LOOP TO CAPTURE DATA.
		boolean continueInput = true;
		do {
			try {
				System.out.println("\nAdding Architect information:\n");
				// Get variables to add to Architect
				System.out.print("Please enter Achitect ID:");
				int architectId = Main.scan.nextInt();
				Main.scan.nextLine();

				System.out.print("Please enter Achitect Name (Name & Surname):");
				String architectName = Main.scan.nextLine();

				// Check if Name exist in the Architect database, if not, capture into database
				ResultSet architectNameExist = statement
						.executeQuery("SELECT * FROM architect WHERE name = '" + architectName + "'");
				if (architectNameExist.next() == false) {

					System.out.print("Please enter Achitect Telephone number:");
					int telephone = Main.scan.nextInt();
					Main.scan.nextLine();

					System.out.print("Please enter Achitect Email:");
					String email = Main.scan.nextLine();

					System.out.print("Please enter the Address of the Achitect:");
					String address = Main.scan.nextLine();
					System.out.println();

					// INSERT INTO Architect
					statement.executeUpdate("INSERT INTO architect values ('" + architectId + "','" + architectName
							+ "','" + telephone + "','" + email + "','" + address + "')");
				} else {
					System.out.println("Architect name already exists");
				}
				// STOP THE WHILE LOOP
				continueInput = false;
			} catch (InputMismatchException e) {
				System.out.println("\nIncorrect format input entered, try again.");
				Main.scan.nextLine();
			}
		} while (continueInput);
	}

	// delete Architect method
	public static void deleteArchitect(Statement statement) throws SQLException {

		// Ask if user knows which Architect they want to delete
		System.out.println("Do you know which Architect you want to Delete? \n1. YES \n2. NO");
		int userKnowsId = Main.scan.nextInt();
		Main.scan.nextLine();

		// If No, display all the architect
		if (userKnowsId != 1) {
			ResultSet showAllArchitect = statement.executeQuery("SELECT * FROM architect");
			Main.displayContactDetailsResults(showAllArchitect);
		}

		// User enters Architect
		System.out.print("\nPlease enter Architect Name & Surname to be deleted:");
		String name = Main.scan.nextLine();

		ResultSet results = statement.executeQuery("SELECT * FROM architect WHERE name = '" + name + "'");

		// If Architect exits, delete it
		if (results.next() == true) {
			// Display Architect that is being deleted
			System.out.println("\nThe Architect is deleted:");
			ResultSet resultShow = statement.executeQuery("SELECT * FROM architect WHERE name = '" + name + "'");
			Main.displayContactDetailsResults(resultShow);

			// Delete the Architect in database
			statement.executeUpdate("DELETE FROM architect WHERE name ='" + name + "'");

			// Search if a project contains this Architect
			ResultSet resultsProject = statement.executeQuery("SELECT * FROM project WHERE architect = '" + name + "'");
			if (resultsProject.next() == true) {
//				 Display the Architect in the project and ask if user wants to delete the project also	 
				System.out.println("\nThe below Project was found with this Architect Name:");
				ResultSet resultsProjectDislay = statement
						.executeQuery("SELECT * FROM project WHERE architect = '" + name + "'");
				Main.displayProjectResults(resultsProjectDislay);

				System.out.println("\nDo you want to delete the above Projects for this Architect?: \n1. Yes \n2. No");
				int itemUpdate = Main.scan.nextInt();
				Main.scan.nextLine();
				if (itemUpdate == 1) {
					// Delete the Project in database
					statement.executeUpdate("DELETE FROM project WHERE architect ='" + name + "'");
				}
			}
		} 
		else {
			System.out.println("\nArchitect was not found");
		}
	}

	// update Architect method
	public static void updateArchitect(Statement statement) throws SQLException {
		boolean continueInput = true;
		do {
			try {
				// Ask if user knows which Architect they want to amend
				System.out.println("Do you know which Architect you want to update by Name? \n1. YES \n2. NO");
				int userKnows = Main.scan.nextInt();
				Main.scan.nextLine();

				// If No, display all the architect
				if (userKnows != 1) {
					ResultSet showAllArchitect = statement.executeQuery("SELECT * FROM architect");
					Main.displayContactDetailsResults(showAllArchitect);
				}

				// User enters Architect Name
				System.out.print("\nPlease enter Architect Name:");
				String architectName = Main.scan.nextLine();

				ResultSet results = statement
						.executeQuery("SELECT * FROM architect WHERE name = '" + architectName + "'");

				// If Name exits, can make amendments
				if (results.next() == true) {

					ResultSet resultShow = statement
							.executeQuery("SELECT * FROM architect WHERE name = '" + architectName + "'");
					Main.displayContactDetailsResults(resultShow);
					System.out.println(
							"\nWhich item do you want to update? \n1. Telephone \n2. Email \n3. Address \n0. Back to Main Screen");
					String itemUpdate = Main.scan.nextLine();
					// Switch statement according to answer given
					switch (itemUpdate) {

					case "0":
						// Back to Main Screen
						break;

					case "1":
						// UPDATE the Telephone Number:
						System.out.print("Please enter new Telephone number for the Architect:");
						int telephoneNumber = Main.scan.nextInt();
						Main.scan.nextLine();
						statement.executeUpdate("UPDATE architect SET telephone =" + telephoneNumber + " WHERE name = '"
								+ architectName + "'");
						break;

					case "2":
						// Update Email
						System.out.print("Please enter Achitect Email:");
						String email = Main.scan.nextLine();
						statement.executeUpdate(
								"UPDATE architect SET email = '" + email + "' WHERE name = '" + architectName + "'");
						break;

					case "3":
						// Update Address
						System.out.print("Please enter the Address of the Achitect:");
						String address = Main.scan.nextLine();
						statement.executeUpdate("UPDATE architect SET address = '" + address + "' WHERE name = '"
								+ architectName + "'");
						break;

					// Default if user entered a wrong character
					default:
						System.out.println("\nArchitect was not ammended\n");
						break;

					}
				} else {
					System.out.println("\nArchitect was not found\n");
				}
				// STOP THE WHILE LOOP
				continueInput = false;
			} catch (InputMismatchException e) {
				System.out.println("\nIncorrect format input entered, try again.");
				Main.scan.nextLine();
			}
		} while (continueInput);

	}
}
