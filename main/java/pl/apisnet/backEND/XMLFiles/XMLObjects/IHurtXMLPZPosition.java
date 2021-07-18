package pl.apisnet.backEND.XMLFiles.XMLObjects;

public class IHurtXMLPZPosition {
    private String symbol;
    private int ilosc;
    private double cena;
    private String EAN;

    public IHurtXMLPZPosition(String symbol, int ilosc, double cena, String EAN) {
        this.symbol = symbol;
        this.ilosc = ilosc;
        this.cena = cena;
        this.EAN = EAN;
    }

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
}
