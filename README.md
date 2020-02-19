# crawler_test

**Goal**

Crawler takes in a url and fetches all the images from the url recursively. 
User can specify the number of threads for each job, 
number of threads must a non-negative int and at most equal to number of processors on host machine.


**Implementation**

**Controller**

There are 3 exposed rest calls

'Post' /crawler/crawl -- begins crawl job

body: {
        "urls" : ["http://4chan.org", "https://yahoo.fr"],
      	"threadCount": 4	
      }

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

**Threading/Crawl**

For the multithreaded solution there is an scheduling threadpool which handles concurrent crawl jobs.
There is a current arbitrary max of 4 concurrent jobs at once.

For each request we create another threadpool at request time. 
This will be used for the recursive calls until all threads are used, then the following recursive call wait for resources.


A good way to further optimize this approach is to split the crawl into 2 phases IO and parsing.
We would have 2 independent threadpool's for each. 

We would have more IO threads grabbing links and feeding it into a queue where the smaller number of parse threads would be taking jobs from the queue.
Most of the time loss is due to IO so this would give a net speedup.


