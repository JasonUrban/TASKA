# TASKA
**Simple Spring Boot REST API with 3 endpoints which allow:**
- /accounts - get all acoounts with their respective balances in JSON format;
- /balance/{id} - receive balance information for account with ID {id} (error message will be returned if the account is not present in the database);
- /transfer/{id}/{amount} - transfer {amount} to account with ID {id} (this also returns error message if the account was not found).

Input values validation is enabled. All access is synchronized so that multiple users can work with the supplied endpoints concurrently.

Used DB is MySQL but can be easily customized in application.properties.

Client application is used for performance testing of the application.
