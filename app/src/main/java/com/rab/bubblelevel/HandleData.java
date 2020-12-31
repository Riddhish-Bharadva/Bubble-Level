package com.rab.bubblelevel;

public class HandleData {
    // Declaration of Global variable starts here.
    private static OneD_Values[] oneDValues;
    private static int index = 0;
    // Declaration of Global variable ends here.

    // Declaration of constructor starts.
    public HandleData()
    {
        index = 0;
        oneDValues = new OneD_Values[500];
    }
    // Declaration of constructor ends here.

    // Below method will be used to insert new value in 1D array.
    protected void record1DValues(OneD_Values value)
    {
        oneDValues[index] = value; // Inserting value at index so far.
        incrementIndex(); // Calling method to increment Index.
    }
    // record1DValues method ends here.

    // Below method will be used to increment index of an array.
    private void incrementIndex()
    {
        index++; // Incrementing index by 1.
        if(index >= 500) // Checking if index is more than 500.
        {
            index = 0; // In case above is true, reset index.
        }
    }
    // incrementIndex method ends here.

    // Below method will be used to fetch last record inserted in 1D array.
    protected OneD_Values get1DLastData()
    {
        if(oneDValues.length>0) // In case there are records in array, do as below. This is to handle initial state of application records.
        {
            if(index != 0)
                return oneDValues[index-1]; // Returning last recorded value in case index is not 0.
            else
                return oneDValues[oneDValues.length-1]; // Returning last recorded value in case index is positioned to 0.
        }
        else // In case there are no records in Array, return 0.
        {
            return null; // Returning null if there are no data.
        }
    }
    // get1DLastData method ends here.
}
