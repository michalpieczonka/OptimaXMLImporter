package pl.apisnet.backEND;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import pl.apisnet.backEND.Entities.Customer;
import pl.apisnet.backEND.XMLFiles.XMLIHurtParser;
import pl.apisnet.backEND.XMLFiles.XMLObjects.ExcelXMLPZPosition;
import pl.apisnet.backEND.XMLFiles.XMLObjects.IHurtXMLPZPosition;
import pl.apisnet.backEND.XMLFiles.XMLObjects.SubiektXMLPZPosition;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class tempMain {

    public static void main(String[] args) throws IOException {
       Optima mainOptima = new Optima("Admin","","TEST2","C:/Program Files (x86)/Comarch ERP Optima");
       System.out.println(mainOptima.connectToOptima());
       System.out.println(mainOptima.getOptimaProgramID());
       System.out.println("odpalanie wczytania xml");
       XMLIHurtParser iHurt = new XMLIHurtParser("FS_8642_2021_RST_f.xml",mainOptima);
       iHurt.readXmlFileHeaders();
       System.out.println("wczytanie xml gotowe");
       iHurt.addMissingEans();
       System.out.println("dodawnaie pz");
       iHurt.addNewPZ();

    }

}

