package pl.apisnet.backEND;

import pl.apisnet.backEND.XMLFiles.XMLIHurtParser;

public class tempMain {
    public static void main(String[] args) {
        Optima mainOptima = new Optima("Admin","","Test3","C:/Program Files (x86)/Comarch ERP Optima");
        System.out.println(mainOptima.connectToOptima());

        System.out.println("Parsowanie:::::");
        XMLIHurtParser iHurt = new XMLIHurtParser("testXMLFile.xml",mainOptima);
        iHurt.readXmlFileHeaders();
        iHurt.addNewPZ();

    }
}
