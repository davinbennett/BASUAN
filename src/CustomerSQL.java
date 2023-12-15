import java.sql.SQLException;
import java.sql.Date;

public interface CustomerSQL {
    void insertCustomer(String nama, String alamat, Date dob) throws SQLException;
    void updateCustomer(String nama, String alamat, Date dob) throws SQLException;
}
