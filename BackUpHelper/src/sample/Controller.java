package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;

public class Controller {

    @FXML TextField sourcePath;
    @FXML TextField targetPath;
    @FXML Label prompt;
    @FXML Button chooseSource;

    public void generateFiles(){
        String source = sourcePath.getText();
        String target = targetPath.getText();
        if(!source.isEmpty() && !target.isEmpty()) {
            GenerateFiles gbf = new GenerateFiles();
            try {
                gbf.generateFiles(source, target);
            } catch (IOException e) {
                e.printStackTrace();
            }
            sourcePath.clear();
            targetPath.clear();
            prompt.setText("Backup Added");
        }
    }

    public void chooseSourceFolder(){
        sourcePath.setText(getFolder());
    }
    public void chooseTargetFolder(){
        targetPath.setText(getFolder());
    }
    private String getFolder(){
        Main main = new Main();
        System.out.println("Get folder");
        DirectoryChooser folder = new DirectoryChooser();
        folder.setInitialDirectory(new File(System.getProperty("user.home")));
        folder.setTitle("BackupHelper");
        String result = folder.showDialog(main.getStage()).getAbsolutePath();
        if(result != null){
            return result;
        }
        else
            return "";
    }
}
