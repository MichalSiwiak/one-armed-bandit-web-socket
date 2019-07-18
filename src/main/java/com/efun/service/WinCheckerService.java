package com.efun.service;
import java.util.List;

public interface WinCheckerService {

    public boolean isWin(List<List<Integer>> symbols, List<List<Integer>> win);

}