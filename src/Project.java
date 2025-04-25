import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;

public class Project {
	// insert project method
	public static void enterNewProject(Statement statement) throws SQLException {
		// USE TRY AND CATCH WITH A DO WHILE LOOP TO CAPTURE DATA.
		boolean continueInput = true;
		do {
			try {

				// Get variables to add to Project
				System.out.print("Please enter Project Number:");
				int projectNumber = Main.scan.nextInt();
				Main.scan.nextLine();
				
				// check if project is in database, if not,add it
				ResultSet ProjectNumberExist = statement
						.executeQuery("SELECT projectNum FROM project WHERE projectNum  = " + projectNumber);
				if (ProjectNumberExist.next() == false) {

					System.out.print("Please enter Project Name:");
					String projectName = Main.scan.nextLine();

					System.out.print("Please enter Building Type:");
					String buildingType = Main.scan.nextLine();

					System.out.print("Please enter Address of the Project:");
					String projectAddress = Main.scan.nextLine();

					System.out.print("Please enter ERF Number:");
					int erfNumber = Main.scan.nextInt();
					Main.scan.nextLine();

					System.out.print("Please enter Total Project Fee:");
					double totalFee = Main.scan.nextDouble();

					System.out.print("Please enter Total amount paid:");
					double amountPaid = Main.scan.nextDouble();

					System.out.print("Please enter Deadline date (YYYYMMDD):");
					int deadlineDate = Main.scan.nextInt();
					Main.scan.nextLine();

					System.out.print("Please enter Engineer Name for this Project:");
					String engineer = Main.scan.nextLine();

					System.out.print("Please enter Project Manager Name for this Project:");
					String projectManager = Main.scan.nextLine();

					// New project will not be finalized
					boolean finalised = false;

					System.out.print("Please enter Architect Name (Name & Surname):");
					String architect = Main.scan.nextLine();
					
					// check if architect is in database, if not, add it
					ResultSet architectExist = statement
							.executeQuery("SELECT name FROM architect WHERE name = '" + architect + "'");
					if (architectExist.next() == false) {
						Architect.enterNewArchitect(statement);
					}

					System.out.print("Please enter Contractor Name (Name & Surname):");
					String contractor = Main.scan.nextLine();
					
					// check if contractor is in database, if not,add it
					ResultSet contractorExist = statement
							.executeQuery("SELECT name FROM contractor WHERE name = '" + contractor + "'");
					if (contractorExist.next() == false) {
						Contractor.enterNewContractor(statement);
					}

					// If there's no projectName, name it via the building type and surname of customer 
					String customer;
					if (projectName == "" || projectName == " ") {

						System.out.print("Please enter ONLY Customer SURNAME:");
						String customerSurname = Main.scan.nextLine();

						System.out.print("Please enter ONLY Customer NAME:");
						String customerName = Main.scan.nextLine();

						customer = customerName + " " + customerSurname;
						projectName = buildingType + " " + customerSurname;
					} else {
						System.out.print("Please enter Customer Name (Name & Surname):");
						customer = Main.scan.nextLine();
					}
					// check if customerName is in database, if not,add it
					ResultSet customerExist = statement
							.executeQuery("SELECT name FROM customer WHERE name = '" + customer + "'");
					if (customerExist.next() == false) {
						Customer.enterNewCustomer(statement);
					}

					// insert to Project(new project not to have completionDate)
					statement.executeUpdate(
							"INSERT INTO project (projectNum , projectName, buildingType, address, erfNum, totalFee, amountPaid, deadlineDate, finalised, architect, contractor, customer, engineer, projectManager) VALUES"
									+ "(" + projectNumber + ",'" + projectName + "','" + buildingType + "','"
									+ projectAddress + "'," + erfNumber + "," + totalFee + "," + amountPaid + ","
									+ deadlineDate + "," + finalised + ",'" + architect + "','" + contractor + "','"
									+ customer + "','" + engineer + "','" + projectManager + "')");

				} else {
					System.out.println("\n- Project Number already exist");
				}
				continueInput = false;
			} catch (InputMismatchException e) {
				System.out.println("\nIncorrect format input entered, try again.");
				Main.scan.nextLine();
			}
		} while (continueInput);

	}
	
