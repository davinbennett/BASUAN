import java.sql.SQLException;

public interface AkunSQL {
    void insertAkun(String rekening, String kodeAkses) throws SQLException;
    void updateAkun(Double Saldo, String KodeAkses) throws SQLException;
}
