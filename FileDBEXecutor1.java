package Loke;

import java.io.*;

import java.sql.*;

import java.util.Scanner;



public class FileDBExecutor1 {
	public static void main(String[] args) {
		String[] menuOptions = {
				"1. Create Tables (TABLE.txt)",

				"2. Insert into DEPT (DEPT.txt)",

				"3. Insert into EMP (EMP.txt)",

				"4. Insert into SALGRADE (SALGRADE.txt)"
		};
		String[] filePaths = {
				"D:\\java\\dept1.txt",

				"D:\\java\\DEPT.txt",

				"D:\\java\\EMP.txt",

				"D:\\java\\SALGRADE.txt"
				};
		String url = "jdbc:mysql://localhost:3306/testdb";

        String user = "root";

        String password = "loki@6699";



        // Display menu

        Scanner scanner = new Scanner(System.in);

        System.out.println("Select the SQL file to execute:");

        for (String option : menuOptions) {

            System.out.println(option);

        }

        System.out.print("Enter your choice (1-4): ");

        int choice = scanner.nextInt();



        if (choice < 1 || choice > 4) {

            System.out.println("Invalid choice. Exiting...");

            return;

        }



        String selectedFile = filePaths[choice - 1];



        try {

            // Load MySQL JDBC Driver

            Class.forName("com.mysql.cj.jdbc.Driver");



            try (

                Connection conn = DriverManager.getConnection(url, user, password);

                Statement stmt = conn.createStatement();

                BufferedReader reader = new BufferedReader(new FileReader(selectedFile))

            ) {

                System.out.println("Connected to database.");

                System.out.println("Executing SQL from: " + selectedFile + "\n");



                StringBuilder queryBuilder = new StringBuilder();

                String line;



                while ((line = reader.readLine()) != null) {

                    line = line.trim();

                    if (line.isEmpty()) continue;



                    queryBuilder.append(line).append(" ");



                    if (line.endsWith(";")) {

                        String query = queryBuilder.toString().trim();

                        query = query.substring(0, query.length() - 1); // Remove trailing semicolon



                        System.out.println("Query: " + query);



                        try {

                            if (query.toLowerCase().startsWith("select")) {

                                ResultSet rs = stmt.executeQuery(query);

                                ResultSetMetaData rsmd = rs.getMetaData();

                                int columnCount = rsmd.getColumnCount();



                                while (rs.next()) {

                                    for (int i = 1; i <= columnCount; i++) {

                                        System.out.print(rs.getString(i) + "\t");

                                    }

                                    System.out.println();

                                }

                                rs.close();

                            } else {

                                int rowsAffected = stmt.executeUpdate(query);

                                System.out.println("Query OK, " + rowsAffected + " row(s) affected.");

                            }

                        } catch (SQLException e) {

                            System.err.println("SQL Error: " + e.getMessage());

                        }



                        System.out.println("--------------------------------------------------");

                        queryBuilder.setLength(0);

                    }

                }



            }

        } catch (ClassNotFoundException e) {

            System.err.println("MySQL JDBC Driver not found.");

        } catch (SQLException e) {

            System.err.println("Database connection error: " + e.getMessage());

        } catch (IOException e) {

            System.err.println("File read error: " + e.getMessage());

        }

    }

}