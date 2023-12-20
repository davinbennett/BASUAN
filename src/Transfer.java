public class Transfer extends Transaksi{
    public Transfer(String namaRefresh, String kodeRefresh, double jumlah) {
        super(namaRefresh, kodeRefresh, jumlah);
    }

    @Override
    public void notif() {
        System.out.println("tf berhasil");
    }
}
