import java.io.IOException;
import java.util.Scanner;



import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        Database db = new Database();
        Router router = new Router(db.getConnection());
        try {
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
        } catch (Exception e) {

        }

    }
}