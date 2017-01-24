/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import non_repeatable_read.T1;
import non_repeatable_read.T2;
import java.sql.Connection;
import java.util.concurrent.Semaphore;

/**
 *
 * @author ASUS
 */
public class Non_Repatable_Read_Demo {
    
    public static void main(String[] args) throws Exception{
        
        
        // semaforo partilhado pelas threads para sincronização inicial
        Semaphore semT1 = new Semaphore(0);
        // semaforo partilhado pelas threads para sincronização inicial
        Semaphore semT2 = new Semaphore(0);
        
        
        //transação T2
        T2 t2 = new T2(Connection.TRANSACTION_READ_UNCOMMITTED,semT2);
        
        //transação T1
        T1 t1 = new T1(Connection.TRANSACTION_SERIALIZABLE, semT2);
        
        //inicio das transações em condiçao de corrida
        t1.start();
        t2.start();
   
    }
}
