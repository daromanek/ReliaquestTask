NOTES: 
Use: gradlew spotlessApply to resolve any syntax errors
gradlew api:clean
gradlew api:build --info
gradlew api:test --rerun-tasks 
gradlew test --tests "com.reliaquest.api.test.EmployeeServiceIntegrationTest"

Not seeing whether tests pass or fail using gradlew api:test or gradlew api:bootstrap - gradlew test --tests "com.example.YourTestClass"
Can get tasks by running gradlew api:tasksCan get dependencies by running gradlew api:dependencies


TO DO:
Resolve any new compile errors
Continue working on unit tests

Get Retry and Error Handler Aspects integration tests in EmployeeServiceIntegrationTests working - commented out currently

Create integration tests to exercise the service and try to get a rate limit failure
Create integration tests to exercise the controller and try to get a rate limit failure

Log to file as well as console
Add AppLoggerProperties to RestTemplateConfig

Apply more comments to my code if time




DONE:(run gradlew in D:\ReliaQuestTask\java-employee-challenge)
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








I've followed standard Single Responsibility Principle/Separation of Concerns principles.


Added logging to the tests by modifying the project-conventions.gradle file's
tasks.named('test') {
    useJUnitPlatform()
}
to:
tasks.named('test') {
    useJUnitPlatform()
    testLogging {
        events "started", "passed", "skipped", "failed" // Show results for started, passed, skipped, and failed tests
        exceptionFormat "full" // Show full stack trace for failed tests
        showStandardStreams = true // Show standard output and error streams
    }
}

Added TestLoggingAspect to show my knowledge of how to use Aspects.  Set this up just for the test classes.  
Another could be added for the normal classes in a similar way.
Added test to packages for all tests so that I could apply my TestLoggingAspect to only work on the test classes

Created unit tests for each of the functional packages in the app

 
For scalability we could implement pagination on the MockEmployeeController.getEmployees() and MockEmployeeService.getMockEmployees() to support this then we could use pagination parameters in the EmployeeService's call to the MockEmployeeController'e endpoint


Caching - used Spring's built in caching which did not allow for time based eviction, however, there are 3rd party solutions that do allow for this.  I've used HazelCast in the past where distributed caching, with advanced data types support, and with the option to persist the data for recovery.
  I've also used MemCache when a lighterweight solution was adequate for the needs of the app as it doesn't support advanced date structures, persistence or discribution.
  

I've honored the Retry-After header if one is provided for a Rate Limiting status code response, otherwise, i just stick with the standard 
   

Went with FixedBackOffPolicy that would go to more then 90 seconds to deal with worst possible backoff time generated on the server.  Updated RestTemplate ReadTimeout to 120 seconds for same reason.
I went with the FixedBackOffPolicy as I didn't have time to create my own BackOffPolicy


