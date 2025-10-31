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

_No third party libraries were used in the development of LittleLogBook_

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

<div align="center">
    <puml src="diagrams/ArchitectureSequenceDiagram.puml" width="600" />
</div>

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<div align="center">
    <puml src="diagrams/ComponentManagers.puml" width="300" />
</div>

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<div align="center">
    <puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>
</div>

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

<div align="center">
    <puml src="diagrams/LogicClassDiagram.puml" width="maxwidth"/>
</div>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<div align="center">
    <puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" width="maxwidth"/>
</div>

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
<div align="center">
    <puml src="diagrams/ParserClasses.puml" width="600"/>
</div>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<div align="center">
    <puml src="diagrams/ModelClassDiagram.puml" width="800"/>
</div>


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="700" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<div align="center">
    <puml src="diagrams/StorageClassDiagram.puml" width="700" />
</div>

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
<div align="center">
    <puml src="diagrams/ViewSequenceDiagram-Overall.puml" alt="ViewOverState" />
</div>

Below is the more in depth breakdown of the Logic and UI Sequence diagrams.

<div align="center">
    <puml src="diagrams/ViewSequenceDiagram-Logic.puml" alt="ViewLogicState" />
</div>
<div align="center">
    <puml src="diagrams/ViewSequenceDiagram-UI.puml" alt="ViewUIState" />
</div>

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
<div align="center">
    <puml src="diagrams/RemindSequenceDiagram-Auto.puml" alt="RemindAutoState" />
</div>

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
<div align="center">
    <puml src="diagrams/RemindSequenceDiagram-Overall.puml" alt="RemindOverallState" />
</div>

Below is the more in depth breakdown of the Logic, Model and UI Sequence diagrams.

<div align="center">
    <puml src="diagrams/RemindSequenceDiagram-Logic.puml" alt="RemindLogicState" />
</div>
<div align="center">
    <puml src="diagrams/RemindSequenceDiagram-UI.puml" alt="RemindUIState" />
</div>

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

