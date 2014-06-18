package itngs_mvc.model.bo;

import itngs_mvc.model.vo.SequenceVO;
import javax.swing.JOptionPane;

/**
 *
 * @author juliana
 */
public class SequenceBO {

    /**
     *Validates the read sequence
     * @param seq object with sequence informations
     * @return true if ok/ false if not ok
     */
    public static boolean isSequence(SequenceVO seq) {
        boolean resp = false;
        if (seq.getName().startsWith("@")) {
            if (seq.getSeq().toString().matches("^[ABCDGHKMNRSTUVXYW]+$")) {
                resp = true;
            }
        }
        return resp;
    }

    /**
     * Received values of trim sequence and validates
     * @param trimBegin initail position of cutting
     * @param trimEnd ending position of cutting
     * @param length the sequence number of chars has
     * @return 
     */
    public static int[] MakeLimits(int trimBegin, int trimEnd, int length) {
        //Declare an array with two values, begin and end of cutoff
        int limits[] = {trimBegin, (length-1) - trimEnd};

        //Validation of values, if values of cutoff were higher of length sequence 
        //a error message is displayed, and exeution is canceled;
        if (trimBegin + trimEnd >= length) {
            JOptionPane.showMessageDialog(null, "Invalid cut values!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        return limits;
    }

}
