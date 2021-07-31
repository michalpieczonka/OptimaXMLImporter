package pl.apisnet.backEND.XMLFiles.XMLObjects;

public abstract class XMLPZPosition {
    protected String nazwa;
    protected String symbol;
    protected int ilosc;
    protected double cena;
    protected int stawkaVat;
    protected String jEW;
    protected String EAN;
    protected boolean isAlreadyInOptima;
    protected boolean isjEWCorrect;


    public boolean isAlreadyInOptima(){
        return isAlreadyInOptima;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getStawkaVat() {
        return stawkaVat;
    }

    public String getjEW() {
        return jEW;
    }

    public String getEAN() {
        return EAN;
    }

    public String getNazwa() {
        return nazwa;
    }
    public void setAlreadyInOptima(boolean alreadyInOptima) {
        isAlreadyInOptima = alreadyInOptima;
    }

    public boolean isIsjEWCorrect() {
        return isjEWCorrect;
    }

    public void setIsjEWCorrect(boolean isjEWCorrect) {
        this.isjEWCorrect = isjEWCorrect;
    }

    public void setjEW(String jEW) {
        this.jEW = jEW;
    }
}
