import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Transfer extends Transaksi{
    public Transfer(String namaRefresh, String kodeRefresh, double jumlah) {
        super(namaRefresh, kodeRefresh, jumlah);
    }



    @Override
    public void notif() throws ParseException {
        System.out.println("\nTransfer BERHASIL.\n" + now() + "\nKlik Enter untuk kembali ke Menu Transaksi");
    }
}
