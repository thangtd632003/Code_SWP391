package context;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBContext_Long {
    /*USE BELOW METHOD FOR YOUR DATABASE CONNECTION FOR BOTH SINGLE AND MULTILPE SQL SERVER INSTANCE(s)*/
    /*DO NOT EDIT THE BELOW METHOD, YOU MUST USE ONLY THIS ONE FOR YOUR DATABASE CONNECTION*/
    public Connection getConnection() throws Exception {
        String url = "jdbc:mysql://" + serverName + ":" + portNumber + "/" + dbName + "?useSSL=false&serverTimezone=UTC";
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, userID, password);
    }

    /*Insert your other code right after this comment*/

    /*Change/update information of your database connection, DO NOT change name of instance variables in this class*/
    private final String serverName = "localhost";
    private final String dbName = "smiledev";
    private final String portNumber = "3306"; // MySQL default port
    private final String userID = "root";     // Thay đổi nếu user khác
    private final String password = "admin"; // Thay đổi password cho phù hợp
    public static void main(String[] args) {
        try {
            Connection conn = new DBContext_Long().getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("Kết nối CSDL thành công!");
            } else {
                System.out.println("Kết nối CSDL thất bại!");
            }
        } catch (Exception e) {
            System.out.println("Kết nối CSDL thất bại!");
            e.printStackTrace();
        }
    }
}