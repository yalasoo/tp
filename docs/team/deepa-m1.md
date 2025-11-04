---
layout: default.md
title: "Zeng Jing's Project Portfolio Page"
---

### Project: LittleLogBook

LittleLogBook is a desktop address book application adapted from AddressBook-3 and is designed for Singapore kindergarten teachers to manage information about their students and colleagues. It features a CLI-interface, and has a GUI created with JavaFX. The code is written in Java.

Given below are my contributions to the project.

* **New Feature**: Favourite command 
    * What it does: Allows users to favourite desired contacts. When a contact is added to favourites, it will have a star icon next to it and using `list` command will display the favourite contacts at the top.
    * Justification: This feature enhances the product by enabling users to quickly get the contacts that they might often need.
    * Highlights: This implementation is unique and user friendly as calling the `fav` command on a contact that is in favourites will remove it from favourites. The parser handles errors such as out of bounds index(es) gracefully and command result provides clear feedback on errors and changes made to the favourites by user.

* **Code contributed**: [RepoSense link](https://nus-cs2103-ay2526s1.github.io/tp-dashboard/?search=deepa-m1&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2025-09-19T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=)

* **Enhancements to existing features**:
    * **UI Improvements**: Updated the GUI to display the star icon for favourite command using listeners. (Pull requests [#167](https://github.com/AY2526S1-CS2103T-F14B-1/tp/pull/167), [#137](https://github.com/AY2526S1-CS2103T-F14B-1/tp/pull/137))
    * **Find Commands**: Allows users to cumulatively filter and narrow down to a specific contact using partial information (Pull requests [#369](https://github.com/AY2526S1-CS2103T-F14B-1/tp/pull/369), [#237](https://github.com/AY2526S1-CS2103T-F14B-1/tp/pull/237), [#131](https://github.com/AY2526S1-CS2103T-F14B-1/tp/pull/131), [#66](https://github.com/AY2526S1-CS2103T-F14B-1/tp/pull/66)).
    * **Testing**: Wrote additional tests for find commands, model manager and favourite commands' related files which consistently hit high code coverages.

* **Documentation**:
    * **User Guide**:
        * Added documentation for the four different `find` commands, `list` command, `help` command and `fav` commands with examples and expected outcomes
        * Enhanced FAQ section, Glossary, Command Summary and Target User section
    * **Developer Guide**:
        * Edited ModelClassDiagram.puml
        * Added the MSS for `find` command
        * Added the manual testing section for `find`, `list` and `favourite` commands

* **Community**:
    * **PRs reviewed** (with non-trivial review comments): PR [#62](https://github.com/AY2526S1-CS2103T-F14B-1/tp/pull/62),[#82](https://github.com/AY2526S1-CS2103T-F14B-1/tp/pull/82), [#129](https://github.com/AY2526S1-CS2103T-F14B-1/tp/pull/129)
    * **Team Collaboration**: Actively participated in team discussions and planning sessions, helping to resolve integration issues between different features
    * **Reported bugs and suggestions for my team and other teams**: Reported 17 bugs during PE-D for another team and multiple alpha-bugs for my own team (Pull requests: [#174](https://github.com/AY2526S1-CS2103T-F14B-1/tp/issues/174), [#202](https://github.com/AY2526S1-CS2103T-F14B-1/tp/issues/202))

* **Tools**:
    * **CI**: Set up the CI workflow in github

    <br>

**Thank you Note**:
  Huge shoutout to my amazing team from whom I learnt a lot. This journey was an enjoyable one despite the late nights and bug fixing because of **EACH and EVERYONE** of you:<br>
    [Theresia](https://github.com/theresiaong),
    [Wei Feng](https://github.com/Hypovolemic),
    [Yatong](https://github.com/yalasoo),
    [Zeng Jing](https://github.com/zjaoyuki)!
 
 To more projects together :)