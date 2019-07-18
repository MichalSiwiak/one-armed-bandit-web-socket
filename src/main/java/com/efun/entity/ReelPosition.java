package com.efun.entity;

import org.springframework.data.annotation.Id;
import java.util.List;

public class ReelPosition {

    @Id
    private String id;
    private String md5Hex;
    private int positions;
    private List<Integer> reelNumbers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMd5Hex() {
        return md5Hex;
    }

    public void setMd5Hex(String md5Hex) {
        this.md5Hex = md5Hex;
    }

    public int getPositions() {
        return positions;
    }

    public void setPositions(int positions) {
        this.positions = positions;
    }

    public List<Integer> getReelNumbers() {
        return reelNumbers;
    }

    public void setReelNumbers(List<Integer> reelNumbers) {
        this.reelNumbers = reelNumbers;
    }

    @Override
    public String toString() {
        return "ReelPosition{" +
                "id='" + id + '\'' +
                ", md5Hex='" + md5Hex + '\'' +
                ", positions=" + positions +
                ", reelNumbers=" + reelNumbers +
                '}';
    }
}