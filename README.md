# MyBankService Microservice
### Project Requirements and Tools
Java 17<br/>
Spring Boot 3.05<br/>
Gradle Build System<br/>
Spring JPA<br/>
Spring Secuirty<br/>
H2 In Memory Database<br/>
Junit 5<br/>
### Application component
api-gateway (Spring Gateway) [http:localhost:9050]<br/>
customer-service (customer management) [http:localhost:9051]<br/>
savings-account-service (account management) [http:localhost:9052]<br/>
### API's List
#### 1) Customer Signup
Endpoint: http://localhost:9050/customer/signup<br/>
Request: {<br/>
    "customerName":"Manish",<br/>
    "password":"password"<br/>
}<br/>
Response: "customer created"<br/>
#### 2) Customer Login
Endpoint: http://localhost:9050/customer/login<br/>
Request: {<br/>
    "customerName":"Manish",<br/>
    "password":"password"<br/>
}<br/>
Response: {<br/>
    "message": "Login successfully",<br/>
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIxIiwic3ViIjoiTWFuaXNoIiwiaWF0IjoxNjgxMDQwMTc4LCJleHAiOjE2ODEwNDM3Nzh9.DMewXRoPrMsxIsy0YHU8HhD4P-WVjkPzZQ59deFXZHKCDfgw1c2R06v2H5NzJPiIwjwrXMFGUtlr4p-hH0rAiw"<br/>
}<br/>
#### 3) Open New Account
Endpoint: http://localhost:9050/savings/openNewAccount<br/>
Header: Authorization:Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIxIiwic3ViIjoiTWFuaXNoIiwiaWF0IjoxNjgxMDQwMTc4LCJleHAiOjE2ODEwNDM3Nzh9.DMewXRoPrMsxIsy0YHU8HhD4P-WVjkPzZQ59deFXZHKCDfgw1c2R06v2H5NzJPiIwjwrXMFGUtlr4p-hH0rAiw<br/>
Request: Empty <br/>
Response: {<br/>
    "id": 1,<br/>
    "customerId": 1,<br/>
    "accountNumber": "5072055470262",<br/>
    "homeBranch": "Copenhagen Denmark",<br/>
    "createdAt": "2023-04-09T11:39:36.290+00:00"<br/>
}<br/>
#### 4) Deposit Money
Endpoint: http://localhost:9050/savings/transaction<br/>
Header: Authorization:Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIxIiwic3ViIjoiTWFuaXNoIiwiaWF0IjoxNjgxMDQwMTc4LCJleHAiOjE2ODEwNDM3Nzh9.DMewXRoPrMsxIsy0YHU8HhD4P-WVjkPzZQ59deFXZHKCDfgw1c2R06v2H5NzJPiIwjwrXMFGUtlr4p-hH0rAiw<br/>
Request: {<br/>
   "accountNumber":"5072055470262",<br/>
   "amount":200,<br/>
   "narration":"Test deposit",<br/>
   "transactionType":"DEPOSIT"<br/>
}<br/>
Response: {<br/>
    "id": 1,<br/>
    "savingsId": 1,<br/>
    "amount": 200,<br/>
    "narration": "Test deposit",<br/>
    "createdAt": "2023-04-09T11:39:52.041+00:00"<br/>
}<br/>
#### 5) Withdraw Money
Endpoint: http://localhost:9050/savings/transaction<br/>
Header: Authorization:Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIxIiwic3ViIjoiTWFuaXNoIiwiaWF0IjoxNjgxMDQwMTc4LCJleHAiOjE2ODEwNDM3Nzh9.DMewXRoPrMsxIsy0YHU8HhD4P-WVjkPzZQ59deFXZHKCDfgw1c2R06v2H5NzJPiIwjwrXMFGUtlr4p-hH0rAiw<br/>
Request: {<br/>
   "accountNumber":"5072055470262",<br/>
   "amount":100,<br/>
   "narration":"Test withdraw",<br/>
   "transactionType":"WITHDRAW"<br/>
}<br/>
Response: {<br/>
    "id": 6,<br/>
    "savingsId": 1,<br/>
    "amount": -100,<br/>
    "narration": "Test withdraw",<br/>
    "createdAt": "2023-04-09T09:03:27.586+00:00"<br/>
}<br/>
#### 6) Last 10 Transaction
Endpoint: http://localhost:9050/savings/transactionList?accountNumber=1794815743807&page=0&size=10<br/>
Header: Authorization:Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIxIiwic3ViIjoiTWFuaXNoIiwiaWF0IjoxNjgxMDQwMTc4LCJleHAiOjE2ODEwNDM3Nzh9.DMewXRoPrMsxIsy0YHU8HhD4P-WVjkPzZQ59deFXZHKCDfgw1c2R06v2H5NzJPiIwjwrXMFGUtlr4p-hH0rAiw<br/>
Request: Empty <br/>
Response: {<br/>
    "content": [<br/>
        {<br/>
            "id": 1,<br/>
            "savingsId": 1,<br/>
            "amount": 200,<br/>
            "narration": "Test deposit",<br/>
            "createdAt": "2023-04-09T10:10:19.687+00:00"<br/>
        },<br/>
        {<br/>
            "id": 2,<br/>
            "savingsId": 1,<br/>
            "amount": -60,<br/>
            "narration": "Test withdraw",<br/>
            "createdAt": "2023-04-09T10:10:28.248+00:00"<br/>
        }<br/>
    ],<br/>
    "pageable": {<br/>
        "sort": {<br/>
            "empty": true,<br/>
            "sorted": false,<br/>
            "unsorted": true<br/>
        },<br/>
        "offset": 0,<br/>
        "pageNumber": 0,<br/>
        "pageSize": 10,<br/>
        "unpaged": false,<br/>
        "paged": true<br/>
    },<br/>
    "last": true,<br/>
    "totalPages": 1,<br/>
    "totalElements": 2,<br/>
    "first": true,<br/>
    "size": 10,<br/>
    "number": 0,<br/>
    "sort": {<br/>
        "empty": true,<br/>
        "sorted": false,<br/>
        "unsorted": true<br/>
    },<br/>
    "numberOfElements": 2,<br/>
    "empty": false<br/>
}
#### 7) Check Balance
Endpoint: http://localhost:9050/savings/getBalance?accountNumber=5072055470262<br/>
Header: Authorization:Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIxIiwic3ViIjoiTWFuaXNoIiwiaWF0IjoxNjgxMDQwMTc4LCJleHAiOjE2ODEwNDM3Nzh9.DMewXRoPrMsxIsy0YHU8HhD4P-WVjkPzZQ59deFXZHKCDfgw1c2R06v2H5NzJPiIwjwrXMFGUtlr4p-hH0rAiw<br/>
Request: Empty<br/>
Response: 560
