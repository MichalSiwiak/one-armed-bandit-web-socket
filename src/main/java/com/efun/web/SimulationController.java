package com.efun.web;

import com.efun.components.SimulationReportEnd;
import com.efun.components.SimulationReportInit;
import com.efun.config.BeansConfiguration;
import com.efun.config.GameConfig;
import com.efun.service.CombinationService;
import com.efun.service.MessageProviderService;
import com.efun.service.SimulationService;
import com.efun.validation.ValidationService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;

@Controller
public class SimulationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    private SimulationService simulationService;
    private ApplicationContext applicationContext;
    private BeansConfiguration beansConfiguration;
    private CombinationService combinationService;
    private MessageProviderService messageProviderService;
    private ValidationService validationService;

    @Value("${path_to_config_file}")
    private String pathToConfigFile;

    @Value("${path_to_results_file}")
    private String pathToResultsFile;

    @Value("${path_to_logs_file}")
    private String pathToLogsFile;


    public SimulationController(SimulationService simulationService,
                                ApplicationContext applicationContext,
                                BeansConfiguration beansConfiguration,
                                CombinationService combinationService,
                                MessageProviderService messageProviderService,
                                ValidationService validationService) {
        this.simulationService = simulationService;
        this.applicationContext = applicationContext;
        this.beansConfiguration = beansConfiguration;
        this.combinationService = combinationService;
        this.messageProviderService = messageProviderService;
        this.validationService = validationService;

    }

    @RequestMapping(value = "/report", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public ResponseEntity<SimulationReportEnd> simulationServicePost(@RequestBody SimulationReportInit simulationReportInit) {
        SimulationReportEnd simulationReportEnd = simulationService.generateLotOFSpins(simulationReportInit);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(pathToResultsFile)) {
            gson.toJson(simulationReportEnd.getCombinationResults(), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        LOGGER.info("Report generated size=" + simulationReportEnd.getRnoScaleList().size());
        return new ResponseEntity<>(simulationReportEnd, HttpStatus.OK);
    }

    @PostMapping("/send")
    public String handleFileUpload(@RequestParam("file") MultipartFile multipartFile, RedirectAttributes redirectAttributes) {
        String[] split = multipartFile.getOriginalFilename().split("\\.");
        String fileExtension = split[split.length - 1];

        // in one method using private method and switch
        if (multipartFile.getOriginalFilename().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please select a valid file");
            LOGGER.warn("Empty config file was uploaded");
            return "redirect:/simulation";
        }
        if (multipartFile.getSize() > 5242880) {
            redirectAttributes.addFlashAttribute("error", "File can not be larger than 5 MB");
            LOGGER.warn("To large config file was uploaded");
            return "redirect:/simulation";
        }
        if (!(fileExtension.equals("yml") || fileExtension.equals("yaml"))) {
            redirectAttributes.addFlashAttribute("error", "Please select a valid YAML format");
            LOGGER.warn("Invalid format of config file");
            return "redirect:/simulation";
        }
        if (messageProviderService.getSessions().size() != 0) {
            redirectAttributes.addFlashAttribute("error", "Cannot change config now - please close active sessions at first");
            LOGGER.warn("Cannot change config because of active sessions");
            return "redirect:/simulation";
        }
        messageProviderService.setTrigger(false);
        File tempFile = new File(pathToConfigFile);
        try {

            multipartFile.transferTo(tempFile);
            GameConfig gameConfigNew = beansConfiguration.parseGameConfigFromFile(tempFile.getAbsolutePath());

            if (validationService.validateGameConfig(gameConfigNew)) {
                applicationContext.getBean(GameConfig.class).setFilterOnlyHighestResultsInWinLine(gameConfigNew.isFilterOnlyHighestResultsInWinLine());
                applicationContext.getBean(GameConfig.class).setWineLineOnlyOnAllActiveReels(gameConfigNew.isWineLineOnlyOnAllActiveReels());
                applicationContext.getBean(GameConfig.class).setReels(gameConfigNew.getReels());
                applicationContext.getBean(GameConfig.class).setSpin(gameConfigNew.getSpin());
                applicationContext.getBean(GameConfig.class).setWinnings(gameConfigNew.getWinnings());
                applicationContext.getBean(GameConfig.class).setWinLines(gameConfigNew.getWinLines());
                combinationService.saveAllCombinationsToDatabase();
                //tempFile.delete();
                redirectAttributes.addFlashAttribute("success", "Game configuration updated");
                LOGGER.info("Game configuration updated");
                messageProviderService.setTrigger(true);
            } else {
                redirectAttributes.addFlashAttribute("error", "Invalid game config file");
                LOGGER.warn("Invalid game config file");
                messageProviderService.setTrigger(true);
            }
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Problem with access config file");
            LOGGER.warn("Problem with access config file" + e.getMessage());
        }
        return "redirect:/simulation";
    }

    @GetMapping(value = "/logs", produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody
    byte[] getFile() throws IOException {
        InputStream in = new FileInputStream(new File(pathToLogsFile));
        return IOUtils.toByteArray(in);
    }

    @GetMapping("/downloadConfig")
    public ResponseEntity<InputStreamResource> downloadFile() {
        File downloadFile = new File(pathToConfigFile);
        InputStreamResource resource = null;
        try {
            //if jar packaging comment this if
            if (downloadFile.length() == 0) {
                downloadFile = ResourceUtils.getFile("classpath:game-configuration.yml");
            }
            resource = new InputStreamResource(new FileInputStream(downloadFile));
        } catch (IOException e) {
            LOGGER.warn("Problem with downloading file" + e.getMessage());
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=" + downloadFile.getName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(downloadFile.length())
                .body(resource);
    }

    @GetMapping("/downloadResults")
    public ResponseEntity<InputStreamResource> downloadResults() {
        File downloadFile = new File(pathToResultsFile);
        InputStreamResource resource = null;
        try {
            resource = new InputStreamResource(new FileInputStream(downloadFile));
        } catch (IOException e) {
            LOGGER.warn("Problem with downloading file" + e.getMessage());
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=" + downloadFile.getName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(downloadFile.length())
                .body(resource);
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