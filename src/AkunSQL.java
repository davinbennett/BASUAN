import java.sql.SQLException;

public interface AkunSQL {
    void insertAkun(String nasabahID, String rekening, String kodeAkses, String password) throws SQLException;
    void updateKode(String kodeNew, String kodeRefresh, String namaRefresh) throws SQLException;
    void updateSaldoIn(double jumlah, String kodeRefresh, String namaRefresh) throws SQLException;
    void updateSaldoOut(double jumlah, String kodeRefresh, String namaRefresh) throws SQLException;


}
