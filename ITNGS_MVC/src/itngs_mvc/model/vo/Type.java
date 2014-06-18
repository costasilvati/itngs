/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package itngs_mvc.model.vo;

/**
 *
 * @author juliana
 */
public final class Type {
    private String name;
    private int offset;
    private int min;
    private int max;

    public Type(String name) {
        TypeFastqVariant(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
    
    
    
    public void TypeFastqVariant(String variant){

        switch (variant) {
            case "Illumina":
                this.setName("FASTQ_ILLUMINA");
                this.setOffset(64);
                this.min = 0;
                this.max = 62;
                break;
            case "Sanger":
                this.setName("FASTQ_SANGER");
                this.setOffset(33);
                this.min = 0;
                this.max = 93;
                break;
            case "Solexa":
                this.setName("FASTQ_SOLEXA");
                this.setOffset(64);
                this.min = -5;
                this.max = 62;
                break;
            case "Roche 454":
                this.setName("454");
                this.setOffset(33);
                this.min = 0;
                this.max = 93;
                break;
            default:
                this.setName(null);
                this.setOffset(-1);
                break;
        }
    }
}
