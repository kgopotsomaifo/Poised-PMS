import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;

public class Customer {
	// insert customer method
	public static void enterNewCustomer(Statement statement) throws SQLException {
		// USE TRY AND CATCH WITH A DO WHILE LOOP TO CAPTURE DATA.
		boolean continueInput = true;
		do {
			try {
				System.out.println("\nAdding Customer information:\n");
				// Get variables to add to Customer
				System.out.print("Please enter Customer ID:");
				int customerId = Main.scan.nextInt();
				Main.scan.nextLine();

				System.out.print("Please enter Customer Name (Name & Surname):");
				String customerName = Main.scan.nextLine();

				// Check if Name exist in the Customer database, if not, capture into database
				ResultSet customerNameExist = statement
						.executeQuery("SELECT * FROM customer WHERE name = '" + customerName + "'");
				if (customerNameExist.next() == false) {

					System.out.print("Please enter Customer Telephone number:");
					int telephone = Main.scan.nextInt();
					Main.scan.nextLine();

					System.out.print("Please enter Customer Email:");
					String email = Main.scan.nextLine();

					System.out.print("Please enter the Address of the Customer:");
					String address = Main.scan.nextLine();
					System.out.println();

					// INSERT INTO Architect
					statement.executeUpdate("INSERT INTO architect values ('"
							+ customerId + "','" + customerName + "','" + telephone + "','" + email + "','"
							+ address + "')");
				} else {
					System.out.println("Customer name already exists");
				}
				// STOP THE WHILE LOOP
				continueInput = false;
			} catch (InputMismatchException e) {
				System.out.println("\nIncorrect format input entered, try again.");
				Main.scan.nextLine();
			}
		} while (continueInput);
	}

	// delete customer method
	public static void deleteCustomer(Statement statement) throws SQLException {
		// Ask if user knows which Customer they want to delete
		System.out.println("Do you know which Customer you want to Delete? \n1. YES \n2. NO");
		int userKnowsId = Main.scan.nextInt();
		Main.scan.nextLine();

		// If No, display all Customers
		if (userKnowsId != 1) {
			ResultSet showAllCustomer = statement.executeQuery("SELECT * FROM customer");
			Main.displayContactDetailsResults(showAllCustomer);
		}

		// User enters Customer
		System.out.print("\nPlease enter Customer Name & Surname to be deleted:");
		String name = Main.scan.nextLine();

		ResultSet results = statement.executeQuery("SELECT * FROM customer WHERE name = '" + name + "'");

		// If Customer exits, delete it
		if (results.next() == true) {
			// Display Customer that is being deleted
			System.out.println("\nThe Customer is deleted:");
			ResultSet resultShow = statement.executeQuery("SELECT * FROM customer WHERE name = '" + name + "'");
			Main.displayContactDetailsResults(resultShow);

			// Delete the Customer in database
			statement.executeUpdate("DELETE FROM customer WHERE name ='" + name + "'");

			// Search if a project contains this Customer
			ResultSet resultsProject = statement.executeQuery("SELECT * FROM project WHERE customer = '" + name + "'");
			if (resultsProject.next() == true) {

				// display the project's customer and confirm deletion
				System.out.println("\nThe below Project was found with this Customer Name:");
				ResultSet resultsProjectDislay = statement
						.executeQuery("SELECT * FROM project WHERE customer = '" + name + "'");
				Main.displayProjectResults(resultsProjectDislay);

				System.out.println("\nDo you want to delete the above Projects for this Customer?: \n1. Yes \n2. No");
				int itemUpdate = Main.scan.nextInt();
				Main.scan.nextLine();

				if (itemUpdate == 1) {
					// Delete the Project in database
					statement.executeUpdate("DELETE FROM project WHERE customer ='" + name + "'");
				}
			}

		} else {
			System.out.println("\nCustomer was not found");
		}

	}

	// update customer method
	public static void updateCustomer(Statement statement) throws SQLException {
		boolean continueInput = true;
		do {
			try {
				// Ask if user knows which Customer they want to amend
				System.out.println("Do you know which Customer you want to update by Name? \n1. YES \n2. NO");
				int userKnows = Main.scan.nextInt();
				Main.scan.nextLine();

				// If No, display all customers
				if (userKnows != 1) {
					ResultSet showAllCustomer = statement.executeQuery("SELECT * FROM customer");
					Main.displayContactDetailsResults(showAllCustomer);
				}

				// User enters Customer Name
				System.out.print("\nPlease enter Customer Name:");
				String customerName = Main.scan.nextLine();

				ResultSet results = statement
						.executeQuery("SELECT * FROM customer WHERE name = '" + customerName + "'");

				// If Name exits, allow updates
				if (results.next() == true) {

					ResultSet resultShow = statement
							.executeQuery("SELECT * FROM customer WHERE name = '" + customerName + "'");
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
						System.out.print("Please enter new Telephone number for the Customer:");
						int telephoneNumber = Main.scan.nextInt();
						Main.scan.nextLine();
						statement.executeUpdate("UPDATE customer SET telephone =" + telephoneNumber + " WHERE name = '"
								+ customerName + "'");
						break;

					case "2":
						// Update Email
						System.out.print("Please enter Customer Email:");
						String email = Main.scan.nextLine();
						statement.executeUpdate(
								"UPDATE customer SET email = '" + email + "' WHERE name = '" + customerName + "'");
						break;

					case "3":
						// Update Address
						System.out.print("Please enter the Address of the Customer:");
						String address = Main.scan.nextLine();
						statement.executeUpdate(
								"UPDATE customer SET address = '" + address + "' WHERE name = '" + customerName + "'");
						break;

					// Default if user entered a wrong character
					default:
						System.out.println("\nCustomer was not ammended\n");
						break;

					}
				} else {
					System.out.println("\nCustomer was not found\n");
				}
				// STOP THE WHILE LOOP
				continueInput = false;
			} catch (InputMismatchException ex) {
				System.out.println("\nIncorrect format input entered, try again.");
				Main.scan.nextLine();
			}
		} while (continueInput);

	}

}
