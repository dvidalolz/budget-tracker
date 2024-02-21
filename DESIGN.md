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
Responsible for adding inputs to a user's account and other input-related logic.

### InputTypeService
Manages input types and subtypes, including creation and removal, tied to specific users.

### UserService
Handles user-specific functionalities such as adding an `InputType` to a user and fetching all user-associated inputs.





# Todo
1) Commentary : Initiate and finish some high level design commentary in all currently declared inputs (this will help put the pieces together)
2) Visualize : Create a UML diagram, a flowchart, and a sequential diagram (all of which at LEAST includes up to userservice fetching all inputs from an users input repo)
3) Context : Create the application context and make its subsequent decisions (what databases, what configuration, what properties, etc..)
4) Create Repos : Implement the logic for repos including its SQL for operations and schema file (include test data insert file as well?)