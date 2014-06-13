/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package itngs_mvc.control;

import itngs_mvc.model.bo.AlignmentBO;
import static itngs_mvc.model.bo.AlignmentBO.TransformFastaName;
import itngs_mvc.model.bo.ChartBO;
import itngs_mvc.model.bo.QualityBO;
import itngs_mvc.model.dao.ChartDAO;
import itngs_mvc.model.dao.QualityDAO;
import itngs_mvc.model.vo.MegablastVO;
import itngs_mvc.model.vo.SequenceVO;
import itngs_mvc.model.vo.Type;
import itngs_mvc.view.MainFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author juliana
 */
public class MainController implements ActionListener, ChangeListener {

    private final MainFrame frame;
    private boolean pair = false;
    private ArrayList<Double> data = new ArrayList<>();

    /**
     * Construtor<br />Recebe o objeto da FramePrincipal para 'observer' seu
     * comportamento, tratar os eventos e redirecionar para a model.
     *
     * @param framePrincipal
     */
    public MainController(MainFrame framePrincipal) {
        this.frame = framePrincipal;
        //Definindo os listeners para os botoes dessa view.
        this.frame.jBStart.addActionListener(this);
        this.frame.jBStartRemove.addActionListener(this);
        this.frame.jBGenerateGraphics.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.frame.jBStart) {

            this.frame.jProgressBarQuality.setString("Processando...");
            CleanQualitySequence();

        } else if (e.getSource() == this.frame.jBStartRemove) {

            removeVector();

        } else if (e.getSource() == this.frame.jBGenerateGraphics) {

            GenerateCharts();
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        showWindow("Ocorreu algum problema! Encerre o programa e comece novamente.","Erro");//To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Exceute quality clean
     */
    public void CleanQualitySequence() {
        this.frame.jProgressBarQuality.setEnabled(true);
        this.frame.jProgressBarQuality.setIndeterminate(true);
        String file1 = this.frame.jTFile1.getText();
        String file2 = this.frame.jTFile2.getText();
        String type = this.frame.getSelectedButtonText(this.frame.buttonGroupDevice);

        int limits[] = {Integer.parseInt(this.frame.jCBTrimBegin.getSelectedItem().toString()),
            Integer.parseInt(this.frame.jCBTrimEnd.getSelectedItem().toString())};

        QualityBO qbo = new QualityBO(
                Double.parseDouble(this.frame.jCBMinComplex.getSelectedItem().toString()),
                Double.parseDouble(this.frame.jCBMinQual.getSelectedItem().toString())
        );

        if (this.frame.getSelectedButtonText(this.frame.buttonGroupPaired).equals("Paired-end")) {
            pair = true;
        }

        SequenceVO sq = new SequenceVO(new Type(type), pair);

        sq.setLimits(limits);

        int op;

        if (sq.getPaired()) {

            op = qbo.QualityClean(sq, file1, file2);
            this.frame.jTFileClean.setText(TransformFastaName(this.frame.jTFile1.getText()));
            this.frame.jTFileClean2.setText(TransformFastaName(this.frame.jTFile2.getText()));

        } else {

            op = qbo.QualityClean(sq, file1);
            this.frame.jTFileClean.setText(TransformFastaName(this.frame.jTFile1.getText()));
        }

        switch (op) {
            case -1:

                showWindow("Erro! A limpeza não foi concluída!", "Erro");
                break;

            case 0:

                this.frame.jProgressBarQuality.setEnabled(false);
                frame.jTPQualityClean.setSelectedIndex(1);
                frame.jTPQualityClean.setEnabledAt(0, false);
                frame.jTPQualityClean.setEnabledAt(1, true);
                frame.jTFVectorSeq.setText("");
                frame.jCheckBox1.setSelected(true);
                break;

            case 1:

                this.frame.jProgressBarQuality.setEnabled(false);
                frame.jTPQualityClean.setSelectedIndex(2);
                frame.jTPQualityClean.setEnabledAt(0, false);
                frame.jTPQualityClean.setEnabledAt(2, true);
                frame.jRBAllCharts.setEnabled(false);
                frame.jRBVectorCharts.setEnabled(false);
                frame.jRBQualCharts.setSelected(true);
                break;

            case 2:
                this.frame.jProgressBarQuality.setEnabled(false);
                System.exit(0);
                break;

        }
    }

    public void showWindow(String text, String windowName) {
        //Custom button text
        JOptionPane.showMessageDialog(null, text, windowName, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Execute alignment of sequence with vector
     */
    public void removeVector() {
        frame.jProgressBar2.setEnabled(true);
        frame.jProgressBar2.setIndeterminate(true);

        String file = frame.jTFile1.getText().substring(0, frame.jTFile1.getText().lastIndexOf("."));
        file = file.substring(file.lastIndexOf("/") + 1, file.length());
        this.frame.jTFOutputFile.setText(file);

        file = null;

        if (pair) {

            file = this.frame.jTFileClean2.getText();

        }

        AlignmentBO albo = new AlignmentBO();

        MegablastVO mgb = new MegablastVO();

        mgb.setExpectValue(frame.jCBExpectValue.getSelectedItem().toString().trim());
        mgb.setCostGapOpen(frame.jCBCostGap.getSelectedItem().toString().trim());
        mgb.setCostExtendGap(frame.jCBCostGapExt.getSelectedItem().toString().trim());
        mgb.setRewardForMatche(frame.jCBPointMatche.getSelectedItem().toString().trim());
        mgb.setPenalty(frame.jCBPenalty.getSelectedItem().toString().trim());
        mgb.setCpuNumber(frame.jCBNumProc.getSelectedItem().toString().trim());
        mgb.setWordSize(frame.jCBWordSize.getSelectedItem().toString().trim());
        mgb.setMinScore(frame.jCBMinScore.getSelectedItem().toString().trim());
        mgb.setIdentityCutoff(frame.jCBIdentityCut.getSelectedItem().toString().trim());
        mgb.setInput1(frame.jTFileClean.getText().trim());
        mgb.setInput2(file);
        mgb.setOutput1("output_megablast1.txt");
        mgb.setOutput2("output_megablast12.txt");

        if(frame.jCheckBox1.isSelected()){
            data = albo.AlignmentAndRemove("univec.fasta", mgb);
        }else{
            try {
                copyFile(this.frame.jTFVectorSeq.getText());
                data = albo.AlignmentAndRemove("database.fasta", mgb);
            } catch (IOException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        

        if (!data.isEmpty()) {

            this.frame.jProgressBar2.setEnabled(false);
            frame.jTPQualityClean.setSelectedIndex(2);
            frame.jTPQualityClean.setEnabledAt(1, false);

        } else {
            showWindow( "Não foram encontrados alinhamentos","Erro");
        }
    }

    /**
     * Generates selected graphics selected by user
     */
    public void GenerateCharts() {
        String type = this.frame.getSelectedButtonText(this.frame.buttonGroupCharts);
        ChartDAO chdao = new ChartDAO();
        BufferedReader br;

        switch (type) {
            case "Todos":

                try {
                    br = new BufferedReader(
                            new FileReader(QualityDAO.CreateStatiscOut(new File(this.frame.jTFile1.getText()), "dataQual")));
                    ChartBO chbo = new ChartBO(chdao.ReadStatistics(br), 0, "Quality Scores");

                    ChartBO chboVector = new ChartBO(data, 2, "Percentual of contaminated sequences");

                    chboVector.showChart("Vector","Organism");
                    chbo.showChart("Scores", "Number of Sequences");

                } catch (FileNotFoundException ex) {
                    showWindow("Erro! Arquivo de estatísticas não encontrado!", "Erro");
                }
                break;
            case "Remoção do vetor":
                ChartBO chboVector = new ChartBO(data, 2, "Percentual of contaminated sequences");
                chboVector.showChart("Vector","Organism");
                break;
            case "Limpeza de qualidade":

                try {
                    br = new BufferedReader(
                            new FileReader(QualityDAO.CreateStatiscOut(new File(this.frame.jTFile1.getText()), "dataQual")));
                    ChartBO chbo = new ChartBO(chdao.ReadStatistics(br), 0, "Quality Scores");
                    chbo.showChart("Scores", "Number of Sequences");
                } catch (FileNotFoundException ex) {
                    showWindow("Erro! Arquivo de estatisticas não encontrado!", "Erro");
                }
                break;
        }
    }
    
    /**
     * Copy file to local execution
     * @param path fasta file with vector or adpter sequences
     * @throws IOException 
     * @throws java.io.FileNotFoundException 
     */
    public void copyFile(String path) throws IOException, FileNotFoundException {
        File source = new File (path);
        File destination = new File("database.fasta");
        if (destination.exists()) {
            destination.delete();
        }

        FileChannel sourceChannel = null;
        FileChannel destinationChannel = null;

        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destinationChannel = new FileOutputStream(destination).getChannel();
            sourceChannel.transferTo(0, sourceChannel.size(),
                    destinationChannel);
        }catch(FileNotFoundException ex){
            showWindow("Arquivo do vetor não encontrado.", "Erro");
        }
        
            finally {
            if (sourceChannel != null && sourceChannel.isOpen()) {
                sourceChannel.close();
            }
            if (destinationChannel != null && destinationChannel.isOpen()) {
                destinationChannel.close();
            }
        }
    }
}
