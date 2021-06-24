### single-stone-demo



### Build Process
This process assumes that maven is installed on your machine.
</br>
1: Clone the Repo to your local machine: This can be done via: https://github.com/lbc2k1385/single-stone-rest-assesment.git
</br>
2:For Eclipse users:
</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	a) Open up your workspace and proceed to File 
	</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	b) File -> Import
	</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	c) Existing Maven Project
	</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	d) Browse to the local repo created earlier
	</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	e) Select the project
	</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	f) Click Finish
	</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	e) To build the project right click on the project and perform Maven Update Project -> Run As -> Install
	</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	g) Run the application.

</br>
3: Via command line:
</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	a) Open a terminal/shell
</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	b) Navigate to the project root: It should look something like: /Users/lucascoffey/Projects/singlestone-rest-demo
</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	c) Enter 'mvn install'. This will create a jar inside of the target folder.
</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	
	d) Navigate to the target folder and enter 'java -jar singlestone-rest-demo-0.0.1-SNAPSHOT.jar'
</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	e) You should see the application start in the terminal.	

### Database Model
The database model is designed to ensure referential integrity.  It is based on the assumption that 1 contact can have 1 address and multiple phone types. In real world cases
it is likely that 1 contact can have many addresses, but I based this structure based on how I saw the json representation in the assessment document.
A note on the jpa mappings.  I found that the cascade annotations were not working as expected.  For example, I would expect the CASCADE.ALL would take care of the parent
child relationship on delete operations.  I found this not to be the case so additional jpa operations were done to ensure that foreign keys were removed prior to deleting certain
entities.  I found this to be a particular problem in the Contact -> Address mapping.  This may be an issue that could be solved with more time, but for the purposes of the assessment.
I decided to accept the solution as is.  As a final note, H2 is used for the database. It is embeded within the application
so you should have no trouble accessing it.  The H2 console is located here: http://localhost:8080/h2-console/.  Entity
creation will be automatically generated at runtime.

### HATEOAS
HATEOAS is enabled via the spring framework for basic contact crud operations.  The only exception is for the call-list resource that has no additional operations that can be performed specifically on the call-list.

### Swagger UI
Spring boot has also provides a swagger ui page build in for ease access to resource operations.  It can be accessed here:
http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config

