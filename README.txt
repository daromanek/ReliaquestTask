NOTES: 
Use: gradlew spotlessApply to resolve any syntax errors
gradlew api:clean
gradlew api:build --info
gradlew api:test --rerun-tasks 
gradlew test --tests "com.reliaquest.api.test.EmployeeServiceIntegrationTest"

Not seeing whether tests pass or fail using gradlew api:test or gradlew api:bootstrap - gradlew test --tests "com.example.YourTestClass"
Can get tasks by running gradlew api:tasksCan get dependencies by running gradlew api:dependencies


TO DO:
Log to file as well as console
Apply more comments to my code if time




DONE:(run gradlew in D:\ReliaQuestTask\java-employee-challenge)
Get Retry and Error Handler Aspects integration tests in EmployeeServiceIntegrationTests working - commented out currently
Create integration tests to exercise the service and try to get a rate limit failure
Create integration tests to exercise the controller and try to get a rate limit failure
Continue working on unit tests
Add AppLoggerProperties to RestTemplateConfig
Resolve any new compile errors
Increase timeouts on RestTemplate to 100 seconds being the max backoff time in the server will be 90 seconds
Deal with Rate Limiting max backoff period of 90 seconds
Need to deal with rate limit requests that will randomly choose to be enforced in the mock service I am calling
Add unit test for logger package classes
Add unit test for CustomResponseErrorHandler.java
Create more unit tests for DTO tests
Introduce aspects for retry and error handling
Separate the inner classes in RestTemplateConfig each into their own class file
Need to import RestTemplateConfig into this project
Need my Controller to implement the interface - so need to deal with Employee Input (DTO??)
Working on ApiApplicationTest unit Tests but getting error that: D:\ReliaQuestTask\java-employee-challenge\api\src\test\java\com\reliaquest\api\ApiApplicationTest.java:10: error: cannot find symbol @SpringBootTest when I run: gradlew api:bootstrap 
- needed to add the correct import in the Test app: import org.springframework.boot.test.context.SpringBootTest;
Created Controller, Service and Model classesGot Mock Employee API running by typing in gradlew server:bootstrap


NOT DONE/WON'T DO IF CAN GET AROUND IT (FRUSTRATING WASTE OF TIME!!!! AND STILL DIDN'T LEARN WHAT"S WRONG WITH MY CONFIG)
Haven't been able to get the gradle project imported into Eclipse and working correctly 
- usually comes in without Project Facet set to Java (can manually set it) 
- but also doesn't have gradle facet set and it's not in the list (which seems to be a broken configuration)
- Could try converting it over to a maven build to get it working in Eclipse but it may be a waste of more time to do so while it can work on the command line from what I am seeing








