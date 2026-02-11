COMPETITOR MANAGEMENT SYSTEM - COURSEWORK REPORT
================================================

1. Introduction
---------------
The Competitor Management System is a Java-based application designed to manage, track, and report on competitors in various competitions. The main objective of this coursework was to build a robust system that integrates a graphical user interface (GUI) with a persistent MySQL database, allowing for user registration (Admin/Player), competition scoring, and detailed performance reporting. The outcomes include a functional desktop application with specialized dashboards for different roles, automated score calculations, and comprehensive statistical analysis.

2. Methodology
--------------
The development followed an Iterative and Incremental software development lifecycle. Each component—starting from core data models (Competitor, Name) to the data access layer (DAOs) and finally the GUI (Swing)—was built, tested, and refined.
Tools Used:
- Language: Java 25 (JDK)
- IDE: Eclipse/VS Code
- GUI: Java Swing (JFrame, JPanel, Layout Managers)
- Database: MySQL with JDBC for connectivity
- Version Control: Git
- Testing: JUnit 5 for unit testing
- Documentation: Javadoc for API documentation

3. Implementation
-----------------

3.1. Competitor Class Development
Logic: The Competitor class encapsulates all relevant data for a participant. Key methods include:
- getOverallScore(): Calculates the average only for "active" scores (>= 0), treating -1 as unattempted.
- getFullDetails() & getShortDetails(): Formats data into readable strings for console or report output.
- loadAllFromDB(): A static method that performs a join between Competitors and CompetitorScores tables to hydrate a list of objects.
Extra Attributes:
- level: Categorizes competitors into Beginner, Intermediate, or Advanced for fairer comparisons.
- country: Added to provide demographic context and support geographical reporting.

3.2. MYSQL and Arrays
The Competitor class uses an integer array (int[] scores) of size 5 to store multiple competition marks. This array is mapped directly from columns score1 through score5 in the MySQL 'competitorscores' table.
The overall score is calculated by iterating through the array, summing all valid scores (those not equal to -1), and dividing by the count of valid scores.

Database Schema:
- Users: (userID, username, password, role) - Handles authentication.
- Competitors: (competitorID, firstName, lastName, country) - Stores profile data.
- CompetitorScores: (competitorID, level, score1, score2, score3, score4, score5) - Stores competition results.
- Questions: (questionID, questionText, optionA-D, correctOption, difficulty) - Stores quiz content.

Connection: JDBC is established via a central DBConnection class that uses DriverManager.getConnection() with local MySQL URL, credentials, and error handling.

3.3. MySQL Integration and Reports
Classes like AdminReportsFrame and PlayerReportsFrame interact with the CompetitorDAO and the Competitor class to fetch data.
Data presentation:
- JTable: Used for displaying lists of players and their scores.
- Leaderboard: Shows top performers sorted by their average scores.
- Statistics Panel: Displays counts, frequency distributions, and overall averages.

3.4. Error Handling
- Input Validation: GUI frames (like LoginRegisterFrame) check for empty fields or invalid formats before submission.
- Database Errors: try-catch-finally blocks manage SQL exceptions. If a connection fails, the user is notified via JOptionPane messages rather than the app crashing.
- Data Integrity: Handled at the application level by checking for null returns and using wasNull() in JDBC result sets.

3.5. Testing
The test directory contains JUnit test cases:
- CompetitorTest: Verifies that the overall score calculation handles various score counts correctly.
- NameTest: Ensures initials and string representations are formatted as expected.
- UserManagerTest: Checks for duplicate registration prevention and credential validation.

4. Javadoc Comments
-------------------
The CompetitorList class, along with others, features Javadoc style comments. Standard tags like @param (for parameters), @return (for return types), and @see (for cross-references) are used. This allows for the automated generation of professional HTML documentation (found in the 'docs' folder).

5. Class Diagram
----------------
The class diagram comprises three layers:
- Models: Name, Competitor, Question (Core data structures).
- Logic: CompetitorList, CompetitorManager (Data management).
- DAOs: UserManager, CompetitorDAO, QuestionDAO (Database interactions).
- GUI: AdminDashboard, PlayerDashboard, QuizFrame, etc. (User interaction).
Associations: CompetitorList contains multiple Competitor objects (1:*). Each Competitor possesses a Name object. QuizFrame aggregates five Question objects from the database.

6. Test Cases
-------------
| Functionality | Input | Expected Output | Status |
|---------------|-------|-----------------|--------|
| Login         | Valid Admin | Open Admin Dashboard | PASS |
| Register      | New Player | User created in DB | PASS |
| Quiz Scoring  | 5/5 Correct | Score 5 saved to DB | PASS |
| Report Gen    | Click Report| JTable filled with data| PASS |

7. Status Report
----------------
The application meets all the primary specifications. It successfully implements the multi-role login, database persistence, score management using arrays, report generation, and automated documentation.

8. Known Bugs and Limitations
-----------------------------
- UI Responsiveness: Large datasets in report tables may slow down loading times.
- Persistence: The database connection is currently hardcoded for 'localhost'.
- Testing Coverage: While core logic is tested, UI-specific automated tests are not yet implemented.

9. Conclusion
-------------
The project successfully delivers a manageable system for competition tracking. It demonstrates a clear separation of concerns using the DAO pattern and provides a user-friendly interface. Future improvements would include implementing a more secure password hashing mechanism and allowing for dynamic database configuration.
