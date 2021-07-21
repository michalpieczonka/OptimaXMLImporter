package pl.apisnet.backEND.XMLFiles;

/**
 * Class responsible for dealing with XML files generated in IHURT application
 */

import com.jacob.com.Dispatch;
import com.jacob.com.SafeArray;
import com.jacob.com.Variant;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import pl.apisnet.backEND.Optima;
import pl.apisnet.backEND.XMLFiles.XMLObjects.IHurtXMLPZPosition;
import pl.apisnet.backEND.XMLFiles.XMLObjects.XMLPZPosition;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLIHurtParser implements XMLInterface {

    private final String optimaFindEanClassName = "findEan"; //Name of finding EAN function in DLL library created in C# using Optima API
    private final String optimaAddNewItemClassName = "addItemByEan"; //Name of adding new Item function in DLL library crated in c# using Optima API
    private final String optimaAddNewPzClassName = "addPZDocument"; //Name of adding new PZ function in DLL library created in c# using Optima API

    private String xmlFilePath;  //Path to XMLFILE to be parsed
    private DocumentBuilderFactory factory;
    private Document xmlFile;
    private NodeList elementsInXml_Pozycja;  //Each single tag in XML FILE -> this case -> 1 single item between <Pozycje> ... </Pozycje>
    private Optima mainOptima;

    //private List<IHurtXMLPZPosition> PZItemsList;
    private List<XMLPZPosition> PZItemsList;

    /**
     * Parameters (Path_To_xml_file, global instance of Optima class Object)
     */
    public XMLIHurtParser(String xmlFilePath, Optima mainOptima){
        this.mainOptima = mainOptima;
        this.xmlFilePath = xmlFilePath;
        this.PZItemsList = new ArrayList<>();
    }


    /**
     * The most important function. Reading xml file, and invoking other necessary methods to deal with given XML File
     */
    @Override
    public void readXmlFileHeaders() {
        try{
            factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            xmlFile = builder.parse(xmlFilePath);
            elementsInXml_Pozycja = xmlFile.getElementsByTagName("POZYCJA");
            for (int i = 0; i  < elementsInXml_Pozycja.getLength(); i++){
                Node headerItem = elementsInXml_Pozycja.item(i);
                if (headerItem.getNodeType() == Node.ELEMENT_NODE){
                    Element Pozycja = (Element) headerItem;
                    //If item with given EAN number not exists
                    if (!checkIfEanNumbersExists(Pozycja.getAttribute("KOD_KRESKOWY"))){
                        //Add new Item to Optima , then add this item to items list in PZ
                        addNewEan(Pozycja.getAttribute("KOD_KRESKOWY"),Pozycja.getAttribute("SYMBOL"), Pozycja.getAttribute("NAZWA_TOWARU"),Integer.parseInt(Pozycja.getAttribute("STAWKA_VAT")), Pozycja.getAttribute("J_EW"));
                        PZItemsList.add(new IHurtXMLPZPosition(Pozycja.getAttribute("SYMBOL"),Integer.parseInt(Pozycja.getAttribute("ILOSC")),Double.parseDouble(Pozycja.getAttribute("CENA_PO_UPUSCIE")),Pozycja.getAttribute("KOD_KRESKOWY"),Pozycja.getAttribute("NAZWA_TOWARU")));
                    }
                    else{
                        //If item is already in Optima, then only add this item to items list in PZ
                        PZItemsList.add(new IHurtXMLPZPosition(Pozycja.getAttribute("SYMBOL"),Integer.parseInt(Pozycja.getAttribute("ILOSC")),Double.parseDouble(Pozycja.getAttribute("CENA_PO_UPUSCIE")),Pozycja.getAttribute("KOD_KRESKOWY"),Pozycja.getAttribute("NAZWA_TOWARU")));
                    }
                }
            }
            //addNewPZ(PZItemsList);


        }catch (ParserConfigurationException e){
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Returns if Optima already contains Ean with given argument
     * Invoking method @optimaFindEanClassName from OptimaLIBB.dll
     * Parameters - Ean number to check
     */
    @Override
    public boolean checkIfEanNumbersExists(String eanNumberToCheck) {
            return Dispatch.call(mainOptima.getOptimaProgramID(),optimaFindEanClassName,eanNumberToCheck).getBoolean();
    }

    /**
     * Adding new Item to Optima
     * Invoking method @optimaAddNewItemClassName from OptimaLIBB.dll
     * Parameters - necessary components of the item (ean,symbol,name,VatRate,unitOfMeasure)
     */
    @Override
    public void addNewEan(String eanNumber, String symbol, String itemName, int vatRate, String unitOfMeasure) {
        Dispatch.call(mainOptima.getOptimaProgramID(),optimaAddNewItemClassName,eanNumber,symbol,itemName,vatRate,unitOfMeasure);
    }


    /**
     * Adding new PZ to Optima
     * Invoking method @optimaAddNewPzClassName from OptimaLIBB.dll
     * Parameters - list of items in XML file
     */
    @Override
    public void addNewPZ() {
        int listOfItemsSize = PZItemsList.size();
        IHurtXMLPZPosition[] tab = PZItemsList.toArray(new IHurtXMLPZPosition[0]);
        SafeArray eansArray = new SafeArray (Variant.VariantString,listOfItemsSize);
        SafeArray amountArray = new SafeArray (Variant.VariantInt, listOfItemsSize);
        SafeArray pricesArray = new SafeArray (Variant.VariantDouble, listOfItemsSize);
        for (int i=0; i < listOfItemsSize; i++){
            eansArray.setString(i, tab[i].getEAN());
            amountArray.setInt(i,tab[i].getIlosc());
            pricesArray.setDouble(i, tab[i].getCena());
        }
        Dispatch.call(mainOptima.getOptimaProgramID(), optimaAddNewPzClassName, eansArray, amountArray, pricesArray);
    }

    @Override
    public void notification() {

    }

    //Getters and setters section


    Optima getMainOptima() {
        return mainOptima;
    }

    public List<XMLPZPosition> getPZItemsList() {
        return PZItemsList;
    }

    String getOptimaFindEanClassName() {
        return optimaFindEanClassName;
    }


}
