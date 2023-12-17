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
    public void insertAkun(String nasabahID, String rekening, String kodeAkses) throws SQLException {
        String query = "INSERT INTO Akun(NasabahID, Rekening, KodeAkses) " +
                "VALUES(?, ?, ?)";
        PreparedStatement ps = connect.preparedStatement(query);
        try {
            ps.setString(1, nasabahID);
            ps.setString(2, rekening);
            ps.setString(3, kodeAkses);

            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateKode(String kodeNew, String kodeOld, String namaOld) throws SQLException {
        String query = "UPDATE Nasabah AS N JOIN Akun AS A ON N.NasabahID = A.NasabahID " +
                "SET KodeAkses = ? " +
                "WHERE Nama = ? AND KodeAkses = ?";
        PreparedStatement ps = connect.preparedStatement(query);

        try {
            ps.setString(1, kodeNew);
            ps.setString(2, namaOld);
            ps.setString(3, kodeOld);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ps.executeUpdate();
    }


}
