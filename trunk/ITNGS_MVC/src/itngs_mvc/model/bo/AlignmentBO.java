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
package itngs_mvc.model.bo;

import itngs_mvc.model.dao.AlignmentDAO;
import itngs_mvc.model.dao.SequenceDAO;
import itngs_mvc.model.vo.AlignmentVO;
import itngs_mvc.model.vo.MegablastVO;
import itngs_mvc.model.vo.SequenceVO;
import itngs_mvc.model.vo.VectorVO;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author juliana
 */
public class AlignmentBO {

    /**
     * Execute the alignment end removal of sequences with similarity vector
     *
     * @param fileVector path of file sequence of vector or univec database
     * @param mgb object with values megablast
     * @return -1 to catch exceptions/ 1 to success
     */
    //@SuppressWarnings("empty-statement")
    public ArrayList<Double> AlignmentAndRemove(String fileVector, MegablastVO mgb) {

        ArrayList<Double> data = new ArrayList();
        VectorVO vo = new VectorVO(fileVector);
        Double totalSeq = 0.0;

        //Database name is name of file (fasta) whitout extenssion
        vo.setDatabaseName(vo.getFilePath().
                substring(0, vo.getFilePath().lastIndexOf(".")));

        VectorBO vb1 = new VectorBO(vo, mgb.getOutput1());
        //mgb.setOutput1(vb1.getAlignmentFileOut());

        if (vb1.databaseFormat()) {
            mgb.setDatabase(vo.getDatabaseName());

            //Check method [single | paired]
            if (mgb.getInput2() != null) {

                VectorBO vb2 = new VectorBO(vo, mgb.getOutput2());
                //mgb.setOutput2(vb2.getAlignmentFileOut());

                if (vb1.ExecuteAlignmentVector(mgb) && vb2.ExecuteAlignmentVector(mgb)) {
                    try {
                        //Create writers to output files result
                        //FASTQ
                        BufferedWriter bwfq1 = new BufferedWriter(
                                new FileWriter(SequenceDAO.CreateFastqOut(new File(mgb.getInput1()), "clean")));
                        BufferedWriter bwfq2 = new BufferedWriter(
                                new FileWriter(SequenceDAO.CreateFastqOut(new File(mgb.getInput2()), "clean")));
                        //FASTA
                        BufferedWriter bwf1 = new BufferedWriter(
                                new FileWriter(SequenceDAO.CreateFastaOut(new File(mgb.getInput1()), "clean")));
                        BufferedWriter bwf2 = new BufferedWriter(
                                new FileWriter(SequenceDAO.CreateFastaOut(new File(mgb.getInput2()), "clean")));

                        //Create writes to single sequences
                        //FASTQ
                        BufferedWriter bwfqS = new BufferedWriter(
                                new FileWriter(SequenceDAO.CreateFastqOut(new File(mgb.getInput1()), "clean_SINGLE")));
                        //FASTA
                        BufferedWriter bwfS = new BufferedWriter(
                                new FileWriter(SequenceDAO.CreateFastaOut(new File(mgb.getInput1()), "clean_SINGLE")));

                        //Get scores and ID
                        ArrayList<AlignmentVO> alig1 = new ArrayList<>();
                        alig1 = getAlignments(vb1);

                        ArrayList<AlignmentVO> alig2 = new ArrayList<>();
                        alig2 = getAlignments(vb2);

                        //Sort scores values
                        ArrayList<Double> score1 = new ArrayList<>();
                        HashMap hm1 = new HashMap();
                        ArrayList<Double> score2 = new ArrayList<>();
                        HashMap hm2 = new HashMap();

                        for (int i = 0; i < alig1.size(); i++) {
                            score1.add(alig1.get(i).getScore());
                            hm1.put(alig1.get(i).getIdSequence(), alig1.get(i).getScore());
                        }

                        for (int i = 0; i < alig2.size(); i++) {
                            score2.add(alig2.get(i).getScore());
                            hm2.put(alig2.get(i).getIdSequence(), alig2.get(i).getScore());
                        }

                        Collections.sort(score1);
                        Collections.sort(score2);

                        //Define 5% of alignments
                        int liberty1 = (score1.size() / 100) * 5;
                        int liberty2 = (score2.size() / 100) * 5;
                        
                        double max1 = 0.0;
                        double max2 = 0.0;

                        //In position of represent 5% the score is a maximum  
                        if (!score1.isEmpty()){
                            max1 = score1.get(liberty1);
                        }
                        if (!score2.isEmpty()){
                            max2 = score2.get(liberty2);
                        }

                        //Create sequences
                        SequenceVO sqvo1 = new SequenceVO(true);
                        SequenceVO sqvo2 = new SequenceVO(true);

                        //Create readers to FATSQ and FASTA files
                        String name = mgb.getInput1().substring(0, mgb.getInput1().lastIndexOf('.')) + ".fastq";
                        BufferedReader brfq1 = new BufferedReader(new FileReader(new File(name)));

                        name = mgb.getInput2().substring(0, mgb.getInput2().lastIndexOf('.')) + ".fastq";
                        BufferedReader brfq2 = new BufferedReader(new FileReader(new File(name)));

                        //Read and separe sequences
                        while (brfq1.ready()) { //The two files have the same size
                            sqvo1 = SequenceDAO.ReadFastq(brfq1, sqvo1);
                            sqvo2 = SequenceDAO.ReadFastq(brfq2, sqvo2);

                            String id1 = sqvo1.getName();
                            String id2 = sqvo2.getName();

                            //Separe 
                            if (hm1.containsKey(id1) && hm2.containsKey(id2)) {

                                double sc1 = (double) hm1.get(id1);
                                double sc2 = (double) hm2.get(id2);

                                if (sc1 <= max1 && sc2 <= max2) {
                                    //Write FASTQ
                                    AlignmentDAO.WriteFastq(sqvo1, bwfq1);
                                    AlignmentDAO.WriteFastq(sqvo2, bwfq2);
                                    //Write FASTA
                                    AlignmentDAO.WriteFasta(sqvo1, bwf1);
                                    AlignmentDAO.WriteFasta(sqvo2, bwf2);

                                } else if (sc1 <= max1) {

                                    AlignmentDAO.WriteFastq(sqvo1, bwfqS);
                                    AlignmentDAO.WriteFasta(sqvo1, bwfS);

                                } else if (sc2 <= max2) {

                                    AlignmentDAO.WriteFastq(sqvo2, bwfqS);
                                    AlignmentDAO.WriteFasta(sqvo2, bwfS);
                                }

                            } else if (hm1.containsKey(id1)) {

                                double sc1 = (double) hm1.get(id1);

                                if (sc1 <= max1) {

                                    AlignmentDAO.WriteFastq(sqvo1, bwfqS);
                                    AlignmentDAO.WriteFasta(sqvo1, bwfS);

                                }

                            } else if (hm2.containsKey(id2)) {

                                double sc2 = (double) hm2.get(id2);

                                if (sc2 <= max2) {

                                    AlignmentDAO.WriteFastq(sqvo2, bwfqS);
                                    AlignmentDAO.WriteFasta(sqvo2, bwfS);

                                }

                            } else {

                                //Write FASTQ
                                AlignmentDAO.WriteFastq(sqvo1, bwfq1);
                                AlignmentDAO.WriteFastq(sqvo2, bwfq2);
                                //Write FASTA
                                AlignmentDAO.WriteFasta(sqvo1, bwf1);
                                AlignmentDAO.WriteFasta(sqvo2, bwf2);

                            }

                            totalSeq += 2;
                        }

                        bwf1.close();
                        bwf2.close();
                        bwfq1.close();
                        bwfq2.close();
                        bwfS.close();
                        bwfqS.close();

                        Double vector;
                        vector = (hm1.size() + hm2.size()) * 100 / totalSeq;
                        data.add(vector);
                        data.add(100 - vector);

                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(AlignmentBO.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null,
                                "Error! Files not found! ", "Error",
                                JOptionPane.INFORMATION_MESSAGE);
                        return null;
                    } catch (IOException ex) {
                        Logger.getLogger(AlignmentBO.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null,
                                "Error! Files not found! ", "Error",
                                JOptionPane.INFORMATION_MESSAGE);
                        return null;
                    }

                }

            } else { //Sequencing single end

                if (vb1.ExecuteAlignmentVector(mgb)) {

                    try {
                        //Create writers to output files result
                        //FASTQ
                        BufferedWriter bwfq1 = new BufferedWriter(
                                new FileWriter(SequenceDAO.CreateFastqOut(new File(mgb.getInput1()), "clean")));
                        //FASTA
                        BufferedWriter bwf1 = new BufferedWriter(
                                new FileWriter(SequenceDAO.CreateFastaOut(new File(mgb.getInput1()), "clean")));

                        //Get scores and ID
                        ArrayList<AlignmentVO> alig1 = new ArrayList<>();
                        alig1 = getAlignments(vb1);

                        //Sort scores values
                        ArrayList<Double> score1 = new ArrayList<>();
                        HashMap hm = new HashMap();

                        for (int i = 0; i < alig1.size(); i++) {
                            score1.add(alig1.get(i).getScore());
                            hm.put(alig1.get(i).getIdSequence(), alig1.get(i).getScore());
                        }

                        Collections.sort(score1);

                        //Define 5% of alignments
                        int liberty1 = (score1.size() / 100) * 5;

                        //In position of represent 5% the score is a maximum  
                        double max1 = 0.0;
                                
                        if (!score1.isEmpty()) {
                             max1 = score1.get(liberty1);
                        } 
                        //Create sequences
                        SequenceVO sqvo1 = new SequenceVO(true);

                        //Create readers to FATSQ and FASTA files
                        String name = mgb.getInput1().substring(0, mgb.getInput1().lastIndexOf('.')) + ".fastq";
                        BufferedReader brfq1 = new BufferedReader(new FileReader(new File(name)));

                        //Read and separe sequences
                        while (brfq1.ready()) { //The two files have the same size
                            sqvo1 = SequenceDAO.ReadFastq(brfq1, sqvo1);

                            String id1 = sqvo1.getName();

                            if (hm.containsKey(id1)) {

                                double sc = (double) hm.get(id1);

                                if (sc <= max1) {
                                    AlignmentDAO.WriteFastq(sqvo1, bwfq1);
                                    AlignmentDAO.WriteFasta(sqvo1, bwf1);
                                }

                            } else {
                                AlignmentDAO.WriteFastq(sqvo1, bwfq1);
                                AlignmentDAO.WriteFasta(sqvo1, bwf1);
                            }

                            totalSeq++;
                        }

                        bwf1.close();
                        bwfq1.close();

                        Double vector = (hm.size() * 100) / totalSeq;
                        data.add(vector);
                        data.add(100 - vector);

                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(AlignmentBO.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null,
                                "Error! Files not found! ", "Error",
                                JOptionPane.INFORMATION_MESSAGE);
                        return null;
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null,
                                "Error in vector remove!! ", "Error",
                                JOptionPane.INFORMATION_MESSAGE);
                        return null;
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(null,
                    "Error! File " + vo.getFilePath() + " not found or don't is format FASTA", "Database error",
                    JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        return data;
    }

    /**
     * Read outpu Megablast tabular file and extract informations about
     * alignments
     *
     * @param vb object with file alignment
     * @return hashMap with names and positions of alignment
     */
    public ArrayList<AlignmentVO> getAlignments(VectorBO vb) {

        //Map whit name and alignment of sequences 1 and 2
        ArrayList<AlignmentVO> alig = new ArrayList<>();
        //Readers of alignment files
        BufferedReader brAlig;

        try {
            brAlig = new BufferedReader(new FileReader(new File(vb.getAlignmentFileOut())));

            //Construct Maps
            alig = AlignmentDAO.ReadAlignmentDefault(brAlig);

            brAlig.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error! File " + vb.getAlignmentFileOut() + " don't is correct format", "Database error",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error! Don't is possible found alignment file", "Error",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        alig = NormalizeValues(alig);

        return alig;
    }

    /**
     * Normalizing MegaBLAST scores
     *
     * @param alig array with id sequences and raw scores
     * @return Array with normalized scores
     */
    public static ArrayList<AlignmentVO> NormalizeValues(ArrayList<AlignmentVO> alig) {

        for (int i = 0; i < alig.size(); i++) {

            AlignmentVO alvo = alig.get(i);

            double score = alvo.getScore();

            alig.get(i).setScore(((VectorVO.getLambda() * score) - (Math.log(VectorVO.getK()))) / Math.log(2));
        }

        return alig;

    }

    /**
     * With path of original files is possible obtain a output of quality clean
     *
     * @param file path file
     * @return path file with: old_name (without old extension) +
     * "_approved.fasta"
     */
    public static String TransformFastaName(String file) {
        file = file.substring(0, file.lastIndexOf("."));//path.length()-6);
        file = file + "_approved.fasta";
        return file;
    }

}
