package com.tigerit.exam;


import static com.tigerit.exam.IO.*;

/**
 * All of your application logic should be placed inside this class.
 * Remember we will load your application from our custom container.
 * You may add private method inside this class but, make sure your
 * application's execution points start from inside run method.
 */

public class Solution implements Runnable {
    int testCase;

    int numberOfTables;

    int tableColumn,tableRow;

    int numberOfQuery;

    Table[] Tables = new Table[15];
    @Override
    public void run() {

        testCase= readLineAsInteger();

        while(testCase-- >0)
        {
            solve();
            
        }

    }
 
    public void solve()
    {


        numberOfTables = readLineAsInteger();
        
        for(int i=0;i<numberOfTables;i++)
        {
            String tableName = readLine();
            String temp = readLine();
            String [] temp2 =  temp.split(" ");
            int numberOfColumn = Integer.parseInt(temp2[0]);   
            int numberOfRow = Integer.parseInt(temp2[1]);
            Tables[i]= new Table(tableName,numberOfColumn,numberOfRow);
                temp = readLine();
                Tables[i].columnNames = temp.split(" ");            
            for(int r=0; r<numberOfRow;r++)
            {
                temp = readLine();

                Tables[i].tableData[r]= temp.split(" ");
            }
            Tables[i].printTable();
            
        }
        


    }
}
class Table{

    String tableName;
    int numberOfRow;
    int numberOfColumn;
    String [][] tableData;
    String [] columnNames;
    Table(String name, int c,int r)
    {
        tableName= name;
        numberOfColumn=c;
        numberOfRow=r;
        tableData = new String[r+5][c+5];
    }
    public void printTable()
    {
        printLine("Table Name: "+tableName);
        for(int i=0;i<numberOfColumn;i++)
        {
            System.out.print(columnNames[i]+" ");
        }
        printLine("");
        for(int i=0;i<numberOfRow;i++)
        {
            for(int j=0;j<numberOfColumn;j++)
            {
                System.out.print(tableData[i][j] + " ");
            }
            printLine("");
        }
    }
}