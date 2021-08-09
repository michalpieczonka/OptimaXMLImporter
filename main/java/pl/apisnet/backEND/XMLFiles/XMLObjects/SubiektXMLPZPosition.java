package pl.apisnet.backEND.XMLFiles.XMLObjects;

import java.math.BigDecimal;

public class SubiektXMLPZPosition extends XMLPZPosition{

    BigDecimal cena2;
    public SubiektXMLPZPosition(String symbol, int ilosc, BigDecimal cena, String EAN, String nazwa, String jEW, int stawkaVat, boolean isAlreadyInOptima, boolean isjEWCorrect) {
        this.symbol = symbol;
        this.ilosc = ilosc;
        this.cena2 = cena;
        this.stawkaVat = stawkaVat;
        this.jEW = jEW;
        this.EAN = EAN;
        this.nazwa = nazwa;
        this.isAlreadyInOptima = isAlreadyInOptima;
        this.isjEWCorrect = isjEWCorrect;
    }
    public int getStawkaVat(){return stawkaVat;}
    public String getjEW(){return jEW;}
    public String getNazwa(){ return nazwa;}
    public String getSymbol() {
        return symbol;
    }

    public int getIlosc() {
        return ilosc;
    }

    public double getCena() {
        double value=cena2.doubleValue();
        return value;
    }

    public String getEAN() {
        return EAN;
    }


    public boolean getIsAlreadyInOptima(){
        return isAlreadyInOptima;
    }

    public boolean isIsjEWCorrect() {
        return isjEWCorrect;
    }

    public void setIsjEWCorrect(boolean isjEWCorrect) {
        this.isjEWCorrect = isjEWCorrect;
    }
}
