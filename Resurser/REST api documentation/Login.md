**Login**
----
  Allows a user to login

* **URL**

  /user/login

* **Method:**

  `POST`
  
*  **Header Params**

   **Required:**
 
   `Username: <username>` <br />
   `Password: <password>` <br />

* **Success Response:**

  * **Code:** 200 <br />
    **Headers:** None <br />
    **Message Body:** `{ <token> }` <br /><br />
 
* **Error Response:**

  * **Code:** 401 UNAUTHORIZED <br />
    **Headers:** `WWW-Authenticate: Username not valid` <br />
    **Message Body:** `{ Username not valid }` <br /><br />

  OR

  * **Code:** 401 UNAUTHORIZED <br />
    **Headers:**  `WWW-Authenticate: Password not valid` <br />
    **Message Body:** `{ Password not valid }` <br /> <br />

  OR

  * **Code:** 401 UNAUTHORIZED <br />
    **Headers:**  `WWW-Authenticate: Not yet approved by admin` <br />
    **Message Body:** `{ Not yet approved by admin }` <br /><br />
