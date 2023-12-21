import java.sql.SQLException;

public interface DaftarRekeningSQL {
    void insertDaftarRekening(int akunID, String namaRek, String listRek) throws SQLException;
    void updateDaftarRekening(String namaNew, String namaRefresh, String kodeRefresh) throws SQLException;
}
