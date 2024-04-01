package tutorial_000.languageNewFeatures;

public class _005_MiscellaneousChanges {
    /*
     * New String Methods
     *
     * The String class has been extended by the following methods :
     * - String.indexOf(String str, int beginIndex, int endIndex) : searches the specified substring in a subrange of the string.
     * - String.indexOf(char ch, int beginIndex, int endIndex) : searches the specified character in a subrange of the string.
     * - String.splitWithDelimiters(String regex, int limit) : splits the string at substrings matched by the regular expression
     *   and returns an array of all parts and splitting strings. The string is split at most limit-1 times, i.e., the last
     *   element of the array could be further divisible.
     */

    /*
     * New StringBuilder and StringBuffer Methods
     *
     * Both StringBuilder and StringBuffer have been extended by the following two methods :
     * - repeat(CharSequence cs, int count) : appends to the StringBuilder or StringBuffer the string cs – count times.
     * - repeat(int codePoint, int count) : appends the specified Unicode code point to the StringBuilder or
     *   StringBuffer – count times. A variable or constant of type char can also be passed as code point.
     */

    /*
     * New Character Methods
     *
     * The following new methods are provided by the Character class:
     * - isEmoji(int codePoint)
     * - isEmojiComponent(int codePoint)
     * - isEmojiModifier(int codePoint)
     * - isEmojiModifierBase(int codePoint)
     * - isEmojiPresentation(int codePoint)
     * - isExtendedPictographic(int codePoint)
     *
     * These methods check whether the passed Unicode code point stands for an emoji or a variant of it.
     */

    /*
     * New Math Methods
     * clamp() method was introduced to replace the following piece of code :
        if (value < min) {
          value = min;
        } else if (value > max) {
          value = max;
        }
     *
     * - int clamp(long value, int min, int max)
     * - long clamp(long value, long min, long max)
     * - double clamp(double value, double min, double max)
     * - float clamp(float value, float min, float max)
     *
     * These methods check whether value is in the range min to max. If value is less than min, they return min; if value
     * is greater than max, they return max.
     */
}
