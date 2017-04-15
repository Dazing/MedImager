**Update User Permission**
----
  Allows an admin to update a user's permission

* **URL**

  /admin/updateuserpermission

* **Method:**

  `PUT`
  
*  **Header Params**

   **Required:**
 
   `Authorization: <token>` <br />
   `ID: <userid>` <br />
   `NewUserPermission: <newuserpermission>` <br />

* **Success Response:**

  * **Code:** 200 <br />
    **Headers:** None <br />
    **Message Body:** `{ User permission updated }` <br /><br />
 
* **Error Response:**

  * **Code:** 409 CONFLICT <br />
    **Headers:** None <br />
    **Message Body:** `{ No user ID provided }` <br /><br />

    OR

  * **Code:** 409 CONFLICT <br />
    **Headers:** None <br />
    **Message Body:** `{ User ID not valid }` <br /><br />

    OR

  * **Code:** 409 CONFLICT <br />
    **Headers:** None <br />
    **Message Body:** `{ No new user permission provided }` <br /><br />

    OR

  * **Code:** 403 FORBIDDEN <br />
    **Headers:** None <br />
    **Message Body:** None <br />
    **Explanation:** Occurs when the user does not have admin permission <br /><br />

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
