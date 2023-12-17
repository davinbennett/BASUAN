import java.sql.SQLException;
import java.sql.Date;

public interface CustomerSQL {
    void insertCustomer(String nama, String alamat, Date dob) throws SQLException;
    void updateNama(String namaNew, String namaOld, String kode) throws SQLException;
    void updateAlamat(String alamatNew, String namaOld, String kode) throws SQLException;
    void updateDOB(Date dobNew, String namaOld, String kode) throws SQLException;
}
