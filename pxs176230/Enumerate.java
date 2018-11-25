/**
 * @author Akhila Perabe (axp178830), Pooja Srinivasan (pxs176230), Shreeya Girish Degaonkar (sxd174830)
 * 
 * Enumerate permutations of an array
 */

package pxs176230;

public class Enumerate<T> {
    T[] arr;
    int k;
    int count;
    Approver<T> app;

    //-----------------Constructors-------------------
    public Enumerate(T[] arr, int k, Approver<T> app) {
        this.arr = arr;
        this.k = k;
        this.count = 0;
        this.app = app;
    }

    public Enumerate(T[] arr, Approver<T> app) {
        this(arr, arr.length, app);
    }

    public Enumerate(T[] arr, int k) {
        this(arr, k, new Approver<T>());
    }

    public Enumerate(T[] arr) {
        this(arr, arr.length, new Approver<T>());
    }

    //-------------Methods of Enumerate class-----------------

    /**
     * Permutation
     * n = arr.length, choose k things, d elements arr[0..d-1] done
     * c more elements are needed from arr[d..n-1].  d = k-c.
     * @param c
     */
    public void permute(int c) {
        if(c==0) {
            visit(arr);
        }
        else{
            int d = k - c;  // start of remaining element index
            permute(c-1);	// Permutation with arr[d] selected
            for(int i = d+1; i < arr.length; i++){
                swap(d,i);
                permute(c-1);	// Permuation with arr[i] selected instead of arr[d] 
                swap(d,i);
            }
        }
    }

    /**
     * Visit array
     * @param array
     */
    public void visit(T[] array) {
        count++;
        app.visit(array, k);
    }

    //----------------------Nested class: Approver-----------------------


    /**
     *  Class to decide whether to extend a permutation with a selected item
     *  Extend this class in algorithms that need to enumerate permutations with precedence constraints
     */
    public static class Approver<T> {
        /**
         * Extend permutation by item?
         * @param item
         * @return
         */
        public boolean select(T item) { return true; }

        /**
         * Backtrack selected item
         * @param item
         */
        public void unselect(T item) { }

        /**
         * Visit a permutation or combination
         * @param array
         * @param k
         */
        public void visit(T[] array, int k) {
            for (int i = 0; i < k; i++) {
                System.out.print(array[i] + " ");
            }
            System.out.println();
        }
    }

    //-----------------------Utilities-----------------------------

    /**
     * Swap elements at i and j
     * @param i
     * @param j
     */
    void swap(int i, int j) {
        T tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    /**
     * Reverse the elements from low to high
     * @param low
     * @param high
     */
    void reverse(int low, int high) {
        while(low < high) {
            swap(low, high);
            low++;
            high--;
        }
    }

    //-----------------------Static Methods-----------------------------

    /**
     *  Enumerate permutations of k items out of n = arr.length
     * @param arr
     * @param k
     * @return
     */
    public static<T> Enumerate<T> permute(T[] arr, int k) {
        Enumerate<T> e = new Enumerate<>(arr, k);
        e.permute(k);
        return e;
    }

    /**
     * Sample driver program
     * @param args
     */
    public static void main(String args[]) {
        int n = 16;
        int k = 3;
        if(args.length > 0) { n = Integer.parseInt(args[0]);  k = n; }
        if(args.length > 1) { k = Integer.parseInt(args[1]); }
        Integer[] arr = new Integer[n];
        for (int i = 0; i < n; i++) {
            arr[i] = i+1;
        }

        System.out.println("Permutations: " + n + " " + k);
        Enumerate<Integer> e = permute(arr, k);
        System.out.println("Count: " + e.count + "\n_________________________");
    }
}
