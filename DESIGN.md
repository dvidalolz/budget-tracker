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
  b) Implement component repositories {created userrepo 02/26} {created typerepo 02/27} {finished all repos 02/28}
  c) Implement services {user/inputtype/subtype services complete 03/01/24}  {inputservice complete 03/03/24}
  d) Create service and data configs {done 03/04/24}
3) Create tests and test all
  d) Create tests
    i) Discern and create necessary test files (based on example) {done 03/05/24}
    ii) Implement tests and their necessary configurations {done 03/06/24}
    ii) UserServiceTests {done 03/08/24} 
    ii) InputTypeServiceTests {done 03/11/24}
      1 - should inputsubtype be associated with a user too?
      Nah, I'll just retrieve a inputtypeid using userid and retrieve all subytypes with that userid
    ii) InputServiceTest {done 03/11/24}
    iii) fix up and finalize all tests {done 03/12/24}
  e) Specific exception classes {done 03/13/24}
  f) javadocs commentary {done 03/13/24}
3) Go through a bit more of the spring framework essentials {doing since 03/14/24}
  a) switch to component scanning {done 03/15/24}
  b) design and create aspects for logging
4) Create RestAPI : This is where you'll handle the transformation of raw input details from frontend and pass it to the service layer
  a) How to secure rest api
    i) OAuth Spring academy course
5) Create frontend : I need to figure out what language I'd like to use for web frontend. Also need to figure out what I'd like it all to look like
6) Push to production
  a) AWS?
  a) Separation of production and testing environment?
7) Extend to include visual processors (graphing input data)



## Consideration
* Consider adding loggers
* Consider the implications of restapi catching specific exceptions
* Must have an input processor which takes in the component parts of an input, creates an input, and passed it onto inputservice

## Completed Considerations
* Make sure to test that entities returned have a generated id (NOt Null)
* Consider the implications of creating user's with an expense/income type as default, without its existence (and therefore, relationship) not being reflected within the database - do the foreign keys of a user/subtype point to an expense/income type? How would they do that if they are nonexistent for that user?
   + Include in user service a helper function which creates default types (userservice will require an inputtyperepo after all)
* Consider if it is necessary to have a set of inputtypes and subtypes in a user, or if they can just be retrieved through database operations. What are the pros and cons of each? (This will come during the inputtype/subtype service implementation)
* Consider the details of the expense and income as defaults, and how it plays out in the inputtype and subtype retrieval. (When it comes to inputtype and subtype, the purpose of retrieval from their respective repositories is to provide all user's inputtypes and subtypes for categorizing visualizations). The Input will include these already. The way it is currently designed, there is no need to include it in the inputtype/subtype repoitory.