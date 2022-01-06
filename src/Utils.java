import java.util.Scanner;

public class Utils {

    Scanner scanner ;

    public Utils(){
        this.scanner = new Scanner(System.in);
    }

    public int getInput(){
        try{
            return Integer.parseInt(scanner.nextLine());

        }catch (Exception e){
            return 0;
        }
    }

}
