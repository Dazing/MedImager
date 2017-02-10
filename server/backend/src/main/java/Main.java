import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        Database db = new Database();
        get("/hello", (req, res) -> "Hello World");
    }
}