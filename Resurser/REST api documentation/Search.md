**Search**
----
  Allows a user to search for examinations filtered by terms and age bounds

* **URL**

  /api/search?{Term1=a}&{Term2=b}&...&{AgeLower=x}&{AgeUpper=y}

* **Method:**

  `GET`
  
*  **URL Params**
    
    Refer to documentation about available terms for a list of possible terms and more

    `Term_ = <string>` <br />
    `AgeLower = <integer>` <br />
    `AgeUpper = <integer>` <br />
    
    **Example URL:**
    
    `/api/search?Dis-now=Psoriasisartrit&Snuff=2 dosor/vecka&AgeLower=60`
  
*  **Header Params**

   **Required:**
 
   `Authorization: <token>` <br />

* **Success Response:**

  * **Code:** 200 <br />
    **Headers:** None <br />
    **Message Body:** <br />
 
```javascript
// Excerpt from a possible response to the example URL above
{
  // ...
  // Possibly more examinations above
  {
    {
    "age": [
      "62"
    ],
    "allergy": [
      "Nej"
    ],
    "biopsySite": [
      "122"
    ],
    "diagDef": [],
    "diagHist": [],
    "diagTent": [],
    "disNow": [
      "Psoriasisartrit"
    ],
    "disPast": [
      "Knölros"
    ],
    "drug": [
      "Mycostatin mixtur"
    ],
    "examinationID": "123456789",
    "factorNeg": [
      "Nej"
    ],
    "factorPos": [
      "Nej"
    ],
    "family": [
      "Nej"
    ],
    "gender": [
      "1"
    ],
    "imagePaths": [
      "Database.mvd/Pictures/G0396/g03964.jpg",
      "Database.mvd/Pictures/G0396/g03963.jpg",
      "Database.mvd/Pictures/G0396/g03962.jpg",
      "Database.mvd/Pictures/G0396/g03961.jpg"
    ],
    "lesnOn": [],
    "lesnSite": [],
    "skinPbl": [],
    "smoke": [
      "Inte dagligen"
    ],
    "snuff": [
      "2 dosor/vecka"
    ],
    "symptNow": [],
    "symptSite": [],
    "treatType": [
      "Expectans"
    ],
    "vasNow": [
      "7.0"
    ]
  },
  // Possibly more examinations below
  // ...
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
