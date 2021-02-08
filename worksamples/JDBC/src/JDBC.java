
//STEP 1. Import required packages
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.Calendar;
import java.util.Scanner;

public class JDBC {
    // JDBC driver name and database URL

    static final String DB_URL = "jdbc:mysql://localhost:3306";

    // Database credentials
    static String USER = "root";
    static String PASS = "";
    static String table;
    static String db;

    // static final String DB = "c:\\xampp\\mysql\\data\\test.db";
    // static Connection conn2 = null;

    public static void main(String[] args)
            throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

        args = new String[] { "CLIDB", "JRTable" };
        db = args[0];
        table = args[1];

        try {
            System.out.println("db: " + db + "\ntaulu: " + table);
            String sqlDB = "CREATE DATABASE IF NOT EXISTS " + db;           
            
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // Closing connection and opening new one after connection with database is ok.
            // conn.close();

            PreparedStatement createDbStat = conn.prepareStatement(sqlDB);
            createDbStat.execute();
                    
            Connection conn2 = DriverManager.getConnection(DB_URL + "/" + db, USER, PASS);

            /*Works, but new user cannot be created if there is no database yet..*/
            Statement stUser = conn2.createStatement();
            stUser.execute("CREATE USER IF NOT EXISTS 'hemmo'@'localhost' IDENTIFIED BY 'hemmoPass'");
            Statement stPass = conn2.createStatement();            
            String grant = "GRANT ALL PRIVILEGES ON " + db + " TO " + "hemmo@localhost";            
            stPass.execute(grant);

            
            String sqlCreateTable = "CREATE TABLE IF NOT EXISTS " + table
                    + "(ID int unsigned auto_increment not null PRIMARY KEY, " + " Nimi VARCHAR(100), "
                    + " date_created timestamp default now())";
            PreparedStatement stmtTable = conn2.prepareStatement(sqlCreateTable);
            stmtTable.executeUpdate(sqlCreateTable);
            
            //Todo: Create ui - interface where one could select what to do.
            
            Ui userInterface = new Ui();
            userInterface.start(table, conn2);                        
            
            System.out.println("Goodbye");
            conn2.close();
            
        } catch (SQLException s) {
            s.printStackTrace();
        }

        // }
    }
}
