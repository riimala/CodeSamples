import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Records {

	private String table;
	private Connection conn2;
	private ResultSet rs;
	private String data;

	public Records() {
		this.rs = null;
		this.data = null;
	}

	public int Count(String table, Connection conn2) {
		int numberOfRows = 0;
		String query = "SELECT COUNT(*) FROM " + table;
		// create the java statement
		try {

			PreparedStatement st = conn2.prepareStatement(query);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				numberOfRows = rs.getInt("count(*)");
			}
			System.out.println("There are " + numberOfRows + " records in " + table);
			return numberOfRows;
		} catch (Exception e) {
			System.out.println("Check XAMPP mysql module");
			e.printStackTrace();
		}
		return 0;
	}

	public void DropTable(String t, Connection conn2) throws SQLException {

		try {
			System.out.println("Table value is " + t);
			String delSql = "DROP TABLE " + t;
			System.out.println(delSql);
			Statement delDB = conn2.createStatement();
			delDB.execute(delSql);
		} catch (Exception e) {
			System.out.println("Cannot destroy table, check mysql module");
			e.printStackTrace();
		}

	}

	// Insert record
	public void CreateRecord(String table, Connection conn2, String userString) throws SQLException {
		try {

			UiAdder add = new UiAdder();
			String uiRowToBeAdded = userString;
			System.out.println("Row content from UiAdder, " + userString);

			// If nothing is coming from Adder then take data from textual interface
			if (uiRowToBeAdded.equals(null) || uiRowToBeAdded.equals("")) {
				String ret = "";
				String toBeStored = "";
				Scanner scan = new Scanner(System.in);
				System.out.println("Data: (close with quit)");
				while (scan.hasNextLine()) {
					ret = scan.nextLine();

					if (ret.equals("quit")) {
						break;
					}
					toBeStored += "\n" + ret;
				}

				System.out.println(toBeStored);

				Calendar calendar = Calendar.getInstance();
				java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());
				String query = " insert into " + table + " (Taski, date_created)" + " VALUES (?, ?)";
				PreparedStatement preparedStmt = conn2.prepareStatement(query);
				// preparedStmt.setInt(1, 1);
				preparedStmt.setString(1, toBeStored);
				preparedStmt.setDate(2, startDate);

				// execute the preparedstatement
				preparedStmt.execute();
				System.out.println("Record inserted");
			}

			else {
				Calendar calendar = Calendar.getInstance();
				java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());
				String query = " insert into " + table + " (Taski, date_created)" + " VALUES (?, ?)";
				PreparedStatement preparedStmt = conn2.prepareStatement(query);
				// preparedStmt.setInt(1, 1);
				preparedStmt.setString(1, uiRowToBeAdded);
				preparedStmt.setDate(2, startDate);

				preparedStmt.execute();
				System.out.println("Record inserted");
			}
		} catch (Exception e) {
			System.out.println("Check XAMPP mysql module");
			e.printStackTrace();
		}
	}

	// Luetaan recordit, connection vielä auki joten ei tarvinne luoda uutta.
	public ResultSet ReadRecords(String table, Connection conn2) {
		String query = "SELECT ID, Taski, date_created FROM " + table;
		// create the java statement
		try {
			Statement st = conn2.createStatement();
			// execute the query, and get a java resultset
			rs = st.executeQuery(query);

			// iterate through the java resultset
			while (rs.next()) {
				int id2 = rs.getInt("ID");
				String Taski2 = rs.getString("Taski");
				Date dateCreated2 = rs.getDate("date_created");

				// print the results
				System.out.format("%s, %s, %s\n", id2, Taski2, dateCreated2);
			}

			return rs;

		} catch (SQLException e) {
			System.out.println("Check XAMPP mysql module");
			e.printStackTrace();
		}
		return null;
	}

	public ResultSet GetResultSet() {
		return this.rs;
	}

	public void DeleteRecords(String table, Connection conn2, int row) {
		Scanner scan = new Scanner(System.in);
		ReadRecords(table, conn2);

		System.out.println("Record to be deleted:");
		int toBeDeleted = row;

		String query = "DELETE FROM " + table + " Where ID = ?";
		try {
			PreparedStatement preparedStmt = conn2.prepareStatement(query);
			preparedStmt.setInt(1, toBeDeleted);

			// execute the preparedstatement
			preparedStmt.execute();

			// Näytä recordit
			ReadRecords(table, conn2);

		} catch (SQLException e) {
			System.out.println("Check XAMPP mysql module");
			e.printStackTrace();
		}

	}
}
