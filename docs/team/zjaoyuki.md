---
layout: default.md
title: "Zeng Jing's Project Portfolio Page"
---

### Project: LittleLogBook

LittleLogBook is a desktop address book application adapted from AddressBook-3. The user interacts with it using a CLI, and it has a GUI created with JavaFX. It is written in Java, and has about 15 kLoC.

Given below are my contributions to the project.

* **New Feature**: Added Birthday field to contacts
  * What it does: allows users to store and view birthday information for each contact in the address book.
  * Justification: This feature enhances the product by enabling users to keep track of important personal dates, making the address book more comprehensive for relationship management.
  * Highlights: This enhancement affected existing commands and storage structure. The implementation required modifying the core `Person` class, updating JSON storage adaptation, and ensuring data validation for date formats. It also involved updating both the GUI and CLI components to display birthday information consistently.

* **New Feature**: Implemented View command
  * What it does: allows users to view detailed information of a specific contact in a pop-up window by their index in the displayed list.
  * Justification: This feature improves user experience by providing a focused view of individual contact details without cluttering the main list view, while keeping the main window accessible.
  * Highlights: The implementation required creating a new command parser, `ViewWindow` class, and ensuring proper index validation against the filtered list. The feature automatically closes the view window when other commands are executed to maintain data consistency.

*   **New Feature**: Implemented Remind command
  * What it does: lists contacts with birthdays occurring within a user-specified number of days from today.
  * Justification: transforms the birthday field from passive data into an active reminder tool, improving user engagement and the application's practical value.
  * Highlights: required implementing complex date comparison logic that handles year-rollovers and leap years, creating a new command and parser, and filtering the model based on a dynamic time window.

* **Code contributed**: [RepoSense link](https://nus-cs2103-ay2526s1.github.io/tp-dashboard/?search=&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2025-09-19T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=zjaoyuki&tabRepo=AY2526S1-CS2103T-F14b-1%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

* **Enhancements to existing features**:
  * **UI Improvements**: Updated the GUI to display the new birthday field and enhanced the contact card layout for better readability and visual appeal (Pull requests #XX, #YY)
  * **Testing**: Wrote additional tests for existing features and new features, increasing test coverage from X% to Y% (Pull requests #ZZ, #AA)

* **Documentation**:
  * **User Guide**:
    * Added documentation for the `view` command with examples and expected outcomes
    * Did cosmetic tweaks to existing documentation of features `add`, `edit`
  * **Developer Guide**:
    * Added implementation details for birthday field
    * Created detailed sequence diagrams with breakdowns for View and Remind command
    * Updated design considerations for the enhanced `Person` class and architecture decisions

* **Community**:
  * **PRs reviewed** (with non-trivial review comments): PR [#252](https://github.com/AY2526S1-CS2103T-F14B-1/tp/pull/252), [#131](https://github.com/AY2526S1-CS2103T-F14B-1/tp/pull/131), [#236](https://github.com/AY2526S1-CS2103T-F14B-1/tp/pull/236)
  * **Team Collaboration**: Actively participated in team discussions and planning sessions, helping to resolve integration issues between different features
  * **Reported bugs and suggestions**: Suggestion [issue #181](https://github.com/AY2526S1-CS2103T-F14B-1/tp/issues/181), [issue #179](https://github.com/AY2526S1-CS2103T-F14B-1/tp/issues/179)

* **Tools**:
  * Utilised JUnit for writing comprehensive test cases
  * Employed JavaFX for UI enhancements and creating the ViewWindow pop-up
  * Used GitHub Projects for task management and team collaboration
