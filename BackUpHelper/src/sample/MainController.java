package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;

public class MainController {

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
        prompt.setText("");
        Main main = new Main();
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

    public void deletePaths() throws Exception{
        Stage delete = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("deleteWindow.fxml"));
        delete.setScene(new Scene(root, 460, 350));
        delete.show();
    }

}
