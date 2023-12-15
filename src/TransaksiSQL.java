import java.sql.SQLException;
import java.sql.Date;

public interface TransaksiSQL {
    void insertTransaksi(String jenisTransaksi, Double jumlah, Date tanggalTransaksi) throws SQLException;
}
