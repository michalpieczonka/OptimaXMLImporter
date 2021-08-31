package pl.apisnet.backEND;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import pl.apisnet.backEND.Entities.Customer;
import pl.apisnet.backEND.Entities.CustomerOptimaDetails;

import javax.persistence.Query;
/**
 * Class responsible for connecting with APIS OptimaImporter Database using Hibernate to authenticate customers
 */
public class DatabaseConf {
    private Configuration configuration; //Main configuration
    private SessionFactory factory; //Factory object for sessions
    private Customer customer;


    public DatabaseConf(){
        this.configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml"); //Config file
        configuration.addAnnotatedClass(Customer.class); //Customer class
        configuration.addAnnotatedClass(CustomerOptimaDetails.class); //Customer optima details class
    }

    /**
     * Class responsible for connecting with APIS OptimaImporter Database, to authenticate customer
     */
    public boolean userAuthentication(String customerName, String customerPassword){
        factory = configuration.buildSessionFactory();
        Session session = factory.getCurrentSession();
        String queryString = "select c from Customer c where (c.CustomerLogin = :login AND c.CustomerPassword = :password)"; //Main query for checking if user exists
        boolean userExists;

        session.beginTransaction();
        Query query = session.createQuery(queryString);
        query.setParameter("login",customerName);
        query.setParameter("password",customerPassword);

        try{
            this.customer = (Customer) query.getSingleResult();
            userExists = true;
        }catch (Exception e){
            userExists = false;
        }finally{
            session.close();
        }
        return userExists;
    }

    public Customer getCustomer() {
        return customer;
    }

    public boolean updateUserOptimaDetails(String Operator, String operatorPassword, String companyName, String optimaPath){
        boolean updateResult = false;
        Session session = null;
        try{
            session = factory.getCurrentSession();
            session.beginTransaction();
            customer.getCustomerOptimaDetails().setOperator(Operator);
            customer.getCustomerOptimaDetails().setOperatorPassword(operatorPassword);
            customer.getCustomerOptimaDetails().setCompanyName(companyName);
            customer.getCustomerOptimaDetails().setOptimaPath(optimaPath);
            session.saveOrUpdate(customer.getCustomerOptimaDetails());
            session.saveOrUpdate(customer);
            session.getTransaction().commit();
            updateResult = true;
        } catch (Exception e){
            System.out.println(e);
        } finally{
            session.close();
        }
        return updateResult;
    }

    public void logoutCustomer(){
        factory.close();
        factory = null;
        configuration = null;
        customer = null;
    }

}
