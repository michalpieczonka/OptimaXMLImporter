package pl.apisnet.userUI;

import pl.apisnet.backEND.Optima;

public final class OptimaHolder {
    private Optima mainOptima;
    private final static OptimaHolder INSTANCE = new OptimaHolder();

    private OptimaHolder(){}

    public static OptimaHolder getInstance() {
        return INSTANCE;
    }

    public Optima getMainOptima() {
        return mainOptima;
    }

    public void setMainOptima(Optima mainOptima) {
        this.mainOptima = mainOptima;
    }


}
