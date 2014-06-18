package itngs_mvc.model.vo;

/**
 *
 * @author juliana
 */
public class SequenceVO {
    private String name;
    private StringBuilder seq;
    private StringBuilder qual;
    private Type type;
    private boolean paired;
    private int[] limits;
    private  double compl;
    private double meanQual;
    private boolean discard;

    public SequenceVO(Type type,boolean paired){
        this.type = type;
        this.paired = paired;
        limits = new int[2];
    }
    
    public SequenceVO(boolean paired){
        this.paired = paired;
        limits = new int[2];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StringBuilder getSeq() {
        return seq;
    }

    public void setSeq(StringBuilder seq) {
        this.seq = seq;
    }

    public StringBuilder getQual() {
        return qual;
    }

    public void setQual(StringBuilder qual) {
        this.qual = qual;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean getPaired() {
        return paired;
    }

    public void setPaired(boolean paired) {
        this.paired = paired;
    }

    public int[] getLimits() {
        return limits;
    }

    public void setLimits(int[] limits) {
        this.limits = limits;
    }
    
     public double getCompl() {
        return compl;
    }

    public void setCompl(double compl) {
        this.compl = compl;
    }

    public double getMeanQual() {
        return meanQual;
    }

    public void setMeanQual(double meanQual) {
        this.meanQual = meanQual;
    }

    public boolean getDiscard() {
        return discard;
    }

    public void setDiscard(boolean discard) {
        this.discard = discard;
    }
    
    
}
