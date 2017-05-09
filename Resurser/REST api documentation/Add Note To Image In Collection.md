**Add note to image in collection**
----
  Allows a user to add a note to an image in a collection

* **URL**

  /collection/note

* **Method:**

  `PUT`
  
*  **Header Params**

   **Required:**
 
   `Authorization: <token>` <br />

* **Data Params :**
  * **JSON:** <br />

```javascript
{       
        "note":"Interesting development",
        "collectionID": "12",
        "collectionitemID":"123"
}
```
 
