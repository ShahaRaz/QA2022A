package pkg1;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ReadExcl {

    private int rowCount;
    private Sheet guru99Sheet;

    public void readExcel(String filePath, String fileName, String sheetName) throws IOException {

        //Create an object of File class to open xlsx file

        File file = new File(filePath + "\\" + fileName);

        //Create an object of FileInputStream class to read excel file

        FileInputStream inputStream = new FileInputStream(file);

        Workbook guru99Workbook = null;

        //Find the file extension by splitting file name in substring  and getting only extension name

        String fileExtensionName = fileName.substring(fileName.indexOf("."));

        //Check condition if the file is xlsx file

        //  if(fileExtensionName.equals(".xlsx")){

        //If it is xlsx file then create object of XSSFWorkbook class

        // guru99Workbook = new Workbook(inputStream);

        //  }

        //Check condition if the file is xls file

        if (fileExtensionName.equals(".xls")) {

            //If it is xls file then create object of HSSFWorkbook class

            guru99Workbook = new HSSFWorkbook(inputStream);

        }

        //Read sheet inside the workbook by its name

        guru99Sheet = guru99Workbook.getSheet(sheetName);

        //Find number of rows in excel file

        rowCount = guru99Sheet.getLastRowNum() - guru99Sheet.getFirstRowNum();


    }


    public int getRowcount() {

        return rowCount;
    }


    public Sheet getsheet() {
        return guru99Sheet;
    }
}