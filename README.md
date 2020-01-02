# Kichen Table (spring blog)
Our application will eventually have the following features:

Any user can view the blog jobPosts.
Users can create an account.
Logged in users can create jobPosts.
Logged in users can edit and delete their own jobPosts.

#Design and implement a “Blog” Web application in Spring MVC + MySQL. Implement the following functionality:

#Home

#Featured Posts
Show the last jobPosts ordered by user id.
Show also the last 5 jobPost titles at the home page (as a sidebar) with a link to the jobPost.

#NavBar
Show [Login] and [Register] buttons (when no user is logged in).

#Login
Login in the blog with existing account (username + password).
Show a success message after login or error message in case of problem.
Register
Register a new user in the MySQL database (by username + password + full name).
Show a success message after registration or error message in case of problem.
Logout
Logout the current user.
This [Logout] button is available after successful login only.
View / Create / Edit / Delete Posts (CRUD Operations)
Logged in users should be able to view all jobPosts, create new jobPost (by title + content) / edit / delete their own jobPosts.
Posts are displayed in a table (one row for each jobPost). At each row a link [Edit] and [Delete] should be displayed.
Create jobPost shows a form to enter the jobPost data (title + content). After the form submission, the jobPost is created in the database. Implement field validation (non-empty fields are required).
Edit jobPost fills an existing jobPost data in a form and allows it to be edited. After successful form submission, the jobPost is edited. Implement field validation.
Delete jobPost shows the jobPost to be deleted and asks for confirmation.
View All Users
Logged in users should be able to view all users (username + full name) in a table.
# search-log
# search-log
