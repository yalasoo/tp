---
layout: default.md
title: "Theresia's Project Portfolio Page"
---

### Project: LittleLogBook

LittleLogBook is a desktop contact management application designed specifically for Singapore kindergarten teachers to efficiently manage student and parent information. Adapted from AddressBook (Level 3), it features a CLI-centric interface optimized for keyboard-driven workflows in fast-paced educational environments.

The application was developed in Java over a 6-week engineering cycle, maintaining a clean codebase with no external dependencies while prioritizing robust functionality and user efficiency.

-----

#### Contribution to Project
##### New Features

**1. Attendance Command**

* **What it does**: Allows teachers to mark student attendance with statuses (present, late, sick, absent) or remove attendance records for specific dates. 
* **Justification**: This feature transforms LittleLogBook from a basic contact manager into a comprehensive classroom management tool, eliminating the need for separate attendance systems. 
* **Highlights**: Implementation required creating a new attendance storage system within Person class, date validation against student birthdays, and batch processing for multiple students. The command handles both individual and range-based indexing with strict date boundary checks.

**2. Attendance Download Command**

* **What it does**: Enables teachers to download attendance reports as CSV files for individual students or entire classes, supporting both daily and monthly reports. 
* **Justification**: Provides actionable data for parent meetings and administrative reporting while maintaining Singapore's educational compliance requirements. 
* **Highlights**: Required implementing CSV generation utilities, file management with unique naming to prevent overwrites, and flexible reporting options (individual vs class-based, daily vs monthly). Integration with existing attendance storage system while ensuring data integrity.

**3. Note Command**

* **What it does**: Enables teachers to add, edit, or remove descriptive notes for each contact, supporting up to 500 characters of additional information. 
* **Justification**: Provides essential context for student management (allergies, parent instructions, behavioral notes) and colleague coordination within the same platform. 
* **Highlights**: Required extending the Person class with note class, implementing character limit validation, and ensuring proper GUI display with text truncation for long notes. Integration with existing add and edit commands while maintaining data persistence.

**4. Sort Command**

* **What it does**: Allows users to sort contacts by name, class, or tag in ascending or descending order, with favorites always displayed at the top. 
* **Justification**: Enhances user productivity by enabling quick organization of large contact lists and prioritizing important contacts. 
* **Highlights**: Implementation involved creating custom comparators, maintaining favorite contact prioritization during sorting, and ensuring case-insensitive sorting for better user experience. Required modifications to the filtered list management in Model.

**5. Calendar GUI**

* **What it does**: Displays a visual monthly calendar showing student attendance records with color-coded status indicators (present, late, sick, absent) for quick overview. 
* **Justification**: Provides teachers with an intuitive, at-a-glance understanding of student attendance patterns and history without needing to check individual dates manually. 
* **Highlights**: Implementation required creating a custom calendar widget with date cell rendering, integrating with existing attendance data storage, and implementing navigation between months. The calendar dynamically updates based on attendance records and provides visual feedback for marked dates with proper color coding for different status types.

-----

##### Enhancements to Existing Features

**1. UI/UX** <br>
Implemented a new pastel blue and white color scheme throughout the application interface, improving visual appeal and readability for extended use.

* **Justification**: The updated color scheme reduces eye strain during prolonged use and creates a more welcoming, child-appropriate interface suitable for educational environments.
* **Highlights**: Required comprehensive CSS updates across all UI components, ensuring color consistency while maintaining accessibility standards. The implementation involved testing color contrast ratios and updating both light and dark theme elements to align with the new design mock-ups.

-----

##### Documentation

**1. UG** <br>
* Implemented comprehensive command documentation with parameter validation tables 
* Added color-coded parameters (pink for mandatory, grey for optional) for better visual clarity 
* Structured command documentation with consistent format: Purpose, Format, Parameters, Samples, Outputs 
* Added `attendance`, `attendanceD`, `note`, and `sort` command documentation with detailed validation rules 
* Included visual examples with screenshots for complex features 
* Formatted FAQ section with categorized questions and answers 
* Added CSV opening guide for user convenience

**2. DG**
* Enhanced storage diagram to include AttendanceCsvStorage class and dependencies 
* Formatted use cases with colored boxes and consistent numbering 
* Improved manual testing appendix with structured test cases and clear prerequisites 
* Contributed to planned enhancements section with specific future features

-----

##### Community and Team

* Actively participated in every team meeting with ideas, suggestions, and feature recommendations.
* Consistently reviewed team member's PRs with detailed comments and/or feedbacks to help improve the code. ([PR #167](https://github.com/AY2526S1-CS2103T-F14B-1/tp/pull/167)).
* Identified and reported 21 bugs during Alpha-testing phase, contributing to significant improvements in application stability and user experience.

-----

##### Other

* All code contribution: [RepoSense](https://nus-cs2103-ay2526s1.github.io/tp-dashboard/?search=&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2025-09-19T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=TheresiaOng&tabRepo=AY2526S1-CS2103T-F14b-1%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

-----

#### Personal Note

**BIG** thank you to all of my team members who contributed massively to the success of LittleLogBook:
* [Deepa](https://github.com/deepa-m1)
* [Wei Feng](http://github.com/Hypovolemic)
* [Yatong](http://github.com/yalasoo)
* [Zeng Jing](http://github.com/zjaoyuki)

This journey wouldn't be the same without each of you. Our meetings taught me so much about teamwork and communication, and those late-night coding sessions where we share our frustration over CodeCov brought us closer together in the best way possible.

All in all, it was such a blessing to be able to cook with you chefs!𓅭