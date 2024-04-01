package tutorial_000.languageNewFeatures;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


public class _001_VirtualThreads {

    public static void main(String[] args) {
        /*
         * In java 21, Virtual Threads are finally finalized and ready to use. But what exactly are Virtual Threads?
         *
         * Virtual Threads allow developers to make applications working with various threads, but by programming in the
         * sequential thread-per-request style. Sequential code is not only easier to write and read but also easier to
         * debug since we can use a debugger to trace the program flow step by step.
         *
         * Writing scalable applications with sequential code is made possible by allowing many virtual threads to share
         * a platform thread (the name given to the conventional threads provided by the operating system). When a virtual
         * thread has to wait or is blocked, the platform thread will execute another virtual thread. That allows us to
         * run up to several million (yes, millions) virtual threads with just a few operating system threads. We even don’t
         * have to change existing Java code. We simply tell our application framework to use virtual threads instead of
         * platform threads.
         *
         * So in just few words, Virtual Treads will be very useful because :
         * - It allow to easily overcome issues like "I have many threads waiting for database/API/service responses, but
         *   not so many CPU available on my machine. What did I do if all CPU might be used by thi waiting threads ?".
         * - Reactive, asynchronous programming is very powerful but not always easy to work with, nor understood by
         *   all developers. Virtual Threads allow to work in sequential style.
         *
         * How Virtual Threads works.
         *
         * Virtual threads looks like normal threads from a Java code perspective, but they are not mapped 1:1 to operating
         * system threads. Instead, there is a pool of so-called carrier threads onto which a virtual thread is temporarily
         * mapped ("mounted"). As soon as the virtual thread encounters a blocking operation, the virtual thread is removed
         * ("unmounted") from the carrier thread, and the carrier thread can execute another virtual thread (a new one or
         * a previously blocked one).
         *
         * The "virtual-threads-mapped-to-carrier-threads.webp" image depict this M:N mapping from n virtual threads to
         * m carrier threads and thus to m operating system threads.
         *
         * The carrier thread pool is a ForkJoinPool : a pool where each thread has its own queue and “steals” tasks from
         * other threads' queues should its own queue be empty. Its size is set by default to
         * Runtime.getRuntime().availableProcessors() and can be adjusted with the VM option
         * jdk.virtualThreadScheduler.parallelism.
         *
         * Let see an example :
         * - We have 3 tasks
         * - Each task execute 4 times
         * - Between its own executions, each task block a relatively long time
         * If we mapped this 3 tasks to a single carrier thread, the execution of this example on a single CPU is depicted
         * in "single-carrier-3-tasks.png" image. Here we well understand that blocking operations no longer block the
         * executing carrier thread. So we may process a large number of blocking requests concurrently using a small pool
         * of carrier threads.
         *
         * Virtual Threads in action with code example
         *
         * Let's take a case in which we have 1 000 tasks, each one waiting 1s to execute then retuning a number. This
         * use case is implement by the "Task" class.
         *
         * Now, we may create a pool of 100 platform threads (ie NON-virtual threads) to process our 1000 tasks :
         */
        executePlatformThreads(1_000);

        /*
         * So, we see that this code take approximately 10 secondes to execute, because each non-virtual thread had to
         * process ten tasks sequentially, one after the other, each lasting about one second.
         *
         * Now, we copy-paste the exact same code, but just replace Executors.newFixedThreadPool(100) by
         * Executors.newVirtualThreadPerTaskExecutor(). This will no longer create a pool of 100 platform thread, but
         * instead creates a new virtual thread for each task.
         */
        executeVirtualThreads(1_000);
        executeVirtualThreads(10_000);
        executeVirtualThreads(100_000);
        executeVirtualThreads(1_000_000);
        /*
         * Here we see there is no match concerning code execution time between platform and virtual threads. On my computer,
         * I got :
         * - Patform threads - 1_000 tasks - approx. 10sec
         * - Virtual threads - 1_000 tasks - approx. 1sec
         * - Virtual threads - 10_000 tasks - approx. 1.05sec
         * - Virtual threads - 100_000 tasks - approx. 1.1sec
         * - Virtual threads - 1_000_000 tasks - approx. 3.5sec
         *
         * Thus, Virtual Threads are executed incredibly faster than Platform Threads.
         */

        /*
         * How to Create Virtual Threads?
         *
         * We have seen that Executors.newVirtualThreadPerTaskExecutor() is one way to create virtual threads : it creates
         * one new virtual thread per task. The two methods below also enable to create virtual threads :
         */
        Thread.startVirtualThread(() -> {
            // code to run in thread
        });

        // Here Thread.ofVirtual() returns a VirtualThreadBuilder whose start() method starts a virtual thread.
        // There is a corresponding Thread.ofPlatform() that returns a PlatformThreadBuilder via which we can start a platform thread.
        Thread.ofVirtual().start(() -> {
            // code to run in thread
        });

        /*
         * Both builders implement the Thread.Builder interface. This allows us to write flexible code that decides at runtime
         * whether it should run in a virtual or in a platform thread :
         */
        Thread.Builder threadBuilder = createThreadBuilder(true);
        threadBuilder.start(() -> {
            // code to run in thread
        });
        /*
         * Also, we can find out if code is running in a virtual thread with Thread.currentThread().isVirtual().
         *
         * Using Virtual Threads with Spring.
         *
         * Spring framework's team released the following article explaining when and how we may use Virtual Threads with
         * Spring : https://spring.io/blog/2022/10/11/embracing-virtual-threads.
         *
         * We'll note this results in all rest controllers running on virtual threads, which may be fine for most use cases,
         * but not for CPU-heavy tasks – those should always run on platform threads.
         */

        /*
         * Advantages of Virtual Threads
         *
         * First, they are inexpensive:
         * - They can be created much faster than platform threads: it takes about 1 ms to create a platform thread, and
         *   less than 1 µs to create a virtual thread.
         * - They require less memory: a platform thread reserves 1 MB for the stack and commits 32 to 64 KB up front,
         *   depending on the operating system. A virtual thread starts with about one KB. However, this is true only for
         *   flat call stacks. A call stack the size of a half megabyte requires that half megabyte in both thread variants.
         * - Blocking virtual threads is cheap because a blocked virtual thread does not block an OS thread. However, it's
         *   not free as its stack needs to be copied to the heap.
         * - Context switches are fast because they are performed in user space, not kernel space, and numerous optimizations
         *   have been made in the JVM to make them faster.
         *
         * Second, we can use virtual threads in a familiar way:
         * - Only minimal changes have been made to the Thread and ExecutorService APIs.
         * - Instead of writing asynchronous code with callbacks, we can write code in the traditional blocking thread-per-request
         *   style.
         * - We can debug, observe, and profile virtual threads with existing tools.
         */

        /*
         * What Are Virtual Threads Not?
         *
         * Virtual threads don't have only advantages. Let's first look at what virtual threads are not, and what we cannot
         * or should not do with them:
         * - Virtual threads are not faster threads – they cannot execute more CPU instructions than a platform thread in
         *   the same amount of time.
         * - They are not preemptive: while a virtual thread is executing a CPU-intensive task, it is not unmounted from
         *   the carrier thread. So if you have 20 carrier threads and 20 virtual threads that occupy the CPU without blocking,
         *   no other virtual thread will be executed.
         * - They do not provide a higher level of abstraction than platform threads. You need to be aware of all the subtle
         *   things that you also need to be aware of when using regular threads. That is, if a virtual thread accesses shared
         *   data, you have to take care of visibility issues, you have to synchronize atomic operations, and so on.
         */

        /*
         * What Are the Limitations of Virtual Threads?
         *
         * 1. Unsupported Blocking Operations
         * Although the vast majority of blocking operations in the JDK have been rewritten to support virtual threads, there
         * are still operations that do not unmount a virtual thread from the carrier thread:
         * - File I/O
         * - Object.wait()
         * In these two cases, a blocked virtual thread will also block the carrier thread. To compensate for this, both
         * operations temporarily increase the number of carrier threads – up to a maximum of 256 threads, which can be
         * changed via the VM option jdk.virtualThreadScheduler.maxPoolSize.
         *
         * 2. Pinning
         * Pinning means that a blocking operation that would normally unmount a virtual thread from its carrier thread
         * does not do so because the virtual thread has been “pinned” to its carrier thread – meaning that it is not
         * allowed to change the carrier thread. This happens in two cases:
         * - inside a synchronized block
         * - if the call stack contains calls to native code
         *
         * 3. No Locks in Thread Dumps
         * Thread dumps currently do not contain data about locks held by or blocking virtual threads. Accordingly, they
         * do not show deadlocks between virtual threads or between a virtual thread and a platform thread.
         */

        /*
         * Thread Dumps With Virtual Threads
         *
         * The conventional thread dumps printed via jcmd <pid> Thread.print do not contain virtual threads. The reason
         * for that is that this command stops the VM to create a snapshot of the running threads. This is feasible for
         * a few hundred or even a few thousand threads, but not for millions of them.
         *
         * Therefore, a new variant of thread dump has been implemented that does not stop the VM (accordingly, the
         * thread dump may not be consistent in itself) but which includes virtual threads in return. This new thread
         * dump can be created with one of these two commands:
         * - jcmd <pid> Thread.dump_to_file -format=plain <file>
         * - jcmd <pid> Thread.dump_to_file -format=json <file>
         * The first command generates a thread dump similar to the traditional one, with thread names, IDs and stack
         * traces. The second command generates a file in JSON format that also contains information about thread containers,
         * parent containers, and owner threads.
         */

        /*
         * Conclusion
         *
         * You should use virtual threads if you have many tasks to be processed concurrently, which primarily contain
         * blocking operations. This is true for most server applications. However, if your server application handles
         * CPU-intensive tasks, you should use platform threads for them.
         */
    }

