import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class test
{
    public test(){
        Scanner sc = new Scanner(System.in);
        boolean flag = false;
        Date date = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);

        while(!flag){
            System.out.println("Masukkan tanggal lahir [dd/mm/yyyy]: ");
            String input = sc.nextLine();
            try {
                date = dateFormat.parse(input);
                flag = true;
            } catch (ParseException e) {
                //
            }
        }
        String DOB = dateFormat.format(date);
        Date dob = null;
        try {
            dob = new SimpleDateFormat("dd/MM/yyyy").parse(DOB);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        System.out.println(dob + " " + DOB);
    }
    public static void main(String[] args){
        new test();
    }

}
