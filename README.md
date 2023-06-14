# BankingSystemSimulation
This is a simulation for banking system using Java and Docker. Install it on your computer and run the command on terminal.

==PLEASE FOLLOW THE INSTRUCTION TO RUN THE APPLICATION==

*Before executing any file, make sure you have downloaded Java and Docker db2 on your computer*

1. Download the .zip and extract it. All the file must be in the same folder to run successfully.

2. Open cmd/powershell or any similar terminal on your computer. If the terminal location is not in the folder yet, locate it using cd command. 

3. Compile all the Java file using "javac *.java" (remove the quotes)

4. Run Docker on your computer in another command window and execute p1_create.sql file

docker exec -ti mydb2 bash -c "su - db2inst1"
db2 -tvf p1_create.sql

*Note: You always have to run db2 -tvf p2_create.sql everytime before you run the application in order to drop all old data and avoid duplications.

5. Return to the cmd/powershell where you compiled Java. Run the following command:

java -cp ";./db2jcc4.jar" ProgramLauncher ./db.properties

This command will first run the db.properties to add some basic data to the tables. 

*Note: DO NOT add >test2.out at the end of the command or you won't see the interface as everything go straight to the .out file.

6. After it finishes executing the db.properties file, you will see a command line interface of the banking application. It would look like this.

==Welcome to the Self Services Banking System! - Main Menu==
1. New Customer
2. Customer Login
3. Exit

7. In customer login, enter 0 for both id and pin to enter administration menu.
