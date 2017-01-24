/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dirty_read;

import connection.DBConnection;
import main.Dirty_Read_Demo;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Thread cria um conexão e escreve na base de dados
 * @author Tiago
 */
public class T1 extends Thread{
    
    private String name;
    private int isolationLevel;
    
    // semaforo sincronizar  T1
    private Semaphore semT1;
    
    // semaforo para sincronizar com T2
    private Semaphore semT2;
    
    
    
    public T1(int isolationLevel,Semaphore sem)
    {
        this.isolationLevel=isolationLevel;
        this.name="Transaction 1";
        this.semT2 = sem;
    }
    
    @Override
    public void run()
    {
        
        try {
            
            
            // criar a conexão
            Connection conn = DBConnection.getSQLConnection();

            conn.setTransactionIsolation(isolationLevel);

            conn.setAutoCommit(false);

            String sql = "update consistencia set Number=? where Id=1";
            PreparedStatement stat = conn.prepareStatement(sql);
            int newNumber = new Random().nextInt(100);
            stat.setInt(1, newNumber);
            int error = stat.executeUpdate();

            System.out.println("Thread " + name + ": escreveu " + newNumber);
            
            //up semaforo
            semT2.release();
            
            System.out.println("Thread " + name + ": bloqueada a espera de input [Continue/Abort] para continuar");
            Scanner sc = new Scanner(System.in);
            String line = sc.nextLine();
            if (line.toLowerCase().startsWith("a"))
            {
                conn.rollback();
                System.out.println("Thread " + name + " Abort");
            }
            else
            {
                conn.commit();
                System.out.println("Thread " + name + " Commit");
            }
            
            conn.close();
        } catch (Exception ex) {
            
            Logger.getLogger(T1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
