**Get Collections**
----
  Allows a user to get its collections

* **URL**

  /collection/

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
        "collectionName": "Karies",
        "collectionDescr": "Bilder på karies",
        "collectionID": "1",
        "userID": "12",
}
{
        "collectionName": "Bakterier",
        "collectionDescr": "Bilder på Bakterier",
        "collectionID": "2",
        "userID": "12",
}
```
 
