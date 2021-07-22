package pl.apisnet.backEND.XMLFiles.XMLObjects;

public class IHurtXMLPZPosition extends XMLPZPosition {
  // private String symbol;
  // private int ilosc;
  // private double cena;
  // private String EAN;

    public IHurtXMLPZPosition(String symbol, int ilosc, double cena, String EAN, String nazwa,String jEW, int stawkaVat, boolean isAlreadyInOptima) {
        this.symbol = symbol;
        this.ilosc = ilosc;
        this.cena = cena;
        this.stawkaVat = stawkaVat;
        this.jEW = jEW;
        this.EAN = EAN;
        this.nazwa = nazwa;
        this.isAlreadyInOptima = isAlreadyInOptima;
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
}
