package pl.apisnet.backEND.XMLFiles;

import com.jacob.com.Dispatch;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.apisnet.backEND.Exceptions.FileStructureException;
import pl.apisnet.backEND.Optima;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class XMLIHurtParserTest {

    private Optima mainOptima;
    private XMLIHurtParser iHurt;

    @BeforeEach
    void createConnectionToNecessaryObjects(){
        mainOptima = new Optima("Admin","","Test3","C:/Program Files (x86)/Comarch ERP Optima");
        iHurt = new XMLIHurtParser("testXMLFile.xml",mainOptima);
    }

    @AfterEach
    void disconnectFromOptimaObjects(){
      //  mainOptima.disconnectFromOptima();
        iHurt.getMainOptima().disconnectFromOptima();
    }

    @Test
    void ifMethodStartsApplicationShouldHaveConnectionWithOptima(){
        assertAll(
                () -> assertTrue(mainOptima.connectToOptima()),
                () -> assertNotNull(iHurt.getMainOptima())
        );
    }

    @Test
    void eanNumberShouldNotExistInGivenXMLFile(){
        mainOptima.connectToOptima();
       assertFalse(Dispatch.call(iHurt.getMainOptima().getOptimaProgramID(), iHurt.getOptimaFindEanClassName(),"123").getBoolean());
    }

    @Test
    void eanNumberShouldExistInGivenXMLFile(){
        mainOptima.connectToOptima();
        assertTrue(Dispatch.call(iHurt.getMainOptima().getOptimaProgramID(), iHurt.getOptimaFindEanClassName(),"5904000044376").getBoolean());
    }

    @Test
    void newItemWithNewEanShouldBeAddedToOptima(){
        mainOptima.connectToOptima();
        assertAll(
                () -> assertFalse(Dispatch.call(iHurt.getMainOptima().getOptimaProgramID(), iHurt.getOptimaFindEanClassName(),"123").getBoolean()),
                () -> iHurt.addNewEan("123","TestItemFromJava","Item test from java",23,"SZT"),
                () -> assertTrue(Dispatch.call(iHurt.getMainOptima().getOptimaProgramID(), iHurt.getOptimaFindEanClassName(),"123").getBoolean())
        );
    }

    @Test
    void pzListShouldContains2Elements(){
        mainOptima.connectToOptima();
        //iHurt.readXmlFileHeaders();
        assertEquals(2,iHurt.getPZItemsList().size());
    }

    @Test
    void pzListShouldContains2ElementsWithSpecifiedRequirements() throws FileStructureException {
        mainOptima.connectToOptima();
        iHurt.readXmlFileHeaders();
       assertAll(
               () -> assertEquals("5904000044376",iHurt.getPZItemsList().get(0).getEAN()),
               () -> assertEquals("BONDEX281",iHurt.getPZItemsList().get(0).getSymbol()),
               () -> assertEquals("5904000044710",iHurt.getPZItemsList().get(1).getEAN()),
               () -> assertEquals("BONDEX282",iHurt.getPZItemsList().get(1).getSymbol())
       );
    }

    @Test
    void exceptionShouldBeThrownWhenInputFileHasIncorrectSynthax(){
        mainOptima = new Optima("Admin","","Test3","C:/Program Files (x86)/Comarch ERP Optima");
        mainOptima.connectToOptima();
        iHurt = new XMLIHurtParser("dodatkowexml/wrongSynthaxtestXMLFile.xml",mainOptima);
        assertThrows(FileStructureException.class,()->iHurt.readXmlFileHeaders());
    }

}