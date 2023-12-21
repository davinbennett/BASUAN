import java.text.ParseException;

public class Withdraw extends Transaksi{
    public Withdraw(String namaRefresh, String kodeRefresh, double jumlah) {
        super(namaRefresh, kodeRefresh, jumlah);
    }

    @Override
    public void notif() throws ParseException {
        System.out.println("\nTransfer BERHASIL.\n" + now() + "\nKlik Enter untuk kembali ke Menu Transaksi");
    }
}
