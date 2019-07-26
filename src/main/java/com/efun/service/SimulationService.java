package com.efun.service;

import com.efun.components.SimulationReportEnd;
import com.efun.components.SimulationReportInit;
import com.efun.entity.CombinationResult;

import java.math.BigDecimal;
import java.util.List;

public interface SimulationService {

    public SimulationReportEnd generateLotOFSpins(SimulationReportInit simulationReportInit);
}
