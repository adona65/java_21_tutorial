package tutorial_000.languageNewFeatures;

public class _004_PatternMatchingForSwitch {
    public static void main (String[] args) {
        /*
         * Introduced by java 17 as preview feature, Pattern Matching for Switch are finally released with java 21.
         * Combined with Record Patterns, this feature allow switch statements and expressions to be formulated over any
         * object.
         *
         */
        printPatternMachting(new Position(5, 3));
        printPatternMachting("str");
        printPatternMachting("My Long String.");
        printPatternMachting(100L);
        /*
         * In addition of getting shorter code, with Pattern Matching for Switch the compiler performs an “analysis of
         * exhaustiveness”. That means the switch statement or expression must cover all possible cases or contain a default
         * branch. Since the Object class in the previous example is arbitrarily extensible, a default branch is mandatory.
         *
         * In contrast, a default branch is not necessary if the switch covers all possibilities of a sealed class hierarchy.
         * Beware : if we got a sealed interface with two possibilities, and use those two in a switch statement without
         * default branch, everything will work. But if the code is the change by adding a third possibility, then we will
         * get a compilation error, forcing us to either add a default branch, or the third possibility into ou switch.
         */

        /*
         * Qualified Enum Constants
         *
         * Until now, we could only implement a switch expression over enum constants using a “guarded pattern” – i.e.,
         * a pattern combined with when. Java 21 simplify it :
            // Switch line prior to java 21
            case CompassEnum  d when d == CompassDirection.NORTH -> .....
            // Thanks to java 21
            case CompassEnum.NORTH -> .....
         *
         */
    }

    private record Position(int x, int y) {}
    private static void printPatternMachting(Object obj) {
        switch (obj) {
            // Without Pattern Matchin for Switch, the following code would have been longer :
            // if (obj instanceof String s && s.length() > 5).......
            case String s when s.length() > 5 -> System.out.println("Long String : " + s.toUpperCase());
            case String s                     -> System.out.println("Short String : " + s.toLowerCase());
            case Integer i                    -> System.out.println("Integer : " + i * i);
            case Position(int x, int y)       -> System.out.println("Position : " + x + "/" + y);
            default                           -> System.out.println("Something else.");
        }
    }
}
