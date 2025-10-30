---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# LittleLogBook Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

_{ list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well }_

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### View Command

#### Implementation
The `view` command displays detailed information about a specific person in a pop-up window.

**Operation:** `view INDEX`

**How it works:**
1. Parses the user-provided index using ViewCommandParser 
2. Validates that the index is within bounds of the current filtered list 
3. Retrieves the corresponding person from the filtered person list using Model#getFilteredPersonList()
4. Creates a ViewCommand object with the target index 
5. When executed, opens a pop-up window (ViewWindow) displaying all person details 
6. The main window remains accessible while the view window is open

**Overall Sequence Diagram for View:**
<puml src="diagrams/ViewSequenceDiagram-Overall.puml" alt="ViewOverState" />

Below is the more in depth breakdown of the Logic and UI Sequence diagrams.

<puml src="diagrams/ViewSequenceDiagram-Logic.puml" alt="ViewLogicState" />

<puml src="diagrams/ViewSequenceDiagram-UI.puml" alt="ViewUIState" />

### Remind Command

#### Implementation
The `remind` command shows current and upcoming birthdays.

**Operation:** `remind`

**How it works:**
1. User launch the program
2. Remind command is automatically called
3. Get list of persons whose birthdays is today or in the upcoming 7 days. 
4. Displays a list of upcoming events/birthdays 
5. Can show both students and colleagues with upcoming dates

**Sequence Diagram for Automated Remind on start:**
<puml src="diagrams/RemindSequenceDiagram-Auto.puml" alt="RemindAutoState" />

**Key Classes:**
- `RemindCommand` - Handles the command execution
- `Birthday` - Contains date logic for reminder calculations
- `Person` - Stores birthday information

**Validation for Birthday:**
- The Birthday class implements comprehensive date validation with the following constraints:
- Format Validation 
  - Required Format: `dd-MM-yyyy` (e.g., 24-12-2005)
  - Regex Pattern: `^\d{2}-\d{2}-\d{4}$` ensures exactly 2 digits for day, 2 for month, and 4 for year 
  - Strict Parsing: Uses `ResolverStyle.STRICT` to reject invalid dates like 31-04-2023 (April has only 30 days)
- Temporal Constraints 
  - Minimum Date: 01-01-1900 - Prevents unrealistically old birth dates 
  - Maximum Date: Current date - Prevents future birth dates 
  - Range Validation: Ensures birthday falls between January 1, 1900 and today

**Overall Sequence Diagram for Remind:**
<puml src="diagrams/RemindSequenceDiagram-Overall.puml" alt="RemindOverallState" />

Below is the more in depth breakdown of the Logic, Model and UI Sequence diagrams.

<puml src="diagrams/RemindSequenceDiagram-Logic.puml" alt="RemindLogicState" />

<puml src="diagrams/RemindSequenceDiagram-UI.puml" alt="RemindUIState" />


### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**: Kindergarten Teacher
* an avid user of typed user commands interface
* prefer desktop apps over other types
* has multiple classes that holds multiple students
* has multiple colleagues

**Value proposition**: Little LogBook helps kindergarten teachers stay organised by managing student and colleague details in one place, making parent contacts quick to access and freeing up more time for teaching.

## User Stories

### Priority Legend
- `* * *` : Must-have (High priority)
- `* *`   : Good-to-have (Medium priority)
- `*`     : Nice-to-have (Low priority)

