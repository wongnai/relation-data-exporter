#Data Exporter for Relational Database

The tool was primarily used for exporting required data from production database (wongnai uses MySQL) to developer's local database
since the production database is too large. Our machines have limit disk space. Moreover, it takes several hours to import back
to developer's machine (although all of our dev hdds are SSD).

The exported file is a list of simple SQLs -- insert into statements -- which should be working on any RDBMs (hopefully).

#Relation
The tool firstly reads all tables' metadata so that it can automatically export all required rows (using foreign key constraints).
For example, if we have 2 tables -- A and B -- and one fk from B.A_ID to A.ID, one a row of B is extracted, the row of A referred by
B.A_ID is also be extracted.

It is able to export relation in reverse manner using explicit configuration (in yaml). This is disabled by default since tuning it
on for all tables resulting in exporting almost every rows from the production database. Using the above tables, A and B, if a row of A
is extracted, rows of B which B.A_ID matches A.ID also be exported.

#Transformation
It's not good idea to export all data out. Some data are too sensitive e.g. user's email or password. The tools also provides simple data
transformation so that some data can be changed before saving to the files.

#Build The Project
It is written in pure Java using spring boot and JDBC. Gradle is used as  built-tool. To build the project, just execute the build
target using gradle wrapper included in the project's source.

		gradlew build

#Usage
The exporter is basically a spring boot command-line application. To run the program, you need java

		java [JAVA_OPTS] -jar <app.jar> <command> [parameters]

There are 2 commands currently:-
* export <config.yaml>"
   to export data using given configuration
* printConfig
   to print example configuration yaml based on current database

available java options:-

* spring.datasource.url
   jdbc connection e.g. jdbc:mysql://localhost:3306/wongnai?useUnicode=true&characterEncoding=UTF-8
* spring.datasource.username
   db username
* spring.datasource.password"
   db password


	 	java -Dspring.datasource.url="jdbc:mysql://localhost:3306/wongnai?useUnicode=true&characterEncoding=UTF-8 \
			 -Dspring.datasource.username=usr \
			 -Dspring.datasource.password=pwd \
			 -jar de-0.1.jar export mydb.yaml;


# Settings
The settings file is in yaml format. It looks like the following:-

		output: /test/test2/test3/test4.sql		# output file to save SQL
        default:								# all default settings go here
          selectors: []							#  no selectors - select nothing from table
          referring:							#  relation settings
            included: false						#  don't do reverse data retrieval
        references:								# references settings
          - pk: TABLE_1.id              	    #  defined fk (table + column) of this refernce
            fk: TABLE_2.t1_id				 	#  defined pk (table + column) of this refernce
            referring:							#  reverse extract
              included: true					#  enable reverse extract
              batchSize: 100					#  100 ids per fetching
              limit: 1000						#  limit result of fetching
        transformations:						# transformations
		  - type: "email"						#  email transformation
			column: "w_user\\.email"			#  regex to match target column name
			from: "^((?!wongnai).)*$"			#  regex to match column's value
			to: "guest@test.wongnai.com"		#  value to change to
        tables:									# tables to initially fetch data
          - name: TABLE_2						#  table name TABLE_2
            selectors: [ "WHERE id = 5", "WHERE id = 6", "WHERE id = 5" ]	# selectors
          - name: TABLE_3						#  table name TABLE_3
            selectors: [ "" ]					#  select everything

