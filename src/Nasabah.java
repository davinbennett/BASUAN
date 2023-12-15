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
    public void updateCustomer(String nama, String alamat, Date dob) throws SQLException {

    }


}
