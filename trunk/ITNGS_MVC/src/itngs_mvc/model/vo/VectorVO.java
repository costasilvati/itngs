/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package itngs_mvc.model.vo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author juliana
 */
public class VectorVO {
    private String filePath;
    private String databaseName;
    private static double k;
    private static double lambda;

    public VectorVO(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public static double getK() {
        return k;
    }

    public static void setK(double k) {
        VectorVO.k = k;
    }

    public static double getLambda() {
        return lambda;
    }

    public static void setLambda(double lambda) {
        VectorVO.lambda = lambda;
    }
    
    
/**
 * Check a characters and format of the file
 * @return true to ok/ false to error format
 */    
    public boolean isFastaFormat(){

        boolean fasta = true;
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
            int line = 1;
            while(br.ready()){
                String leu = br.readLine();
                if(leu.startsWith(">")){

                }else if(leu.matches("^[ACTGNURYKMSWBDHVX]+$")){
                    
                }else{
                    fasta = false;
                    System.out.println("Erro linha "+line);
                    return fasta;
                }
                line++;
            }
            
            
        } catch (FileNotFoundException ex) {
            return false;
        } catch (IOException ex) {
            return false;
        }
        
        return fasta;
    }
}
