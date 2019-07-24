package com.efun.components;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class TotalWinInSpin {

    private BigDecimal totalMultiply;
    private List<ResultWin> resultWinList;

    public BigDecimal getTotalMultiply() {
        return totalMultiply;
    }

    public void setTotalMultiply(BigDecimal totalMultiply) {
        this.totalMultiply = totalMultiply;
    }

    public List<ResultWin> getResultWinList() {
        return resultWinList;
    }

    public void setResultWinList(List<ResultWin> resultWinList) {
        this.resultWinList = resultWinList;
    }

    @Override
    public String toString() {
        return "TotalWinInSpin{" +
                "totalMultiply=" + totalMultiply +
                ", resultWinList=" + resultWinList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TotalWinInSpin that = (TotalWinInSpin) o;
        return Objects.equals(totalMultiply, that.totalMultiply) &&
                Objects.equals(resultWinList, that.resultWinList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalMultiply, resultWinList);
    }
}
