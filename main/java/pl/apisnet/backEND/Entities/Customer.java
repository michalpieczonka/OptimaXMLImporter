package pl.apisnet.backEND.Entities;


import javax.persistence.*;

@Entity
@Table(name = "Customer")
public class Customer {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Customer")
    private int Id;

    @Column(name = "CustomerLogin")
    private String CustomerLogin;

    @Column(name = "CustomerPassword")
    private String CustomerPassword;

    @Column(name = "CustomerNIP")
    private int CustomerNIP;

    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "Id_CustomerOptimaDetails")
    private CustomerOptimaDetails customerOptimaDetails;

    public Customer(){

    }

    public Customer(String customerLogin, String customerPassword, int customerNIP) {
        this.CustomerLogin = customerLogin;
        this.CustomerPassword = customerPassword;
        this.CustomerNIP = customerNIP;
    }

    //Getters and setters

    public String getCustomerLogin() {
        return CustomerLogin;
    }

    public void setCustomerLogin(String customerLogin) {
        CustomerLogin = customerLogin;
    }

    public String getCustomerPassword() {
        return CustomerPassword;
    }

    public void setCustomerPassword(String customerPassword) {
        CustomerPassword = customerPassword;
    }

    public int getCustomerNIP() {
        return CustomerNIP;
    }

    public void setCustomerNIP(int customerNIP) {
        CustomerNIP = customerNIP;
    }

    public CustomerOptimaDetails getCustomerOptimaDetails() {
        return customerOptimaDetails;
    }

    public void setCustomerOptimaDetails(CustomerOptimaDetails customerOptimaDetails) {
        this.customerOptimaDetails = customerOptimaDetails;
    }
}
