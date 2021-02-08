import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;


public class Records {

	public void Count(String table, Connection conn2) {
		int numberOfRows = 0;
        String query = "SELECT ID, Nimi, date_created FROM " + table;
     // create the java statement
        try {
			Statement st = conn2.createStatement();
			 // execute the query, and get a java resultset
	        ResultSet rs = st.executeQuery(query);
	        while(rs.next()) {
	        	numberOfRows = rs.getInt("ID");
	        }
	        System.out.println("There are " + numberOfRows +  " records in " + table);
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
	
	public void DropDB(String db, Connection conn2) throws SQLException {		
		Scanner scan = new Scanner(System.in);
		String delSql = "DROP DATABASE IF EXISTS" + db;
		System.out.println(delSql);
		System.out.println("WARNING !!!! You are about to destroy " + db + "database with all data,\ndo you really want to do this (yes)? ");
		String ret = scan.nextLine();
		if (ret.equals("yes") || ret.equals("Yes")) {
			Statement delDB = conn2.createStatement();
			delDB.execute(delSql);
		}
	}
	
    // Insert record
    public void CreateRecord(String table, Connection conn2) throws SQLException {
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
        String query = " insert into " + table + " (Nimi, date_created)" + " VALUES (?, ?)";
        PreparedStatement preparedStmt = conn2.prepareStatement(query);
        // preparedStmt.setInt(1, 1);
        preparedStmt.setString(1, toBeStored);
        preparedStmt.setDate(2, startDate);

        // execute the preparedstatement
        preparedStmt.execute();
        System.out.println("Record inserted");
    	//scan.close();
    }


    //Luetaan recordit, connection vielä auki joten ei tarvinne luoda uutta.
    public void ReadRecords(String table, Connection conn2 ) {
    	// our SQL SELECT query. 
        // if you only need a few columns, specify them by name instead of using "*"
        String query = "SELECT ID, Nimi, date_created FROM " + table;
     // create the java statement
        try {
			Statement st = conn2.createStatement();
			 // execute the query, and get a java resultset
	        ResultSet rs = st.executeQuery(query);
	        
	        // iterate through the java resultset
	        while (rs.next())
	        {
	          int id = rs.getInt("ID");
	          String nimi = rs.getString("Nimi");
	          Date dateCreated = rs.getDate("date_created");          
	          
	          // print the results
	          System.out.format("%s, %s, %s\n", id, nimi, dateCreated);
	        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void DeleteRecords(String table, Connection conn2) {
    	Scanner scan = new Scanner(System.in);
    	//Näytä recordit
    	ReadRecords(table, conn2);
   	
    	System.out.println("Record to be deleted:");
    	int toBeDeleted = scan.nextInt();    	
    	
        String query = "DELETE FROM " + table + " Where ID = ?";
        try {
        	PreparedStatement preparedStmt = conn2.prepareStatement(query);
            preparedStmt.setInt(1, toBeDeleted);

            // execute the preparedstatement
            preparedStmt.execute();

            //Näytä recordit
        	ReadRecords(table, conn2);
	        
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}
