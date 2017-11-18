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
    
    Table(String name, int c,int r)
    {
        tableName= name;
        numberOfColumn=c;
        numberOfRow=r;
        tableData = new String[r+5][c+5];
        columnIndex = new HashMap<String,Integer>();
    }
    public void createIndexColumns()
    {
        for(int i=0;i<numberOfColumn;i++)
        {
            columnIndex.put(columnNames[i],i);
        }

    }
    // public void printTable()
    // {
    //     printLine("Table Name: "+tableName);
    //     for(int i=0;i<numberOfColumn;i++)
    //     {
    //         System.out.print(columnNames[i]+" ");
    //     }
    //     printLine("");
    //     for(int i=0;i<numberOfRow;i++)
    //     {
    //         for(int j=0;j<numberOfColumn;j++)
    //         {
    //             System.out.print(tableData[i][j] + " ");
    //         }
    //         printLine("");
    //     }
    // }
}
public class Solution implements Runnable {
    int testCase;

    int caseNo;

    int numberOfTables;

    int tableColumn,tableRow;

    int columnResult;

    int numberOfQuery;


    int [][] joinResult = new int[205][205];

    String[] joinResultColumnName = new String[205];

    int[] tableOneSelectedColumnIndex = new int[105];
    int[] tableTwoSelectedColumnIndex = new int[105];


    Table[] Tables = new Table[15];

    HashMap<String,Integer> tableNameAlias = new HashMap<String,Integer>();
    HashMap<String,Integer> tableArrayIndex = new HashMap<String,Integer>();

    
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


        numberOfTables = readLineAsInteger();
        
        for(int i=0;i<numberOfTables;i++)
        {
            String tableName = readLine();
            String rowColumnInputLine = readLine();
            String[] rowAndColumn =  rowColumnInputLine.split(" ");
            int numberOfColumn = Integer.parseInt(rowAndColumn[0]);   
            int numberOfRow = Integer.parseInt(rowAndColumn[1]);

            Tables[i]= new Table(tableName,numberOfColumn,numberOfRow);
            tableArrayIndex.put(tableName,i);
            String columnNamesLine = readLine();
            Tables[i].columnNames = columnNamesLine.split(" ");            
            Tables[i].createIndexColumns();
            String tableDataInputLine;
            for(int r=0; r<numberOfRow;r++)
            {
                tableDataInputLine = readLine();

                Tables[i].tableData[r]= tableDataInputLine.split(" ");
            }

            //Tables[i].printTable();
            
        }

