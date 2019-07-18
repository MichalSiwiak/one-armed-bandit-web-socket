package com.efun.service;

import com.efun.entity.ReelPosition;

import java.util.List;


public interface CheckResultService {

    public List<List<Integer>> isReelPositionInCache(List<Integer> activeReels, int rno);
}
