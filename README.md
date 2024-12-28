# ReliaQuest Coding Solution by Dave Romanek

#### General Comments about my Solution
- In general I follow the Single Responsibility Principle and Separation of Concerns principle for improved maintainability, refactoring, reuse, and understandability of code.
- I value test driven development as a way to ensure that I deliver quality solutions.  So I focused on a lot of unit and integration tests and ended up with 89 tests in total.
- I did try to go beyond the original request in the assessment in an attempt to showcase additional experience and knowledge I have that you might find useful.
- I will admit that I aimed for an ambitious solution that would show that I do have a lot of experience and that I am willing to work hard and while I got a lot of it done I did run out of time to do everything I had hoped to do.


## Gotchas/Oddities I found and adjusted to
#### JSON prefix naming strategy used in MockEmployee
I choose to solve this using the same prefix naming strategy in my Employee model class.

#### EmployeeController's Interface has deleteEmployeeById but MockEmployeeController has deleteEmployee where it needs the request body to provide the name not the id of the employee to delete
I adjusted the controller to find the name of the employee with the id provided in the request parameter to my endpoint and then pass that into the MockEmployeeController's endpoint in the DeleteMockEmployeeInput's name field.



## Design Decisions
#### Logging
I created an AppLogger class to be used to handle all logging in the app in a consistent way with additional helpful information and formating.  I used AppLoggerProperties to allow for a property to set the logging level of the app logging.  ie... DEBUG, INFO, WARNING and ERROR.  From experience I understand the benefits of using configuration properties that can be managed.

#### Aspect Oriented Programming
I added a couple of aspects to show my knowledge of using Aspects (specifically an ErrorHandlingAspect and RetryAspect) and how they can implement common functionality in a single location while allowing business logic to be cleaner and creating more maintainable code.
I added a couple of libraries to the API's build.gradle to support the use of aspects:
    implementation 'org.aspectj:aspectjrt:1.9.16' // Add AspectJ Runtime
    implementation 'org.aspectj:aspectjweaver:1.9.16' // Add AspectJ Weaver

#### Spring's RestTemplate
Even though it wasn't requested I used a RestTemplate bean with Spring configuration properties for the connect and read timeout settings.  

#### Spring's RetryTemplate
Even though it wasn't requested I did opt to use Spring's RetryTemplate.  I used Spring configuration properties to set the nummber of max retries and interval between retries.
I added the following spring library to the API's build.gradle to support this functionality:
    implementation 'org.springframework.retry:spring-retry:1.3.1' // Add spring-retry

Unfortunately, I am not sure I am happy with where I ended up with the BackOffPolicy I switched to during the last day of my work on this project.  I originally went with a ExponentialBackOffPolicy that would retry up to 4 times at 1 second, 2 seconds, 4 seconds and 8 seconds later which I think would be a sweet spot automatic retries for most applications to try to guarantee success while not making the end user wait too long for the results.  However last night, upon reviewing the server's RandomRequestLimitInterceptor I discovered that the 429 error code, Too Many Requests, could lock out calls for between 30 and 90 seconds so I had the "brilliant idea" to move to a BackOffPolicy that would let the app automatically retry for up to 100 seconds every 5 seconds to handle the longest possible random backoff time set by the server while completing within 5 seconds of whenever the server would allow us to make another request.  From my test cases this works as it should but it does slow down some of my integration tests quite a bit which isn't the end of the world but it was a bit annoying.  :)  I am not convinced this is a bad solution, however, I my gut tells me there may be problems using this for all types of retries.  However, I just didn't have time to switch it back as I was already up against the deadline for getting this turned in.

I've coded up the RetryAspect to honor the Retry-After response header if one is provided for a Rate Limiting status code response just to show how I would do this but after looking at the Server code I know it is not sent so that code will never be executed in this project.
   


#### LoggingInterceptor
I wrote an ClientHttpRequestInterceptor that I've used in the past to help debug/track Http request traffic.

#### Caching (Scalability/Performance)
I used Spring's built in caching in the EmployeeService which can help performance and scalability.  While Spring's built-in caching does not have all the features I would have liked for this particular project I believe it was sufficient.  The main thing that I wish it had was that it did not allow for time based eviction of the cache.
However, there are 3rd party caching solutions that do offer a much richer feature set.  I've used HazelCast in the past where it offers the following features: distributed caching, with advanced data types support, and with the option to persist the data for recovery.
I've also used MemCache when a lighter weight solution was adequate for the needs of the app but it doesn't support advanced date structures, persistence or distribution.
  

#### Testing 
I added console logging to the tests by modifying the project-conventions.gradle file:
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
In this way I could see pass/failures directly in the console when I ran the tests via gradle.


#### Scalability Considerations
An additional scalability feature I've used in the past when data sets are large would be to implement pagination on the MockEmployeeController.getEmployees() and MockEmployeeService.getMockEmployees().  Then we could use pagination parameters in the EmployeeService's call to the MockEmployeeController's endpoint

#### React UI
I created a React Web UI to test out my service and have included it in the repository, although, I am sure you probably have your own UI you use to test the solutions provided.  I didn't spend any time polishing the UI so it's not super good looking but I did include it in case you wanted to see that I can do Full Stack development.

	
	

## Things I ran out of time to complete
#### Profiles
I had a use case where setting up a testing profile would have been a good idea but it wasn't essential and I never got around to doing it.

#### Too Many Requests Handling
In retrospect, I think I might have moved away from the BackOff Policy that I ended up with in favor of a more standard retry and backoff policy while handling the long delay we needed to implement for the Too Many Requests handling in the RetryAspect where I would have slept for perhaps as much as the full 90 second maximum time before I threw the exception to allow the retry policy to start its retrying.  Or perhaps I would have split the delay between a sleep in the retry aspect and a slightly less long retry policy.

#### Environment Variable Configuration Properties
In some use cases setting up Spring properties to read from Environment variables can be beneficial but I ran out of time to implement this.

#### Code Comments
In general, when I am handing off code for someone else to maintain or integrate with I would have taken a pass to thoroughly comment the code to make sure what I've done is understood and clean but again this is another thing that I just ran out of time to complete.  I was able to do this with the Aspects in case the team hasn't had experience with AOP.

#### Logging to File in addition to Console
I wanted to adjust the project to log to a rotating log file based on the last N runs of the app but I didn't get around to this either.



