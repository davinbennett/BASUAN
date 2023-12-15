import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Akun implements AkunSQL{
    private final Connect connect = Connect.getInstance();
    private String rekening;
    private Double saldo;
    private String kodeAkses;

    public Akun(String rekening, Double saldo, String kodeAkses) {
        this.rekening = rekening;
        this.saldo = saldo;
        this.kodeAkses = kodeAkses;
    }

    public String getRekening() {
        return rekening;
    }

    public void setRekening(String rekening) {
        this.rekening = rekening;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public String getKodeAkses() {
        return kodeAkses;
    }

    public void setKodeAkses(String kodeAkses) {
        this.kodeAkses = kodeAkses;
    }

    @Override
    public void insertAkun(String rekening, String kodeAkses) throws SQLException {
        String query = "INSERT INTO Akun(Rekening, KodeAkses) " +
                "VALUES(?, ?)";
        PreparedStatement ps = connect.preparedStatement(query);
        try {
            ps.setString(1, rekening);
            ps.setString(2, kodeAkses);

            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateAkun(Double Saldo, String KodeAkses) throws SQLException {

    }
}
