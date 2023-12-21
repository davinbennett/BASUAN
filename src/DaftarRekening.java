import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DaftarRekening implements DaftarRekeningSQL{
    private final Connect connect = Connect.getInstance();

    @Override
    public void insertDaftarRekening(int akunID, String namaRek, String listRek) throws SQLException {
        String query = "INSERT INTO DaftarRekening(AkunID, NamaRek, ListRek) " +
                "VALUES(?, ?, ?)";
        PreparedStatement ps = connect.preparedStatement(query);
        try {
            ps.setInt(1, akunID);
            ps.setString(2, namaRek);
            ps.setString(3, listRek);

            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateDaftarRekening(String namaNew, String namaRefresh, String kodeRefresh) throws SQLException {
        String query = "UPDATE DaftarRekening " +
                "SET NamaRek = ? " +
                "WHERE NamaRek IN ( " +
                "   SELECT Nama " +
                "   FROM Nasabah AS N JOIN Akun AS A ON N.NasabahID = A.NasabahID " +
                "   WHERE Nama = ? AND KodeAkses = ? " +
                ")";
        PreparedStatement ps = connect.preparedStatement(query);
        try {
            ps.setString(1, namaNew);
            ps.setString(2, namaRefresh);
            ps.setString(3, kodeRefresh);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ps.executeUpdate();
    }
}