[//]: # (&#40;For all use cases below, the **System** is the `LittleLogBook` and the **Actor** is the `user`, unless specified otherwise&#41;)

<div style="background: #f5f5f5; padding: 15px; border-radius: 5px; border-top: 4px solid #ffd519; margin: 10px 0;">

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
</div>

<div style="background: #f5f5f5; padding: 15px; border-radius: 5px; border-top: 4px solid #ffd519; margin: 10px 0;">

**Use case: View a contact**

**MSS**

1. User opens LittleLogBook.
2. LittleLogBook shows list of all the contacts added.
3. User requests to view a specific contact. 
4. LittleLogBook validates input information. 
5. LittleLogBook finds the matching contact. 
6. LittleLogBook displays the contact's full information in a pop-up window.

Use case ends.

**Extensions**

* 4a. The requested contact to be viewed is invalid.
    * 4a1. LittleLogBook shows an error message and request for a valid input.
      Use case resumes at step 3.
</div>

<div style="background: #f5f5f5; padding: 15px; border-radius: 5px; border-top: 4px solid #ffd519; margin: 10px 0;">

**Use case: Delete a contact**

**MSS**

1. User opens LittleLogBook. 
2. LittleLogBook shows list of all the contacts added. 
3. User requests to delete a specific contact in the list. 
4. LittleLogBook validates input information. 
5. LittleLogBook displays a confirmation popup asking the user to confirm the deletion.
6. Users confirms the deletion. 
7. LittleLogBook deletes the person and updates the list.

Use case ends.

**Extensions**

* 3a. The contact does not exist. 
    * 3a.1 LittleLogBook requests for valid input.
         Use case resumes at step 3.
* 5a. User cancels the deletion.
   * 5a.1 LittleLogBook closes the confirmation popup and goes back to main window.
         Use case resumes at step 2.
</div>

<div style="background: #f5f5f5; padding: 15px; border-radius: 5px; border-top: 4px solid #ffd519; margin: 10px 0;">

**Use case: Check birthday reminders**

**MSS**

1. User opens LittleLogBook.
2. LittleLogBook automatically checks for birthdays and shows reminder notification.
3. User requests to manually check for birthday reminders.
4. LittleLogBook validates input information. 
5. LittleLogBook looks through all contacts for birthdays today and within the next 7 days. 
6. LittleLogBook displays formatted birthday reminders.

Use case ends.

**Extensions**

* 5a. There are birthdays today.
    * 5a1. LittleLogBook shows a list of people whose birthday is today.
* 5b. There are upcoming birthdays within 7 days.
    * 5b1. LittleLogBook shows a list of people whose birthday is upcoming.
* 5c. No birthdays today or within 7 days.
    * 5c1. LittleLogBook tells the user that there is no upcoming birthdays. 
* 5d. LittleLogBook is empty.
    * 5d1. LittleLogBook tells the user that there is no contacts in LittleLogBook
</div>

<div style="background: #f5f5f5; padding: 15px; border-radius: 5px; border-top: 4px solid #ffd519; margin: 10px 0;">


**Use case: Find a contact based on partial name**

**MSS**

1. User opens LittleLogBook.
2. LittleLogBook shows list of all the contacts added.
3. User enters the command with partial name(s).
4. LittleLogBook validates input information.
5. LittleLogBook filters contacts matching partial name(s) and updates contact list.
6. LittleLogBook displays the result.

Use case ends.

**Extensions**

* 4a. The input information is invalid.
    * 4a1. LittleLogBook shows an error message.
      Use case resumes at step 3.
</div>

<div style="background: #f5f5f5; padding: 15px; border-radius: 5px; border-top: 4px solid #ffd519; margin: 10px 0;">

**Use case: Sort contacts**

**MSS**

1. User opens LittleLogBook. 
2. LittleLogBook shows list of all the contacts added. 
3. User requests to sort contacts by a specific field and order. 
4. LittleLogBook validates input information. 
5. LittleLogBook successfully sorts contacts and displays them in the new order.

Use case ends.

**Extensions**

* 4a. Missing parameters.
    * 4a.1 LittleLogBook requests for valid input.
      Use case resumes at step 3.
* 4b. The input information is invalid.
    * 4b.1 LittleLogBook requests for valid input.
      Use case resumes at step 3.
</div>

<div style="background: #f5f5f5; padding: 15px; border-radius: 5px; border-top: 4px solid #ffd519; margin: 10px 0;">


**Use case: Favourite a contact**

**MSS**

1. User opens LittleLogBook.
2. LittleLogBook shows list of all the contacts added.
3. User enters the command with index(es).
4. LittleLogBook validates input information.
5. LittleLogBook updates contact list by updating the favourites contacts.
6. LittleLogBook displays the result.

Use case ends.

**Extensions**

* 4a. The input information is invalid.
    * 4a1. LittleLogBook shows an error message.
      Use case resumes at step 3.
</div>

<div style="background: #f5f5f5; padding: 15px; border-radius: 5px; border-top: 4px solid #ffd519; margin: 10px 0;">

**Use case: Marks attendance**

**MSS**

1. User opens LittleLogBook. 
2. LittleLogBook shows list of all the contacts added. 
3. User requests to mark attendance of a specific contact. 
4. LittleLogBook validates input information. 
5. LittleLogBook successfully marks contact's attendance.

Use case ends.

**Extensions**

* 4a. No contact matches the information.
    *    4a.1 LittleLogBook requests for valid input.
         Use case resumes at step 3.
* 4b. Contact is a colleague.
    *    4b.1 LittleLogBook requests for valid input.
         Use case resumes at step 3.
* 4c. Date provided is before student's born date.
    *    4c.1 LittleLogBook requests for valid input.
         Use case resumes at step 3.
* 4d. Date provided is beyond today's date.
    *    4d.1 LittleLogBook requests for valid input.
         Use case resumes at step 3.
</div>

<div style="background: #f5f5f5; padding: 15px; border-radius: 5px; border-top: 4px solid #ffd519; margin: 10px 0;">

**Use case: Downloads student monthly attendance report**

**MSS**

1. User opens LittleLogBook. 
2. LittleLogBook shows list of all the contacts added. 
3. User requests to Download monthly attendance report of a student.
4. LittleLogBook validates input information. 
5. LittleLogBook successfully downloads student's attendance.

Use case ends.

**Extensions**

* 4a. Student is not inside contact.
    *    4a.1 LittleLogBook requests for valid input.
         Use case resumes at step 3.
* 4b. The input information is invalid.
    *    4b.1 LittleLogBook requests for valid input.
         Use case resumes at step 3.
</div>

<div style="background: #f5f5f5; padding: 15px; border-radius: 5px; border-top: 4px solid #ffd519; margin: 10px 0;">

**Use case: Downloads class daily attendance report**

**MSS**

1. User opens LittleLogBook.
2. LittleLogBook shows list of all the contacts added.
3. User requests to Download daily attendance report of a class.
4. LittleLogBook validates input information.
5. LittleLogBook successfully downloads student's attendance.

Use case ends.

**Extensions**

* 4b. The input information is invalid.
    *    4b.1 LittleLogBook requests for valid input.
         Use case resumes at step 3.
</div>

<div style="background: #f5f5f5; padding: 15px; border-radius: 5px; border-top: 4px solid #ffd519; margin: 10px 0;">

**Use case: Downloads class monthly attendance report**

**MSS**

1. User opens LittleLogBook.
2. LittleLogBook shows list of all the contacts added.
3. User requests to Download monthly attendance report of a class.
4. LittleLogBook validates input information.
5. LittleLogBook successfully downloads student's attendance.

Use case ends.

**Extensions**

* 4b. The input information is invalid.
    *    4b.1 LittleLogBook requests for valid input.
         Use case resumes at step 3.
</div>

*{More to be added}*

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.

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

1. **Initial launch**
   - Download the jar file and copy into an empty folder 
   - Double-click the jar file<br>
   - **Expected:** Shows the GUI with a set of sample contacts. The window size may not be optimum.

<br>

2. **Saving window preferences**
   - Resize the window to an optimum size. Move the window to a different location. Close the window. 
   - Re-launch the app by double-clicking the jar file.<br>
   - **Expected:** The most recent window size and location is retained.

----------------------------------------------------------------------------------------------------------------------------------

### Adding contacts

##### Adding a valid contact

1. **Adding a student contact with all fields**
    - Test case: `add n/John Doe p/98765432 e/john.doe@gmail.com a/123 Main Street c/K1A b/15-03-2018 t/student desc/Allergic to peanuts`
    - **Expected:** New student contact added successfully with all specified fields.

1. **Adding a colleague contact with optional note**
    - Test case: `add n/Mary Tan p/91234567 e/marytan@e.nut.edu a/123 Jurong West Ave 6 c/K2B b/24-12-2017 t/colleague desc/Allergic to peanuts`<br>**Expected**: New colleague contact added with note.

2. **Adding a colleague contact with mandatory fields only**
    - Test case: `add n/Marie p/98765432 e/john.doe@gmail.com a/123 Main Street c/K1A b/15-03-2018 t/colleague`
    - **Expected:** New colleague contact added with only required fields.

1. **Adding contact with mixed tags (same info)**
    - Prerequisites: Student contact "John Doe" exists
    - Test case: `add n/John Doe p/98765432 e/john.doe@gmail.com a/Blk 456, Den Road, #01-355 c/K1A b/15-03-2018 t/colleague`<br>**Expected**: Success - different tags allow identical info.

1. **Invalid parameter formats**
    - Test case: `add n/John123 p/123 e/invalid-email a/ c/InvalidClass b/32-13-2020 t/invalidtag`<br>**Expected**: Multiple validation errors shown.

----------------------------------------------------------------------------------------------------------------------------------

### Editing contacts

##### Editing contact fields

1. **Editing a contact's basic information**
   - Prerequisites: List all persons. Note the details of contact at index 1.
   - Test case: `edit 1 n/New Name p/87654321 e/new.email@school.edu`
   - **Expected:** The command box shows the edited information, and the contact list refreshes with the updated entry.

<br> 

2. **Editing contact with duplicate detection**
   - Prerequisites: Add 2 contacts with the same details for every field EXCEPT name. Assume they are at index `x` and `y`
   - Test case: `edit x n/{y's name}` (e.g., If y's name is `Bob` then run `edit x n/Bob`)
   - **Expected:** The command box shows a duplicate alert message. No changes are made to the contact list.

<br>

3. **Editing contact with missing parameters**
    - Test case: `edit`, `edit 1` (assuming 1 is a valid index)
    - **Expected:** No detail is edited. Error details shown in the status message. Status bar remains the same.

<br>

4. **Editing a contact with invalid parameter format**
     - Test case: `edit 1 p/abc`, `edit 1 e/not-an-email` (assuming 1 is a valid index)
     - **Expected:** No detail is edited. Error details shown in the status message. Status bar remains the same.
   
----------------------------------------------------------------------------------------------------------------------------------

### Deleting contacts

##### Deleting by index and name

1. **Deleting a contact by valid index**
   - Prerequisites: List all persons. Multiple persons in the list.
   - Test case: `delete 1`
   - **Expected:** A confirmation popup appears. The contact is deleted upon confirmation, otherwise, the app returns to the main window. The command box shows a success or cancellation message.

<br>

2. **Deleting a contact by invalid index**
   - Test case: `delete 1`, `delete`, `delete x`, `...` (where x is larger than the list size)
   - **Expected:** No person is deleted. Error details shown in the status message. Status bar remains the same.

<br>

3. **Deleting a contact by name with full name**
   - Prerequisite: List all persons. Alex Yeoh is in the person list.
   - Test case: `delete n/ALex Yeoh`
   - **Expected:** If only one person has this name, a confirmation popup appears, and the contact is deleted upon confirmation. Otherwise, a popup showing multiple matches appears first, followed by a confirmation popup for the selected contact.

<br>

4. **Deleting a contact by name with partial name**
   - Prerequisites: List all persons. Multiple persons in the list with `a` in their name.
   - Test case: `delete n/a`
   - **Expected:** A popup shows possible matches for selection. After the user makes a selection, a confirmation popup appears.

<br>

5. **Deleting a contact by name with a invalid name**
    - Prerequisites: List all persons. No person in the list with `random name` in their name..
    - Test case: `delete n/random name`
    - **Expected:** A popup shows no matches found. Pressing Enter returns to the main window.

----------------------------------------------------------------------------------------------------------------------------------

### Sorting contacts

##### Sorting by different fields
- All sort comparison is done alphabetically.

1. **Sorting contacts by name**
   - Test case: `sort f/name`
   - **Expected:** Contacts sorted alphabetically by name.

<br>

2. **Sorting contacts by class in descending order**
   - Test case: `sort f/class o/desc`
   - **Expected:** Contacts sorted by class in reverse alphabetical order.

----------------------------------------------------------------------------------------------------------------------------------

### Birthday reminders

##### Checking birthday notifications

1. **Manual reminder check with birthdays today and upcoming**
    - Prerequisites: At least one contact has birthday today, and at least one has birthday within next 7 days
    - Test case: `remind`<br>**Expected**: Shows two sections: "Happy Birthday to these people today!" and "Upcoming birthdays in the next 7 days:" with numbered lists.

1. **Manual reminder with only upcoming birthdays**
    - Prerequisites: No contacts have birthday today, but some have birthdays within next 7 days
    - Test case: `remind`<br>**Expected**: Shows "No birthdays today!" followed by "Upcoming birthdays in the next 7 days:" section.

1. **Manual reminder with no upcoming birthdays**
    - Prerequisites: No contacts have birthdays today or within next 7 days
    - Test case: `remind`<br>**Expected**: Shows "No upcoming birthdays found." message.

1. **Manual reminder with empty address book**
    - Prerequisites: Clear all contacts using `clear` command
    - Test case: `remind`<br>**Expected**: Shows "No contacts in LittleLogBook." message.

1. **Automatic reminder on startup**
    - Prerequisites: Contacts with birthdays today and/or upcoming exist
    - Action: Close and reopen the app<br>**Expected**: Birthday reminders shown automatically in the result display when app starts.

1. **Reminder formatting verification**
    - Prerequisites: Contacts with various birthday scenarios exist
    - Test case: `remind`<br>**Expected**: Each entry shows:
        - Name in correct format
        - Birthday in dd-MM-yyyy format
        - Tags in square brackets (if present)
        - "(TODAY!)" for today's birthdays
        - "(in X day(s))" for upcoming birthdays

1. **Reminder with extraneous parameters**
    - Test case: `remind extra parameter`<br>**Expected**: Command works normally (extraneous parameters ignored).

1. **Cross-year birthday handling**
    - Prerequisites: Test in late December with contacts having January birthdays
    - Test case: `remind`<br>**Expected**: Correctly shows upcoming birthdays that cross into next year.

**Testing Tips for `remind` command:**
- Use system date changes to simulate different scenarios
- Test across month and year boundaries
- Verify the "in X days" calculation is accurate
- Check that both automatic (startup) and manual execution work
- Ensure the output is helpful and actionable for kindergarten teachers

----------------------------------------------------------------------------------------------------------------------------------

### Note management

##### Adding and editing notes

1. **Adding a note to a contact**
   - Test case: `note 1 desc/Allergic to peanuts and dairy products`
   - **Expected:** Note successfully added to contact.

<br>

2. **Removing a note from a contact**
   - Test case: `note 1`
   - **Expected:** Existing note removed from contact.

----------------------------------------------------------------------------------------------------------------------------------

### Viewing contacts

##### Detailed contact viewing

1. **Viewing valid contact**
    - Prerequisites: Multiple contacts in list
    - Test case: `view 1`<br>**Expected**: Popup window shows full contact details.

1. **Viewing student vs colleague**
    - Test cases: `view 1` (student), `view 2` (colleague)<br>**Expected**: Different layouts shown (student shows attendance, colleague does not).

1. **Viewing with invalid index**
    - Test case: `view 0`<br>**Expected**: Error message about invalid index.

----------------------------------------------------------------------------------------------------------------------------------

### Attendance management

##### Marking attendance

1. **Marking attendance for a single student**
    - Prerequisites: Ensure contact at index 1 is a student.
    - Test case: `attendance 1 s/present`
    - **Expected:** Attendance marked successfully for current date.

<br>

2. **Marking attendance for multiple students with specific date**
    - Prerequisites: Ensure contact at index 1, 2, 3 is a student.
    - Test case: `attendance 1,2,3 s/late d/15-03-2024`
    - **Expected:** Attendance marked for all specified students on given date.

<br>

3. **Marking attendance for multiple contacts (student and colleague)**
    - Prerequisites: Ensure you have at least one student and one colleague at index 1, 2, 3. 
    - Test case: `attendance 1,2,3 s/late`
    - **Expected:** Attendance marked ONLY for students.

<br>

4. **Marking attendance for a colleague**
    - Prerequisites: Ensure contact at index 1 is a colleague.
    - Test case: `attendance 1 s/sick`
    - **Expected:** Error message and attendance rule reminder.

<br>

5. **Marking attendance with invalid date**
    - Test case: `attendance 1 s/present d/29-02-2023`
    - **Expected:** Error message for invalid date.

----------------------------------------------------------------------------------------------------------------------------------

### Attendance reports

##### Downloading reports

1. **Downloading individual student monthly report**
    - Prerequisites: Ensure contact at index 1 is a student with attendance records.
    - Test case: `attendanceD 1 m/03-2024`
    - **Expected:** CSV report downloaded successfully.

<br>

2. **Downloading multiple individual students monthly report**
    - Prerequisites: Ensure contact at index 1, 2, 3 is a student with attendance records.
    - Test case: `attendanceD 1-3 m/03-2024`
    - **Expected:** CSV report downloaded successfully.

<br>

3. **Downloading individual student daily report**
    - Prerequisites: Ensure contact at index 1 is a student with attendance records.
    - Test case: `attendanceD 1 d/01-03-2024`
    - **Expected:** Error message. Individual report are monthly only.

<br>

4. **Downloading class-based daily report**
    - Test case: `attendanceD c/K1A d/15-03-2024`
    - **Expected:** Class attendance report downloaded for specified date.
    
<br>

5. **Downloading multiple class-based daily report**
    - Test case: `attendanceD c/K1A c/K2B d/15-03-2024`
    - **Expected:** Class attendance reports downloaded for specified date.

<br>

6. **Downloading class-based monthly report**
    - Test case: `attendanceD c/K1A`
    - **Expected:** Class attendance report downloaded for current month.

<br>

7. **Downloading multiple class-based monthly report**
    - Test case: `attendanceD c/K1A c/K2B`
    - **Expected:** Class attendance reports downloaded for current month.

----------------------------------------------------------------------------------------------------------------------------------

### Saving data

##### Dealing with missing/corrupted data files

1. **Simulating missing data file:**
    - Navigate to the `data/` folder in your LittleLogBook directory
    - Delete or rename the `addressbook.json` file
    - Launch LittleLogBook
    - **Expected behavior:** LittleLogBook should start with a fresh empty address book and create a new `addressbook.json` file automatically

<br>

2. **Simulating corrupted data file:**
    - Open the `data/addressbook.json` file in a text editor
    - Manually modify the JSON structure to be invalid, for example:
        - Remove essential fields like `"name"`, `"phone"`, etc. from a contact
        - Change field types (e.g., change a phone number to an array: `"phone": [12345678]`)
        - Introduce JSON syntax errors (remove closing braces, add extra commas)
        - Add invalid date formats in birthday/attendance fields
        - Remove the entire `"persons"` array or make it null
    - Save the file and launch LittleLogBook
    - **Expected behavior:** LittleLogBook should detect the corruption and start with an empty address book.

<br>

3. **Testing with invalid birthday formats:**
    - Change a contact's birthday to an invalid format (e.g., `"32-13-2020"`, `"birthday": "not-a-date"`)
    - **Expected behavior:** LittleLogBook should either use a default date or show an error during startup

----------------------------------------------------------------------------------------------------------------------------------

### Finding contacts
##### Similar process for the different find commands (`find-n`, `find-c`, `find-t`, `find-p` )

1. **Simulating find for no-matches:**
    - Add contacts to the LittleLogBook with no names containing z or y, no classes containing K1A
    - Input the find command with required parameter(s) to find for an information such as partial name that does not exist in your contacts
        - Test case: `find-p 123456789 `
        - Test case: `find-n zy z y`
        - Test case: `find-c k1A`
    - Press enter
    - **Expected behavior:** LittleLogBook should show empty contact list with information on how to proceed.

<br>

2. **Simulating find for matches:**
    - Add contacts to the LittleLogBook with class starting with K, names containing b and/or d
    - Input the find command with required parameter(s) to find for an information such as partial phone number that exists in your contacts
        - Test case: `find-p 8 9`
        - Test case: `find-c k`
        - Test case: `find-n b d`
        - Test case: `find-t stu coll`
    - Press enter
    - **Expected behavior:** LittleLogBook should show filtered contact list of only those that match the input string with information on how to proceed.

<br>

3. **Simulating find with no parameter inputs:**
   - Input the find command with no parameter
        - - Test case: `find-c `
   - **Expected behavior:** LittleLogBook should say `invalid command format` and guide users on next steps.
   
<br>
   
4. **Simulating find with wrong parameter inputs:**
   - Input the find command 
       - Example: passing in alphabetic string for find-p command
       - Test case: `find-p ala`
       - Test case: `find-t 1`
       - Test case: `find-n @ !`
       - Test case: `find-c !`
   - Press enter
   - **Expected behavior:** LittleLogBook should state the valid inputs that are allowed to guide user.

<box type="info" seamless="true">

**Note:** To continue testing other commands, use `list` command as guided by LittleLogBook GUI to escape the filtered view.

</box>

----------------------------------------------------------------------------------------------------------------------------------

### Favourite contacts

1. **Simulating adding a contact to favourites for the first time:**
    - Add a new contact to a LittleLogBook
    - Input the `fav` command with index of newly added contact 
        - Test case: `fav 1` assuming added contact is the first contact
    - Press enter
    - **Expected behavior:** LittleLogBook should show a successful message with the name of the added contact. There will be a star icon indicated next to that contact.

<br>

2. **Simulating removing a contact from favourites:**
    - Add a new contact to the LittleLogBook
    - Do the process of adding that contact to favourites _(Refer to point 1 in this section)_
    - Input the favourite command with index of that specific contact
        - Test case: `fav 1` assuming added contact is the first contact
    - Press enter
    - **Expected behavior:** LittleLogBook should show succesful message with information on the contact that is removed from favourites. The star icon next to the contact will disappear.

<br>
   
3. **Checking `list` behaviour:**
    - Add two new contacts to the LittleLogBook
    - Do the process of adding one of the two contacts to favourites _(Refer to point 1 in this section)_
        - Test case: `fav 2` assuming added contacts are the first and second respectively
    - Input `list` command
    - Press enter
    - **Expected behavior:** LittleLogBook should show those added to favourites on the top of the list

----------------------------------------------------------------------------------------------------------------------------------

### Edge Cases and Error Handling
1. **Command case sensitivity**
    - Test: `ADD`, `Add`, `add`<br>**Expected**: only commands in lower case should work (case-insensitive).

1. **Parameter order variations**
    - Test: `add p/98765432 n/John Doe` (reverse order)<br>**Expected**: Should work correctly.

1. **Extra spaces in commands**
    - Test: `add   n/John   Doe   p/98765432`<br>**Expected**: Should handle gracefully (trim spaces).

----------------------------------------------------------------------------------------------------------------------------------

### System commands

##### Basic system operations

1. **Listing all contacts**
    - Test case: `list`
    - **Expected:** All contacts displayed with favourites at top.

<br>

2. **Clearing all contacts**
    - Test case: `clear`
    - **Expected:** Confirmation and removal of all contacts.

<br>

3. **Viewing help**
    - Test case: `help`
    - **Expected:** Help window opens with link to our user guide.

<br>

4. **Exiting application**
    - Test case: `exit`
    - **Expected:** Application closes gracefully.

**Note:** These instructions provide a starting point for testers. Testers should perform additional *exploratory* testing beyond these specified cases, including:
- Testing concurrent operations
- Testing boundary conditions
- Testing error recovery
- Testing UI responsiveness with different data volumes
