package pl.apisnet.backEND.XMLFiles;

import java.util.List;

/**
 * Interface responsible for all XMLParsers
 */

public interface XMLInterface {

    /**
     * Method responsible for reading XML-file, parsing it, and calling checkIfEanNumberExists()
     */
    void readXmlFileHeaders();

    /**
     * Method responsible for checking if parsed EAN numbers are already in OPTIMA databse, using DLL (OptimaLIBB)
     */
    boolean checkIfEanNumbersExists(String eanNumber);

    /**
     * Method responsible for adding new Item to Optima database, using DLL (OptimaLIBB)
     */
    void addNewEan(String eanNumber, String symbol, String itemName, int vatRate, String unitOfMeasure);

    /**
     * Method responsible for creating new PZ Document in Optima, using DLL (OptimaLIBB)
     */
    void addNewPZ(List<Object> itemList);

    /**
     * Additional method responsible for sending communication to userUI
     */
    void notification();
}
