# relation-data-exporter
Data Exporter for Relational Databaase

The tools was primarily used for exporting required data from production MySQL to developer's local database since the production
database is too large and is not fit in developer's machine. The exported file is simple SQL (insert into).
All foreign key constraints are used for getting required data. They can also be used to get the data from the table
that refers to it. (2 ways)

The exporter is basically a spring boot command-line application. To run the program, you need java

		java [JAVA_OPTS] -jar <app.jar> <command> [parameters]");

There are 2 commands currently:-
* export <config.yaml>"
   to export data using given configuration");
* printConfig
   to print example configuration yaml based on current database");

available java options:- ");

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


## Settings

The settings file is in yaml format. It looks like the following:-

		output: /test/test2/test3/test4.sql
        default:
          selectors: []
          referring:
            included: false
            batchSize: 10
            limit: null
        references:
          - fk: TABLE_2.t1_id
            pk: TABLE_1.id
            referring:
              included: true
              batchSize: 100
              limit: 1000
        tables:
          - name: TABLE_2
            selectors: [ "WHERE id = 5", "WHERE id = 6", "WHERE id = 5" ]
          - name: TABLE_3
            selectors: [ "" ]

The output is filename of the exported sql.

The default is default settings for all references and tables.

The references is list of foreign key references. Normally, the references are auto populated using JDBC metadata.
However they can be added using the settings. The referring tells the exporter to load the referring data that use the primary key.

The tables tells the export to initially load specified rows.
