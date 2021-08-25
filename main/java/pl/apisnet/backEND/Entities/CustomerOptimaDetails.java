package pl.apisnet.backEND.Entities;

import javax.persistence.*;

@Entity
@Table(name = "CustomerOptimaDetails")
public class CustomerOptimaDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_CustomerOptimaDetails")
    private int IdCustomerOptimaDetails;

    @Column(name = "Operator")
    private String Operator;

    @Column(name = "OperatorPassword")
    private String OperatorPassword;

    @Column(name = "CompanyName")
    private String CompanyName;

    @Column(name = "OptimaPath")
    private String OptimaPath;


    public CustomerOptimaDetails(){
    }

    public CustomerOptimaDetails(String operator, String operatorPassword, String companyName, String optimaPath) {
        this.Operator = operator;
        this.OperatorPassword = operatorPassword;
        this.CompanyName = companyName;
        this.OptimaPath = optimaPath;
    }

    public String getOperator() {
        return Operator;
    }

    public void setOperator(String operator) {
        Operator = operator;
    }

    public String getOperatorPassword() {
        return OperatorPassword;
    }

    public void setOperatorPassword(String operatorPassword) {
        OperatorPassword = operatorPassword;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getOptimaPath() {
        return OptimaPath;
    }

    public void setOptimaPath(String optimaPath) {
        OptimaPath = optimaPath;
    }
}
