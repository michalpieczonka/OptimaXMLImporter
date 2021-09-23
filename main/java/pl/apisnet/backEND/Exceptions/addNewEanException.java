package pl.apisnet.backEND.Exceptions;

public class addNewEanException extends Exception{
    public addNewEanException(){
        super("Nie udalo sie dodac nowego towaru !");
    }
}
