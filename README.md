
# Java REST API Example
Simple RESTful API for transfer of funds between accounts

## Quickstart
In the application root, compile the app with
```
mvn clean compile assembly:single
```
Run the application and expose port 8080 with
```
java -jar target/another-rest-example-1.0-SNAPSHOT-jar-with-dependencies.jar 
```
Some tests require the application to be running.
## Tests

Run the application tests with
```
mvn clean test
```

## Dependencies
- [Spark](http://sparkjava.com/) for the API endpoints
- [JUnit](https://junit.org/junit5/) for basic unit test assertions
- [REST Assured](https://github.com/rest-assured/rest-assured) for dummy HTTP requests
- [Maven](https://maven.apache.org), necessary for the quickstart above

## API endpoints
Check the balance of Alice
```
$ curl -X GET http://localhost:8080/balance -H "user: Alice"
"Your balance: 5"
```
Transfer one unit of currency from Bob to Alice
```
$ curl -X POST http://localhost:8080/transfer/Alice -H "from: Bob" -H "amount: 1"
"Sent: 1, new balance: 4"
```

## Limitations
1. There is no authentication or identification of users
2. The project uses concurrent hashmaps as a local datastore (in place of a database)
3. This version has hardcoded the funds of Alice and Bob to 5 units each


