package com.efun.validation;

import com.efun.message.EndParams;
import com.efun.message.InitParams;
import com.efun.message.SpinParams;

public interface ValidationService {

    public boolean validateInitParams(InitParams initParams, String gameId);
    public boolean validateSpinParams(SpinParams spinParams);
    public boolean validateEndParams(EndParams endParams);
}
