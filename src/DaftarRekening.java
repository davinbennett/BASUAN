import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DaftarRekening implements DaftarRekeningSQL{
    private final Connect connect = Connect.getInstance();
    public DaftarRekening(String rekening) {
        this.rekening = rekening;
    }

    private String rekening;

    public String getRekening() {
        return rekening;
    }

    public void setRekening(String rekening) {
        this.rekening = rekening;
    }

    @Override
    public void insertDaftarRekening(String rekening) throws SQLException {
        String query = "INSERT INTO DaftarRekening(Rekening) " +
                "VALUES(?)";
        PreparedStatement ps = connect.preparedStatement(query);
        try {
            ps.setString(1, rekening);

            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
