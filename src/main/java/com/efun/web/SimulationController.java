package com.efun.web;

import com.efun.components.SimulationReportEnd;
import com.efun.components.SimulationReportInit;
import com.efun.config.BeansConfiguration;
import com.efun.config.GameConfig;
import com.efun.service.CombinationService;
import com.efun.service.SimulationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

@Controller
public class SimulationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    private SimulationService simulationService;
    private ApplicationContext applicationContext;
    private BeansConfiguration beansConfiguration;
    private CombinationService combinationService;

    public SimulationController(SimulationService simulationService,
                                ApplicationContext applicationContext,
                                BeansConfiguration beansConfiguration,
                                CombinationService combinationService) {
        this.simulationService = simulationService;
        this.applicationContext = applicationContext;
        this.beansConfiguration = beansConfiguration;
        this.combinationService = combinationService;
    }

    @RequestMapping(value = "/report", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public ResponseEntity<SimulationReportEnd> simulationServicePost(@RequestBody SimulationReportInit simulationReportInit) {
        SimulationReportEnd simulationReportEnd = simulationService.generateLotOFSpins(simulationReportInit);
        LOGGER.info("Report generated size="+simulationReportEnd.getRnoScaleList().size());
        return new ResponseEntity<>(simulationReportEnd, HttpStatus.OK);
    }

    @PostMapping("/send")
    public String handleFileUpload(@RequestParam("file") MultipartFile multipartFile,
                                   RedirectAttributes redirectAttributes,
                                   HttpSession httpSession) {
        if (multipartFile.getOriginalFilename().isEmpty()) {
            redirectAttributes.addAttribute("error", "Please select a valid file!");
        } else if (multipartFile.getSize() > 5242880) {
            redirectAttributes.addAttribute("error", "File can not be larger than 5 MB!");
        } else if (!multipartFile.getContentType().equals("application/json")) {
            redirectAttributes.addAttribute("error", "Please select a valid format!");
        } else {
            File tempFile = new File("D:\\tempFile_" + httpSession.getId());
            try {
                //add verification of Game config
                multipartFile.transferTo(tempFile);
                GameConfig gameConfigNew = beansConfiguration.parseGameConfigTest(tempFile);
                applicationContext.getBean(GameConfig.class).setFilterOnlyHighestResultsInWinLine(gameConfigNew.isFilterOnlyHighestResultsInWinLine());
                applicationContext.getBean(GameConfig.class).setWineLineOnlyOnAllActiveReels(gameConfigNew.isWineLineOnlyOnAllActiveReels());
                applicationContext.getBean(GameConfig.class).setReels(gameConfigNew.getReels());
                applicationContext.getBean(GameConfig.class).setSpin(gameConfigNew.getSpin());
                applicationContext.getBean(GameConfig.class).setWinnings(gameConfigNew.getWinnings());
                applicationContext.getBean(GameConfig.class).setWinLines(gameConfigNew.getWinLines());

                combinationService.saveAllCombinationsToDatabase();

                tempFile.delete();
                redirectAttributes.addFlashAttribute("success", "Game configuration updated.");
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("error",
                        "Problem with parsing json");
                LOGGER.warn("Problem with parsing json" + e.getMessage());
            }
        }
        return "redirect:/simulation";
    }

  /*  @RequestMapping(value = "/someReport", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public ResponseEntity<SimulationReportInit> simulationServiceGet() {

        SimulationReportInit simulationReportInit = new SimulationReportInit();
        simulationReportInit.setActiveReels(Arrays.asList(0, 1, 2));
        simulationReportInit.setActiveWinLines(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7));
        simulationReportInit.setSize(1000);
        simulationReportInit.setStart(50);
        simulationReportInit.setStartingBalance(new BigDecimal("50000"));
        simulationReportInit.setBet(new BigDecimal("100"));

        return new ResponseEntity<>(simulationReportInit, HttpStatus.OK);
    }*/
}