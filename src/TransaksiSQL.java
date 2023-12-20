import java.sql.SQLException;
import java.sql.Date;
import java.sql.Timestamp;

public interface TransaksiSQL {
    void insertTransaksi(int akunID, String jenisTransaksi, Timestamp tanggalTransaksi) throws SQLException;

    // nnt tambahi view transaksi

}
