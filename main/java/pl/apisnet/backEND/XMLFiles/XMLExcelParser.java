package pl.apisnet.backEND.XMLFiles;

import pl.apisnet.backEND.XMLFiles.XMLObjects.XMLPZPosition;

import java.util.List;

public class XMLExcelParser extends XMLImporter{
    @Override
    public void readXmlFileHeaders() {

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




}
