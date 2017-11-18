package com.tigerit.exam;


import static com.tigerit.exam.IO.*;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Comparator;

/**
 * All of your application logic should be placed inside this class.
 * Remember we will load your application from our custom container.
 * You may add private method inside this class but, make sure your
 * application's execution points start from inside run method.
 */

class Table{

    String tableName;
    int numberOfRow;
    int numberOfColumn;

    String [][] tableData;
    String [] columnNames;
    HashMap<String,Integer> columnIndex;
    // Table object constuctor for storing TableName, numberOfRow and numberOfColumn   
    Table(String name, int c,int r)
    {
        tableName= name;
        numberOfColumn=c;
        numberOfRow=r;
        tableData = new String[r+5][c+5];
        columnIndex = new HashMap<String,Integer>();
    }

    /**
   * This method is used to map column names with its corresponding indices
   */
    public void createIndexColumns()
    {
        for(int i=0;i<numberOfColumn;i++)
        {
            columnIndex.put(columnNames[i],i);
        }

    }
    
}
public class Solution implements Runnable {

    int testCase;

    int caseNo;

    int numberOfTables;

    int columnResult;

    int numberOfQuery;


    //2-D array to save joined table data
    int [][] joinResult = new int[205][205];

    //selected column names to display after joining
    String[] joinResultColumnName = new String[205];

    //Arrays to maintain orders of printing column names
    int[] tableOneSelectedColumnIndex = new int[105];
    int[] tableTwoSelectedColumnIndex = new int[105];


    Table[] Tables;

    //hashmap to link short forms of a table to its corresponding index in Tables[] array 
    HashMap<String,Integer> tableArrayIndex;

    //hashmap to get index of a table in Tables[] array with Table name 
    HashMap<String,Integer> tableNameAlias;

    
    @Override
    public void run() {

        testCase= readLineAsInteger();
        caseNo=1;
        while(testCase-- >0)
        {
            solve();
            
        }

    }
 
