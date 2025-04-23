import java.util.*;

public class GenericMethods {

    // (a) Generic method to count elements with a specific property
    public static <T> int countWithProperty(Collection<T> collection, Property<T> property) {
        int count = 0;
        for (T element : collection) {
            if (property.test(element)) {
                count++;
            }
        }
        return count;
    }

    // Functional interface for property testing
    @FunctionalInterface
    public interface Property<T> {
        boolean test(T t);
    }

    // (b) Generic method to exchange positions of two elements in an array
    public static <T> void exchange(T[] array, int index1, int index2) {
        if (index1 < 0 || index2 < 0 || index1 >= array.length || index2 >= array.length) {
            throw new IndexOutOfBoundsException("Invalid indices");
        }
        T temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    // (c) Generic method to find the maximal element in a range of a list
    public static <T extends Comparable<T>> T findMaximalElement(List<T> list, int begin, int end) {
        if (begin < 0 || end > list.size() || begin >= end) {
            throw new IllegalArgumentException("Invalid range");
        }
        T max = list.get(begin);
        for (int i = begin + 1; i < end; i++) {
            if (list.get(i).compareTo(max) > 0) {
                max = list.get(i);
            }
        }
        return max;
    }

    public static void main(String[] args) {
        // Example usage

        // (a) Count elements with a specific property
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        int oddCount = countWithProperty(numbers, n -> n % 2 != 0);
        System.out.println("Count of odd numbers: " + oddCount);

        // (b) Exchange positions in an array
        Integer[] array = {1, 2, 3, 4, 5};
        exchange(array, 1, 3);
        System.out.println("Array after exchange: " + Arrays.toString(array));

        // (c) Find maximal element in a range
        List<String> words = Arrays.asList("apple", "orange", "banana", "pear");
        String maxWord = findMaximalElement(words, 1, 4);
        System.out.println("Maximal element: " + maxWord);
    }
}