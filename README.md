\# Zoo Management System



Console application for managing a zoo, built in Java with a MySQL database as the final project for the Introduction to Databases course at FIT, Mediterranean University.



\## Features



\- \*\*Categories:\*\* CRUD with a hierarchical structure (subcategories), with protection against deleting categories that have subcategories or animals

\- \*\*Animals:\*\* CRUD with status, enclosure and category, with protection against deleting animals that have examinations, tours or caretakers

\- \*\*Veterinary examinations:\*\* CRUD, filtering by animal

\- \*\*Animal–tour assignments:\*\* CRUD and listing tours by date

\- \*\*Reports:\*\*

&#x20; - Top caretaker by number of distinct animal categories, per specialization

&#x20; - Veterinarians with an above-average number of examinations in a given period

&#x20; - Enclosures with an above-average number of animals, filtered by enclosure type

\- Input validation for all user entries (dates, numbers, required fields)



\## Database design



14 tables, including:

\- \*\*Inheritance:\*\* `zaposleni` (employee) specialized into `staratelj` (caretaker) and `veterinar` (veterinarian)

\- \*\*Hierarchy:\*\* self-referencing foreign key for category/subcategory

\- \*\*Many-to-many relationships\*\* through junction tables (animal–tour, animal–caretaker, veterinarian–certificate)



The ER model, relational model and technical documentation are in the \[`docs`](docs) folder.



\## Project structure



```

src/

├── app/       Main class and console menu

├── dao/       Data access objects (JDBC)

├── db/        Database connection

├── model/     Domain classes

└── reports/   Report queries

database/      SQL script for schema and sample data

docs/          ER model, relational model, technical documentation

lib/           MySQL Connector/J

```



\## Running locally



1\. Create the database by running `database/zooloski\_vrt.sql` in MySQL or MariaDB.

2\. The connection in `Main.java` uses `localhost:3306`, user `root`, no password. Change it if your setup differs.

3\. Compile and run (Windows):



```

javac -d bin -cp lib\\mysql-connector-java-8.0.30.jar src\\app\\\*.java src\\dao\\\*.java src\\db\\\*.java src\\model\\\*.java src\\reports\\\*.java

java -cp "bin;lib\\mysql-connector-java-8.0.30.jar" app.Main

```



The user interface and database are in Montenegrin.

