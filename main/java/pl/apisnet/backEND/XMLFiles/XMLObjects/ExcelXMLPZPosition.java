package pl.apisnet.backEND.XMLFiles.XMLObjects;

public class ExcelXMLPZPosition extends XMLPZPosition {

    public ExcelXMLPZPosition(String symbol, int ilosc, double cena, String EAN, String nazwa,String jEW, int stawkaVat, boolean isAlreadyInOptima, boolean isjEWCorrect) {
        this.symbol = symbol;
        this.ilosc = ilosc;
        this.cena = cena;
        this.stawkaVat = stawkaVat;
        this.jEW = jEW;
        this.EAN = EAN;
        this.nazwa = nazwa;
        this.isAlreadyInOptima = isAlreadyInOptima;
        this.isjEWCorrect = isjEWCorrect;
    }
    public ExcelXMLPZPosition(){

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
        return cena;
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
