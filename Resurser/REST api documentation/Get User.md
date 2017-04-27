**Get User**
----
  Allows a user to get its user info

* **URL**

  /user/getuser

* **Method:**

  `GET`
  
*  **Header Params**

   **Required:**
 
   `Authorization: <token>` <br />

* **Success Response:**

  * **Code:** 200 <br />
    **Headers:** None <br />
    **Message Body:** <br />

```javascript
// Example
{
        "firstName": "Börje",
        "id": "4",
        "lastName": "Plåt",
        "userPermission": "admin",
        "username": "börje@medimager.com"
}
```
 
* **Error Response:**

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