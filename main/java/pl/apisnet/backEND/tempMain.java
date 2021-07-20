package pl.apisnet.backEND;

import pl.apisnet.backEND.XMLFiles.XMLIHurtParser;

public class tempMain {
    public static void main(String[] args) {
        Optima mainOptima = new Optima("Admin","","Test4","C:/Program Files (x86)/Comarch ERP Optima");
        System.out.println(mainOptima.connectToOptima());

        System.out.println("odpalanie wczytania xml");
        XMLIHurtParser iHurt = new XMLIHurtParser("FS_8642_2021_RST_f.xml",mainOptima);
        iHurt.readXmlFileHeaders();
        System.out.println("wczytanie xml gotowe");
        System.out.println("dodawnaie pz");
        iHurt.addNewPZ();

    }
}
