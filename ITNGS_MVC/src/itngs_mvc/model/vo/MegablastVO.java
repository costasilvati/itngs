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
package itngs_mvc.model.vo;

/**
 *
 * @author juliana
 */
public class MegablastVO {

    private String input1;
    private String input2;
    private String database;
    private String output1;
    private String output2;
    private String expectValue;
    private String costGapOpen;
    private String costExtendGap;
    private String rewardForMatche;
    private String penalty;
    private String cpuNumber;
    private String wordSize;
    private String minScore;
    private String identityCutoff;
    private int time = 0;

    public String getExpectValue() {
        return expectValue;
    }

    public void setExpectValue(String expectValue) {
        this.expectValue = expectValue;
    }

    public String getCostGapOpen() {
        return costGapOpen;
    }

    public void setCostGapOpen(String costGapOpen) {
        this.costGapOpen = costGapOpen;
    }

    public String getCostExtendGap() {
        return costExtendGap;
    }

    public void setCostExtendGap(String costExtendGap) {
        this.costExtendGap = costExtendGap;
    }

    public String getRewardForMatche() {
        return rewardForMatche;
    }

    public void setRewardForMatche(String rewardForMatche) {
        this.rewardForMatche = rewardForMatche;
    }

    public String getPenalty() {
        return penalty;
    }

    public void setPenalty(String penalty) {
        this.penalty = penalty;
    }

    public String getCpuNumber() {
        return cpuNumber;
    }

    public void setCpuNumber(String cpuNumber) {
        this.cpuNumber = cpuNumber;
    }

    public String getWordSize() {
        return wordSize;
    }

    public void setWordSize(String wordSize) {
        this.wordSize = wordSize;
    }

    public String getMinScore() {
        return minScore;
    }

    public void setMinScore(String minScore) {
        this.minScore = minScore;
    }

    public String getIdentityCutoff() {
        return identityCutoff;
    }

    public void setIdentityCutoff(String identityCutoff) {
        this.identityCutoff = identityCutoff;
    }

    public String getInput1() {
        return input1;
    }

    public void setInput1(String input) {
        this.input1 = input;
    }

    public String getInput2() {
        return input2;
    }

    public void setInput2(String input) {
        this.input2 = input;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getOutput1() {
        return output1;
    }

    public void setOutput1(String output) {
        this.output1 = output;
    }

    public String getOutput2() {
        return output2;
    }

    public void setOutput2(String output) {
        this.output2 = output;
    }

    @Override
    public String toString() {
        String input = input1;
        String output = output1;

        if (time == 2) {
            input = input2;
            output = output2;
        }
        String ret = "megablast"
                + " -i " + input;

        if (!expectValue.equals("10")) {
            ret = ret + " -e " + expectValue;
        }

        if (!costGapOpen.equals("-1")) {
            ret = ret + " -G " + costGapOpen;
        }

        if (!costExtendGap.equals("-1")) {
            ret = ret + " -E " + costExtendGap;
        }

        if (!rewardForMatche.equals("1")) {
            ret = ret + " -r " + rewardForMatche;
        }

        if (!penalty.equals("-3")) {
            ret = ret + " -q " + penalty;
        }

        if (!cpuNumber.equals("1")) {
            ret = ret + " -a " + cpuNumber;
        }

        if (!wordSize.equals("28")) {
            ret = ret + " -W " + wordSize;
        }

        if (!minScore.equals("0")) {
            ret = ret + " -s " + minScore;
        }

        if (!identityCutoff.equals("0")) {
            ret = ret + " -p " + identityCutoff;
        }

        ret = ret + " -d " + database + " -o " + output;

        time = 2;
        return ret;
    }

}
