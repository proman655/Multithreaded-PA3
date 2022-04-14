# Multithreaded-PA3
Programming Assignment 3 for COP4520. Coded in Java using Visual Studio Code

Filenames:<br/>
Problem 1: BirthdayPresentParty.java <br/>
Problem 2: AtmosphericModule.java

# Run Instructions:
1. Open Terminal
2. Navigate to directory containing BirthdayPresentParty.java and/or AtmosphericModule.java
3. Compile using: 
```bash
  javac BirthdayPresentParty.java
```
or
```bash
  javac AtmosphericModule.java
```
4. Run using: 
```bash
  java BirthdayPresentParty
```
or
```bash
  java AtmosphericModule
```
# Problem Statement
Problem 1: The Birthday Presents Party (50 points)

The Minotaur’s birthday party was a success. The Minotaur received a lot of presents from his guests. The next day he decided to sort all of his presents and start writing “Thank you” cards. Every present had a tag with a unique number that was associated with the guest who gave it. Initially all of the presents were thrown into a large bag with no particular order. The Minotaur wanted to take the presents from this unordered bag and create a chain of presents hooked to each other with special links (similar to storing elements in a linked-list). In this chain (linked-list) all of the presents had to be ordered according to their tag numbers in increasing order. The Minotaur asked 4 of his servants to help him with creating the chain of presents and writing the cards to his guests. Each servant would do one of three actions in no particular order:

Take a present from the unordered bag and add it to the chain in the correct location by hooking it to the predecessor’s link. The servant also had to make sure that the newly added present is also linked with the next present in the chain.
Write a “Thank you” card to a guest and remove the present from the chain. To do so, a servant had to unlink the gift from its predecessor and make sure to connect the predecessor’s link with the next gift in the chain.
Per the Minotaur’s request, check whether a gift with a particular tag was present in the chain or not; without adding or removing a new gift, a servant would scan through the chain and check whether a gift with a particular tag is already added to the ordered chain of gifts or not.
As the Minotaur was impatient to get this task done quickly, he instructed his servants not to wait until all of the presents from the unordered bag are placed in the chain of linked and ordered presents. Instead, every servant was asked to alternate adding gifts to the ordered chain and writing “Thank you” cards. The servants were asked not to stop or even take a break until the task of writing cards to all of the Minotaur’s guests was complete.

After spending an entire day on this task the bag of unordered presents and the chain of ordered presents were both finally empty!

Unfortunately, the servants realized at the end of the day that they had more presents than “Thank you” notes. What could have gone wrong?

Can we help the Minotaur and his servants improve their strategy for writing “Thank you” notes?

Design and implement a concurrent linked-list that can help the Minotaur’s 4 servants with this task. In your test, simulate this concurrent “Thank you” card writing scenario by dedicating 1 thread per servant and assuming that the Minotaur received 500,000 presents from his guests.

Problem 2: Atmospheric Temperature Reading Module (50 Points)

You are tasked with the design of the module responsible for measuring the atmospheric temperature of the next generation Mars Rover, equipped with a multi-core CPU and 8 temperature sensors. The sensors are responsible for collecting temperature readings at regular intervals and storing them in shared memory space. The atmospheric temperature module has to compile a report at the end of every hour, comprising the top 5 highest temperatures recorded for that hour, the top 5 lowest temperatures recorded for that hour, and the 10-minute interval of time when the largest temperature difference was observed. The data storage and retrieval of the shared memory region must be carefully handled, as we do not want to delay a sensor and miss the interval of time when it is supposed to conduct temperature reading. 

Design and implement a solution using 8 threads that will offer a solution for this task. Assume that the temperature readings are taken every 1 minute. In your solution, simulate the operation of the temperature reading sensor by generating a random number from -100F to 70F at every reading. In your report, discuss the efficiency, correctness, and progress guarantee of your program.

# Proof of Correctness:

Problem 1: <br/>
My implementation uses a Lock Free Linked List to store the elements in the chain, in order. I also use an Atomic Integer variable to record the amount of cards that are produced after removing a gift. I used a stack to store the unordered bag of gifts. This bag was constructed by pushing an n amount of elements to a stack and then shuffling that stack. As the threads call upon the run function, they enter a while loop that will keep them in there until everything is finished. Threads are responsible for 3 tasks concurrently, one is to remove the gifts from the bag stack and put it into the ordered lock free linked list. Another task is to "write a thank you note" and remove that gift from the lock free linked list. The final task that can be done concurrently is to check if a gift is in the chain. In distributing the threads to do these 3 tasks, a solution with a decent runtime can be accomplished.

Problem 2: <br/>
This implementation used a Reentrant Lock system to maintain mutual exclusion. A Priority Queue to determine both the top 5 and bottom 5 temperature readings. Every thread has a checked boolean value that is set to true when it has reported its temperature recordings for a given hour. As every thread enters its run function, they enter 2 while-loops. The outer one iterates until the amount of inputted hours to simulate is over. The inner while-loop checks to see if all the threads have been checked yet. If not, it will keep looping. The if-statement within the inner while-loop checks to see if the current thread has reported its temperature readings for a given hour. If not, it adds its temperature reading for that given iteration and then is marked as checked. That is all done after acquiring the Reentrant lock that I mentioned earlier, and releasing the lock after those operations have concluded. One thread is marked as isReporter, which gives that thread the role of printing out that hours readings and data once the hour has passed. This is also contained within a lock/unlock. The use of the threads.sleep() function was necessary to maintain syncronization between all the threads.


# Experimental Evaluation:
Processor used for testing: 11th Gen Intel(R) Core(TM) i7-1165G7 @ 2.80GHz (8 CPUs), ~2.8GHz <br/>
Ubuntu was used for compiling

My experimental evaulation for the first problem consisted of testing a various amount of methods and data structures to see which one gave us the most accurate and efficient results as stated in the Proof of Correctness. 

For the second problem, I used the thread.Sleep() function to sort of synchronize the threads after each hour has passed. This is a janky solutionm, but it was the only one I could figure out that would fix the synchronization issue.
