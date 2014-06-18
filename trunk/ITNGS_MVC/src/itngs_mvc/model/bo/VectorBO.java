/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package itngs_mvc.model.bo;

import itngs_mvc.model.vo.MegablastVO;
import itngs_mvc.model.vo.VectorVO;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juliana
 */
public class VectorBO {

    private VectorVO vo;
    private final String alignmentFileOut;

    public VectorBO(VectorVO vo, String align) {
        this.vo = vo;
        this.alignmentFileOut = align;
    }

    public String getAlignmentFileOut() {
        return alignmentFileOut;
    }

    /**
     * Execute a alignment using megablast NCBI against database created
     *
     * @param mgb object with values command Megablast
     * @return
     */
    public boolean ExecuteAlignmentVector(MegablastVO mgb) {
        boolean ret = false;
        try {
            String command = mgb.toString();

            int val = Runtime.getRuntime().exec(command).waitFor();
            if (val == 0) {
                ret = true;
            }

        } catch (IOException | InterruptedException ex) {
            return false;
        }

        return ret;
    }

    /**
     * Create a BLAST database, with vector sequence file to use in alignment
     *
     * @return true to ok execution/ false to error
     */
    public boolean databaseFormat() {
        boolean ret = false;
        try {
            if (vo.isFastaFormat()) {
                String command = "formatdb "
                        + "-i " + vo.getFilePath()
                        + " -n " + vo.getDatabaseName()
                        + " -p F";

                int val = Runtime.getRuntime().exec(command).waitFor();
                if (val == 0) {
                    ret = true;
                }
            }
        } catch (IOException ex) {
            return false;
        } catch (InterruptedException ex) {
            Logger.getLogger(VectorBO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return ret;
    }

}
