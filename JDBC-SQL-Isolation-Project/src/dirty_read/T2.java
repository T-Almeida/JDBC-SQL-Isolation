/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dirty_read;

import connection.DBConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Dirty_Read_Demo;

/**
 *
 * @author ASUS
 */
public class T2 extends Thread{
    

    private String name;
    private int isolationLevel;
    
    
    // semaforo para sincronizar T2
    private Semaphore semT2;
    
    public T2(int isolationLevel,Semaphore semT2)
    {
        this.isolationLevel=isolationLevel;
        this.name="Transaction 2";
        this.semT2 = semT2;
    }
    
    @Override
    public void run()
    {
        try {

            semT2.acquire();

            // criar a conexão
            Connection conn = DBConnection.getSQLConnection();

            conn.setTransactionIsolation(isolationLevel);

            conn.setAutoCommit(false);

            String sql = "select Number from consistencia where Id=1";
            ResultSet result = conn.createStatement().executeQuery(sql);
            int number=-1;
            if (result.next())
                number = result.getInt(1);
            
            System.out.println("Thread " + name + ": leu " + (number==-1?"erro":number));
            
            
            //System.out.flush();
            
            //sem.acquire();
            conn.commit();
            
            System.out.println("Thread " + name + ": commit ");
            conn.close();
        } catch (Exception ex) {

            Logger.getLogger(T1.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
