package pl.apisnet.backEND.XMLFiles;

/**
 * Class responsible for dealing with XML files generated in IHURT application
 */

import com.jacob.com.Dispatch;
import com.jacob.com.SafeArray;
import com.jacob.com.Variant;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import pl.apisnet.backEND.Exceptions.FileStructureException;
import pl.apisnet.backEND.Exceptions.ImportPZException;
import pl.apisnet.backEND.Optima;
import pl.apisnet.backEND.XMLFiles.XMLObjects.IHurtXMLPZPosition;
import pl.apisnet.backEND.XMLFiles.XMLObjects.XMLPZPosition;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
    public void readXmlFileHeaders() throws FileStructureException {
        try{
            //First check what type of file was given by Customer
            BufferedReader br = new BufferedReader(new FileReader(xmlFilePath));
            br.readLine();
            String basicLine = br.readLine();
            br.close();
            basicLine = basicLine.substring(0,2); //Get only first two characters
            //Based on first two characters, decide which parser use
            switch (basicLine) {
                case "<W" -> readXMLIHurtSyntax(); //For typical Polish IHurt xml file
                case "<D" -> readXMLIHurtEnglishSyntax(); //For typical English IHurt xml file
                case "  " -> readXMLUnknownSyntax(); //For extra random xml file
                default -> throw new FileStructureException(); //If none matches then throw exception
            }
        } catch (IOException e){
            System.out.println(e);
        } catch (SAXException e) {
            throw new FileStructureException();
        }


    }

    private void readXMLIHurtSyntax() throws FileStructureException, SAXException {
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
                            PZItemsList.add(new IHurtXMLPZPosition(Pozycja.getAttribute("SYMBOL"),Integer.parseInt(Pozycja.getAttribute("ILOSC")),Double.parseDouble(Pozycja.getAttribute("CENA_PO_UPUSCIE")),Pozycja.getAttribute("KOD_KRESKOWY"),Pozycja.getAttribute("NAZWA_TOWARU"),Pozycja.getAttribute("J_EW"),Integer.parseInt(Pozycja.getAttribute("STAWKA_VAT")), false,true,false));
                        } else{ //Here it can stack because if jEW is not same as oryginal in Optima then its problem
                            PZItemsList.add(new IHurtXMLPZPosition(Pozycja.getAttribute("SYMBOL"),Integer.parseInt(Pozycja.getAttribute("ILOSC")),Double.parseDouble(Pozycja.getAttribute("CENA_PO_UPUSCIE")),Pozycja.getAttribute("KOD_KRESKOWY"),Pozycja.getAttribute("NAZWA_TOWARU"),Pozycja.getAttribute("J_EW"),Integer.parseInt(Pozycja.getAttribute("STAWKA_VAT")), false,false,false));
                        }
                    }
                    else{
                        //If item is already in Optima, then only add this item to items list in PZ
                        if(UnitsOfMeasure.contains(Pozycja.getAttribute("J_EW").trim())){
                            //Add this item to items list in PZ
                            PZItemsList.add(new IHurtXMLPZPosition(Pozycja.getAttribute("SYMBOL"),Integer.parseInt(Pozycja.getAttribute("ILOSC")),Double.parseDouble(Pozycja.getAttribute("CENA_PO_UPUSCIE")),Pozycja.getAttribute("KOD_KRESKOWY"),Pozycja.getAttribute("NAZWA_TOWARU"),Pozycja.getAttribute("J_EW"),Integer.parseInt(Pozycja.getAttribute("STAWKA_VAT")), true,true,true));
                        } else{
                            PZItemsList.add(new IHurtXMLPZPosition(Pozycja.getAttribute("SYMBOL"),Integer.parseInt(Pozycja.getAttribute("ILOSC")),Double.parseDouble(Pozycja.getAttribute("CENA_PO_UPUSCIE")),Pozycja.getAttribute("KOD_KRESKOWY"),Pozycja.getAttribute("NAZWA_TOWARU"),Pozycja.getAttribute("J_EW"),Integer.parseInt(Pozycja.getAttribute("STAWKA_VAT")), true,false,true));
                        }
                    }
                }
            }
            if (PZItemsList.size() == 0)
                throw new FileStructureException();
            else{
                if (itemSymbolAsEan){
                    for (XMLPZPosition item: PZItemsList){
                        item.setSymbol(item.getEAN());
                        if (item.isAlreadyInOptima())
                            checkIfItemIsCorrect(item);
                    }
                }
                else{
                    for(XMLPZPosition item: PZItemsList){
                        if (item.isAlreadyInOptima())
                            checkIfItemIsCorrect(item);
                    }
                }
            }

        }catch (ParserConfigurationException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readXMLIHurtEnglishSyntax() throws FileStructureException, SAXException{
        try{
            factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            xmlFile = builder.parse(xmlFilePath);
            elementsInXml_Pozycja = xmlFile.getElementsByTagName("Detail");
            for (int i = 0; i  < elementsInXml_Pozycja.getLength(); i++){
                Node headerItem = elementsInXml_Pozycja.item(i);
                if (headerItem.getNodeType() == Node.ELEMENT_NODE){
                    Element Pozycja = (Element) headerItem;
                    //If item with given EAN number not exists
                    if (!checkIfEanNumbersExists(Pozycja.getElementsByTagName("EAN").item(0).getTextContent())){
                        if(UnitsOfMeasure.contains(Pozycja.getElementsByTagName("UnitOfMeasure").item(0).getTextContent())){
                            //Add this item to items list in PZ
                               PZItemsList.add(new IHurtXMLPZPosition(Pozycja.getElementsByTagName("SupplierItemCode").item(0).getTextContent(),Integer.parseInt(Pozycja.getElementsByTagName("Quantity").item(0).getTextContent()),Double.parseDouble(Pozycja.getElementsByTagName("UnitPriceAfterDiscount").item(0).getTextContent()),Pozycja.getElementsByTagName("EAN").item(0).getTextContent(),Pozycja.getElementsByTagName("Description").item(0).getTextContent(),Pozycja.getElementsByTagName("UnitOfMeasure").item(0).getTextContent(),Integer.parseInt(Pozycja.getElementsByTagName("TaxRate").item(0).getTextContent()), false,true,false));
                        } else{ //Here it can stack because if jEW is not same as oryginal in Optima then its problem
                              PZItemsList.add(new IHurtXMLPZPosition(Pozycja.getElementsByTagName("SupplierItemCode").item(0).getTextContent(),Integer.parseInt(Pozycja.getElementsByTagName("Quantity").item(0).getTextContent()),Double.parseDouble(Pozycja.getElementsByTagName("UnitPriceAfterDiscount").item(0).getTextContent()),Pozycja.getElementsByTagName("EAN").item(0).getTextContent(),Pozycja.getElementsByTagName("Description").item(0).getTextContent(),Pozycja.getElementsByTagName("UnitOfMeasure").item(0).getTextContent(),Integer.parseInt(Pozycja.getElementsByTagName("TaxRate").item(0).getTextContent()), false,false,false));
                        }
                    }
                    else{
                        //If item is already in Optima, then only add this item to items list in PZ
                        if(UnitsOfMeasure.contains(Pozycja.getElementsByTagName("UnitOfMeasure").item(0).getTextContent())){
                            //Add this item to items list in PZ
                               PZItemsList.add(new IHurtXMLPZPosition(Pozycja.getElementsByTagName("SupplierItemCode").item(0).getTextContent(),Integer.parseInt(Pozycja.getElementsByTagName("Quantity").item(0).getTextContent()),Double.parseDouble(Pozycja.getElementsByTagName("UnitPriceAfterDiscount").item(0).getTextContent()),Pozycja.getElementsByTagName("EAN").item(0).getTextContent(),Pozycja.getElementsByTagName("Description").item(0).getTextContent(),Pozycja.getElementsByTagName("UnitOfMeasure").item(0).getTextContent(),Integer.parseInt(Pozycja.getElementsByTagName("TaxRate").item(0).getTextContent()), true,true,true));
                        } else{
                               PZItemsList.add(new IHurtXMLPZPosition(Pozycja.getElementsByTagName("SupplierItemCode").item(0).getTextContent(),Integer.parseInt(Pozycja.getElementsByTagName("Quantity").item(0).getTextContent()),Double.parseDouble(Pozycja.getElementsByTagName("UnitPriceAfterDiscount").item(0).getTextContent()),Pozycja.getElementsByTagName("EAN").item(0).getTextContent(),Pozycja.getElementsByTagName("Description").item(0).getTextContent(),Pozycja.getElementsByTagName("UnitOfMeasure").item(0).getTextContent(),Integer.parseInt(Pozycja.getElementsByTagName("TaxRate").item(0).getTextContent()), true,false,true));
                        }
                    }
                }
            }
            if (PZItemsList.size() == 0)
                throw new FileStructureException();
            else{
                if (itemSymbolAsEan){
                    for (XMLPZPosition item: PZItemsList){
                        item.setSymbol(item.getEAN());
                        if (item.isAlreadyInOptima())
                            checkIfItemIsCorrect(item);
                    }
                }
                else{
                    for(XMLPZPosition item: PZItemsList){
                        if (item.isAlreadyInOptima())
                            checkIfItemIsCorrect(item);
                    }
                }
            }

        }catch (ParserConfigurationException e){
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readXMLUnknownSyntax() throws FileStructureException, SAXException{
        if (itemSymbolAsEan){
            try{
                factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                xmlFile = builder.parse(xmlFilePath);
                elementsInXml_Pozycja = xmlFile.getElementsByTagName("Pozycja");
                for (int i = 0; i  < elementsInXml_Pozycja.getLength(); i++){
                    Node headerItem = elementsInXml_Pozycja.item(i);
                    if (headerItem.getNodeType() == Node.ELEMENT_NODE){
                        Element Pozycja = (Element) headerItem;
                        //If item with given EAN number not exists
                        if (!checkIfEanNumbersExists(Pozycja.getElementsByTagName("EAN").item(0).getTextContent())){
                            if(UnitsOfMeasure.contains(Pozycja.getElementsByTagName("JM").item(0).getTextContent())){
                                //Add this item to items list in PZ
                                PZItemsList.add(new IHurtXMLPZPosition(Pozycja.getElementsByTagName("EAN").item(0).getTextContent(),(int)Float.parseFloat(Pozycja.getElementsByTagName("Ilosc").item(0).getTextContent()),Double.parseDouble(Pozycja.getElementsByTagName("Cena").item(0).getTextContent()),Pozycja.getElementsByTagName("EAN").item(0).getTextContent(),Pozycja.getElementsByTagName("Nazwa").item(0).getTextContent(),Pozycja.getElementsByTagName("JM").item(0).getTextContent(),Integer.parseInt(Pozycja.getElementsByTagName("StawkaVAT").item(0).getTextContent()), false,true,false));
                            } else{ //Here it can stack because if jEW is not same as oryginal in Optima then its problem
                                PZItemsList.add(new IHurtXMLPZPosition(Pozycja.getElementsByTagName("EAN").item(0).getTextContent(),(int)Float.parseFloat(Pozycja.getElementsByTagName("Ilosc").item(0).getTextContent()),Double.parseDouble(Pozycja.getElementsByTagName("Cena").item(0).getTextContent()),Pozycja.getElementsByTagName("EAN").item(0).getTextContent(),Pozycja.getElementsByTagName("Nazwa").item(0).getTextContent(),Pozycja.getElementsByTagName("JM").item(0).getTextContent(),Integer.parseInt(Pozycja.getElementsByTagName("StawkaVAT").item(0).getTextContent()), false,false,false));
                            }
                        }
                        else{
                            //If item is already in Optima, then only add this item to items list in PZ
                            if(UnitsOfMeasure.contains(Pozycja.getElementsByTagName("JM").item(0).getTextContent())){
                                //Add this item to items list in PZ
                                PZItemsList.add(new IHurtXMLPZPosition(Pozycja.getElementsByTagName("EAN").item(0).getTextContent(),(int)Float.parseFloat(Pozycja.getElementsByTagName("Ilosc").item(0).getTextContent()),Double.parseDouble(Pozycja.getElementsByTagName("Cena").item(0).getTextContent()),Pozycja.getElementsByTagName("EAN").item(0).getTextContent(),Pozycja.getElementsByTagName("Nazwa").item(0).getTextContent(),Pozycja.getElementsByTagName("JM").item(0).getTextContent(),Integer.parseInt(Pozycja.getElementsByTagName("StawkaVAT").item(0).getTextContent()), true,true,true));
                            } else{
                                PZItemsList.add(new IHurtXMLPZPosition(Pozycja.getElementsByTagName("EAN").item(0).getTextContent(),(int)Float.parseFloat(Pozycja.getElementsByTagName("Ilosc").item(0).getTextContent()),Double.parseDouble(Pozycja.getElementsByTagName("Cena").item(0).getTextContent()),Pozycja.getElementsByTagName("EAN").item(0).getTextContent(),Pozycja.getElementsByTagName("Nazwa").item(0).getTextContent(),Pozycja.getElementsByTagName("JM").item(0).getTextContent(),Integer.parseInt(Pozycja.getElementsByTagName("StawkaVAT").item(0).getTextContent()), true,false,true));
                            }
                        }
                    }
                }
                if (PZItemsList.size() == 0)
                    throw new FileStructureException();
                else{
                    for(XMLPZPosition item: PZItemsList){
                        if (item.isAlreadyInOptima())
                            checkIfItemIsCorrect(item);
                    }
                }

            }catch (ParserConfigurationException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else
            throw new FileStructureException();

    }

    /**
     * Adding new PZ to Optima
     * Invoking method @optimaAddNewPzClassName from OptimaLIBB.dll
     * Parameters - list of items in XML file
     */
    @Override
    public void addNewPZ() throws ImportPZException {
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
        //Operation result = true if PZ was successfully created | false = if PZ was not successfully created
        boolean operationResult =  Dispatch.call(mainOptima.getOptimaProgramID(), optimaAddNewPzClassName, eansArray, amountArray, pricesArray).getBoolean();
        if (!operationResult)
            throw new ImportPZException();
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