       numberOfQuery= readLineAsInteger();
       while(numberOfQuery-- >0)
       {
           String queryLineOne = readLine();
           String queryLineTwo = readLine();
           String queryLineThree = readLine();
           String queryLineFour = readLine();

           String[] lineTwoSpilled = queryLineTwo.split(" ");
           String[] lineThreeSpilled = queryLineThree.split(" ");
           
           String TableOneName = lineTwoSpilled[1];
           String TableTwoName = lineThreeSpilled[1];
//           printLine("Length :"+lineTwoSpilled.length);
           
           if(lineTwoSpilled.length==2)
           {
                tableNameAlias.put(TableOneName,tableArrayIndex.get(TableOneName));
                tableNameAlias.put(TableTwoName,tableArrayIndex.get(TableTwoName));

           }
           else
           {
                tableNameAlias.put(lineTwoSpilled[2],tableArrayIndex.get(TableOneName));
                tableNameAlias.put(lineThreeSpilled[2],tableArrayIndex.get(TableTwoName));

           }
            /*
           printLine("Table one: "+TableOneName);
           printLine("Table one alias : "+tableNameAlias.get(lineTwoSpilled[2]));
           printLine("Table two: "+TableTwoName);
           printLine("Table one alias : "+tableNameAlias.get(lineThreeSpilled[2]));
            */
        String[] lineFourSplitted = queryLineFour.split(" ");
        
        
        String[] tableOneConditionColumn = lineFourSplitted[1].split("\\.");
        String[] tableTwoConditionColumn = lineFourSplitted[3].split("\\.");
        /*
        printLine (TableOneName);
        printLine( TableTwoName);
        printLine (tableOneConditionColumn[1]);
        printLine (tableTwoConditionColumn[1]);
        printLine (queryLineOne);
        */
        executeQuery(TableOneName, TableTwoName, tableOneConditionColumn[1],tableTwoConditionColumn[1], queryLineOne);



       } 


    }
    public void executeQuery(String TableOne, String TableTwo, String tableonecondition, String tabletwocondition, String queryLine)
    {

        int tableOneIndex = tableArrayIndex.get(TableOne);
        int tableTwoIndex = tableArrayIndex.get(TableTwo);
        
        Table tableOneObj = Tables[tableOneIndex];
        Table tableTwoObj = Tables[tableTwoIndex];


        int tableOneCondition=0;
        int tableTwoCondition=0;
  
//        printLine(tableonecondition);
//        printLine(tabletwocondition);

        
        for(int i=0;i<tableOneObj.numberOfColumn;i++)
        {
  //          printLine(tableOneObj.columnNames[i]);
            if(tableonecondition.equals(tableOneObj.columnNames[i]))
            {
                tableOneCondition=i;
                break;
            }
        }
        for(int i=0;i<tableTwoObj.numberOfColumn;i++)
        {
            if(tabletwocondition.equals(tableTwoObj.columnNames[i]))
            {
                tableTwoCondition=i;
                break;
            }
        }
//        printLine(tableOneCondition);
//       printLine(tableTwoCondition);
        

        int rowJoinResult=0;
        int columJoinResult=0;
        String [] selectedItems = queryLine.split("[,\\s]+");
        columnResult= selectedItems.length-1;


        for(int i=0;i<tableOneObj.numberOfColumn;i++)
            tableOneSelectedColumnIndex[i]=-1;
        for(int i=0;i<tableTwoObj.numberOfColumn;i++)
            tableTwoSelectedColumnIndex[i]=-1;

        if(selectedItems[1].equals("*"))
        {
//            printLine("START");
            for(int i=0;i<tableOneObj.numberOfColumn;i++)
            {
                joinResultColumnName[columJoinResult]=tableOneObj.columnNames[i];
                tableOneSelectedColumnIndex[i]=columJoinResult++;
            }
            
            for(int i=0;i<tableTwoObj.numberOfColumn;i++)
            {
                joinResultColumnName[columJoinResult]=tableTwoObj.columnNames[i];
                tableTwoSelectedColumnIndex[i]=  columJoinResult++;
            }
            columJoinResult=0;
            columnResult = tableOneObj.numberOfColumn+ tableTwoObj.numberOfColumn;

        }
        else
        {
            
            for(int i=1;i<selectedItems.length; i++)
            {
                int tIndex  = tableNameAlias.get(selectedItems[i].split("\\.")[0]);
                
                if(tIndex ==tableOneIndex)
                {
                    String column = selectedItems[i].split("\\.")[1];
                    int col = tableOneObj.columnIndex.get(column);
                    joinResultColumnName[columJoinResult]=tableOneObj.columnNames[col];
                    tableOneSelectedColumnIndex[col]=columJoinResult++;

                } 
                else
                {
                    String column = selectedItems[i].split("\\.")[1];
                    int col = tableTwoObj.columnIndex.get(column);
                    joinResultColumnName[columJoinResult]=tableTwoObj.columnNames[col];
                    tableTwoSelectedColumnIndex[col]=columJoinResult++;                } 


            }
            

        }




        for(int i=0;i < tableOneObj.numberOfRow;i++)
        {
            for(int j=0;j<tableTwoObj.numberOfRow;j++)
            {
                if(tableOneObj.tableData[i][tableOneCondition].equals(tableTwoObj.tableData[j][tableTwoCondition] ))
                {
                    columJoinResult=0;


                    for(int k=0;k<tableOneObj.numberOfColumn;k++)
                        if(tableOneSelectedColumnIndex[k]!=-1)
                        {
                            joinResult[rowJoinResult][tableOneSelectedColumnIndex[k]]= Integer.parseInt(tableOneObj.tableData[i][k]);
                        }

                    for(int k=0;k<tableTwoObj.numberOfColumn;k++)
                    {
                        //printLine(tableTwoSelectedColumnIndex[k]);
                        if(tableTwoSelectedColumnIndex[k]!=-1)
                            joinResult[rowJoinResult][tableTwoSelectedColumnIndex[k]]=Integer.parseInt(tableTwoObj.tableData[i][k]);
                    }
                    rowJoinResult++;

                }


            }
        }

        // sort resultant array lexicographically
        
        Arrays.sort(joinResult,0, rowJoinResult,new Comparator<int[]>(){

            @Override
            public int compare(int[] o1, int[] o2) {
                for(int i=0; i< columnResult; i++)
                {
                    if(o1[i]!=o2[i])
                    {
                        return o1[i]-o2[i];
                    }
                }
                return 0;
            }
        });
        
        printResult(rowJoinResult, columnResult, queryLine);
        /*
        printLine("Tble 1 "+TableOne);
        printLine("Tbl 2 "+TableTwo);
        printLine(tableonecondition);
        printLine(tabletwocondition);
        printLine(query);
        */

    }
    public void printResult(int r,int c , String query)
    {
        
        for(int i=0;i<c;i++)
        {
            if(i==c-1)
            {
                System.out.println(joinResultColumnName[i]);
            }
            else
                System.out.print(joinResultColumnName[i]+" ");
                
        }
        
        for(int i=0;i<r;i++)
        {
            for(int j=0;j<c;j++)
            {
                if(j==c-1)
                    System.out.println(joinResult[i][j]);
                else                
                    System.out.print(joinResult[i][j]+ " ");

            }
        }

        System.out.println();    
    }
}
