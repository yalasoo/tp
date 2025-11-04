---
layout: default.md
title: "Yang Yatong's Project Contributions Page"
---

### Project: LittleLogBook

LittleLogBook is a desktop address book application adapted from AddressBook-3. The user interacts with it using a CLI, and it has a GUI created with JavaFX. It is written in Java, and has about 15 kLoC.

Given below are my contributions to the project.

* **New Feature**: Added Pop-up windows for delete command
    * What it does: allows users to view contact information and confirm deletion before a contact is permanently removed.
    * Justification: This feature enhances user experience by preventing careless mistakes and ensuring that contacts are only deleted intentionally.
    * Highlights: The implementation involved integrating a `DeletePopup` and `InfoPopup` into the existing DeleteCommand. It required modifications to the command execution flow to pause for user confirmation.

* **New Feature**: Added Pop-up windows for clear command
    * What it does: allows users to double-check their action before irreversibly clearing all contacts.
      * Justification: This feature improves reliability and safety by preventing users from accidentally deleting their entire contact list, ensuring that such critical actions are done deliberately and consciously.
    * Highlights: The feature was implemented using `InfoPopup` integrated into the existing ClearCommand. It required handling command interruptions properly and ensuring the pop-up dialog works consistently across both CLI and GUI interactions.

* **Code contributed**: [RepoSense link](https://nus-cs2103-ay2526s1.github.io/tp-dashboard/?search=&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2025-09-19T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=yalasoo&tabRepo=AY2526S1-CS2103T-F14b-1%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

* **Enhancements to existing features**:
    * **Testing**: Wrote additional tests for existing features and new features

* **Documentation**:
    * **User Guide**:
        * Added documentation for the `delete` command with examples and expected outcomes
    * **Developer Guide**:
        * Added implementation details for delete and edit command
        * Added MSS for delete and edit command
        * Created detailed sequence diagrams with breakdowns for delete command

* **Community**:
    * **PRs reviewed** Consistently reviewed teammates’ PRs with constructive feedback
    * **Team Collaboration**: Actively participated in team discussions and planning sessions, helping to resolve integration issues between different features
    * **Reported bugs and suggestions**: Actively tested during Alpha and filed detailed bug reports with descriptions

**Personal Note**:

A big thank you to all my teammates! This course has been enjoyable because of you guys! :D
[Deepa](https://github.com/deepa-m1)
[Theresia](https://github.com/theresiaong),
[Wei Feng](https://github.com/Hypovolemic),
[Zeng Jing](https://github.com/zjaoyuki)!
Looking forward to more cooking sessions together, Masterchefs!


