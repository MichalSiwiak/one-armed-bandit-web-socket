package com.efun.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReelsNamesServiceImpl implements ReelsNamesService {

    //to add some meta information
    @Override
    public String getNameOfReel(List<Integer> reel, int spin) {
        StringBuffer reelName = new StringBuffer();
        reel.stream().forEach(element -> reelName.append(element));
        reelName.append(spin);
        return DigestUtils.md5Hex(reelName.toString()).toUpperCase();
    }
}
