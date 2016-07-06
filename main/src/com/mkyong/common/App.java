package com.mkyong.common;

import org.hibernate.Session;
import com.mkyong.persistence.HibernateUtil;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println("Maven + Hibernate + MySQL");
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        session.beginTransaction();
        Stock stock = new Stock();
        
        stock.setStockCode("4716");
        stock.setStockName("GENN");
        
        session.save(stock);
       
        
        
        //session.delete(stock);
        
        session.getTransaction().commit();
        
        
    }
}
