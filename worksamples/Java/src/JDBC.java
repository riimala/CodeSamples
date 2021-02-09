
//STEP 1. Import required packages
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.Calendar;
import java.util.Scanner;

public class JDBC {
    // JDBC driver name and database URL
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, SQLException {
    	ConnectionDB c = new ConnectionDB();
    	Connection conn = c.Initialize();
    }
}
