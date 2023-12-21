import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Akun implements AkunSQL{
    private final Connect connect = Connect.getInstance();
    private String rekening;
    private Double saldo;
    private String kodeAkses;

    public Akun() {

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
    public void insertAkun(int nasabahID, String rekening, String kodeAkses, String password) throws SQLException {
        String query = "INSERT INTO Akun(NasabahID, Rekening, KodeAkses, Password) " +
                "VALUES(?, ?, ?, ?)";
        PreparedStatement ps = connect.preparedStatement(query);
        try {
            ps.setInt(1, nasabahID);
            ps.setString(2, rekening);
            ps.setString(3, kodeAkses);
            ps.setString(4, password);

            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateKode(String kodeNew, String kodeRefresh, String namaRefresh) throws SQLException {
        String query = "UPDATE Nasabah AS N JOIN Akun AS A ON N.NasabahID = A.NasabahID " +
                "SET KodeAkses = ? " +
                "WHERE Nama = ? AND KodeAkses = ?";
        PreparedStatement ps = connect.preparedStatement(query);

        try {
            ps.setString(1, kodeNew);
            ps.setString(2, namaRefresh);
            ps.setString(3, kodeRefresh);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ps.executeUpdate();
    }

    @Override
    public void updateSaldoIn(double jumlah, String kodeRefresh, String namaRefresh) throws SQLException {
        String query = "UPDATE Akun AS a " +
                "JOIN Nasabah AS n ON a.NasabahID = n.NasabahID " +
                "SET Saldo = Saldo + ? " +
                "WHERE Nama = ? AND KodeAkses = ?";
        PreparedStatement ps = connect.preparedStatement(query);

        try {
            ps.setDouble(1, jumlah);
            ps.setString(2, namaRefresh);
            ps.setString(3, kodeRefresh);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ps.executeUpdate();
    }

    @Override
    public void updateSaldoOut(double jumlah, String kodeRefresh, String namaRefresh) throws SQLException {
        String query = "UPDATE Akun AS a " +
                "JOIN Nasabah AS N ON A.NasabahID = N.NasabahID " +
                "SET Saldo = Saldo - ? " +
                "WHERE Nama = ? AND KodeAkses = ?";
        PreparedStatement ps = connect.preparedStatement(query);

        try {
            ps.setDouble(1, jumlah);
            ps.setString(2, namaRefresh);
            ps.setString(3, kodeRefresh);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ps.executeUpdate();
    }

    @Override
    public void updateSaldoInVersi2(double jumlah, String namaPenerima) throws SQLException {
        String query = "UPDATE Akun AS A JOIN Nasabah AS N ON A.NasabahID = N.NasabahID " +
                "SET Saldo = Saldo + ? " +
                "WHERE Nama = ?";
        PreparedStatement ps = connect.preparedStatement(query);

        try {
            ps.setDouble(1, jumlah);
            ps.setString(2, namaPenerima);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ps.executeUpdate();
    }
}
