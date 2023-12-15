import java.sql.*;

public class Connect {
    private final String USERNAME = "root";
    private final String PASSWORD = "qwerty12";
    private final String DATABASE = "basuan";
    private final String HOST = "localhost:3306";
    private final String CONNECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);

    public ResultSet rs;
    public ResultSetMetaData rsm;

    private Connection con;
    private Statement st;
    private static Connect connect;

    public static Connect getInstance(){
        if(connect == null)
            return new Connect();
        return connect;
    }

    private Connect(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
            st = con.createStatement();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet execQuery(String query){
        try {
            rs = st.executeQuery(query);
            rsm = rs.getMetaData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rs;
    }

    public void execUpdate(String query){
        try {
            st.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PreparedStatement preparedStatement(String query) throws SQLException {
        PreparedStatement ps = null;
        ps = con.prepareStatement(query);
        return ps;
    }
}
