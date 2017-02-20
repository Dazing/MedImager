package Users;

import com.sun.org.apache.xpath.internal.operations.String;

import java.sql.*;
import java.util.*;

/**
 * Created by Dazing on 2017-02-20.
 */
public class UserHandler {

    private Connection database;

    public UserHandler(Connection connection) {
        database = connection;
    }

    public User getUserById (int id) {
        String query = "SELECT * FROM users WHERE id = "+id.toString();

        try {
            // create the java statement
            Statement st = database.createStatement();

            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);

            int id = rs.getInt("id");
            String username = rs.getString("usernamer");
            String email = rs.getString("email");
            String firstname = rs.getString("first_name");
            String lastname = rs.getString("last_name");
            String userPermission = rs.getString("user_permission");

            return new User(id, username, email, firstname, lastname, userPermission);
        }
        catch (SQLException e) {
            return null;
        }
    }
}
