package io.starskyoio.asr.controller;

import io.starskyoio.asr.baidu.aip.client.AuthClient;
import io.starskyoio.asr.baidu.aip.model.AuthResponse;
import io.starskyoio.asr.holder.TokenHolder;
import io.starskyoio.asr.model.OutputExcelModel;
import io.starskyoio.asr.service.ExcelService;
import io.starskyoio.asr.service.VopService;
import io.starskyoio.asr.util.AlertUtils;
import io.starskyoio.asr.util.FileValidateUtils;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@FXMLController
public class MainContorller implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainContorller.class);

    @Autowired
    private VopService vopService;

    @Autowired
    private ExcelService excelService;

    @Autowired
    private AuthClient authClient;

    @FXML
    private Button inputBtn;

    @FXML
    private Button outputBtn;

    @FXML
    private Button startBtn;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label inputLabel;

    @FXML
    private Label outputLabel;

    @FXML
    private Label msgLabel;

    private String inputPath;
    private String outputPath;
    private volatile boolean isCompleted = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            AuthResponse authResponse = authClient.getToken();
            if (authResponse.getError() != null) {
                throw new RuntimeException(authResponse.getErrorDescription());
            }
            TokenHolder.token = authResponse.getAccessToken();
            LOGGER.info("获取token成功, token:{}", authResponse.getAccessToken());
        } catch (Exception e) {
            LOGGER.error("获取token失败, errorMsg:{}", e.getMessage());
            AlertUtils.error("获取token失败");
            inputBtn.setDisable(true);
            outputBtn.setDisable(true);
            startBtn.setDisable(true);
        }
    }

    @FXML
    private void selectInputBtnClick(final Event event) {
        File file = new DirectoryChooser().showDialog(inputBtn.getScene().getWindow());
        if (file != null) {
            inputPath = file.getAbsolutePath();
            inputLabel.setText(file.getAbsolutePath());
            inputLabel.setTooltip(new Tooltip(file.getAbsolutePath()));
        }
        isCompleted = false;
        msgLabel.setVisible(false);
        progressBar.setVisible(false);
        msgLabel.setText("提示");
        progressBar.setProgress(0.0);
    }

    @FXML
    private void selectOuputBtnClick(final Event event) {
        File file = new DirectoryChooser().showDialog(outputBtn.getScene().getWindow());
        if (file != null) {
            outputPath = file.getAbsolutePath();
            outputLabel.setText(file.getAbsolutePath());
            outputLabel.setTooltip(new Tooltip(file.getAbsolutePath()));
        }
    }

    @FXML
    private void startBtnClick(final Event event) {
        if (isCompleted) {
            AlertUtils.info("识别任务已完成，请重新设置导入文件夹");
            return;
        }

        if (StringUtils.isEmpty(inputPath)) {
            AlertUtils.info("请设置导入文件夹");
            return;
        }

        if (StringUtils.isEmpty(outputPath)) {
            AlertUtils.info("请设置导出文件夹");
            return;
        }

        startTask();
    }

    private void startTask() {
        File inputFile = new File(inputPath);
        List<File> files = FileUtils.listFiles(inputFile, null, true)
                .stream()
                .filter(file -> FileValidateUtils.checkFileName(file.getName()))
                .collect(Collectors.toList());

        if (files.size() == 0) {
            AlertUtils.info("导入文件夹中不存在文件");
            return;
        }

        inputBtn.setDisable(true);
        outputBtn.setDisable(true);
        startBtn.setDisable(true);
        msgLabel.setVisible(true);
        progressBar.setVisible(true);

        final int total = files.size();
        AtomicInteger complete = new AtomicInteger(0);

        List<OutputExcelModel> dataList = new CopyOnWriteArrayList<>();
        for (File file : files) {
            vopService.doTask(file, (taskResult) -> {
                String baseName = FilenameUtils.getBaseName(taskResult.getFileName());
                String[] noArr = baseName.split("_");
                OutputExcelModel model = new OutputExcelModel();
                model.setProofNo(noArr[0]);
                model.setSignalNo(noArr[1]);
                model.setContent(taskResult.getContent());
                model.setResult(taskResult.getErrNo() == 0 ? "1" : "2");
                model.setFail(taskResult.getErrNo() == 0 ? "" : "错误码:" + taskResult.getErrNo() + ", 错误信息:" + taskResult.getErrMsg());
                dataList.add(model);

                //更新进度条
                double progress = complete.incrementAndGet() / (double) total;
                Platform.runLater(() -> {
                    progressBar.setProgress(progress);
                    msgLabel.setText("运行中:" + complete.intValue() + "/" + total);
                });

                if (complete.intValue() == total) {
                    synchronized (MainContorller.this) {
                        if (!isCompleted) {
                            isCompleted = true;
                            Platform.runLater(() -> {
                                inputBtn.setDisable(false);
                                outputBtn.setDisable(false);
                                startBtn.setDisable(false);
                                msgLabel.setText("完成:" + complete.intValue() + "/" + total);
                            });
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                            String outputFile = outputPath + File.separator + "语音识别_" + sdf.format(new Date()) + ".xlsx";
                            excelService.doTask(outputFile, dataList);
                        }
                    }
                }
            });
        }

    }

}
