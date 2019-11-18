# Schoolproject

Small project for teaching purposes.

* Wildfly
* JEE 8 
* Java 11
* Git
* Maven
* MySQL

## Wildfly configuration

Install any Wildfly release you want. I use 18.

Add a user, and place the school.cli script under the bin folder.<br>
Create database school. The script will need a mysql connector under `C:\Users`
to work. 

The script is predefined for `mysql.connector-java-8.0.12.jar`. Change location and version for your own liking.

Start Wildfly, and once running, open a new prompt, and go to the bin folder.<br>
Write `jboss-cli -c --file=school.cli`

It should say outcome success. Write `jboss-cli -c --command=:reload` to restart the server.

To run mvn: `wildfly:undeploy clean:clean wildfly:deploy`

## HTTP Methods

| HTTP | Endpoints |
| ------ | ------ |
| GET All| [/school/student/][1] <br> Lists all available students|
| GET By Email| [/school/student/emails/{email}][2] <br> Returns one student with that unique email. Path parameter: email |
| GET By Name | [/school/student/names/{name}][3]  <br> Returns one or more students. Path parameter: name|
| POST | [/school/student/add][4] <br> Json body: { forename: "", lastName: "", email: "" } |
| PUT | [/school/student/][5] <br> Query parameters: forename, lastname, email|
| PATCH  | [/school/student/][6] <br> Json body: { forename: "", lastName: "", email: "" } |
| DELETE  | [/school/student/{email}][7] <br> Path parameter: email |


   [1]: <https://localhost:8080/school/student/>
   [2]: <https://localhost:8080/school/student/emails/{email}>
   [3]: <https://localhost:8080/school/student/names/{name}>
   [4]: <https://localhost:8080/school/student/add>
   [5]: <https://localhost:8080/school/student>
   [6]: <https://localhost:8080/school/student/>
   [7]: <https://localhost:8080/school/student/{email}>
 