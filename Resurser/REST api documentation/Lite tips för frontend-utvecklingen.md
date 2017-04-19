**Lite tips för frontend-utvecklingen**
----
----
* **Vid mottagande ev 409-errorn "User permission updated, please login again"**

  Släng token och redirecta till login-sida för att logga in igen, eftersom user permission har blivit uppdaterat i databasen och inte matchar user permission i tokenet. Detta kan ske om en användare ex. nyligen uppgraderats från normal till admin.

* **Efter att användare gjort en unregister:**

  Släng tokenet helt enkelt