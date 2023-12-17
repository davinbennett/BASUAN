import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;


public class Nasabah implements CustomerSQL{
    private final Connect connect = Connect.getInstance();
    private String nama;
    private String alamat;
    private Date dob;

    public Nasabah(String nama, String alamat, Date dob) {
        this.nama = nama;
        this.alamat = alamat;
        this.dob = dob;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    @Override
    public void insertCustomer(String nama, String alamat, Date dob) throws SQLException {
        String query = "INSERT INTO Nasabah(Nama, Alamat, DOB) " +
                        "VALUES(?, ?, ?)";
        PreparedStatement ps = connect.preparedStatement(query);
        try {
            ps.setString(1, nama);
            ps.setString(2, alamat);
            ps.setDate(3, dob);

            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void updateNama(String namaNew, String namaOld, String kode) throws SQLException {
        String query = "UPDATE Nasabah AS N JOIN Akun AS A ON N.NasabahID = A.NasabahID " +
                "SET Nama = ? " +
                "WHERE N.Nama = ? AND A.KodeAkses = ?";
        PreparedStatement ps = connect.preparedStatement(query);
        try {
            ps.setString(1, namaNew);
            ps.setString(2, namaOld);
            ps.setString(3, kode);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ps.executeUpdate();
    }

    @Override
    public void updateAlamat(String alamatNew, String namaOld, String kode) throws SQLException {
        String query = "UPDATE Nasabah AS N JOIN Akun AS A ON N.NasabahID = A.NasabahID " +
                "SET Alamat = ? " +
                "WHERE Nama = ? AND KodeAkses = ?";
        PreparedStatement ps = connect.preparedStatement(query);
        try {
            ps.setString(1, alamatNew);
            ps.setString(2, namaOld);
            ps.setString(3, kode);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ps.executeUpdate();
    }

    @Override
    public void updateDOB(Date dobNew, String namaOld, String kode) throws SQLException {
        String query = "UPDATE Nasabah AS N JOIN Akun AS A ON N.NasabahID = A.NasabahID " +
                "SET DOB = ? " +
                "WHERE Nama = ? AND KodeAkses = ?";
        PreparedStatement ps = connect.preparedStatement(query);
        try {
            ps.setDate(1, dobNew);
            ps.setString(2, namaOld);
            ps.setString(3, kode);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ps.executeUpdate();
    }


}
