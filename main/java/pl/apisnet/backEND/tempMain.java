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
      Configuration conf = new Configuration();
      conf.configure("hibernate.cfg.xml");
      conf.addAnnotatedClass(Customer.class);
      SessionFactory factory = conf.buildSessionFactory();
        //SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.getCurrentSession();
        Customer admin = new Customer();
        admin.setLogin("Admin");
        admin.setPassword("Limanowa2021");
        admin.setNIP(123456789);
        session.beginTransaction();
        session.save(admin);
        session.getTransaction().commit();
        factory.close();
        //     List<SubiektXMLPZPosition> pozycje = new ArrayList<>();
        //     String NLfile = "testdlug3.epp";

        //     FileInputStream fstream = new FileInputStream(NLfile);
        //     BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        //     String strLine;
        //     int counter = 0;
        //     int firstLineCounter = 0;
        //     int secondLineCounter = 0;
        //    //Read File Line By Line
        //     while ((strLine = br.readLine()) != null)   {
        //         if (strLine.equals("[ZAWARTOSC]")){
        //             //1 wystapienie naglowka [zawartosc] do odczytu jednostki miary, ceny, rabatu i stawki vat
        //             if (counter == 0){
        //                 firstLineCounter = secondLineCounter;
        //             }
        //             //5 wystapienie naglowka [zawartsc] do odczytu nazwy towaru, kodu EAN, symbolu
        //             if(counter == 5){
        //                 break;
        //             }else {
        //                 counter++;
        //             }
        //         }
        //         secondLineCounter++;
        //     }
        //     System.out.println(firstLineCounter+" "+secondLineCounter);
        //     fstream.close();

        //    List<String> result1 = null;
        //    List<String> result2 = null;
        //    BufferedReader reader;
        //    try{
        //       //Odczytanie pierwszego wystapienia [zawartosc] z pliku epp i zapisanie go do listy result1
        //       reader = new BufferedReader(new FileReader(NLfile));
        //           result1 = reader.lines().skip(firstLineCounter)
        //                   .dropWhile(line -> !line.equals("[ZAWARTOSC]"))
        //                   .skip(1) // skip the start line
        //                   .takeWhile(line -> !line.equals("[NAGLOWEK]"))
        //                   .collect(Collectors.toList());
        //           reader.close();

        //       //Odczytanie piatego wystapienia [zawartosc] z pliku epp i zapisanie go do listy result2
        //       reader = new BufferedReader(new FileReader(NLfile));
        //       result2 = reader.lines().skip(secondLineCounter)
        //               .dropWhile(line -> !line.equals("[ZAWARTOSC]"))
        //               .skip(1) // skip the start line
        //               .takeWhile(line -> !line.equals("[NAGLOWEK]"))
        //               .collect(Collectors.toList());
        //    } catch (IOException ie) {
        //       System.out.println("Unable to create " + NLfile + ": " + ie.getMessage());
        //       ie.printStackTrace();
        //    }

        //    //Wyczyszczenie nulli z parsera z listy elementow
        //    for(int i=0;i<result1.size();i++){
        //             if (result2.get(i).equals(""))
        //                 result2.remove(result2.get(i));

        //             if (result1.get(i).equals("")){
        //                 result1.remove(result1.get(i));
        //             }
        //    }

        //Parsowanie danych do tymczasowych tablic
        //    String[] Res1 = null;
        //    String[] Res2 = null;
        //    for (int i=0; i<result1.size();i++){
        //             Res1 = result1.get(i).split(",");
        //             Res2 = result2.get(i).split(",");
        //        //Uklad elementow:
        //        //symbol - 2Res[1], ilosc - 1Res[10]? ,EAN - 2Res[3], nazwa - 2Res[4], jEW - 1Res[9], stawkaVat - 1Res[15]
        //        //Cena - tutaj problem subiekt nie eksportuje ceny po rabacie, tylko cene netto i % rabatu -> trzeba policzyc
        //        //Cena netto - 1Res[13] ; Rabat% - 1Res[8]
        //        BigDecimal cena = BigDecimal.valueOf(Double.parseDouble(Res1[13]));
        //        BigDecimal rabat = cena.multiply((BigDecimal.valueOf(Double.parseDouble(Res1[8])).divide(BigDecimal.valueOf(100))));
        //        rabat.setScale(2,RoundingMode.HALF_UP);
        //        cena = cena.subtract(rabat);
        //        cena = cena.setScale(2, RoundingMode.HALF_UP);
        //        System.out.println(cena);

        //        pozycje.add(new SubiektXMLPZPosition(Res2[1].replace("\"",""),(int)(Double.parseDouble(Res1[10])),cena,Res2[3].replace("\"",""),Res2[4].replace("\"",""),Res1[9],(int)(Double.parseDouble(Res1[15])),false,false ));
        //    }
        // }

    }

}

