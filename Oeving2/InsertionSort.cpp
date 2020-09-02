
class InsertionSort{

    int binarySearch(int a[], int item, int low, int high)
    {
        if (high <= low)
            return (item > a[low])?  (low + 1): low;

        int mid = (low + high)/2;

        if(item == a[mid])
            return mid+1;

        if(item > a[mid])
            return binarySearch(a, item, mid+1, high);
        return binarySearch(a, item, low, mid-1);
    }

public: static void insertionSort(int array[], int left, int right)
    {
        int i, loc, j, k, selected;

        for (i = left; i < right; ++i)
        {
            j = i - 1;
            selected = array[i];

            // find location where selected sould be inseretd
            loc = binarySearch(array, selected, left, j);

            // Move all elements after location to create space
            while (j >= loc)
            {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = selected;
        }
    }
};