| Priority | As a …​                | I want to …​                                | So that I can…​                                         |
|----------|------------------------|---------------------------------------------|---------------------------------------------------------|
| `* * *`  | new teacher            | see usage instructions                      | refer to instructions when I forget how to use the App  |
| `* * *`  | teacher                | add a new person                            | be efficient when managing contacts                     |
| `* * *`  | teacher                | delete a person                             | remove contacts I no longer need                        |
| `* * *`  | teacher                | find a person by name                       | locate contact without going through the entire list    |
| `* *`    | teacher                | view a person's contact details             | get the information I need quickly                      |
| `* *`    | forgetful teacher      | add notes to a person's entries             | remember details about the person                       |
| `* *`    | teacher                | mark attendance of my student               | track presence easily                                   |
| `* *`    | person who makes typos | edit contacts                               | correct mistakes without re-adding the contact          |
| `* *`    | person who makes typos | input validation (e.g., phone only digits)  | reduce mistakes when entering data                      |
| `* *`    | kindergarten teacher   | search contacts using partial names         | find information more easily                            |
| `* *`    | forgetful teacher      | confirmation pop-ups before deleting        | avoid erasing information by accident                   |
| `* *`    | kindergarten teacher   | sort contacts by categories (students, ...) | filter information quickly                              |
| `* *`    | forgetful teacher      | detect duplicate contacts                   | avoid multiple entries of the same student              |
| `* *`    | kindergarten teacher   | attach a student’s photo                    | quickly match names to faces                            |
| `* *`    | kindergarten teacher   | categorise students (class, age, bus group) | find information more efficiently                       |
| `* *`    | kindergarten teacher   | mark preferred contact methods              | respect parent preferences                              |
| `* *`    | kindergarten teacher   | save parents’ contact details               | reach them in emergencies                               |
| `* *`    | kindergarten teacher   | add pickup person details                   | ensure students go home safely                          |
| `* *`    | kindergarten teacher   | store multiple emergency contacts           | have options if one is unavailable                      |
| `* *`    | kindergarten teacher   | save colleagues’ information                | reach them when I need help                             |
| `* *`    | kindergarten teacher   | group colleagues by role                    | contact the right person quickly                        |
| `* *`    | kindergarten teacher   | "mark all present" option                   | save time by only marking absentees                     |
| `* *`    | kindergarten teacher   | see color code for attendance               | identify status quickly (red = absent, green = present) |
| `* *`    | kindergarten teacher   | check a student’s attendance history        | spot patterns of absence                                |
| `* *`    | kindergarten teacher   | generate attendance reports                 | submit them to school admin easily                      |
| `*`      | kindergarten teacher   | get reminders of school events              | stay prepared                                           |
| `*`      | kindergarten teacher   | get reminders of birthdays                  | celebrate students’ birthdays                           |
| `*`      | older teacher          | customise font size                         | see more clearly                                        |
| `*`      | kindergarten teacher   | mark frequently contacted colleagues        | find them faster                                        |

### Use cases

(For all use cases below, the **System** is the `LittleLogBook` and the **Actor** is the `user`, unless specified otherwise)

**Use case: Add a contact**

**MSS**

1. User opens LittleLogBook.
2. LittleLogBook shows list of all the contacts added.
3. User enters the contact information.
4. LittleLogBook validates input information.
5. LittleLogBook saves contact and updates contact list.
6. LittleLogBook displays success confirmation.

Use case ends.

**Extensions**

* 4a. The input information is invalid.
    * 4a1. LittleLogBook shows an error message.
      Use case resumes at step 3.

**Use case: View a contact**

**MSS**

1. User opens LittleLogBook.
2. LittleLogBook shows list of all the contacts added.
3. User enters a specific contact name.
4. LittleLogBook validates input information and finds the matching contact.
5. LittleLogBook displays the contact information.

Use case ends.

**Extensions**

* 4a. The input information is invalid.
    * 4a1. LittleLogBook shows an error message.
      Use case resumes at step 3.

**Use case: Delete a contact**

**MSS**

1.  User opens LittleLogBook.
2.  LittleLogBook shows list of all the contacts added.
3.  User requests to delete a specific contact in the list.
4.  LittleLogBook displays a confirmation popup asking the user to confirm the deletion.
5.  Users confirms the deletion.
6.  LittleLogBook deletes the person and updates the list.

    Use case ends.

**Extensions**

* 3a. The contact does not exist.
  *    3a.1 LittleLogBook requests for valid input.
       Use case resumes at step 3.
* 5a. User cancels the deletion.
  *    5a.1 LittleLogBook closes the confirmation popup and goes back to main window.
       Use case resumes at step 2.


**Use case: Searches a contact**

**MSS**

1.  User opens LittleLogBook.
2.  LittleLogBook shows list of all the contacts added.
3.  User requests to search a contact in the list with partial information.
4.  LittleLogBook shows list of all contacts matching the information.

    Use case ends.

**Extensions**

* 3a. No contact matches the information.
    *    3a.1 LittleLogBook requests for valid input.
         Use case resumes at step 3.

**Use case: Marks attendance**

**MSS**

1.  User opens LittleLogBook.
2.  LittleLogBook shows list of all the contacts added.
3.  User requests to mark attendance of a specific student.
4.  LittleLogBook succesfully marks student's attendance.

    Use case ends.

**Extensions**

* 3a. No contact matches the information.
    *    3a.1 LittleLogBook requests for valid input.
         Use case resumes at step 3.


*{More to be added}*

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  Only authenticated users (teachers) can access the app.

*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Contact**: A person's name, class (for student), birthday (for student), phone number's (colleague, student's parents), emails (colleague, student's parents)
* **Notes**: A section inside contact for additional information

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: Popup window appears for confirmation. After the user confirms to proceed with the deletion, first contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
