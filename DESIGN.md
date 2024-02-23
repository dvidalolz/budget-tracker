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

### UserRepo
Handles `User` entity persistence operations.

### InputTypeRepo
Facilitates `InputType` entity persistence operations.

### InputSubTypeRepo
Manages `InputSubType` entity persistence operations.

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
1) InputType and InputSubTypeRepository : To manage user-associated types and subtypes
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
  a) Declare method signatures in repositories
  a) Create schemas and generate input test data
  b) Implement component repositories
  c) Create service configs
  d) Create test configs
  e) Create Service Tests
