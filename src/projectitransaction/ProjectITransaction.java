
package projectitransaction;

import java.io.*;
import java.nio.*;
import dao.LineSequential;
import ProjectIMario.*;


public class ProjectITransaction 
{

   
    public static void main(String[] args)
    {
       String inFile = "E:/CIS3306 - Business Programming II/ProjectITransaction/ProjectITransaction.dat";
       String inFileStreamName = "Project1Transaction";
       String tranFile = "E:/CIS3306 - Business Programming II/ProjectITransaction/ProjectI.dat";
       String tRecord = " ";
      
       int primeNumber = 2113;
       int recordLength = 32;
       
       final int [] offsetMarks = {9, 15, 35, 41, 42}; 
       int offset;
       
       String inputLine = " ";
       TransactionRecord tr;
       
       try(RelativeFile outDataStream = new RelativeFile(tranFile, primeNumber, recordLength);)
       {  
           initialization(inFile, inFileStreamName);

           while((inputLine = LineSequential.read(inFileStreamName)) != null)
       {
             tr = new TransactionRecord();//Declare new transaction record.
           
           //Updates the data file..
            update(outDataStream, tr, inputLine, offsetMarks);
       }
           
           termination(inFileStreamName);
       
       }
       catch(FileNotFoundException e)
       {
           System.out.println(e.getMessage());
       }
       catch(IOException e)
       {
           System.out.println(e.getMessage());
       }
               
    }
    
    static void initialization(String inFile, String inFileStreamName)
    {
        LineSequential.open(inFile, inFileStreamName, "input");
    }
    
    static void initializeTransactionFields(TransactionRecord tr, String inputLine, int [] offsetMarks)
    {
        tr.setEmployeeNumber(Integer.valueOf(inputLine.substring(0, offsetMarks[0])));
        tr.setSales(Integer.valueOf(inputLine.substring(offsetMarks[0], offsetMarks[1])));
        tr.setRegion(inputLine.substring(offsetMarks[1], offsetMarks[2]));
        tr.setCommission(Float.valueOf(inputLine.substring(offsetMarks[2], offsetMarks[3])));
        tr.setTranCode(Integer.valueOf(inputLine.substring(offsetMarks[3], offsetMarks[4])));
        
    }
    
    static void update(RelativeFile outDataStream, TransactionRecord tr, String inputLine, int [] offsetMarks) throws IOException
    {
        initializeTransactionFields(tr, inputLine, offsetMarks);
        
        int employeeNumber = tr.getEmployeeNumber();
        
        
        switch(tr.getTranCode())
        {
            case 1: //For deleting.
            {
                outDataStream.deleteRecord(employeeNumber);
                break;
            }
            case 2: //For adding
            {
                outDataStream.addRecord(tr);
                break;
            }
            case 3:
            {
                //Modifying sales
                int offset = 4;
                int sales = tr.getSales();
                outDataStream.modifySales(employeeNumber, offset, sales);
                break;
            }
            case 4:
            {
                //Modifying region
                int offset = 0;
                String region = tr.getRegion();
                outDataStream.modifyRegion(employeeNumber, offset, region);
                break;
            }
            case 5:
            {
                //Modifiying commission
                int offset = 0;
                float commission = tr.getCommission();
                outDataStream.modifyCommission(employeeNumber, offset, commission);
                break;
            }
            default:
            {
                System.out.println("Error.");
                break;
            }
       
        
        }
        
        
        
    
    }
    static void termination(String inFileStreamName)
    {
        LineSequential.close(inFileStreamName, "input");
        System.out.println("File is complete.");
    }
}