    private static Thread.Builder createThreadBuilder(boolean isVirtual) {
        if(isVirtual) {
            return Thread.ofVirtual();
        }
        return Thread.ofPlatform();
    }

    private static void executeVirtualThreads(int tasksNumber) {
        System.out.println("Will begin Virtual threads execution for " + tasksNumber + "tasks.");
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Task> tasks = new ArrayList<>();
            for (int i = 0; i < tasksNumber; i++) {
                tasks.add(new Task(i));
            }

            long time = System.currentTimeMillis();

            List<Future<Integer>> futures = executor.invokeAll(tasks);

            long sum = 0;
            for (Future<Integer> future : futures) {
                sum += future.get();
            }

            time = System.currentTimeMillis() - time;

            System.out.println("Virtual threads finish : sum = " + sum + "; time = " + time + " ms");
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("---------------------------");
    }

    private static void executePlatformThreads(int tasksNumber) {
        System.out.println("Will begin platform threads execution for " + tasksNumber + "tasks.");
        // Here we'll note that ExecutorService is auto-closeable since Java 19 (it can be surrounded with a try-with-resources
        // block). At the end of the block, ExecutorService.close() is called, which in turn calls shutdown() and awaitTermination()
        // (and possibly shutdownNow() should the thread be interrupted during awaitTermination()).
        try (ExecutorService executor = Executors.newFixedThreadPool(100)) {
            List<Task> tasks = new ArrayList<>();
            for (int i = 0; i < tasksNumber; i++) {
                tasks.add(new Task(i));
            }

            long time = System.currentTimeMillis();

            List<Future<Integer>> futures = executor.invokeAll(tasks);

            long sum = 0;
            for (Future<Integer> future : futures) {
                sum += future.get();
            }

            time = System.currentTimeMillis() - time;

            System.out.println("Platform Thread finish : sum = " + sum + "; time = " + time + " ms");
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("---------------------------");
    }
}