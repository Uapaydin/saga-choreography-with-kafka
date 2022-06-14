# saga-choreography-with-kafka
This project is an experiment for saga transaction management.
This approach created because my company did not want to use any external application for transaction handling. And we were not allowed to use dynamic topic creation on kafka.

In this project we are using a custom library that helps us to create remote calls, a custom Aspect created to collect every request send to other microservices for each rest call, using RequestScope

To test the code you can call, following call tries to create an order with 200 unit but inventory does not have this many items so code throws a SagaTransactionException.

url: [localhost:8080/v1/order]()

payload

`{
"customerId": 1,
"inventoryId":2,
"count":200
}`