    public void solve()
    {
        printLine("Test: "+caseNo++);

        tableNameAlias = new HashMap<String,Integer>();
        tableArrayIndex = new HashMap<String,Integer>();
        Tables = new Table[15];
        numberOfTables = readLineAsInteger();
        
        // Take input details of the tables
        for(int i=0;i<numberOfTables;i++)
        {
            String tableName = readLine();
            tableName = tableName.trim();

            String rowColumnInputLine = readLine();
            rowColumnInputLine = rowColumnInputLine.trim();
            String[] rowAndColumn =  rowColumnInputLine.split(" ");
            int numberOfColumn = Integer.parseInt(rowAndColumn[0]);   
            int numberOfRow = Integer.parseInt(rowAndColumn[1]);

            Tables[i] = new Table(tableName,numberOfColumn,numberOfRow);

            //map tables name with with corresponding index in hashmap
            tableArrayIndex.put(tableName,i);

            String columnNamesLine = readLine();
            Tables[i].columnNames = columnNamesLine.split(" ");            
            Tables[i].createIndexColumns();

            String tableDataInputLine;
            for(int r=0; r<numberOfRow;r++)
            {
                tableDataInputLine = readLine();
                //split tableDataInput to an array of numberOfColumn Strings and assign in to i'th tables r'th row
                Tables[i].tableData[r] = tableDataInputLine.split(" ");
            }

            
        }

       numberOfQuery = readLineAsInteger();

       while(numberOfQuery-- >0)
       {
           String queryLineOne = readLine();
           queryLineOne = queryLineOne.trim();

           String queryLineTwo = readLine();
           queryLineTwo = queryLineTwo.trim();

           String queryLineThree = readLine();
           queryLineThree = queryLineThree.trim();

           String queryLineFour = readLine();
           queryLineFour = queryLineFour.trim();

           String[] lineTwoSpilled = queryLineTwo.split(" ");
           String[] lineThreeSpilled = queryLineThree.split(" ");
           
           String TableOneName = lineTwoSpilled[1]; 
           String TableTwoName = lineThreeSpilled[1];
           
           //When no short form of table is given
           if(lineTwoSpilled.length==2)
           {
                tableNameAlias.put(TableOneName,tableArrayIndex.get(TableOneName));
                tableNameAlias.put(TableTwoName,tableArrayIndex.get(TableTwoName));

           }
           // map short form of table to the corresponding table index
           else
           {
                tableNameAlias.put(lineTwoSpilled[2],tableArrayIndex.get(TableOneName));
                tableNameAlias.put(lineThreeSpilled[2],tableArrayIndex.get(TableTwoName));

           }
            
        String[] lineFourSplitted = queryLineFour.split(" ");
        
        //Condition columns for table one and two extracted
        String[] tableOneConditionColumn = lineFourSplitted[1].split("\\.");
        String[] tableTwoConditionColumn = lineFourSplitted[3].split("\\.");
        
        executeQuery(TableOneName, TableTwoName, tableOneConditionColumn[1],tableTwoConditionColumn[1], queryLineOne);

       } 


    }
    /**
   * This method is used to join the two tables given from parameter and query line
   * @param TableOne First tables name as String
   * @param TableTwo  Second tables name as String
   * @param tableonecondition first tables condition column as string
   * @param tabletwocondition second tables condition column as string
   * @param queryLine  SELECT query line   
   * @return void This dosent return anything, joins the tables and calls printResult() to display output
   */
    public void executeQuery(String TableOne, String TableTwo, String tableonecondition, String tabletwocondition, String queryLine)
    {

        int tableOneIndex = tableArrayIndex.get(TableOne);
        int tableTwoIndex = tableArrayIndex.get(TableTwo);
        
        Table tableOneObj = Tables[tableOneIndex];
        Table tableTwoObj = Tables[tableTwoIndex];


        int tableOneCondition = 0;
        int tableTwoCondition = 0;

        // loop to find tableonecondintions index
        for(int i=0;i<tableOneObj.numberOfColumn;i++)
        {

            if(tableonecondition.equals(tableOneObj.columnNames[i]))
            {
                tableOneCondition = i;
                break;
            }
        }

        // loop to find tabletwocondintions index
        for(int i=0;i<tableTwoObj.numberOfColumn;i++)
        {
            if(tabletwocondition.equals(tableTwoObj.columnNames[i]))
            {
                tableTwoCondition = i;
                break;
            }
        }
        

        int rowJoinResult = 0;
        int columJoinResult = 0;

        String[] selectedItems = queryLine.split("[,\\s]+");
        columnResult = selectedItems.length-1;


        //initialize tableOneSelectedColumnIndex and tableOneSelectedColumnIndex with -1
        for(int i=0;i<tableOneObj.numberOfColumn;i++)
            tableOneSelectedColumnIndex[i] =- 1;
        for(int i=0;i<tableTwoObj.numberOfColumn;i++)
            tableTwoSelectedColumnIndex[i] =- 1;

        // select all columns from table one and table two  to joinResultColumnName[] array
        if(selectedItems[1].equals("*"))
        {

            for(int i=0;i<tableOneObj.numberOfColumn;i++)
            {
                joinResultColumnName[columJoinResult] = tableOneObj.columnNames[i];
                tableOneSelectedColumnIndex[i] = columJoinResult++;
            }
            
            for(int i=0;i<tableTwoObj.numberOfColumn;i++)
            {
                joinResultColumnName[columJoinResult] = tableTwoObj.columnNames[i];
                tableTwoSelectedColumnIndex[i]=  columJoinResult++;
            }
            columJoinResult = 0;
            columnResult = tableOneObj.numberOfColumn + tableTwoObj.numberOfColumn;

        }
        // mark selected columns from table one and table two  to joinResultColumnName[] array
        else
        {
            
            for(int i=1;i<selectedItems.length; i++)
            {
                int tableIndex  = tableNameAlias.get(selectedItems[i].split("\\.")[0]);
                
                if(tableIndex == tableOneIndex)
                {
                    String column = selectedItems[i].split("\\.")[1];
                    int col = tableOneObj.columnIndex.get(column);
                    joinResultColumnName[columJoinResult] = tableOneObj.columnNames[col];
                    tableOneSelectedColumnIndex[col] = columJoinResult++;

                } 
                else
                {
                    String column = selectedItems[i].split("\\.")[1];
                    int col = tableTwoObj.columnIndex.get(column);
                    joinResultColumnName[columJoinResult] = tableTwoObj.columnNames[col];
                    tableTwoSelectedColumnIndex[col] = columJoinResult++;                } 
                }
        }


        // add values to joinResult array accoroding to condition

        for(int i=0;i < tableOneObj.numberOfRow;i++)
        {
            for(int j=0;j<tableTwoObj.numberOfRow;j++)
            {

                // Tableone column and Tabletwo column Condition checking 
                if(tableOneObj.tableData[i][tableOneCondition].equals(tableTwoObj.tableData[j][tableTwoCondition] ))
                {
                    columJoinResult = 0;


                    for(int k=0;k<tableOneObj.numberOfColumn;k++)
                    {
                        //tableOneSelectedColumnIndex[k] is -1 when the specific column is not needed to display,
                        if(tableOneSelectedColumnIndex[k] != -1)
                        {
                            joinResult[rowJoinResult][tableOneSelectedColumnIndex[k]]= Integer.parseInt(tableOneObj.tableData[i][k]);
                        }
                    }
                    for(int k=0;k<tableTwoObj.numberOfColumn;k++)
                    {
                        
                        if(tableTwoSelectedColumnIndex[k] != -1)
                        {
                            joinResult[rowJoinResult][tableTwoSelectedColumnIndex[k]]=Integer.parseInt(tableTwoObj.tableData[i][k]);
                        }
                    }

                    rowJoinResult++;

                }


            }
        }

        // sort joinResult array lexicographically
        Arrays.sort(joinResult,0, rowJoinResult,new Comparator<int[]>(){
            @Override
            public int compare(int[] o1, int[] o2) {
                for(int i=0; i< columnResult; i++)
                {
                    if(o1[i] != o2[i])
                    {
                        return o1[i]-o2[i];
                    }
                }
                return 0;
            }
        });
        
        printResult(rowJoinResult, columnResult);
        
    }
    /**
   * This method is used to display result array
   * @param r row number of joined table
   * @param c column number of joined table
   * @return void This dosent return anything, joins the tables and calls printResult() to display output
   */


    public void printResult(int r,int c)
    {
        
        for(int i=0;i<c;i++)
        {
            if(i==c-1)
                System.out.println(joinResultColumnName[i]);
            else
                System.out.print(joinResultColumnName[i] +" ");
        }

        for(int i=0; i<r; i++)
        {
            for(int j=0; j<c; j++)
            {
                if(j==c-1)
                    System.out.println(joinResult[i][j]);
                else                
                    System.out.print(joinResult[i][j] + " ");

            }
        }
        System.out.println();    
    }
}
