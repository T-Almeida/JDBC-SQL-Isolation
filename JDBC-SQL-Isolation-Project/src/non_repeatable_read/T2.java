/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package non_repeatable_read;

import connection.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ASUS
 */
public class T2 extends Thread{
    
    private String name;
    private int isolationLevel;
    
    // semaforo sincronizar  T1
    private Semaphore semT1;
    
    // semaforo para sincronizar com T2
    private Semaphore semT2;
    
    
    
    public T2(int isolationLevel,Semaphore sem)
    {
        this.isolationLevel=isolationLevel;
        this.name="Transaction 2";
        this.semT2 = sem;
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

            // W(x)
            String sql = "update consistencia set Number=? where Id=1";
            PreparedStatement stat = conn.prepareStatement(sql);
            int newNumber = new Random().nextInt(100);
            stat.setInt(1, newNumber);
            int error = stat.executeUpdate();

            System.out.println("Thread " + name + ": escreveu " + newNumber);
            
            System.out.println("Thread " + name + ": Commit");
            conn.commit();
               
            
            conn.close();
        } catch (Exception ex) {
            
            Logger.getLogger(dirty_read.T1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
