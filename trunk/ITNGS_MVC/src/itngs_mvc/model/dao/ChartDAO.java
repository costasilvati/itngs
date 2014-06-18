/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package itngs_mvc.model.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;

/**
 *
 * @author juliana
 */
public class ChartDAO {

    
    /**
     * Read one line of typeFiles not implemented in BioJava
     * @param br reader to statistics file
     * @return line fqr
     */
    public ArrayList<Double> ReadStatistics(BufferedReader br) {
        ArrayList<Double> array = new ArrayList<>();
        try {
            while (br.ready()) {
                array.add(Double.parseDouble(br.readLine()));
            }
        } catch (IOException erro) {
            JOptionPane.showConfirmDialog(null, "Data chart file canot open!", "Error", JOptionPane.INFORMATION_MESSAGE);
        }
       return array;
    }
    
}
