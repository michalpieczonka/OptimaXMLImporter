package pl.apisnet.backEND.XMLFiles;


import com.jacob.com.Dispatch;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import pl.apisnet.backEND.Exceptions.FileStructureException;
import pl.apisnet.backEND.Exceptions.ImportPZException;
import pl.apisnet.backEND.Optima;
import pl.apisnet.backEND.XMLFiles.XMLObjects.XMLPZPosition;

import javax.xml.parsers.DocumentBuilderFactory;
import java.util.Arrays;
import java.util.List;

/**
 * AbstractClass responsible for all XMLParsers
 */

public abstract class XMLImporter {
    protected final String optimaFindEanClassName = "findEan"; //Name of finding EAN function in DLL library created in C# using Optima API
    protected final String optimaAddNewItemClassName = "addItemByEan"; //Name of adding new Item function in DLL library crated in c# using Optima API
    protected final String optimaAddNewPzClassName = "addPZDocument"; //Name of adding new PZ function in DLL library created in c# using Optima API
    protected final String optimaCheckItemCorrectness = "checkEanCorectness"; //Name of function in DLL to check if Item (with specific EAN number) readed from XMLFile
    // has the same unit of measure as  item that is already stored in Optima

    protected String xmlFilePath;  //Path to XMLFILE to be parsed
    protected DocumentBuilderFactory factory;
    protected Document xmlFile;
    protected NodeList elementsInXml_Pozycja;  //Each single tag in XML FILE -> this case -> 1 single item between <Pozycje> ... </Pozycje>
    protected Optima mainOptima;

    protected List<XMLPZPosition> PZItemsList; //List of items readed from file, imported by User
    protected List<String> UnitsOfMeasure = Arrays.asList("szt","Szt","SZT","godz","Godz","kg","Kg","litr","Litr","m","M","mkw","Mkw","opak","Opak"); //List of units of measure available in Optima
    /**
     * Method responsible for reading XML-file, parsing it, and calling checkIfEanNumberExists()
     */
    public abstract void readXmlFileHeaders() throws FileStructureException;

    /**
     * Method responsible for checking if parsed EAN numbers are already in OPTIMA databse, using DLL (OptimaLIBB)
     * Returns if Optima already contains Ean with given argument
     * Invoking method @optimaFindEanClassName from OptimaLIBB.dll
     * Parameters - Ean number to check
     */
    public boolean checkIfEanNumbersExists(String eanNumber){
        return Dispatch.call(mainOptima.getOptimaProgramID(),optimaFindEanClassName,eanNumber).getBoolean();
    };

    /**
     * Method responsible for adding new Item to Optima database, using DLL (OptimaLIBB)
     * Adding new Item to Optima
     * Invoking method @optimaAddNewItemClassName from OptimaLIBB.dll
     * Parameters - necessary components of the item (ean,symbol,name,VatRate,unitOfMeasure)
     */
    public void addNewEan(String eanNumber, String symbol, String itemName, int vatRate, String unitOfMeasure){
        Dispatch.call(mainOptima.getOptimaProgramID(),optimaAddNewItemClassName,eanNumber,symbol,itemName,vatRate,unitOfMeasure);
    };


    /**
     * Method responsible for creating new PZ Document in Optima, using DLL (OptimaLIBB)
     */
    public abstract void addNewPZ() throws ImportPZException;


    /**
     * Method responsible for adding missing items (with missing EAN's numbers) into Optima
     * Adding missing EANS to Optima
     * Invoking method @optimaAddNewItemClassName from OptimaLIBB.dll
     */
    public void addMissingEans(){
        for (XMLPZPosition item : PZItemsList){
            //If item is not avaliable in Optima
            if(!item.isAlreadyInOptima()){
                addNewEan(item.getEAN(),item.getSymbol(),item.getNazwa(),item.getStawkaVat(),item.getjEW()); //Adding new Item into Optima
                item.setAlreadyInOptima(true); //Changing item status (Already in Optima = True)
            }
        }
    }

    /**
     * Method responsible for checking if Items, that are already stored in Optima has the same unit of measure as items readed from XML File
     * If the measurement units differ then method is changing unit of measure to one, stored in Optima
     * Invoking method @checkEanCorectness from OptimaLIBB.dll
     */
    protected void checkIfItemIsCorrect(XMLPZPosition item){
        String itemCorrect = "ok";
        String result = Dispatch.call(mainOptima.getOptimaProgramID(),optimaCheckItemCorrectness,item.getEAN().trim(),item.getjEW().trim()).getString();
        //If result (unit of measure) is different than already stored in Optima, then change it into one, stored in optima
        if (!result.equals(itemCorrect)){
            item.setjEW(result);
            item.setjEWWasIncorrect(true);
        } else{
            item.setjEWWasIncorrect(false);
        }
    }



    public List<XMLPZPosition> getPZItemsList() {
        return PZItemsList;
    }

    public List<String> getUnitsOfMeasure(){
        return UnitsOfMeasure;
    }

}
