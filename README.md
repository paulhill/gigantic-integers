Problem
-------

Create a thread-safe, multi-threaded queue-processing service.
The service should read from an input Queue of Strings and carry out an operation on them.

Assume that the operation is so processor-intensive that you’ll need to use parallel threads to keep up with the rate at which items are added to the queue.
(Assume the service will eventually be deployed on a machine with many cores.)

Implement two operations, both of which assume that the strings are gigantic integers (e.g. 100+ digits):

* Maintain a count of all those that are larger than some cut-off value.
* Similarly, find the maximum and minimum values.

The service should provide a thread-safe way for other threads to query the current best answer (either the count so far, or the maximum/minimum values encountered so far).

Write a utility that will pump numbers into your queue from a (possibly very large) text file.

Solution
--------

I wrote a little command line utility that processes a series of files containing one gigantic integer on each line.

It uses RxJava to stream the processing into multiple Threads.
You can see the threads in the output.

To build from source:

```
./gradlew clean build
```

To run the utility:
```
java -jar build/libs/giganticintegers-1.0-SNAPSHOT.jar 0
```

You'll see this output

```
[main] 1546039402043 Hello!
[main] 1546039402059 Please provide the path to a file full of tasty integers (defaults to ./input.txt)
```
From left to right is printed the Thread name, the epoc milliseconds, and a message.

The program is now waiting for you to input the location of a file full of integers

If you just hit enter it will look for a file in your current directory called input.txt. You'll find an example in the root of the project directory.

Enter a valid file path and you'll see something like this:

```
[Timer-0] 1546039654167 count=0 min=999999999999999999999999999... max=-99999999999999999999999999...
```

This line is the separate timer thread checking in on the processor.
For small input files you won't do much because the file will be processed pretty quickly
but if you give it a file with a million integers or more it will show you the progress every 100ms.

```
[RxComputationThreadPool-6] 1546039654262 complete
[RxComputationThreadPool-8] 1546039654262 complete
[RxComputationThreadPool-7] 1546039654262 complete
[RxComputationThreadPool-2] 1546039654262 complete
[RxComputationThreadPool-4] 1546039654262 complete
[RxComputationThreadPool-5] 1546039654262 complete
[RxComputationThreadPool-3] 1546039654262 complete
[RxComputationThreadPool-1] 1546039654262 complete
```

These lines are the computation rails (Threads) completing. 
The number of rails will match the number of cores on the machine you execute the utility on.

```
[RxComputationThreadPool-1] 1546039654265 count=50 min=-17422706223666315951578128... max=174138716982088079464145020...
```
This line is the completion of the final computation thread and outputs the count, max and min 
in fetching technicolour if your console supports it.

```
[main] 1546039654267 Nom nom nom... Processed that file in 200ms
```
The main thread now reports that the file has been consumed.
```
[main] 1546039654267 Please provide the path to a file full of tasty integers (defaults to ./input.txt)
```
...and asks for another file to consume. 
This will repeat ad-nauseum until you Ctrl+c out of the program.
(at which point any other spawned threads will also terminate, no zombies wandering about here)

Tests can be run thus:

```
./gradlew cleanTest test
```
and you'll see this output
```
> Task :test

giganticintegers.GiganticIntegersProcessorTest > process PASSED
```

Well, that's about it for the running the utility.

Notes
-----

If you want to simulate a processor intensive operation (because count/max/min really isn't) 
you can add a Thread.sleep(1) at line 62 of GiganticIntegersProcessor and feed it a file with 1,000,000 lines.

I made an AtomicBigInteger class because I needed an Atomic Reference to a BigInteger and a PrintUtils class. 
Neither is particularly production ready (not a lot of defensive parameter checking and such).

There is also a MakeIntegers class I used to generate test files full of gigantic integers.

I think I'll call it a day there and push this up for review.
I hope it passes muster. 
It's not the sort of thing one would launch into production without some more polish, testing, instrumentation and packaging but how that is done differs in every org and it's christmas and I'd rather spend time with my kids.

Enjoy!
