# Application Design Overview

## Entities

### Input
Represents a financial transaction with attributes like amount, date, and associations with `InputType` and `InputSubType`.

### InputType
Categorizes inputs into high-level classifications such as "Income" or "Expense".

### InputSubType
Provides a more granular classification under `InputType`, like "Salary" or "Groceries".

### User
Represents an application user, containing user information and associated `InputType`s.

## Repositories

### InputRepo
Manages `Input` entity persistence operations.
#### Functionality
1) Save an input
2) Retrieve all input by userId
3) Delete an input by inputid

### UserRepo
Handles `User` entity persistence operations.
#### Functionality
1) Save a user 
2) Find a user by username/id
3) Delete a user by id  

### InputTypeRepo
Facilitates `InputType` entity persistence operations.
1) Save an inputType
2) Fetch all inputtypes by userid
3) Delete inputType

### InputSubTypeRepo
Manages `InputSubType` entity persistence operations.
1) Save an input subtype
2) Fetch all inputsubtype by userid
3) Delete input subtype

## Services

### InputService
#### Responsibilities
1) Creating inputs and associating them with users.
2) Fetching all inputs for visualization purposes.
#### Dependencies
1) Input Repository : for managing inputs
2) UserService: to retrieve account for input operations

### InputTypeService
#### Responsibilities
1) Managing all the input types and subtypes, including creation, update, and deletion.
2) Fetching input types/subtypes by user and input subtypes by typeid
#### Dependencies
1) InputType and InputSubType Repository : To manage user-associated types and subtypes
2) UserService: to retrieve account to associate inputtype/subtype


### UserService
#### Responsibilities
1) Creating, updating, and deleting users.
2) Retrieving users
#### Dependencies
1) UserRepo : for managing and retrieval of users










# Todo
// Implement rest of userdetails.java 
1) Commentary : Initiate and finish some high level design commentary in all currently declared inputs (this will help put the pieces together) {Done 2/22}
2) Visualize : Create a UML diagram, a flowchart, and a sequential diagram (all of which at LEAST includes up to userservice fetching all inputs from an users input repo)
  a) Define high-level overview of service function implementions {Done 2/23}
  b) Visualize definitions (nah 2/23)
2) Context : Create the application context and make its subsequent decisions (what databases, what configuration, what properties, etc..)
  a) Declare method signatures in repositories {done 02/24}
      i) What should I retrieve inputs by? It just has an inputId, it is not tied to a user {tied it 02/24}
  a) Create schemas and generate input test data {created schema 02/25} {made testdata 02/26}
  b) Implement component repositories {created userrepo 02/26} {created typerepo 02/27} (CONTINUE MAKING SUBTYPEREPO)
    i) First implement inputtype and inputsubtype repos
    ii) Consider options:
      1 - Give input repo access to inputtype and subtype repo
      2 - Create service layer component for coordinating between different repositories
  c) Implement services
  c) Create service configs
  d) Create test configs
  e) Create Service Tests
  3) Iterate and refactor using test configs

* Consider the exact details of the expense and income as defaults, and how it plays out in the inputtype and subtype retrieval (When it comes to inputtype and subtype, the purpose of retrieval from their respective repositories is to get a sense of all user's inputtypes and subtypes, from which you can give your application logic access to what graph attributes to use for certain visualization queries). The Input will include these already, there is no need to include it in the inputtype/subtype, the way you have it designed currently

* Consider the business logic implications of automatically deleting all related data when a user or input type is deleted. Ensure this behavior is consistent with your application's data management policies and user expectations.
* Consider the implications of creating user's with an expense/income type as default, without its existence (and therefore, relationship) not being reflected within the database - do the foreign keys of an account point to an expense/income type? How would they do that if they are nonexistent for that user?
   + Include in user service a helper function which creates default types (userservice will require an inputtyperepo after all)