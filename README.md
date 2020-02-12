# crawler_test

**Goal**

Crawler takes in a url and fetches all the images from the url recursively. 
User can specify the number of threads for each job, 
number of threads must a non-negative int and at most equal to number of processors on host machine.


**Implementation**

**Controller**

There are 3 exposed rest calls

'Post' /crawler/crawl -- begins crawl job

'Get' /crawler/{jobId}/progress -- retrieves progress

'Get' /crawler/{jobId}/result -- retrieves final results


**Data Struct**
 
Job

All the information related to the data manipulation is here, 
the information on the jobid, each crawljob, (1 crawl job == 1 url) and the status of each crawjob 

InputPayload/Responses

Here is all the data that is handled on the controller level, all information going in and out
Coming in is the list of urls + number of threads (InputPayload)
Going out is the status and progress on each job (ProgressResponse) and the results (ResultResponse)

**Service**

The only point of mention is when starting crawl, 
we create a job, and create all of the crawljobs that are associated with each url

**Cache**

Here I decided to opt for a in memory solution only. 
This simplifies the design, 
the data does not really need to be stored since it will loose its freshness over time, 
this application is more suited for ad-hoc data retrieval.

There is a DAO interface that can be still expanded to use a nosql db in the future

**Threading**

For the multithreaded solution I opted for a simple threadpool that is created at request time.
We then submit each of the crawljobs as a runnable and order a shutdown of threadbool 
when the jobs are all done.

Ideally for performance we would have a threadpool created on startup, and we could re-use the threads when idle.
This would have a good overhead savings on thread creation.


**Crawler**

When the Crawler starts a Crawl, 
it will setup timers for benchmarks, 
initialize a visited cache, initialize the results list and start the crawl call (which is recursive)

the visited cache serves to keep track of which links have been visited, to avoid wasteful re-visiting of the same link
the results list is to hold the results as they are being gathered.

the return state for the recursive function is checking the 3 conditions

    - have we visited this url before? if yes return
    - have we gotten to depth 2? if yes return
    - if url is empty, then return

Otherwise we get a list of all links on a page, 
visit all those links, 
grab all of the images, and increment depth, repeat


