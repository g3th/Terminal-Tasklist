# Kotlin Tasklist for Terminals
## Implementing a daily task list TUI

A project which implements a task-list for terminals, with simple TUI menu which allows the user to choose different options. The menu now includes a proper print layout, composed of a table which includes time, date, task, and colour-coded priority and due tags.

The task section of the table only has space for 44 characters at most, and the layout class is able to adequately split larger inputs into various chunks, and subsequently organize them into the appropriate format.

Allows the user to:

1) Add Tasks by number (i.e. 1 - Dentist)
2) Add current date and time for tasks (i.e. 1 2023-11-11 11:33)
3) Add a task priority (i.e. 1 2023-11-11 11:33 C (for Critical))
4) Edit every aspect of the created task, from date to rewriting the task itself.

The program will also attach a 'due-date-tag' to the top of the task, next to date/time, to indicate whether the task is overdue or ontime.

Warning: It is recommended to clone and build in IntelliJ or similar IDE (or gradle if you like pain). Building in terminal throws errors like 'java.lang.NoClassDefFoundError: kotlinx/datetime/Clock$System' due to kotlinx.datetime not working properly.
 
Work in progress.
