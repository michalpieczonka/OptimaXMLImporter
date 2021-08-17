package pl.apisnet.userUI;

import pl.apisnet.backEND.DatabaseConf;

public final class DatabaseHolder {
    private DatabaseConf dbConf;
    private final static DatabaseHolder INSTANCE = new DatabaseHolder();

    private DatabaseHolder(){

    }

    public static DatabaseHolder getInstance(){
        return INSTANCE;
    }

    public DatabaseConf getDbConf(){
      return dbConf;
    }

    public void setDbConf(DatabaseConf dbConf){
        this.dbConf = dbConf;
    }
}
