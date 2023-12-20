public class Withdraw extends Transaksi{
    public Withdraw(String namaRefresh, String kodeRefresh, double jumlah) {
        super(namaRefresh, kodeRefresh, jumlah);
    }

    @Override
    public void notif() {
        System.out.println("Withdraw telah berhasil dilakukan.\nKlik Enter untuk kembali ke Menu Transaksi");
    }
}
