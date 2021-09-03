package pl.apisnet.backEND.XMLFiles;

import com.jacob.com.Dispatch;
import com.jacob.com.SafeArray;
import com.jacob.com.Variant;
import pl.apisnet.backEND.Optima;
import pl.apisnet.backEND.XMLFiles.XMLObjects.IHurtXMLPZPosition;
import pl.apisnet.backEND.XMLFiles.XMLObjects.SubiektXMLPZPosition;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class XMLSubiektParser extends XMLImporter{

    public XMLSubiektParser(String xmlFilePath, Optima mainOptima){
        this.mainOptima = mainOptima;
        this.xmlFilePath = xmlFilePath;
        this.PZItemsList = new ArrayList<>();
    }

    @Override
    public void readXmlFileHeaders() {

      try{
          FileInputStream fstream = new FileInputStream(xmlFilePath);
          BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
          String strLine;
          int counter = 0;
          int firstLineCounter = 0;
          int secondLineCounter = 0;
          //Read File Line By Line
          while ((strLine = br.readLine()) != null)   {
              if (strLine.equals("[ZAWARTOSC]")){
                  //1 wystapienie naglowka [zawartosc] do odczytu jednostki miary, ceny, rabatu i stawki vat
                  if (counter == 0){
                      firstLineCounter = secondLineCounter;
                  }
                  //5 wystapienie naglowka [zawartsc] do odczytu nazwy towaru, kodu EAN, symbolu
                  if(counter == 5){
                      break;
                  }else {
                      counter++;
                  }
              }
              secondLineCounter++;
          }
          //System.out.println(firstLineCounter+" "+secondLineCounter);
          fstream.close();

          List<String> result1 = null;
          List<String> result2 = null;
          BufferedReader reader;
          try{
              //Odczytanie pierwszego wystapienia [zawartosc] z pliku epp i zapisanie go do listy result1
              reader = new BufferedReader(new FileReader(xmlFilePath));
              result1 = reader.lines().skip(firstLineCounter)
                      .dropWhile(line -> !line.equals("[ZAWARTOSC]"))
                      .skip(1) // skip the start line
                      .takeWhile(line -> !line.equals("[NAGLOWEK]"))
                      .collect(Collectors.toList());
              reader.close();

              //Odczytanie piatego wystapienia [zawartosc] z pliku epp i zapisanie go do listy result2
              reader = new BufferedReader(new FileReader(xmlFilePath));
              result2 = reader.lines().skip(secondLineCounter)
                      .dropWhile(line -> !line.equals("[ZAWARTOSC]"))
                      .skip(1) // skip the start line
                      .takeWhile(line -> !line.equals("[NAGLOWEK]"))
                      .collect(Collectors.toList());
          } catch (IOException ie) {
              System.out.println("Unable to create " + xmlFilePath + ": " + ie.getMessage());
              ie.printStackTrace();
          }

          //Wyczyszczenie nulli z parsera z listy elementow
          for(int i=0;i<result1.size();i++){
              if (result2.get(i).equals(""))
                  result2.remove(result2.get(i));

              if (result1.get(i).equals("")){
                  result1.remove(result1.get(i));
              }
          }

          //Parsowanie danych do tymczasowych tablic
          String[] Res1 = null;
          String[] Res2 = null;
          for (int i=0; i<result1.size();i++){
              Res1 = result1.get(i).split(",");
              Res2 = result2.get(i).split(",");

              byte[] bytes_ISO8852 = Res2[4].getBytes("ISO-8859-1");
              String UTF82ConvertedString = new String(bytes_ISO8852);
              System.out.println(UTF82ConvertedString);

              //Uklad elementow:
              //symbol - 2Res[1], ilosc - 1Res[10]? ,EAN - 2Res[3], nazwa - 2Res[4], jEW - 1Res[9], stawkaVat - 1Res[15]
              //Cena - tutaj problem subiekt nie eksportuje ceny po rabacie, tylko cene netto i % rabatu -> trzeba policzyc
              //Cena netto - 1Res[13] ; Rabat% - 1Res[8]
              BigDecimal cena = BigDecimal.valueOf(Double.parseDouble(Res1[13]));
              BigDecimal rabat = cena.multiply((BigDecimal.valueOf(Double.parseDouble(Res1[8])).divide(BigDecimal.valueOf(100))));
              rabat.setScale(2, RoundingMode.HALF_UP);
              cena = cena.subtract(rabat);
              cena = cena.setScale(2, RoundingMode.HALF_UP);

             // System.out.println(cena);

              if (!checkIfEanNumbersExists(Res2[3].replace("\"",""))){
                  if(UnitsOfMeasure.contains(Res1[9].trim())){
                      //Add this item to items list in PZ
                      PZItemsList.add(new SubiektXMLPZPosition(Res2[1].replace("\"",""),(int)(Double.parseDouble(Res1[10])),cena,Res2[3].replace("\"",""),Res2[4].replace("\"",""),Res1[9].replace("\"",""),(int)(Double.parseDouble(Res1[15])),false,true ));
                  } else{ //Here it can stack because if jEW is not same as oryginal in Optima then its problem
                      PZItemsList.add(new SubiektXMLPZPosition(Res2[1].replace("\"",""),(int)(Double.parseDouble(Res1[10])),cena,Res2[3].replace("\"",""),Res2[4].replace("\"",""),Res1[9].replace("\"",""),(int)(Double.parseDouble(Res1[15])),false,false ));
                  }
              }
              else{
                  //If item is already in Optima, then only add this item to items list in PZ
                  if(UnitsOfMeasure.contains(Res2[3].replace("\"",""))){
                      //Add this item to items list in PZ
                      PZItemsList.add(new SubiektXMLPZPosition(Res2[1].replace("\"",""),(int)(Double.parseDouble(Res1[10])),cena,Res2[3].replace("\"",""),Res2[4].replace("\"",""),Res1[9].replace("\"",""),(int)(Double.parseDouble(Res1[15])),true,true ));
                  } else{
                      PZItemsList.add(new SubiektXMLPZPosition(Res2[1].replace("\"",""),(int)(Double.parseDouble(Res1[10])),cena,Res2[3].replace("\"",""),Res2[4].replace("\"",""),Res1[9].replace("\"",""),(int)(Double.parseDouble(Res1[15])),true,false ));
                  }
              }

          }

       }catch (Exception e){ }

    }

    /**
     * Adding new PZ to Optima
     * Invoking method @optimaAddNewPzClassName from OptimaLIBB.dll
     * Parameters - list of items in XML file
     */
    @Override
    public void addNewPZ() {
        int listOfItemsSize = PZItemsList.size();
        SubiektXMLPZPosition[] tab = PZItemsList.toArray(new SubiektXMLPZPosition[0]);
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
}
