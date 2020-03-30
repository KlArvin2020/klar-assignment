# CashBack Service
## The endpoints
The CashBack Service is a REST service that accepts rewards and calculates the earned cash back amount
for a given day.
### Reward endpoint
The Reward endpoint accepts a JSON list of objects containing information about transactions.
Information contained in a transaction object:
- **amount**: the amount of money transacted, must not be less than zero
- **reward**: the reward factor, between 0 and 1, non-inclusive
- **timestamp**: timestamp of the transaction (specified in milliseconds since 1970-01-01, must not be below 0)

**Endpoint URL:** `/rewards`

**Example request:**
```
curl -s -o /dev/null -w "%{http_code}\n" -X POST -H "Content-Type: application/json" "localhost:8080/rewards"-d '
[
  {
    "amount": 150.00,
    "reward": 0.1,
    "timestamp": 1580641200000
  },{
    "amount": 50.00,
    "reward": 0.99999,
    "timestamp": 1580641200001
  }
]'
```
**Expected Output:** ```201```

### Total cash back endpoint
The endpoint accepts a date (formatted as `YYYY-MM-DD`) path parameter and returns total cash back generated for that 
date.

**Endpoint URL**: `/reward/total/{date}`

**Example request:**
```
curl -X GET "localhost:8080/reward/total/2020-02-02"
```
**Expected Output:** 
```
{"average":1.00,"min":0.50,"max":1.50,"total":2.00}
```
## Running the application
Run the application through executing `com.arvin.klar.assignment.AssignmentApplication`.
Spring Boot should then make the REST endpoint available at localhost using the default port (8080).
## Testing the application
Tests can be found in `src/test/java/com/arvin/klar/assignment`.
Currently only endpoint tests and a test making sure the application can run exist.
The endpoints are tested in a way that makes sure invalid requests return the correct HTTP status codes,
and that successive requests lead to the correct output being returned.
## Design
### Controller
The controller is where the REST requests get mapped to Java functions.
The controller (`com.arvin.klar.assignment.rest.RewardsController`) validates the requests and then retrieves
the relevant information from the data store.
### Data retrieval
Data retrieval is done through retrieving it from the data store. The data store is defined as an interface
at `com.arvin.klar.assignment.data.DataStore` and concrete implementations can easily be swapped out.
Right now only a in-memory data store exists.
#### In-memory data store
The in-memory data store (`com.arvin.klar.assignment.data.MemoryDataStore`) stores tracker objects for each date where
a transaction has taken place. The tracker objects are stored in a `HashMap` to allow `O(1)` lookup time.
The in-memory data store does not store or persist transactions in any way.
The in-memory data store only stores the information relevant to return information about the cash back rewards for specific dates.

The in-memory data store is thread-safe and handles all rewards in a transaction before the lock on the data store is
released and another transaction can access it.