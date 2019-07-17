package com.efun.validation;

import com.efun.message.InitParams;
import com.efun.message.SpinParams;
import org.springframework.stereotype.Service;


@Service
public class ValidationServiceImpl implements ValidationService {


    @Override
    public boolean validateSpin(SpinParams spinParams) {
        try {

            int bet = Integer.parseInt(spinParams.getBet());
            int rno = Integer.parseInt(spinParams.getRno());

            if (rno < 0 || bet < 0) {
                return false;
            } else {
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }


    }
}
