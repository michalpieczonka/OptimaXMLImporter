package pl.apisnet.backEND.XMLFiles;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pl.apisnet.backEND.Exceptions.FileStructureException;
import pl.apisnet.backEND.Optima;
import pl.apisnet.backEND.XMLFiles.XMLObjects.ExcelXMLPZPosition;

import java.io.FileInputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLExcelParser extends XMLImporter{

    private String sheetName;

    /**
     * Parameters (Path_To_xml_file, global instance of Optima class Object)
     */
    public XMLExcelParser(String xmlFilePath, Optima mainOptima){
        this.mainOptima = mainOptima;
        this.xmlFilePath = xmlFilePath;
        this.PZItemsList = new ArrayList<>();
    }

    /**
     * The most important function. Reading xml file, and invoking other necessary methods to deal with given XML File
     * File is correct only if
     */
    @Override
    public void readXmlFileHeaders() {
        List<String> correctHeaderNames = Arrays.asList("Symbol","symbol","Nazwa towaru","nazwa towaru","Kod kreskowy","kod kreskowy","stawka vat" +
                "EAN","ean","Jednostka miary", "miara", "J EW","J_EW","j ew","j ew","Stawka Vat", "Stawka_VAT","stawka_vat" +
                "stawka_VAT","ilosc","ilość","Ilość","Cena","cena","cena po upuście","cena_po_upuście","cena_po_upuscie","cena po upuscie","Cena po upuście");

        List<String> header = new ArrayList<>();
        try{
            FileInputStream inputStream = new FileInputStream(xmlFilePath);
            XSSFWorkbook excelFile = new XSSFWorkbook(inputStream); //Getting selected Excel file
            XSSFSheet excelSheet = excelFile.getSheet(sheetName); //Getting selected Sheet in Excel file
            //Getting header names
            Row headerRow = excelSheet.getRow(0);

            //Reading header into header list
            for (Cell cell : headerRow){
                if (correctHeaderNames.contains(cell.getStringCellValue().trim())){
                    header.add(cell.getStringCellValue());
                }
            }
            //Only if header contains 7 elements -> otherwise there is error
            if (header.size() == 7){
                // Kolejnosc po indexach
                int []elementsOrder = new int [7]; //0 - Nazwa towaru, 1 - Symbol, 2 - EAN, 3 - jEW, 4 - Stawka Vat, 5 - ilosc, 6 - Cena po upuscie
                for (int i=0; i<header.size(); i++){
                    if (header.get(i).contains("azw")){  //Nazwa Towaru
                        elementsOrder[0] = i;

                    } else if (header.get(i).contains("ymb")){ //Symbol
                        elementsOrder[1] = i;
                    } else if (header.get(i).contains("reskowy")||header.get(i).contains("ean") || header.get(i).contains("EAN")){ //Kod kreskowy/Ean
                        elementsOrder[2] = i;
                    }  else if (header.get(i).contains("ew") || header.get(i).contains("EW") || header.get(i).contains("dnost")){ //JEW/Jednostka miary
                        elementsOrder[3] = i;
                    }  else if (header.get(i).contains("vat") || header.get(i).contains("awk")) { //stawka vat
                        elementsOrder[4] = i;
                    } else if (header.get(i).contains("lo") ) { //stawka vat
                        elementsOrder[5] = i;
                    } else if (header.get(i).contains("ena") || header.get(i).contains("scie")) { //stawka vat
                        elementsOrder[6] = i;
                    } else {
                        throw new FileStructureException();
                    }
                }
                for (int i=0; i<7; i++)
                    System.out.println(elementsOrder[i]);

                //Reading values from excel
                int rows = excelSheet.getLastRowNum();
                int cols = 7;

                for(int r=1; r<rows; r++){
                    XSSFRow row = excelSheet.getRow(r);

                    //Nazwa towaru
                    XSSFCell nameCell = row.getCell(elementsOrder[0]);
                    XSSFCell symbolCell = row.getCell(elementsOrder[1]);
                    XSSFCell eanCell = row.getCell(elementsOrder[2]);
                    XSSFCell jewCell = row.getCell(elementsOrder[3]);
                    XSSFCell vatCell = row.getCell(elementsOrder[4]);
                    XSSFCell iloscCell = row.getCell(elementsOrder[5]);
                    XSSFCell cenaCell = row.getCell(elementsOrder[6]);
                    List<XSSFCell> tempCellList = Arrays.asList(nameCell,symbolCell,eanCell,jewCell,vatCell,iloscCell,cenaCell);
                    for (XSSFCell cell : tempCellList)
                        cell.setCellType(CellType.STRING);
                    PZItemsList.add(new ExcelXMLPZPosition(symbolCell.getStringCellValue(),Integer.parseInt(iloscCell.getStringCellValue()),Double.parseDouble(cenaCell.getStringCellValue()),eanCell.getStringCellValue(),nameCell.getStringCellValue(),jewCell.getStringCellValue(),Integer.parseInt(vatCell.getStringCellValue()),false,false));
                }

                for(int i=0; i<PZItemsList.size(); i++)
                    System.out.println(PZItemsList.get(i).getSymbol());

            }
            else {
                throw new FileStructureException();
            }


        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public boolean checkIfEanNumbersExists(String eanNumber) {
        return false;
    }

    @Override
    public void addNewEan(String eanNumber, String symbol, String itemName, int vatRate, String unitOfMeasure) {

    }

    @Override
    public void addNewPZ() {

    }

    @Override
    public void addMissingEans() {

    }

    public void setSheetName(String sheetName){
        this.sheetName = sheetName;
    }

    public List<String> getAvailableSheets(){
        List<String> avaliableSheets = new ArrayList<>();
        try{
            FileInputStream inputStream = new FileInputStream(xmlFilePath);
            XSSFWorkbook excelFile = new XSSFWorkbook(inputStream);
            for (int i = 0; i < excelFile.getNumberOfSheets(); i++)
            {
                avaliableSheets.add(excelFile.getSheetAt(i).getSheetName());
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return avaliableSheets;
    }




}
