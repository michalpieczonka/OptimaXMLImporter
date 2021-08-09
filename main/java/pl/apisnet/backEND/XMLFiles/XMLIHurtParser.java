package pl.apisnet.backEND.XMLFiles;

/**
 * Class responsible for dealing with XML files generated in IHURT application
 */

import com.jacob.com.Dispatch;
import com.jacob.com.SafeArray;
import com.jacob.com.Variant;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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

public class XMLIHurtParser extends XMLImporter {


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
                        if(UnitsOfMeasure.contains(Pozycja.getAttribute("J_EW").trim())){
                            //Add this item to items list in PZ
                            PZItemsList.add(new IHurtXMLPZPosition(Pozycja.getAttribute("SYMBOL"),Integer.parseInt(Pozycja.getAttribute("ILOSC")),Double.parseDouble(Pozycja.getAttribute("CENA_PO_UPUSCIE")),Pozycja.getAttribute("KOD_KRESKOWY"),Pozycja.getAttribute("NAZWA_TOWARU"),Pozycja.getAttribute("J_EW"),Integer.parseInt(Pozycja.getAttribute("STAWKA_VAT")), false,true));
                        } else{ //Here it can stack because if jEW is not same as oryginal in Optima then its problem
                            PZItemsList.add(new IHurtXMLPZPosition(Pozycja.getAttribute("SYMBOL"),Integer.parseInt(Pozycja.getAttribute("ILOSC")),Double.parseDouble(Pozycja.getAttribute("CENA_PO_UPUSCIE")),Pozycja.getAttribute("KOD_KRESKOWY"),Pozycja.getAttribute("NAZWA_TOWARU"),Pozycja.getAttribute("J_EW"),Integer.parseInt(Pozycja.getAttribute("STAWKA_VAT")), false,false));
                        }
                    }
                    else{
                        //If item is already in Optima, then only add this item to items list in PZ
                        if(UnitsOfMeasure.contains(Pozycja.getAttribute("J_EW").trim())){
                            //Add this item to items list in PZ
                            PZItemsList.add(new IHurtXMLPZPosition(Pozycja.getAttribute("SYMBOL"),Integer.parseInt(Pozycja.getAttribute("ILOSC")),Double.parseDouble(Pozycja.getAttribute("CENA_PO_UPUSCIE")),Pozycja.getAttribute("KOD_KRESKOWY"),Pozycja.getAttribute("NAZWA_TOWARU"),Pozycja.getAttribute("J_EW"),Integer.parseInt(Pozycja.getAttribute("STAWKA_VAT")), true,true));
                        } else{
                            PZItemsList.add(new IHurtXMLPZPosition(Pozycja.getAttribute("SYMBOL"),Integer.parseInt(Pozycja.getAttribute("ILOSC")),Double.parseDouble(Pozycja.getAttribute("CENA_PO_UPUSCIE")),Pozycja.getAttribute("KOD_KRESKOWY"),Pozycja.getAttribute("NAZWA_TOWARU"),Pozycja.getAttribute("J_EW"),Integer.parseInt(Pozycja.getAttribute("STAWKA_VAT")), true,false));
                        }
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






    //Getters and setters section


    Optima getMainOptima() {
        return mainOptima;
    }


//
    String getOptimaFindEanClassName() {
        return optimaFindEanClassName;
    }


}
