import java.sql.SQLException;

public interface AkunSQL {
    void insertAkun(String nasabahID, String rekening, String kodeAkses) throws SQLException;
    void updateKode(String kodeNew, String kodeOld, String namaOld) throws SQLException;
}
