/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template typeFile, choose Tools | Templates
 * and open the template in the editor.
 */
package itngs_mvc.model.dao;

import itngs_mvc.model.vo.SequenceVO;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juliana
 */
public class SequenceDAO {

    /**
     * Read one fastq sequence (4 lines) of the fastq typeFile
     * @param br fqrer of the typeFile
     * @param seq object recives sequences values
     * @return object seq with description, sequence and quality
     */
    public static SequenceVO ReadFastq(BufferedReader br, SequenceVO seq) {
        int numberLine = 0;
        boolean ok = false;
        StringBuilder bf = new StringBuilder();
        String[] read = {"", "", "", ""};
        
        try {
            while (br.ready()) {
                if (numberLine < 4) {
                    read[numberLine] = br.readLine();
                    numberLine++;
                } else {
                    if (read[0].startsWith("@")) {
                        if (read[1].matches("^[ACTGNURYKMSWBDHVX]+$")) {
                            if (read[2].startsWith("+")) {
                                if (read[3].length() == read[1].length()) {
                                    ok = true;
                                }
                            }
                        }
                    }

                    if (ok) {
                        seq.setName(read[0]);
                        bf.append(read[1]);
                        seq.setSeq(bf);
                        bf = new StringBuilder();
                        bf.append(read[3]);
                        seq.setQual(bf);
                        break;
                    } else {
                        seq = null;
                    }
                }
            }
        } catch (IOException erro) {
            seq = null;
            return seq;
        }
        return seq;

    }

    /**
     * Write in a specific typeFile sequences in FASTQ format
     * @param seq sequence to write
     * @param bw buffer of write
     */
    public static void WriteFastq(SequenceVO seq, BufferedWriter bw) {
//        String comp = "";
//        if (!seq.getType().getName().equals("454")) {
//            comp = "@";
//        }
        try {
            int limits[] = seq.getLimits();
            bw.write(seq.getName()
                    + "\n" + seq.getSeq().substring(limits[0], limits[1])
                    + "\n+\n"
                    + seq.getQual().substring(limits[0], limits[1]) + "\n");
            bw.flush();
        } catch (IOException ex) {
            Logger.getLogger(SequenceDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Write in a specific typeFile sequences in FASTA format
     * @param seq sequence to write
     * @param bw buffer of write
     */
    public static void WriteFasta(SequenceVO seq, BufferedWriter bw) {
        try {
            int limits[] = seq.getLimits();
            bw.write(">" + seq.getName()+"\n"
                     + seq.getSeq().substring(limits[0], limits[1]) + "\n");
            bw.flush();
        } catch (IOException ex) {
            Logger.getLogger(SequenceDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static File CreateFastqOut(File file, String append) {
        String path = file.getAbsolutePath();
        path = path.substring(0, path.lastIndexOf("."));
        path = path + "_" + append + ".fastq";
        File newFile = new File(path);
        return newFile;
    }

    public static File CreateFastaOut(File file, String append) {
        String path = file.getAbsolutePath();
        path = path.substring(0, path.lastIndexOf("."));
        path = path + "_" + append + ".fasta";
        File newFile = new File(path);
        return newFile;
    }
}
