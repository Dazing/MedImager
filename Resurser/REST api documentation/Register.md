**Register**
----
  Allows a user to register

* **URL**

  /user/register

* **Method:**

  `POST`
  
*  **Header Params**

   **Required:**
 
   `Username: <username>` <br />
   `Password: <password>` <br />
   `FirstName: <firstname>` <br />
   `LastName: <lastname>` <br />

* **Success Response:**

  * **Code:** 200 <br />
    **Headers:** None <br />
    **Message Body:** `{ Preliminary registration complete, awaiting approval }` <br /><br />
 
* **Error Response:**

  * **Code:** 409 CONFLICT <br />
    **Headers:** None <br />
    **Message Body:** `{ Account already registered }` <br /><br />
