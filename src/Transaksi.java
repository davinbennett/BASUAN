import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Timestamp;

public class Transaksi implements TransaksiSQL{

    private String namaRefresh;
    private String jenisTransaksi;
    private double jumlah;
    private Timestamp TanggalTransaksi;
    private double saldo;
    private int akunID;
    private String kodeRefresh;
    private final Connect connect = Connect.getInstance();

    public Transaksi() {

    }

    @Override
    public void insertTransaksi(int akunID, String jenisTransaksi, Timestamp tanggalTransaksi) throws SQLException {
        String query = "INSERT INTO Transaksi(AkunID, JenisTransaksi, Jumlah, TanggalTransaksi) " +
                "VALUES(?, ?, ?, ?)";
        PreparedStatement ps = connect.preparedStatement(query);
        try {
            ps.setInt(1, akunID);
            ps.setString(2, jenisTransaksi);
            ps.setDouble(3, this.jumlah);
            ps.setTimestamp(4, tanggalTransaksi);

            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Transaksi(String namaRefresh, String kodeRefresh, double jumlah) {
        super();
        this.namaRefresh = namaRefresh;
        this.kodeRefresh = kodeRefresh;
        this.jumlah = jumlah;
    }

    public void notif() {

    }

}
