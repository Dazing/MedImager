import java.sql.Connection;
import static spark.Spark.*;
import Users.*;

public class Router {

    public Router (Connection connection) {
        get("/", (req, res) -> {
            return "Hello";
        });

        get("/login", (req, res) -> {
            return "GET Login";
        });

        post("/login", (req, res) -> {
            return "POST Login";
        });

        get("/search", (req, res) -> {
            return "GET Search";
        });

        post("/search", (req, res) -> {
            return "POST Search";
        });


        get("/", (req, res) -> {
            return "Hello";
        });

        get("/", (req, res) -> {
            return "Hello";
        });

    }
}
