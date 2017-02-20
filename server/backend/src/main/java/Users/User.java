package Users;

/**
 * Created by Dazing on 2017-02-20.
 */
public class User {

    private int id;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String userPermission;

    public User (int id, String username, String email, String firstname, String lastname, String userPermission) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.userPermission = userPermission;
    }

    public String getUsername () { return this.username; };

    public String getEmail () { return this.email; };

    public String getFirstname () { return this.firstname; };

    public String getLastname () { return this.lastname; };

    public String getUserPermission () { return this.userPermission; };

    public void setEmail (String email) { this.email = email; }

    public void setUsername (String username) { this.username = username; }

    public void setFirstname (String firstname) { this.firstname = firstname; }

    public void setLastname (String lastname) { this.lastname = lastname; }

    public void setUserPermission (String userPermission) { this.userPermission = userPermission; }

}
