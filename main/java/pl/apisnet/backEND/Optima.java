package pl.apisnet.backEND;

/**
 * Class responsible for connecting with Comarch Optima dll
 */
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.SafeArray;
import com.jacob.com.Variant;

public class Optima {
    private final String optimaDLL = "OptimaLIBB.OptimaUtils"; //Class DLL library created in C# using Optima API
    private final String optimaLoginClassInDLL = "loginToOptima"; //Name of login function in DLL library created in C# using Optima API
    private final String optimaLogoutClassInDLL = "logoutFromOptima"; //Name of logout and unlock Optima function in DLL library created in C# using Optima API
    private final String optimaVersionCheckerClassInDLL = "checkOptimaVersion"; //Name of checking version funcion in DLL library to check what is Optima Version
    private final String optimaCompanyNamesClassInDll = "getCompanys"; //Name of function in DLL to download all existing companys in Optima


    private boolean optimaConnection; //Flag responsible for checking if connection with Optima was sucessfully created
    private String optimaUserName;
    private String optimaUserPassword;
    private String optimaCompanyName;
    private String optimaInstallationPath;
    private ActiveXComponent optimaProgramID;

    public Optima(){

    }

    public Optima(String optimaUserName, String optimaUserPassword, String optimaCompanyName, String optimaInstallationPath) {
        this.optimaConnection = false;
        this.optimaUserName = optimaUserName;
        this.optimaUserPassword = optimaUserPassword;
        this.optimaCompanyName = optimaCompanyName;
        this.optimaInstallationPath = optimaInstallationPath;
        this.optimaProgramID = null;
    }

    /**
     * Creating connecting for OptimaAPI trought dll -> OptimaLIBB.dll
     */
    public boolean connectToOptima(){
        try{
            optimaProgramID = new ActiveXComponent(optimaDLL);
            System.out.println("The Library been loaded, and an activeX component been created");

            //Connection with optima -> 1st parameter is ActiveXComponent variable, loaded from optimadll,
            // 2nd parameter is function in dll name -> responsible for connecting to Optima trought OptimaAPI
            Variant isConnected = Dispatch.call(optimaProgramID, optimaLoginClassInDLL, optimaUserName,optimaUserPassword,optimaCompanyName,optimaInstallationPath);
            optimaConnection = isConnected.getBoolean();

        }catch(Exception e){
            e.printStackTrace();
        }
        return optimaConnection;
    }

    /**
     * Disconnecting from OptimaAPI trought dll -> OptimaLIBB.dll
     */
    public void disconnectFromOptima(){
        Dispatch.call(optimaProgramID,optimaLogoutClassInDLL);
        optimaProgramID = null;
        optimaConnection = false;
    }

    /**
     * Returns if connection to Optima exists
     */
    public boolean checkOptimaConnection(){
        return this.optimaConnection;
    }

    /**
     * Returns reference for Optima ActiveXComponent mapped by Jacob Liblary from -> OptimaLIBB.dll
     */
    public ActiveXComponent getOptimaProgramID() {
        return optimaProgramID;
    }
    public String getOptimaCompanyName() {
        return optimaCompanyName;
    }

    public String getOptimaVersion(){
        Variant optimaVersion = Dispatch.call(optimaProgramID, optimaVersionCheckerClassInDLL, optimaInstallationPath);
        return optimaVersion.getString();
    }

    public String[] getCompanysFromOptima(){
        optimaProgramID = new ActiveXComponent(optimaDLL);
        SafeArray companys = Dispatch.call(optimaProgramID,optimaCompanyNamesClassInDll).toSafeArray();
        return companys.toStringArray();
    }
}
