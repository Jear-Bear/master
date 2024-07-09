public class search {
    static String look(int[][] arr, int r, int c)
    {
        String bruh = "";
        String notFound = "No sequence found";
        for (int i = 0; i < r; i++)
        {
            for (int j = 0; j < c; j++) //runs match cases at each location in matrix
            {
                int matchDown = lookDown(arr, i, j, r, c);
                int matchRight = lookRight(arr, i, j, r, c);
                int matchDiagRight = lookDiagRight(arr, i, j, r, c);
                int matchDiagLeft = lookDiagLeft(arr, i, j, r, c);
                if ((matchDown != -1)) //checks if match found a sequence
                {
                    bruh = "Sequence of " + Integer.toString(arr[lookDown(arr, i, j, r, c)][j]) + " starts at " + Integer.toString(i) + ", " + Integer.toString(j); //assigns location to a string
                    return bruh; //returns the string
                }
                else if ((matchRight != -1)) //checks if match found a sequence
                {
                    bruh = "Sequence of " + Integer.toString(arr[i][lookRight(arr, i, j, r, c)]) + " starts at " + Integer.toString(i) + ", " + Integer.toString(lookRight(arr, i, j, r, c)); //assigns location to a string
                    return bruh; //returns the string
                }
                else if ((matchDiagRight != -1)) //checks if match found a sequence
                {
                    bruh = "Sequence of " + Integer.toString(arr[i + lookDiagRight(arr, i, j, r, c)][j + lookDiagRight(arr, i, j, r, c)]) + " starts at " + Integer.toString(i) + ", " + Integer.toString(lookDiagRight(arr, i, j, r, c)); //assigns location to a string
                    return bruh; //returns the string
                }
                else if ((matchDiagLeft != -1)) //checks if match found a sequence
                {
                    bruh = "Sequence of " + Integer.toString(arr[i][j]) + " starts at " + Integer.toString(i) + ", " + Integer.toString(j); //assigns location to a string
                    return bruh; //returns the string
                }
            }
        }
        return notFound; //returns not found statement if the loop is exited
    }

    static int lookDown(int[][] arr, int i, int j, int r, int c)
    {
        int count = 0;
        int k = i;
        int num = arr[k][j];
        for(k = 0; k < r - 1; k++)
        { 
            if ((arr[k][j] == arr[k + 1][j]) && (arr[k][j] == num)) 
            {
                count += 1;
                num = arr[k + 1][j];
            }
        }
        if (count == 3) return k - 3;
        else return -1;
    }

    static int lookRight(int[][] arr, int i, int j, int r, int c)
    {
        int count = 0;
        int k = j;
        int num = arr[i][k];
        for(k = 0; k < c - 1; k++)
        { 
            if ((arr[i][k] == arr[i][k + 1]) && (arr[i][k] == num) && count < 3) 
            {
                count += 1;
                num = arr[i][k + 1];
            }
        }
        if (count == 3) return k - 3;
        else return -1;
    }

    static int lookDiagLeft(int[][] arr, int i, int j, int r, int c)
    {
        int count = 0;
        int k = j;
        int num = arr[i][k];
        for(k = 0; (k < r - i) && (k < j); k++)
        {
            if((i + k + 1 < r) && (0 < i + k + 1) && (j - k - 1 >= 0))
            {
                if((arr[i + k][j - k] == arr[i + k + 1][j - k - 1]) && count < 3 && num == arr[i + k + 1][j - k - 1])
                {
                    count += 1;
                    num = arr[i + k + 1][j - k - 1];
                }
            }
        }
        if (count == 3)
        {
            return k - 4;
        }
        else return -1;
    }

    static int lookDiagRight(int[][] arr, int i, int j, int r, int c)
    {
        int count = 0;
        int k = j;
        int num = arr[i][k];
        for(k = 0; (k < c - 1 - j) && (k < r - 1 - j); k++)
        {
            if((i + k + 1 < r) && (j + k + 1 < c))
            {
                if((arr[i + k][j + k] == arr[i + k + 1][j + k + 1]) && count < 3 && num == arr[i + k + 1][j + k + 1])
                {
                    count += 1;
                    num = arr[i + k + 1][j + k + 1];
                }
            }
        }
        if (count == 3) return k - 3;
        else return -1;
    }
}
