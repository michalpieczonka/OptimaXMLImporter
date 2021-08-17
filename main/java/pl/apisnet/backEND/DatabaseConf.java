package pl.apisnet.backEND;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
            System.out.println(customer.getCustomerOptimaDetails().getCompanyName());
            userExists = true;
        }catch (Exception e){
            userExists = false;
        }finally{
            session.close();
            factory.close();
        }
        return userExists;
    }

    public Customer getCustomer() {
        return customer;
    }

    // private void hibernateLogin(){
 //

 //     session.beginTransaction();
 //     Query qu = session.createQuery(qString);
 //     qu.setParameter("password","Limanowwa2021");
 //     qu.setParameter("login","Admin");
 //     //List<Customer> asd = qu.getResultList();
 //     //System.out.println(asd.size());
 //     //String tmp = asd.get(0).getCustomerOptimaDetails().getCompanyName();
 //     //System.out.println(tmp);
 //     try{
 //         List<Customer> asd = qu.getResultList();
 //         System.out.println(asd.size());
 //         String tmp = asd.get(0).getCustomerOptimaDetails().getCompanyName();
 //         System.out.println(tmp);
 //     }catch (Exception e){
 //         System.out.println("mamy nulla");
 //     } finally{
 //         session.close();
 //         factory.close();
 //     }
 // }
}
