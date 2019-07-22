package com.efun.components;

import java.math.BigDecimal;
import java.util.List;

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
}
