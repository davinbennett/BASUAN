import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;

public class Transaksi implements TransaksiSQL{
    private final Connect connect = Connect.getInstance();
    private String jenisTransaksi;
    private Double jumlah;
    private Date TanggalTransaksi;

    public Transaksi(String jenisTransaksi, Double jumlah, Date tanggalTransaksi) {
        this.jenisTransaksi = jenisTransaksi;
        this.jumlah = jumlah;
        TanggalTransaksi = tanggalTransaksi;
    }

    public String getJenisTransaksi() {
        return jenisTransaksi;
    }

    public void setJenisTransaksi(String jenisTransaksi) {
        this.jenisTransaksi = jenisTransaksi;
    }

    public Double getJumlah() {
        return jumlah;
    }

    public void setJumlah(Double jumlah) {
        this.jumlah = jumlah;
    }

    public Date getTanggalTransaksi() {
        return TanggalTransaksi;
    }

    public void setTanggalTransaksi(Date tanggalTransaksi) {
        TanggalTransaksi = tanggalTransaksi;
    }

    @Override
    public void insertTransaksi(String jenisTransaksi, Double jumlah, Date tanggalTransaksi) throws SQLException {
        String query = "INSERT INTO Transaksi(JenisTransaksi, Jumlah, TanggalTransaksi) " +
                "VALUES(?, ?, ?)";
        PreparedStatement ps = connect.preparedStatement(query);
        try {
            ps.setString(1, jenisTransaksi);
            ps.setDouble(2, jumlah);
            ps.setDate(3, tanggalTransaksi);

            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
