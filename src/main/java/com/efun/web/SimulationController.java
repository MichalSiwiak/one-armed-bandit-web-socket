package com.efun.web;

import com.efun.components.SimulationReportEnd;
import com.efun.components.SimulationReportInit;
import com.efun.service.SimulationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;
import java.util.Arrays;

@Controller
public class SimulationController {

    private SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @RequestMapping(value = "/report", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public ResponseEntity<SimulationReportEnd> simulationServicePost(@RequestBody SimulationReportInit simulationReportInit) {
        SimulationReportEnd simulationReportEnd = simulationService.generateLotOFSpins(simulationReportInit);
        return new ResponseEntity<>(simulationReportEnd, HttpStatus.OK);
    }

    @RequestMapping(value = "/someReport", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public ResponseEntity<SimulationReportInit> simulationServiceGet() {

        SimulationReportInit simulationReportInit = new SimulationReportInit();
        simulationReportInit.setActiveReels(Arrays.asList(0, 1, 2));
        simulationReportInit.setActiveWinLines(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7));
        simulationReportInit.setSize(1000);
        simulationReportInit.setStart(50);
        simulationReportInit.setStartingBalance(new BigDecimal("50000"));
        simulationReportInit.setBet(new BigDecimal("100"));

        return new ResponseEntity<>(simulationReportInit, HttpStatus.OK);
    }

}