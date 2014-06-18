package itngs_mvc.model.bo;

import static itngs_mvc.model.bo.SequenceBO.MakeLimits;
import itngs_mvc.model.dao.QualityDAO;
import itngs_mvc.model.dao.SequenceDAO;
import itngs_mvc.model.vo.SequenceVO;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author juliana
 */
public class QualityBO {

    private final double minComplex;
    private final double minQual;
    private boolean make = true;

    public QualityBO(double minComplex, double minQual) {
        this.minComplex = minComplex;
        this.minQual = minQual;
    }

    public boolean getMake() {
        return make;
    }

    public void setMake(boolean make) {
        this.make = make;
    }

    /**
     * Clean sequences SINGLE-END Two threads are start sequencing not is 454 To
     * 454 sequences the procces is make here
     *
     * @param sq sequence informations
     * @param file1 path to file
     * @return opção escolhida pelo usuário, se erro -1
     */
    public int QualityClean(SequenceVO sq, String file1) {

        try {
            //Sequences FASTQ
            BufferedWriter bwa = new BufferedWriter(
                    new FileWriter(SequenceDAO.CreateFastqOut(new File(file1), "approved"))
            );

            BufferedWriter bwaf = new BufferedWriter(
                    new FileWriter(SequenceDAO.CreateFastaOut(new File(file1), "approved"))
            );

            //Create files to statistics
            BufferedWriter bwStatistcQual = new BufferedWriter(
                    new FileWriter(QualityDAO.CreateStatiscOut(new File(file1), "dataQual"))
            );

            BufferedReader br = new BufferedReader(new FileReader(new File(file1)));
            boolean ok = true;

            int lim[] = sq.getLimits();

            while (br.ready()) {
                sq = SequenceDAO.ReadFastq(br, sq);

                if (sq != null) {

                    sq.setLimits(MakeLimits(lim[0], lim[1], sq.getSeq().length()));

                    sq = PerformClean(sq);//, fq);
                    
                    if (sq == null){
                        return -1;
                    }

                    QualityDAO qd = new QualityDAO();
                    qd.WriteStatistic(sq.getMeanQual(), bwStatistcQual);

                    if (!sq.getDiscard()) {
                        SequenceDAO.WriteFastq(sq, bwa);
                        SequenceDAO.WriteFasta(sq, bwaf);
                    }
                } else {
                    JOptionPane.showConfirmDialog(null,
                            "Arquivo FASTQ tem problemas em sua formatação",
                            //"FASTQ file have problems in your format",
                            "Erro no arquivo FASTQ",
                            //"Error in FASTQ file",
                            JOptionPane.INFORMATION_MESSAGE);
                    return -1;
                }
            }

            bwa.close();
            bwaf.close();
            bwStatistcQual.close();
            make = false;
            //Custom button text
            Object[] options = {"Sim", //"Yes",
                "Não",//"No",
                "Cancelar"};
            int n = JOptionPane.showOptionDialog(null,
                    "A limpeza de qualidade terminou"//"The quality clean is successfull!"
                    + "Gostaria de remover as sequências contaminantes? Clique em \"Sim\".",//"Would like to perform the removal contaminating sequences?",
                    "Successo",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            return n;

        } catch (IOException ex) {
            return -1;
        }
    }

    /**
     * Clean sequences PAIRED-END Two threads are start sequencing not is 454 To
     * 454 sequences the procces is make here
     *
     * @param sq sequence informations
     * @param file1 path to file 1
     * @param file2 path to file 1
     * @return opção escolhida pelo usuário, se erro -1
     */
    public int QualityClean(SequenceVO sq, String file1, String file2) {
        int n = 2;
        try {
            //Approved Sequences FASTQ and FASTA 1
            BufferedWriter bwa1 = new BufferedWriter(
                    new FileWriter(SequenceDAO.CreateFastqOut(new File(file1), "approved"))
            );
            BufferedWriter bwaf1 = new BufferedWriter(
                    new FileWriter(SequenceDAO.CreateFastaOut(new File(file1), "approved"))
            );
            //Approved Sequences FASTQ and FASTA 2
            BufferedWriter bwa2 = new BufferedWriter(
                    new FileWriter(SequenceDAO.CreateFastqOut(new File(file2), "approved"))
            );
            BufferedWriter bwaf2 = new BufferedWriter(
                    new FileWriter(SequenceDAO.CreateFastaOut(new File(file2), "approved"))
            );

            //Singles approved Sequences FASTQ and FASTA
            BufferedWriter bws = new BufferedWriter(
                    new FileWriter(SequenceDAO.CreateFastqOut(new File(file1), "all_single"))
            );
            BufferedWriter bwsf = new BufferedWriter(
                    new FileWriter(SequenceDAO.CreateFastaOut(new File(file1), "all_single"))
            );

            //Create files to statistics
            BufferedWriter bwStaticQual = new BufferedWriter(
                    new FileWriter(QualityDAO.CreateStatiscOut(new File(file1), "dataQual"))
            );

            //Create Readers
            BufferedReader br1 = new BufferedReader(new FileReader(new File(file1)));
            BufferedReader br2 = new BufferedReader(new FileReader(new File(file2)));

            boolean ok = true;

            int lim[] = sq.getLimits();

            SequenceVO sq1 = new SequenceVO(sq.getType(), sq.getPaired());
            SequenceVO sq2 = new SequenceVO(sq.getType(), sq.getPaired());

            while (br1.ready() && br2.ready()) {
                sq1 = SequenceDAO.ReadFastq(br1, sq1);
                sq2 = SequenceDAO.ReadFastq(br2, sq2);

                if (sq != null) {
                    sq1.setLimits(sq.getLimits());
                    sq2.setLimits(sq.getLimits());

                    sq1.setLimits(MakeLimits(lim[0], lim[1], sq1.getSeq().length()));
                    sq2.setLimits(MakeLimits(lim[0], lim[1], sq2.getSeq().length()));

                    sq1 = PerformClean(sq1);//, fq);
                    sq2 = PerformClean(sq2);//, fq);
                    
                    if (sq1 == null || sq2 == null){
                        return -1;
                    }
                    //Save data to charts
                    QualityDAO qd = new QualityDAO();

                    qd.WriteStatistic(sq1.getMeanQual(), bwStaticQual);
                    qd.WriteStatistic(sq2.getMeanQual(), bwStaticQual);

                    //Separe 
                    if (!sq1.getDiscard() && !sq2.getDiscard()) {
                        SequenceDAO.WriteFastq(sq1, bwa1);
                        SequenceDAO.WriteFastq(sq2, bwa2);

                        SequenceDAO.WriteFasta(sq1, bwaf1);
                        SequenceDAO.WriteFasta(sq2, bwaf2);

                    } else if (!sq1.getDiscard()) {

                        SequenceDAO.WriteFastq(sq1, bws);
                        SequenceDAO.WriteFasta(sq1, bwsf);

                    } else if (!sq2.getDiscard()) {

                        SequenceDAO.WriteFastq(sq2, bws);
                        SequenceDAO.WriteFasta(sq2, bwsf);

                    }
                } else {
                    JOptionPane.showConfirmDialog(null,
                            "Arquivo FASTQ tem problemas em sua formatação",
                            //"FASTQ file have problems in your format",
                            "Erro no arquivo FASTQ",
                            //"Error in FASTQ file",
                            JOptionPane.INFORMATION_MESSAGE);
                    return -1;
                }
            }
            bws.close();
            bwa1.close();
            bwa2.close();
            bwStaticQual.close();
        } catch (IOException ex) {
            return -1;
        }
        Object[] options = {"Sim", //"Yes",
            "Não",//"No",
            "Cancelar"};
        n = JOptionPane.showOptionDialog(null,
                "A limpeza de qualidade terminou. "//"The quality clean is successfull!"
                + "Gostaria de remover as sequências contaminantes? Clique em \"Sim\".",//"Would like to perform the removal contaminating sequences?",
                "Successo",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        return n;
    }

    /**
     * Execute mothods that analyze the sequence
     *
     * @param seq object with sequence information fq object generated by the
     * FastqReader org.biojava3.sequencing.io.fastq.Fastq;
     * @return object with total analyze sequence
     */
    public SequenceVO PerformClean(SequenceVO seq) {
        seq = Complexity(seq);

        if (seq.getCompl() >= minComplex) {
            seq = MeanQuality(seq);
            
        } else {
            seq = MeanQuality(seq);
            seq.setDiscard(true);
        }
        return seq;
    }

    /**
     * Write the mean of quality to sequences NOT implemnted in BioJava The mean
     * is calculated which the sum of all scores divided by length sequence
     *
     * @param seq object with sequence information
     * @return altered object with men quality
     */
    private SequenceVO MeanQuality(SequenceVO seq) {

        //Transform quality to chars
        char scores[] = new char[seq.getQual().length()];
        seq.getQual().getChars(0, seq.getQual().length(), scores, 0);

        //Declair array of values scores
        int[] qual = new int[scores.length];

        //Chars to values of ASCII table
        for (int i = 0; i < scores.length; i++) {
            qual[i] = (int) scores[i];
            qual[i] = qual[i] - seq.getType().getOffset();
            if(qual[i] > seq.getType().getMax() || qual[i] < seq.getType().getMin()){
                JOptionPane.showMessageDialog(null, 
                        "O tipo de arquivo escolhido não pode ter scores de qualidade menor que "+
                                seq.getType().getMin()+" ou maior que "+ seq.getType().getMax()+"", 
                        "Erro no tipo de arquivo", JOptionPane.INFORMATION_MESSAGE);
                return  null;
            }
        }

        //Trim End and begin defined for user
        int limits[] = seq.getLimits();

        //Variable keep the sum of scores
        double sum = 0.00;

        //Window is a fourth of length sequence
        int window;
        window = (limits[1] - limits[0]) / 10;

        boolean stop = true;
        int init = limits[0];
        int end = limits[1];

        //Sliding Window quality filtering 
        // --> direction
        while (init < window * 2 && stop) {

            double meanWindow = 0.00;

            for (int i = init; i <= init + window; i++) {
                meanWindow += qual[i];
            }

            meanWindow = meanWindow / window;

            if (meanWindow >= minQual) {
                stop = false;
            } else {
                init++;
            }
        }

        stop = true;

        //Sliding Window quality filtering 
        // <-- direction
        while (end > window * 2 && stop) {

            double meanWindow = 0.00;

            for (int i = end; i >= end - window; i--) {
                meanWindow += qual[i];
            }

            meanWindow = meanWindow / window;

            if (meanWindow >= minQual) {
                stop = false;
            } else {
                end--;
            }
        }

        if (end - init >= window * 5) {
            limits[0] = init;
            limits[1] = end;
            seq.setLimits(limits);
            seq.setDiscard(false);
        } else {
            seq.setDiscard(true);
        }
        
        int limite[];
        limite = seq.getLimits();
        //Loop is limited by the defined cutoff
        for (int i = limite[0]; i < limite[1]; i++) {
            sum += (double) qual[i];
        }

        //Mean of quality is obtained from sum divided by length
        seq.setMeanQual(sum / limite[1]-limite[0]);

        return seq;
    }

    /**
     * Obtains the entropy value that defines the complexity of the sequence
     * Entropy difines how a nucleotid appears in distrubution of the sequence
     *
     * @param sq object with sequence information in fq
     * @return altered object with complexity value
     */
    private static SequenceVO Complexity(SequenceVO sq) {

        /**
         * Array quantChar contais the quantity of char: "A" in 0 position "T"
         * in 1 position "C" in 2 position "G" in 3 position
         */
        double quantChar[] = {0, 0, 0, 0};
        int limits[] = sq.getLimits();

        //Obtains the sequence, transform in a positition of array
        StringBuilder sb = sq.getSeq();
        String seq[] = new String[sb.length()];

        seq = sq.getSeq().toString().split("");

        for (int i = limits[0]; i < limits[1]; i++) {
            switch (seq[i]) {
                case "A":
                    quantChar[0]++;
                    break;
                case "T":
                    quantChar[1]++;
                    break;
                case "C":
                    quantChar[2]++;
                    break;
                case "G":
                    quantChar[3]++;
                    break;
                case "":
                    break;
            }
        }

        double length = quantChar[0] + quantChar[1] + quantChar[2] + quantChar[3];
        double complex = 0.0;
        for (int i = 0; i < quantChar.length; i++) {
            complex += -quantChar[i] / length * Math.log(1 / quantChar[i] / Math.log(2));
        }
        sq.setCompl(complex / 4);

        return sq;
    }
}
