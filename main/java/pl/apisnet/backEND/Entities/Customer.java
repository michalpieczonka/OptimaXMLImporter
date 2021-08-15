package pl.apisnet.backEND.Entities;


import javax.persistence.*;

@Entity
@Table(name = "User")
public class Customer {
    @Id
    @Column(name = "Id")
    private int Id;

    @Column(name = "Login")
    private String Login;

    @Column(name = "Password")
    private String Password;

    @Column(name = "NIP")
    private int NIP;


    //Getters and setters
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public int getNIP() {
        return NIP;
    }

    public void setNIP(int NIP) {
        this.NIP = NIP;
    }
}
