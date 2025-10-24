---
layout: default.md
title: "Zeng Jing's Project Portfolio Page"
---

### Project: LittleLogBook

LittleLogBook is a desktop address book application used for teaching Software Engineering principles. The user interacts with it using a CLI, and it has a GUI created with JavaFX. It is written in Java, and has about 10 kLoC.

Given below are my contributions to the project.

* **New Feature**: Added Birthday field to contacts
    * What it does: allows users to store and view birthday information for each contact in the address book.
    * Justification: This feature enhances the product by enabling users to keep track of important personal dates, making the address book more comprehensive for relationship management.
    * Highlights: The implementation required modifying the core Person class, updating storage functionality, and ensuring data validation for date formats. It also involved updating both the GUI and CLI components to display birthday information.

* **New Feature**: Implemented View command
    * What it does: allows users to view detailed information of a specific contact by their index in the displayed list.
    * Justification: This feature improves user experience by providing a focused view of individual contact details without cluttering the main list view.
    * Highlights: The implementation required creating a new command parser, result display mechanism, and ensuring proper error handling for invalid indices.

* **Enhancements to existing features**:
    * **UI Improvements**: Enhanced the user interface to better display contact information including the new birthday field, improving readability and visual appeal.
    * **Testing**: Significantly improved test coverage by adding comprehensive unit and integration tests for new features (Birthday field and View command) as well as existing functionality.

* **Documentation**:
    * User Guide:
        * Documented the new `view` command with examples and expected outcomes.
    * Developer Guide:
        * Added implementation details and sequence diagrams for the Birthday field and View command features.
        * Updated design considerations for the enhanced Person class.

* **Community**:
    * **PR Reviews**: Conducted thorough code reviews for teammates' pull requests, providing constructive feedback on code quality, design patterns, and test coverage.
    * **Team Collaboration**: Actively participated in team discussions and planning sessions, helping to resolve integration issues between different features.

* **Tools**:
    * Utilized JUnit for writing comprehensive test cases.
    * Employed JavaFX for UI enhancements.
    * Used GitHub Projects for task management and collaboration.