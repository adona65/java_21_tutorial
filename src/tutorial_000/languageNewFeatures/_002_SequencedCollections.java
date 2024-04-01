package tutorial_000.languageNewFeatures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class _002_SequencedCollections {
    public static void main (String[] args) {
        ArrayList<String> list = new ArrayList<>(Arrays.asList(new String[] { "1", "2", "3" }));

        /*
         * Prior to Java 21, to access the last element of a list we would to something like that :
         */
        System.out.println("Last list element (prior java 21): " + list.get(list.size() - 1));
        /*
         * Java 21 introduce a brand-new method to do so :
         */
        System.out.println("Last list element (java 21): " + list.getLast());

        System.out.println("-------------------------");
        /*
         * Similar additions were made to LinkedHashSet class to retrieve first and last elements :
         */
        LinkedHashSet<String> linkedSet = new LinkedHashSet<>(Arrays.asList(new String[] { "1", "2", "3" }));
        System.out.println("First set element (prior java 21): " + linkedSet.iterator().next());
        System.out.println("First set element (prior java 21): " + linkedSet.getFirst());
        System.out.println("Last set element (java 21): " + linkedSet.getLast());

        /*
         * SequencedCollection Interface
         *
         * Java 21 introduced the interface SequencedCollection for enabling uniform methods for accessing the elements
         * of a collection :
         * - Previously seen getFirst() and getLast()
         * - void addFirst(E) : inserts an element at the beginning of the collection (throw an UnsupportedOperationException
         *   for immutable collections)
         * - void addLast(E) : appends an element to the end of the collection (throw an UnsupportedOperationException
         *   for immutable collections)
         * - E removeFirst() : removes the first element and returns it (throw an UnsupportedOperationException
         *   for immutable collections)
         * - E removeLast() : removes the last element and returns it (throw an UnsupportedOperationException
         *   for immutable collections)
         * - SequencedCollection reversed() : returns a view on the collection in reverse order (may be used to iterate
         *   backward over the collection) => changes to the original collection are visible in the returned backward
         *   and vice versa.
         */

        /*
         * SequencedSet Interface
         *
         * SequencedSet inherits from Set and SequencedCollection. It provides no additional methods but overrides the
         * reversed() method to replace the SequencedCollection return type with SequencedSet.
         *
         * /!\ addFirst(E) and addLast(E) have a special meaning in SequencedSet: if the element to be added is already
         * in the set, it will be moved to the beginning or end of the set, respectively.
         */

        /*
         * SequencedMap Interface
         *
         * As collections (List, Set) and maps represent two separate class hierarchies, java 21 introduced SequencedMap
         * interface to offers easy access to first and last element of ordered maps. This interface also define convenient
         * methods :
         * - Entry<K, V> firstEntry() : returns the first key-value pair of the map
         * - Entry<K, V> lastEntry() : returns the last key-value pair of the map
         * - Entry<K, V> pollFirstEntry() : removes the first key-value pair and returns it
         * - Entry<K, V> pollLastEntry() : removes the last key-value pair and returns it
         * - V putFirst(K, V) : inserts a key-value pair at the beginning of the map
         * - V putLast(K, V) : appends a key-value pair to the end of the map
         * - SequencedMap<K, V> reversed() : returns a view on the map in reverse order
         * - SequencedSet sequencedKeySet() : returns the keys of the map
         * - SequencedCollection<V> sequencedValues() : returns the values of the map
         * - SequencedSet<Entry<K,V>> sequencedEntrySet() : returns all entries of the map
         */

        /*
         * New Collections Methods
         *
         * The Collections utility class has been extended with some static utility methods :
         * - newSequencedSetFromMap(SequencedMap map) : analogous to Collections.setFromMap(…), this method returns a
         *   SequencedSet with the properties of the underlying map.
         * - unmodifiableSequencedCollection(SequencedCollection c) : analogous to Collections.unmodifiableCollection(…)
         *   returns an unmodifiable view of the underlying SequencedCollection.
         * - Collections.unmodifiableSequencedMap(SequencedMap m) : returns an unmodifiable view of the underlying
         *   SequencedMap, analogous to Collections.unmodifiableMap(…).
         * - Collections.unmodifiableSequencedSet(SequencedSet s) : returns an unmodifiable view of the underlying
         *   SequencedSet, analogous to Collections.unmodifiableSet(…).
         */
    }
}
