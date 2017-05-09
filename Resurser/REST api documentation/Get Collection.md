**Get Collection**
----
  Allows a user to get a certain collection

* **URL**

  /collection/{id}

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
        "examinationID": "9231239012",
        "note": "",
        "collectionID": "12",
        "collectionitemID": "122",
}
{
        "collectionName": "9231556012",
        "collectionDescr": "Se tandrad",
        "collectionID": "12",
        "collectionitemID": "123",
}
```
 
