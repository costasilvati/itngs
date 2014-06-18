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
 * @author juliana
 */
public class AlignmentVO {
    private String idSequence;
    private double score;

    public AlignmentVO(String idSequence) {
        this.idSequence = idSequence;
    }

    public AlignmentVO() {
    }

    public String getIdSequence() {
        return idSequence;
    }

    public void setIdSequence(String idSequence) {
        this.idSequence = idSequence;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
    
    
}
