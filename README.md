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
My implementation uses a Reentrant Lock to ensure that only one thread is in the "labrynth" while-loop at a time. The guests are able to enter the labrynth multiple times, as stated in the problem description. The strategy behind finding out at what point every guest has eaten a cake is determined before anyting begins. A guest at random is chosen to be the "decider" guest. This decider guest will keep track of how many times he finds the cake missing in the labrynth. The other guests should only eat the cake one time and one time only. Using this strategy, the guests won't have to  communicate to eachother. Once the decider guest finds the cake missing an "n guests" amount of times, he can deduce that all guests have eaten the cake once, and announces it to the Minotaur, ending the game and breaking all threads out of their while-loops.

Problem 2: <br/>

The 1st strategy discussed in the project description really has no advantages to the other two strategies as guests would just be prone to crowding around the door and they wont have a garauntee that they will even be able to have access to the room at some point. <br/>
The 3rd strategy had the disadvantage of a slow runtime since guests would be fighting over a queue-like structure. I found strategy 2 to be the best for the purposes of this problem.

For this problem, I implemented the second strategy that is mentioned in the problem statement. I used a boolean variable called availableBusy to let each guest know if the room was available or not. If the room wasnt available, they will re-loop and wait until it becomes available. Once the guest sees that the room is availabe, they will enter the room, make sure to change the availableBusy variable to false, indicating that the room is busy. The guest will then be checked to see if they have seen the vase yet, if not, change the threads hasSeen variable to true. Otherwise, wait for a bit and then leave the room, setting the availablility of the room to true on its way out. Once it is determined that all guests have seen the vase once (AKA hasSeen is true for all threads) then the program will output and terminate.

# Experimental Evaluation:
Processor used for testing: 11th Gen Intel(R) Core(TM) i7-1165G7 @ 2.80GHz (8 CPUs), ~2.8GHz <br/>
Ubuntu was used for compiling

My experimental evaulation for the first problem consisted of testing a various amount of methods and data structures to see which one gave us the most accurate and efficient results as stated in the Proof of Correctness. 
