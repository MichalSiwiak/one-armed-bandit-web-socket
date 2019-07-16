package com.efun.entity;

import com.efun.message.WinLineData;
import org.bson.types.ObjectId;
import java.util.Date;
import java.util.List;

/**
 * Class representing entity of information about all created games.
 * gameId and authorizationToken have simple implementation:
 * Their values are build using md5 algorithm contains actual time,
 * session id and size of sessions.
 * @author Michał Siwiak
 */

public class GameDto {

    //in start
    private ObjectId id;
    private String gameId;
    private String authorizationToken;
    private WinLineData winlineData;
    //in spin
    private List<Integer> spinList;
    private int numberOfSpins;
    private List<Double> winList;
    private double sumOfWins;
    //in start
    private Date startDate;
    private Date lastSpinDate;
    //in end
    private Date endDate;
    //in all statuses
    private String status;

    @Override
    public String toString() {
        return "GameDto{" +
                "id=" + id +
                ", gameId='" + gameId + '\'' +
                ", authorizationToken='" + authorizationToken + '\'' +
                ", winlineData=" + winlineData +
                ", spinList=" + spinList +
                ", numberOfSpins=" + numberOfSpins +
                ", winList=" + winList +
                ", sumOfWins=" + sumOfWins +
                ", startDate=" + startDate +
                ", lastSpinDate=" + lastSpinDate +
                ", endDate=" + endDate +
                ", status=" + status +
                '}';
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getAuthorizationToken() {
        return authorizationToken;
    }

    public void setAuthorizationToken(String authorizationToken) {
        this.authorizationToken = authorizationToken;
    }

    public WinLineData getWinlineData() {
        return winlineData;
    }

    public void setWinlineData(WinLineData winlineData) {
        this.winlineData = winlineData;
    }

    public List<Integer> getSpinList() {
        return spinList;
    }

    public void setSpinList(List<Integer> spinList) {
        this.spinList = spinList;
    }

    public int getNumberOfSpins() {
        return numberOfSpins;
    }

    public void setNumberOfSpins(int numberOfSpins) {
        this.numberOfSpins = numberOfSpins;
    }

    public List<Double> getWinList() {
        return winList;
    }

    public void setWinList(List<Double> winList) {
        this.winList = winList;
    }

    public double getSumOfWins() {
        return sumOfWins;
    }

    public void setSumOfWins(double sumOfWins) {
        this.sumOfWins = sumOfWins;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getLastSpinDate() {
        return lastSpinDate;
    }

    public void setLastSpinDate(Date lastSpinDate) {
        this.lastSpinDate = lastSpinDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
