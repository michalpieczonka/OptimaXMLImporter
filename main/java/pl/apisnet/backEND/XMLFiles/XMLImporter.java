package pl.apisnet.backEND.XMLFiles;


import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
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

    protected String xmlFilePath;  //Path to XMLFILE to be parsed
    protected DocumentBuilderFactory factory;
    protected Document xmlFile;
    protected NodeList elementsInXml_Pozycja;  //Each single tag in XML FILE -> this case -> 1 single item between <Pozycje> ... </Pozycje>
    protected Optima mainOptima;

    //private List<IHurtXMLPZPosition> PZItemsList;
    protected List<XMLPZPosition> PZItemsList; //List of items readed from file, imported by User
    protected List<String> UnitsOfMeasure = Arrays.asList("szt","Szt","godz","Godz","kg","Kg","litr","Litr","m","M","mkw","Mkw","opak","Opak"); //List of units of measure available in Optima
    /**
     * Method responsible for reading XML-file, parsing it, and calling checkIfEanNumberExists()
     */
    public abstract void readXmlFileHeaders();

    /**
     * Method responsible for checking if parsed EAN numbers are already in OPTIMA databse, using DLL (OptimaLIBB)
     */
    public abstract boolean checkIfEanNumbersExists(String eanNumber);

    /**
     * Method responsible for adding new Item to Optima database, using DLL (OptimaLIBB)
     */
    public abstract void addNewEan(String eanNumber, String symbol, String itemName, int vatRate, String unitOfMeasure);

    /**
     * Method responsible for creating new PZ Document in Optima, using DLL (OptimaLIBB)
     */
    public abstract void addNewPZ();

    /**
     * Method responsible for adding missing items (with missing EAN's numbers) into Optima
     */
    public abstract void addMissingEans();

    public List<XMLPZPosition> getPZItemsList() {
        return PZItemsList;
    }

    public List<String> getUnitsOfMeasure(){
        return UnitsOfMeasure;
    }

}
