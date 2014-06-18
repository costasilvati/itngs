/*
 * Copyright (C) 2014 juliana
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package itngs_mvc.model.dao;

import java.util.HashMap;
import itngs_mvc.model.vo.AlignmentVO;
import itngs_mvc.model.vo.SequenceVO;
import itngs_mvc.model.vo.VectorVO;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author juliana
 */
public class AlignmentDAO {

    /**
     * Read a line of tabular alignment file (output of Megablast) and create a
     * object
     *
     * @param br reader of output alignment file
     * @return Object with data of alignment
     */
    public static HashMap ReadAlignment(BufferedReader br) {

        AlignmentVO alg;
        HashMap hmp = new HashMap();

        try {

            while (br.ready()) {

                String[] data = br.readLine().split("\t");
                alg = new AlignmentVO(data[0]);

                alg.setScore(Double.parseDouble(data[11].trim()));

                if (hmp.containsKey(data[0])) {

                    AlignmentVO tmp = (AlignmentVO) hmp.get(data[0]);

                    if (tmp.getScore() < alg.getScore()) {
                        hmp.put(data[0], alg);
                    }
                } else {
                    hmp.put(data[0], alg);
                }
            }
            br.close();
        } catch (IOException ex) {
            hmp = new HashMap();
            return hmp;
            //HasLogger.getLogger(AlignmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hmp;
    }

    public static ArrayList<AlignmentVO> ReadAlignmentDefault(BufferedReader br) {
    
        AlignmentVO alg = null;
        ArrayList<AlignmentVO> hmp = new ArrayList<>();

        try {
            boolean score = false;
            boolean query = false;

            while (br.ready()) {
                String readed = br.readLine().trim();

                if (readed.startsWith("Query=")) {

                    readed = readed.trim();
                    alg = new AlignmentVO(readed.substring(6, readed.length()));
                    query = true;

                } else if (query && readed.startsWith("Score = ")) {

                    if (alg != null) {

                        String data[] = readed.split("=");
                        data = data[1].split("bits");
                        alg.setScore(Double.parseDouble(data[0].trim()));
                        score = false;
                        query = false;
                        hmp.add(alg);
                    }
                }else if(readed.startsWith("Lambda")){
                    readed = br.readLine().trim();
                    String data[] = readed.split("\\s+");
                    VectorVO.setLambda(Double.parseDouble(data[0]));
                    VectorVO.setK(Double.parseDouble(data[1]));
                    break;
                }
            }

        } catch (IOException ex) {
            hmp = new ArrayList<>();
            return hmp;
            //Logger.getLogger(AlignmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return hmp;
    }

    /**
     * Write in a specific typeFile sequences in FASTQ format
     *
     * @param seq sequence to write
     * @param bw buffer of write
     */
    public static void WriteFastq(SequenceVO seq, BufferedWriter bw) {
        try {
            //int limits[] = seq.getLimits();
            bw.write(seq.getName()
                    + "\n" + seq.getSeq()
                    //+ "\n" + seq.getSeq().substring(limits[0], limits[1])
                    + "\n+\n"
                    + seq.getQual() + "\n");
            //+ seq.getQual().substring(limits[0], limits[1]) + "\n");
            bw.flush();
            //bw.close();
        } catch (IOException ex) {
            Logger.getLogger(SequenceDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Write in a specific typeFile sequences in FASTA format
     *
     * @param seq sequence to write
     * @param bw buffer of write
     */
    public static void WriteFasta(SequenceVO seq, BufferedWriter bw) {
        try {
            //int limits[] = seq.getLimits();
            bw.write(">" + seq.getName().replace("@", "")
                    + "\n" + seq.getSeq() + "\n");
            //+ "\n" + seq.getSeq().substring(limits[0], limits[1]) + "\n");
            bw.flush();
            //bw.close();
        } catch (IOException ex) {
            Logger.getLogger(SequenceDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        File f = new File("/home/juliana/itngs/trunk/ITNGS_MVC/teste_m0.txt");
        BufferedReader br;
        try {
            br = new BufferedReader(
                    new FileReader(f));
            ReadAlignmentDefault(br);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AlignmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
