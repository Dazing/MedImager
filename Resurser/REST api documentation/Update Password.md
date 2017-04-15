**Update Password**
----
  Allows a user to update its password

* **URL**

  /user/updatepassword

* **Method:**

  `PUT`
  
*  **Header Params**

   **Required:**
 
   `Authorization: <token>` <br />
   `NewPassword: <password>` <br />

* **Success Response:**

  * **Code:** 200 <br />
    **Headers:** None <br />
    **Message Body:** `{ Password updated }` <br /><br />
 
* **Error Response:**

  * **Code:** 409 CONFLICT <br />
    **Headers:** None <br />
    **Message Body:** `{ No new password provided }` <br /><br />

    OR
    
  * **Code:** 401 UNAUTHORIZED <br />
    **Headers:** `WWW-Authenticate: Token must be provided` <br />
    **Message Body:** `{ Token must be provided }` <br /><br />

    OR

  * **Code:** 401 UNAUTHORIZED <br />
    **Headers:** `WWW-Authenticate: Token not valid` <br />
    **Message Body:** `{ Token not valid }` <br /><br />

    OR

  * **Code:** 401 UNAUTHORIZED <br />
    **Headers:** `WWW-Authenticate: Token expired` <br />
    **Message Body:** `{ Token expired }` <br /><br />

    OR

  * **Code:** 409 CONFLICT <br />
    **Headers:** None <br />
    **Message Body:** `{ User permission updated, please login again }` <br /><br />
