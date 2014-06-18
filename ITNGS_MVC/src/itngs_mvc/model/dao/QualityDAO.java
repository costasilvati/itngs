package itngs_mvc.model.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juliana
 */
public class QualityDAO {

    public void WriteStatistic(double score, BufferedWriter bw) {
        try {
            bw.write(""+score+"\n");
            bw.flush();
        } catch (IOException ex) {
            Logger.getLogger(SequenceDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static File CreateStatiscOut(File file, String append) {
        String path = file.getAbsolutePath();
        path = path.substring(0, path.lastIndexOf("."));
        path = path + "_" + append + ".txt";
        File newFile = new File(path);
        return newFile;
    }
    
}
