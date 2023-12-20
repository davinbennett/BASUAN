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
}
