package tutorial_000.languageNewFeatures;

public class _003_RecordPatterns {
    public static void main (String[] args) {
        /*
         * Introduced by java 19 as preview feature, Record Patterns are finally released with java 21. Here we will see
         * some examples about Record Patterns and how they can be combined with Pattern Matching for instanceof and
         * Pattern Matching for switch to access the fields of a record without explicit casts and without using access
         * methods.
         */

        /*
         * Record Patterns and Pattern Matching for instanceof
         *
         * Let say we have an object and want to perform action on it depending on its class. We may do so by using
         * Pattern Matching or Record Pattern for instanceof :
         */
        printForInstanceOf(new Position(1,5));
        printForInstanceOf(new Situation(8, 3));
        printForInstanceOf("My String.");

        System.out.println("------------------------------");

        /*
         * Record Patterns and Pattern Matching for switch
         *
         * The previous example might be written by using Pattern Matching or Record Pattern for switch :
         */
        printForSwitch(new Position(33,2));
        printForSwitch(new Situation(20, 22));
        printForSwitch("My String.");

        System.out.println("------------------------------");

        /*
         * Nested Record Patterns
         *
         * We can not only match to a record whose fields are objects or primitives. We can also match on a record whose
         * fields are also records. Let say we have the following Record with nested Records :
         */
        Path path = new Path(new Position(1,5), new Situation(21,15));
        /*
         * As for past examples, we might print information about Path Record :
         */
        printWithoutRecordPattern(path);
        printWithRecordPattern(path);
        printNestedRecordPattern(path);

        System.out.println("------------------------------");

        /*
         * Usefulness of Record Patterns
         *
         * Let say we have an interface (Location) that is implement by two Records (Position2D and Position3D). The now,
         * we get a third Generic Record (GPS) with two Location parameters of the same type :
         */
        GPS gps = new GPS(new Position2D(4,5), new Position2D(3,1));
        /*
         * Now, thanks to record patterns we may test if GPS Record use Position2D or Position3D easily with a single line :
         */
        printInterfaceImplementation(gps);


    }

    private record Position(int x, int y) {}
    private record Situation(int x, int y) {}
    private record Path(Position from, Situation to) {}

    private static void printForInstanceOf(Object o) {
        // Use Pattern Matching for instanceof.
        if (o instanceof Position p) {
            System.out.printf("InstanceOf Pattern Matching : object is a position: %d/%d%n", p.x(), p.y());
        // Use record pattern. We access x and y directly instead of using "p.x()" and "p.y()" like previous line.
        } else if (o instanceof Situation(int x, int y)) {
            System.out.printf("InstanceOf Record pattern : object is a Situation: %d/%d%n", x, y);
        } else {
            System.out.printf("InstanceOf Object is something else : %s%n", o);
        }
    }

    private static void printForSwitch(Object o) {
        switch (o) {
            // Use Pattern Matching for instanceof.
            case Position p -> System.out.printf("Switch Pattern Matching : object is a position: %d/%d%n", p.x(), p.y());
            // Use record pattern. We access x and y directly instead of using "p.x()" and "p.y()" like previous line.
            case Situation(int x, int y) -> System.out.printf("Switch Record pattern : object is a Situation: %d/%d%n", x, y);
            default -> System.out.printf("Switch Object is something else : %s%n", o);
        }
    }

    private static void printWithoutRecordPattern(Object o) {
        switch (o) {
            case Path p -> System.out.printf("WithoutRecordPattern : Object is a Path: %d/%d -> %d/%d%n", p.from().x(), p.from().y(), p.to().x(), p.to().y());
            default -> System.out.printf("WithoutRecordPattern : Object is something else : %s%n", o);
        }
    }

    private static void printWithRecordPattern(Object o) {
        switch (o) {
            case Path(Position from, Situation to) -> System.out.printf("WithRecordPattern : Object is a Path: %d/%d -> %d/%d%n", from.x(), from.y(), to.x(), to.y());
            default -> System.out.printf("WithRecordPattern : Object is something else : %s%n", o);
        }
    }

    private static void printNestedRecordPattern(Object o) {
        switch (o) {
            case Path(Position(int x1, int y1), Situation(int x2, int y2)) -> System.out.printf("NestedRecordPattern : Object is a Path: %d/%d -> %d/%d%n", x1, y1, x2, y2);
            default -> System.out.printf("NestedRecordPattern : Object is something else : %s%n", o);
        }
    }

    private sealed interface Location permits Position2D, Position3D {}
    private record Position2D(int x, int y) implements Location {}
    private record Position3D(int x, int y, int z) implements Location {}
    private record GPS<P extends Location>(P from, P to) {}

    private static void printInterfaceImplementation(Object o) {
        switch (o) {
            /*
             * Here without record patterns, we would have written something like :
                case GPS gps when gps.from() instanceof Position2D from && gps.to() instanceof Position2D to
             */
            case GPS(Position2D from, Position2D to) -> System.out.printf("InterfaceImplementation : object is a 2D position: %d/%d -> %d/%d%n", from.x(), from.y(), to.x(), to.y());
            case GPS(Position3D from, Position3D to) -> System.out.printf("InterfaceImplementation : object is a 3D position: %d/%d/%d -> %d/%d/%d%n", from.x(), from.y(), from.z(), to.x(), to.y(), to.z());
            default -> System.out.printf("NestedRecordPattern : Object is something else : %s%n", o);
        }
    }
}
