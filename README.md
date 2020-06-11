# AgendaCheck
Project to help me, and my team better adjust work schedule of teammates to customer in-store traffic.
It takes data from to .xlsx files and creates new report.

# Technology stack

- Java
- Maven
- Apache POI
- JFreeChart

# Problems solved by program

- program creates report about how well-fitted schedule is forecasted turnover.
- it gives sheet for whole store and every retail department,
- program takes schedule hours and forcasted turnover from two .xlsx files,
- names of department often vary in files (for exemple "Mountains" in one file can be "hiking" in another), program matches them.
- program sugest changes in schedule to better fit forecasted turnover,

# What I have learn so far, in this project

- Apache POI,
- that dates are counted as days since 01.01.1900,
- testing by printing ends while project grows,
- proper naming matters, or You will lose it,
- class should be dedicated to specific actions
- mind-mapping helps to keep track of classes connections, 
- I get my first ever out of memory Java heap space error with 10M+ objects (infinity loop was to blame)

# To do

- FileInput check
- cellStyles from map to geters
- user interface 