	// delete project method 
	public static void deleteProject(Statement statement) throws SQLException {
		boolean continueInput = true;
		do {
			try {

				// Ask user which Project they want to delete
				System.out.println("Do you know which project you want to Delete by Project Number? \n1. YES \n2. NO");
				int userKnowsId = Main.scan.nextInt();
				Main.scan.nextLine();

				// If No, display all projects information
				if (userKnowsId != 1) {
					ResultSet showAllProject = statement.executeQuery("SELECT * FROM project");
					Main.displayProjectResults(showAllProject);
				}

				// If yes, user enters project number
				System.out.print("\nPlease enter Project Number:");
				int projectNumber = Main.scan.nextInt();
				Main.scan.nextLine();
				ResultSet results = statement.executeQuery("SELECT * FROM project WHERE projectNum= '" + projectNumber + "'");

				// If project number exits, delete it
				if (results.next() == true) {
					System.out.println("\nThe project is deleted");
					// Delete the project in database					
					statement.executeUpdate("DELETE FROM project WHERE projectNum ='" + projectNumber + "'");

				} else {
					System.out.println("\nProject number not found");
				}
				// STOP THE WHILE LOOP
				continueInput = false;
			} catch (InputMismatchException e) {
				System.out.println("\nIncorrect format input entered, try again.");
				Main.scan.nextLine();
			}
		} while (continueInput);

	}
	// method to update project via projectName
	public static void updateProjectName(Statement statement) throws SQLException {
		boolean continueInput = true;
		do {
			try {

				// Ask if user knows which Project Name they want to amend
				System.out.println("Do you know which project you want to update by Project Name? \n1. YES \n2. NO");
				int userKnowsId = Main.scan.nextInt();
				Main.scan.nextLine();

				// If No, display all projects basic information
				if (userKnowsId != 1) {
					ResultSet showAllProject = statement.executeQuery("SELECT * FROM project");
					Main.displayProjectResults(showAllProject);
				}

				// User enters project Name
				System.out.print("\nPlease enter Project Name:");
				String projectName = Main.scan.nextLine();
				
				ResultSet results = statement
						.executeQuery("SELECT * FROM project WHERE projectName = '" + projectName + "'");
				

				// If project number exits, can make amendments
				if (results.next() == true) {
					Main.displayProjectResults(results);

					System.out.println(
							"\nWhich item do you want to update? \n1. ProjectNumber \n2. BuildingType \n3. BuildingType \n4. Address \n5. ERF Number \n6. TotalFee \n7. AmountPaid \n8. Deadline \n9. CompletionDate \n10. Architect \n11. Contractor \n12. Customer \n13. Engineer \n14. Project Manager \n15. Finalised \n0. Back to Main Screen");
					String itemUpdate = Main.scan.nextLine();

					// Switch statement according to answer given
					switch (itemUpdate) {

					case "0":
						// Back to Main Screen
						break;

					case "1":
						// UPDATE the Project Number:
						System.out.print("Please enter new Project Number:");
						int projectNumber = Main.scan.nextInt();
						Main.scan.nextLine();
						statement.executeUpdate("UPDATE project SET projectNum =" + projectNumber
								+ " WHERE projectName = '" + projectName + "'");
						break;
						
					case "2":
						// UPDATE the Project Name:
						System.out.print("Please enter new Project Number:");
						int projectNames = Main.scan.nextInt();
						Main.scan.nextLine();
						statement.executeUpdate("UPDATE project SET projectName =" + projectNames
								+ " WHERE projectName = '" + projectName + "'");
						break;

					case "3":
						// UPDATE the Building Type:
						System.out.print("Please enter new Building Type:");
						String buildingType = Main.scan.nextLine();
						statement.executeUpdate("UPDATE project SET buildingType = '" + buildingType
								+ "' WHERE projectName = '" + projectName + "'");
						break;

					case "4":
						// UPDATE the Address:
						System.out.print("Please enter new project Address:");
						String address = Main.scan.nextLine();
						statement.executeUpdate("UPDATE project SET address = '" + address + "' WHERE projectName = '"
								+ projectName + "'");
						break;

					case "5":
						// UPDATE the ERF Number:
						System.out.print("Please enter new ERF Number:");
						int erfNumber = Main.scan.nextInt();
						Main.scan.nextLine();
						statement.executeUpdate("UPDATE project SET erfNum = " + erfNumber + " WHERE projectName = '"
								+ projectName + "'");
						break;

					case "6":
						// UPDATE the Total Fee:
						System.out.print("Please enter new Total Fee:");
						double totalFee = Main.scan.nextDouble();
						statement.executeUpdate("UPDATE project SET totalFee = " + totalFee + " WHERE projectName = '"
								+ projectName + "'");
						break;

					case "7":
						// UPDATE the Amount Paid:
						System.out.print("Please enter new Total Amount Paid:");
						double amountPaid = Main.scan.nextDouble();
						statement.executeUpdate("UPDATE project SET amountPaid = " + amountPaid
								+ " WHERE projectName = '" + projectName + "'");
						break;

					case "8":
						// UPDATE the Deadline:
						System.out.print("Please enter the new Deadline date (YYYYMMDD):");
						int deadlineDate = Main.scan.nextInt();
						Main.scan.nextLine();
						statement.executeUpdate("UPDATE project SET deadlineDate = " + deadlineDate
								+ " WHERE projectName = '" + projectName + "'");
						break;

					case "9":
						// UPDATE the completion date:
						System.out.print("Please enter the new CompletionDate date (YYYYMMDD):");
						int completionDate = Main.scan.nextInt();
						Main.scan.nextLine();
						statement.executeUpdate("UPDATE project SET completionDate = " + completionDate
								+ " WHERE projectName = '" + projectName + "'");

						// UPDATE finalized to True if project is completed
						boolean finalised = true;
						statement.executeUpdate("UPDATE project SET finalised = " + finalised + " WHERE projectName = '"
								+ projectName + "'");
						break;

					case "10":
						// UPDATE the architect:
						System.out.print("Please enter new Architect Name (Name & Surname):");
						String architect = Main.scan.nextLine();
			
						// Check if the Name of the Architect exist in the database, if not, add it
						ResultSet architectExist = statement
								.executeQuery("SELECT name FROM architect WHERE name = '" + architect + "'");
						if (architectExist.next() == false) {
							Architect.enterNewArchitect(statement);
						}

						statement.executeUpdate("UPDATE project SET architect = '" + architect
								+ "' WHERE projectName = '" + projectName + "'");
						break;

					case "11":
						// UPDATE the Contractor:
						System.out.print("Please enter new Contractor Name (Name & Surname):");
						String contractor = Main.scan.nextLine();
						// Check if the Name of the contractor exist in the database, if not, add it
						
						ResultSet contractorExist = statement
								.executeQuery("SELECT name FROM contractor WHERE name = '" + contractor + "'");
						if (contractorExist.next() == false) {
							Contractor.enterNewContractor(statement);
						}

						statement.executeUpdate("UPDATE project SET contractor = '" + contractor
								+ "' WHERE projectName = '" + projectName + "'");
						break;

					case "12":
						// UPDATE the Customer:
						System.out.print("Please enter new Customer Name (Name & Surname):");
						String customer = Main.scan.nextLine();

						// Check if the Name of the Customer exist in the Customer database, if not,add it
						ResultSet customerExist = statement
								.executeQuery("SELECT name FROM customer WHERE name = '" + customer + "'");
						if (customerExist.next() == false) {
							Customer.enterNewCustomer(statement);
						}

						statement.executeUpdate("UPDATE project SET customer = '" + customer + "' WHERE projectName = '"
								+ projectName + "'");
						break;

						
					case "13":
						// UPDATE the Engineer:
						System.out.print("Please enter Name of the new project Engineer:");
						String engineer = Main.scan.nextLine();
						statement.executeUpdate("UPDATE project SET engineer = '" + engineer + "' WHERE projectName = '"
								+ projectName + "'");
						break;
						
					case "14":
						// UPDATE the Project Manager:
						System.out.print("Please enter Name of the new Project Manager:");
						String projectManager = Main.scan.nextLine();
						statement.executeUpdate("UPDATE project SET projectManager = '" + projectManager + "' WHERE projectName = '"
								+ projectName + "'");
						break;
						
					case "15":
						// Update Finalized:
						System.out.println("Update Project to Finalised: \n1. YES \n2. NO");
						int finalisedAnswer = Main.scan.nextInt();
						Main.scan.nextLine();

						boolean finalised1;
						if (finalisedAnswer == 1) {
							finalised1 = true;
							statement.executeUpdate("UPDATE project SET finalised = " + finalised1
									+ " WHERE projectName = '" + projectName + "'");
						} else if (finalisedAnswer == 2) {
							finalised1= false;
							statement.executeUpdate("UPDATE project SET finalised = " + finalised1
									+ " WHERE projectName = '" + projectName + "'");
						} else {
							System.out.println("Finalised was not updated");
						}

						break;

					// Default if user entered a wrong character
					default:
						System.out.println("\nProject was not ammended\n");
						break;
					}
				} else {
					System.out.println("\n- Selected project Name was not found  in the Database");
				}
				// STOP THE WHILE LOOP
				continueInput = false;
			} catch (InputMismatchException e) {
				System.out.println("\nIncorrect format input entered, try again.");
				Main.scan.nextLine();
			}
		} while (continueInput);

	}
	// method to update project via projectNumber
	public static void updateProjectNumber(Statement statement) throws SQLException {
		boolean continueInput = true;
		do {
			try {

				// Ask if user knows which Project they want to amend
				System.out.println("Do you know which project you want to update by Project Number? \n1. YES \n2. NO");
				int userKnowsId = Main.scan.nextInt();
				Main.scan.nextLine();

				// If No, launch search method
				if (userKnowsId != 1) {
					ResultSet showAllProject = statement.executeQuery("SELECT * FROM project");
					Main.displayProjectResults(showAllProject);
				}

				// User enters project number
				System.out.print("\nPlease enter Project Number:");
				int projectNumber = Main.scan.nextInt();
				Main.scan.nextLine();

				ResultSet results = statement
						.executeQuery("SELECT * FROM project WHERE projectNum  =" + projectNumber);

				// If project number exits, can make amendments
				if (results.next() == true) {

					ResultSet resultShow = statement
							.executeQuery("SELECT * FROM project WHERE projectNum =" + projectNumber);
					Main.displayProjectResults(resultShow);

					System.out.println(
							"\nWhich item do you want to update? \n1. ProjectNumber \n2. ProjectName \n3. BuildingType \n4. Address \n5. ERF Number \n6. TotalFee \n7. AmountPaid \n8. Deadline \n9. CompletionDate \n10. Architect \n11. Contractor \n12. Customer \n13. Engineer \n14. Project Manager \n15. Finalised \n0. Back to Main Screen");
					String itemUpdate = Main.scan.nextLine();

					// Switch statement according to answer given
					switch (itemUpdate) {

					case "0":
						// Back to Main Screen
						break;
						
					case "1":
						// UPDATE the Project Number:
						System.out.print("Please enter new Project Number:");
						String projectNum = Main.scan.nextLine();
						statement.executeUpdate("UPDATE project SET projectNum = '" + projectNum
								+ "' WHERE projectNum = " + projectNumber);
						break;

					case "2":
						// UPDATE the Project Name:
						System.out.print("Please enter new Project Name:");
						String projectName = Main.scan.nextLine();
						statement.executeUpdate("UPDATE project SET projectName = '" + projectName
								+ "' WHERE projectNum = " + projectNumber);
						break;

					case "3":
						// UPDATE the Building Type:
						System.out.print("Please enter new Building Type:");
						String buildingType = Main.scan.nextLine();
						statement.executeUpdate("UPDATE project SET buildingType = '" + buildingType
								+ "' WHERE projectNum = " + projectNumber);
						break;

					case "4":
						// UPDATE the Address:
						System.out.print("Please enter new project Address:");
						String address = Main.scan.nextLine();
						statement.executeUpdate("UPDATE project SET address = '" + address + "' WHERE projectNum = "
								+ projectNumber);
						break;

					case "5":
						// UPDATE the ERF Number:
						System.out.print("Please enter new ERF Number:");
						int erfNumber = Main.scan.nextInt();
						Main.scan.nextLine();
						statement.executeUpdate("UPDATE project SET erfNum = " + erfNumber
								+ " WHERE projectNum = " + projectNumber);
						break;

					case "6":
						// UPDATE the Total Fee:
						System.out.print("Please enter new Total Fee:");
						double totalFee = Main.scan.nextDouble();
						statement.executeUpdate("UPDATE project SET TotalFee = " + totalFee + " WHERE projectNum = "
								+ projectNumber);
						break;

					case "7":
						// UPDATE the Amount Paid:
						System.out.print("Please enter new Total Amount Paid:");
						double amountPaid = Main.scan.nextDouble();
						statement.executeUpdate("UPDATE project SET AmountPaid = " + amountPaid
								+ " WHERE projectNum = " + projectNumber);
						break;

					case "8":
						// UPDATE the Deadline:
						System.out.print("Please enter the new Deadline date (YYYYMMDD):");
						int deadlineDate = Main.scan.nextInt();
						Main.scan.nextLine();
						statement.executeUpdate("UPDATE project SET deadlineDate = " + deadlineDate
								+ " WHERE projectNum = " + projectNumber);
						break;

					case "9":
						// UPDATE the completion date:
						System.out.print("Please enter the new CompletionDate date (YYYYMMDD):");
						int completionDate = Main.scan.nextInt();
						Main.scan.nextLine();
						statement.executeUpdate("UPDATE project SET completionDate = " + completionDate
								+ " WHERE projectNum = " + projectNumber);

						// UPDATE finalized to True if project is completed
						boolean finalised = true;
						statement.executeUpdate("UPDATE project SET finalised = " + finalised
								+ " WHERE projectNum = " + projectNumber);
						break;

					case "10":
						// UPDATE the architect:
						System.out.print("Please enter new Architect Name (Name & Surname):");
						String architect = Main.scan.nextLine();

						// Check if the Name of the Architect exist in the Architect database, if not,
						// capture into database
						ResultSet architectExist = statement
								.executeQuery("SELECT name FROM architect WHERE name = '" + architect + "'");
						if (architectExist.next() == false) {
							Architect.enterNewArchitect(statement);
						}

						statement.executeUpdate("UPDATE project SET architect = '" + architect
								+ "' WHERE projectNum = " + projectNumber);
						break;

					case "11":
						// UPDATE the Contractor:
						System.out.print("Please enter new Contractor Name (Name & Surname):");
						String contractor = Main.scan.nextLine();

						// Check if the name of the Contractor exist in the Contractor database, if not, add to database
						ResultSet contractorExist = statement
								.executeQuery("SELECT name FROM contractor WHERE name = '" + contractor + "'");
						if (contractorExist.next() == false) {
							Contractor.enterNewContractor(statement);
						}

						statement.executeUpdate("UPDATE project SET contractor = '" + contractor
								+ "' WHERE projectNum = " + projectNumber);
						break;

					case "12":
						// UPDATE the Customer:
						System.out.print("Please enter new Customer Name (Name & Surname):");
						String customer = Main.scan.nextLine();

						// Check if the Name of the Customer exist in the Customer database, if not, add it
						ResultSet customerExist = statement
								.executeQuery("SELECT name FROM customer WHERE name = '" + customer + "'");
						if (customerExist.next() == false) {
							Customer.enterNewCustomer(statement);
						}

						statement.executeUpdate("UPDATE project SET customer = '" + customer
								+ "' WHERE projectNum = " + projectNumber);
						break;
						
					case "13":
						// UPDATE the Engineer:
						System.out.print("Please enter new Engineer:");
						String engineer = Main.scan.nextLine();
						statement.executeUpdate("UPDATE project SET engineer = '" + engineer + "' WHERE projectNum = "
								+ projectNumber);
						break;
						
					case "14":
						// UPDATE the Address:
						System.out.print("Please enter new Project Manager:");
						String projectManager = Main.scan.nextLine();
						statement.executeUpdate("UPDATE project SET projectManager = '" + projectManager + "' WHERE projectNum = "
								+ projectNumber);
						break;

					case "15":
						// Update Finalized:
						System.out.println("Update Project to Finalised: \n1. YES \n2. NO");
						int finalisedAnswer = Main.scan.nextInt();
						Main.scan.nextLine();

						// Based on answer, update finalized for the project
						boolean finased;
						if (finalisedAnswer == 1) {
							finased = true;
							statement.executeUpdate("UPDATE project SET finalised = " + finased
									+ " WHERE projectNum = " + projectNumber);
						} else if (finalisedAnswer == 2) {
							finased = false;
							statement.executeUpdate("UPDATE project SET finalised = " + finased
									+ " WHERE projectNum = " + projectNumber);
						} else {
							System.out.println("Project was not finalised");
						}
						break;

					// if user entered an invalid character
					default:
						System.out.println("\n Project was not ammended\n");
						break;
					}
				} else {
					System.out.println("\n- Project number was not found in the Database");
				}
				// END OF WHILE LOOP
				continueInput = false;
			} catch (InputMismatchException e) {
				System.out.println("\nIncorrect format input entered, try again.");
				Main.scan.nextLine();
			}
		} while (continueInput);

	}
}
