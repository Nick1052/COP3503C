/* Nicolas Gonzalez
 * Dr. Andrew Steinberg
 * COP3503 Summer 2022
 * Programming Assignment 1
 */

import java.util.Random;

public class Lottery {

    private String ticket;

    // Pre-conditions: None
    // Post-conditions: Constructs a Lottery object with a String ticket that is empty.
    Lottery() {
        ticket = "";
    }
    // Pre-conditions: Random Obj needs to be passed
    // Post-conditions: Constructs a Lottery Object and gives the object a string ticket
    // Note: The string format makes sure there are always 6 digits. So if the random number is 12, it will be formatted as 000012
    Lottery(Random obj) {
        ticket = String.format("%06d", obj.nextInt(1000000));
    }
    // Pre-conditions: Random Obj needs to be passed
    // Post-conditions: Returns a random String winner.
    // Note: The string format makes sure there are always 6 digits. So if the random number is 12, it will be formatted as 000012
    public static String GenerateRandomWinner(Random obj) {
        return String.format("%06d", obj.nextInt(1000000));
    }
    // Pre-conditions: The max value of the index and Random obj
    // Post-conditions: Returns an index value
    public static int GenerateSelectWinner(int maxSize, Random obj) {
        return obj.nextInt(maxSize);
    }

    // Pre-conditions: Lottery arr, the target winner, and the bounds to search of the lottery arr
    // Post-conditions: Returns true if found the target within the array, false if not found
    // Note: Solution 1 uses a linear search to find the winner. Does it in O(n)
    public static boolean Solution1(Lottery[] arr, String winner, int size) {

        for(int i = 0; i < size; i++)
            if (arr[i].GetTicket().equals(winner))
                return true;

        return false;
    }
    // Pre-conditions: Lottery arr, the index bounds, low and high, and the target winner
    // Post-conditions: Returns true if found the target within the array, false if not found
    // Note: Solution 2 uses a BinSearch to find the winner. Does it in O(lg n)
    public static boolean Solution2(Lottery[] arr, int low, int high, String winner) {

        while(low <= high) {
            int mid = (low + high) / 2;

            if(arr[mid].GetTicket().compareTo(winner) < 0)
                low = mid + 1;
            else if(arr[mid].GetTicket().compareTo(winner) > 0)
                high = mid - 1;
            else
                return true;
        }
        return false;
    }
    // Pre-conditions: None
    // Post-conditions: Returns the object's ticket String
    public String GetTicket() {
        return ticket;
    }

    // Pre-conditions: A lottery array needed to be passed
    // Post-condition: Lottery array will be sorted through a Merge Sort
    // Notes: Calls Sort which is an override method to do the Merge sort
    public static void Sort(Lottery[] arr) {
        Sort(arr, 0, arr.length - 1);
    }

    // Pre-conditions: Needs a lottery arr, the beginning index of an array, and ending index of array
    // Post-conditions: After the last call, the array will be sorted
    // Notes: This is the divider in the Merge Sort
    public static void Sort(Lottery[] arr,int start,int end) {

        if(start < end) {
            int mid = (start + end) / 2;
            Sort(arr, start, mid);
            Sort(arr, mid + 1, end);
            merge(arr, start, mid + 1, end);
        }
    }
    // Pre-conditions: Needs the lottery arr, start index, middle index, and end index.
    // Post-conditions: Merges and sort the array from the start index to the end index
    // Notes: This is the merger and sorter in the Merge Sort
    public static void merge(Lottery[] arr, int start, int mid, int end) {

        Lottery[] temp = new Lottery[end - start + 1];

        int c1 = start, c2 = mid, mc = 0;

        while(c1 < mid || c2 <= end) {
            if(c2 > end || (c1 < mid && (arr[c1].GetTicket().compareTo(arr[c2].GetTicket()) < 0)))
                temp[mc++] = arr[c1++];
            else
                temp[mc++] = arr[c2++];
        }

        for(int i = start; i <= end; i++)
            arr[i] = temp[i - start];
    }
}
