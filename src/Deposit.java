public class Deposit extends Transaksi{
    public Deposit(String namaRefresh, String kodeRefresh, double jumlah) {
        super(namaRefresh, kodeRefresh, jumlah);
    }

    @Override
    public void notif() {
        System.out.println("\nDeposit telah berhasil dilakukan.\nKlik Enter untuk kembali ke Menu Transaksi");

    }
}
