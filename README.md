# Account Management: IoBank

NB: This is just a proof of concept, in-memory storage was used as directed.
This application is simple account management system that outlines the basic process of a bank, it explains the basic type of transaction and the how accounts are created using the phone number and some other information, deposit and withdrawal of transactions.

This application uses Spring / Spring Boot and Java, using Java Collections framework such as Maps, List etc. as the data storage.

As you examine the project, be sure to make recommendation or report any issue to the contacts listed below.
## Assumption
- Desktop Dockers is installed on the host machine 
- Docker-compose is installed 

## Table of Contents
- [Account Management: IoBank](#account-management-iobank)
    - [Table of Contents](#table-of-contents)
    - [General Information](#general-information)
    - [Technologies Used](#technologies-used)
    - [Setup](#setup)
        - [To Run](#to-run)
        - [Swagger Setup](#swagger-setup)
    - [Project Structure](#project-structure)
        - [Config](#config)
        - [Enums](#enums)
        - [Exception](#exception)
        - [Mapper](#mapper)
        - [Model](#model)
        - [Route](#route)
        - [Service](#service)
        - [Storage](#storage)
        - [Utils](#utils)
        - [Validation](#validation)
    - [Usage](#usage)
    - [Endpoints](#endpoints)
        - [Account Endpoints](#account-endpoints)
        - [Transaction Endpoints](#transaction-endpoints)
        - [Settings Endpoint](#settings-endpoints)
    - [Testing](#testing)
    - [Contributor](#contributor)




## General Information
This project reflects the bank process in its most basic form, in the project an account be processed through the following:
* Creation
* Retreivial.
* Listing.
* Suspension.
  While transactions that can be performed are:
* Deposit.
* Withdrawal.
* Listing of Transaction.


## Technologies Used
* Language and Frameworks
    * Spring 5 and Spring Boot 2.7
    * Java 11
* Dependencies
    * jackson-datatype-jsr310
    * commons-lang3
    * springfox-swagger-ui
    * spring-boot-starter-web
    * lombok
* Tools
    * IntelliJ IDEA
* Testing
    * Mockito
    * JUnit5
## Setup
Fork or Clone the project from [here](https://github.com/timothy-odofin/accountmgt) , then open the project on your favorite IDE and run.

### Dockerfile
This contains necessary credential and operations to be performed for deployment to docker desktop,There is also a user permission definition for security of the application
it also contains a docker-compose file where the port number and the application behaviour are being defined.
This file in summary contains the service details of the application, aside this there is a file called run.sh and run.bat
To run the application,
* Ensure Desktop Dockers is running
* Ensure you are able to login to Dockers environment
* On Terminal/Command prompt, change to the project directory
```
cd the-project-directory
```
On Windows
```
    execute run.bat
```
on Linux
```
    execute run.sh
```

Please note that the above run file will run both the unit testing, integration testing and start the application after the testings have been passed, along with detailed logs displaying in the process.

### To Run
```
 $ mvn clean spring-boot:run

```
The base URL is http://localhost:4000/

To access the application after running,
visit http://localhost:4000/swagger-ui.html
### Swagger Setup
To run the project using swagger, run the project and view the page on using
http://localhost:4000/swagger-ui.html
As specified here
the port: 4000,
the context path: ""
You can customize this parameter by altering the pom.xml file by setting
server.port
spring.servlet.contextPath
```
server.port=9000
spring.servlet.contextPath=my-path
```
## Project Structure
The application structure has the following packages under the root package(iobank.org.accountmgt)
### Config
This is the package that contain all the external configuration of the application such as Swagger, CORS,AsyncExecutor
### Enums
This is where enumeration used in the project are located, like the account type, currency type.
### Exception
This contains the customize exceptions used in the application such as BadRequestException.
### Mapper
This contains a class that has static methods that maps requests to model, model to response.
### Model
This contains subpackage that has all the request, response and exception classes used.
### Controller
This contains the controller for both transaction, Settings and account, in these classes we have the endpoint methods
### Service
This contains the interface and service implementation classes for the Account, Settings and Transaction.
### Storage
This is the class that serves as the in-memory datastore, used Map to stores all the application data.
### Utils
This contains classes that hold the response messages, endpoint paths, Application code, utility methods etc.
### Validation
This contains the AppValidator class, it check if the payloads are checked and valid before passing to the application, otherwise it returns an error methods in accordance to the parameter that is not valid.
## Usage
Navigating through an unknown project can be a bit tedious atimes, but we will make that as easy as possible here,
The parameter requested are listed below along with their definitions
* Account Type
    * Savings
    * Current
    * Fixed
* Channel Type
    * Cash
    * Cheque
    * Transfer
* Currency Type
    * NGN (Naira)
    * USD (Dollar)
    * EU (Euro)
    * GBP (Pound)
* Transaction Type
    * Deposit
    * Withdrawal

## Endpoints
There two different routes to the application: AccountRoute and Transaction, for the End-to-end testing to be carried out properly, the tester must first register as a customer and create an account before any transaction can be take place;
> ⚠️ When testing the endpoints the Types should be the first values e.g currency should be NGN not Naira.

### Account Endpoints

|         Endpoint         |  Method |       Route          |                 Payload                    |
|--------------------------|---------|----------------------|--------------------------------------------|
|Create Customer           | POST    |     /customer/add    | Email, Phone, Address, Name                | 
| Create Customer Account  | POST    | /customer/account/add| AccountType, Currency, CustomerPhone       |
| Suspoend/Unsuspend Account| POST   | /customer/account/suspend| AccountNumber, CustomerPhone, EnableAccount|
| Retrieve Account         | GET     | /customer/account    |  AccountNumber, CustomerPhone              |
| Retreive Account         | GET     | /customer/add/get    | AccountNumber                              |
| List Accounts by Customer| GET     | /customer/account/list| CustomerPhone|
| List Customers | GET | /customer/list | None|


### Transaction Endpoints


| Endpoint          | Method | Route     | Payload      |
|------|------|--------------|----------------------------|
| Deposit                            | POST | /transaction/deposit      | AccountNumber, Channel, Amount, Narration | 
| List Transactions                  | GET  | /transaction/list         | AccountNumber|
| List Transaction by Account Number | GET  | transaction/list/by-account-number | Account Number,Transdate |
| Withdrawal                         | POST | /transaction/withdraw | AccountNumber, Channel, Amount, Narration |

### Settings Endpoints

| Endpoint        |   Method   | Route                           | Payload |
|-----------------|------------|---------------------------------|--------|
| List Account Type | GET | /settings/list/type/account     | None   |
| List Channel Type | GET | /settings/list/type/channel     | None   |
| List Currency Type | GET | /settings/list/type/currency    | None |
| List Transaction Type | GET | /settings/list/type/transaction | None|

## Testing
So far different testing has been done on the application, testing such as end-to-end testing, Unit Testing and Integrated Testing.
For the Unit testing, Mockito was used to test each controller or route, the tests are written in the test folder.
Test done on this application
* Unit Testing
* Integrated Testing
* End-to-end Testing
## Contributor
| Name      | Email | Contact | Github                                      |
|-----------|-------|---------|---------------------------------------------|
| Oyejide Odofin | odofintimothy@gmail.com | +234 7065990878 | [github](https://github.com/timothy-odofin) |
