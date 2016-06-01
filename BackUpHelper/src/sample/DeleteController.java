package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by niklas on 31/05/16.
 */
public class DeleteController implements Initializable {

    @FXML ListView pathList;

    String[] paths;
    String[] timestamps;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> list = new ArrayList<>();
        try{
            paths = getPaths();
            timestamps = getTimeStamps();
        }catch(IOException e){
            e.printStackTrace();
        }

        for(int i = 0; i < paths.length; i += 2) {
            if (paths[i] != null) {
                list.add(paths[i] + " -> " + paths[i+1]);
            }
            else break;
        }
        ObservableList<String> items = FXCollections.observableArrayList(list);
        pathList.setItems(items);
    }

    private String[] getPaths() throws IOException {
        File pathFile = new File(System.getProperty("user.home") + File.separator + "BackupHelper" +
                File.separator + "bin" + File.separator + "paths.txt");
        return readFile(pathFile);
    }

    private String[] getTimeStamps() throws IOException{
        File timestamptFile = new File(System.getProperty("user.home") + File.separator + "BackupHelper" +
                File.separator + "bin" + File.separator + "timestamps.txt");
        return readFile(timestamptFile);
    }

    private String[] readFile(File source) throws IOException{
        String[] list = new String[50];
        BufferedReader br = new BufferedReader(new FileReader(source));
        String line = null;
        int i = 0;
        while ((line = br.readLine()) != null) {
            list[i] = line;
            i++;
        }
        br.close();
        return list;
    }

    public void okPressed(){
        int selected = pathList.getSelectionModel().getSelectedIndex();
        try{
            updateFiles(selected);
        }catch(IOException e){
            e.printStackTrace();
        }
        Stage stage = (Stage) pathList.getScene().getWindow();
        stage.close();
    }

    private void updateFiles(int selected) throws IOException{
        GenerateFiles gf = new GenerateFiles();
        File pathFile = new File(System.getProperty("user.home") + File.separator
                + "BackupHelper" + File.separator + "bin" + File.separator + "paths.txt");
        File timestampFile = new File(System.getProperty("user.home") + File.separator
                + "BackupHelper" + File.separator + "bin" + File.separator + "timestamps.txt");
        pathFile.delete();
        timestampFile.delete();
        int i = 0;
        int j = 0;
        while(paths[i] != null){
            if(!(j == selected)){
                gf.writePathTXT(paths[i], paths[i+1]);
                gf.writeTimeStamp(paths[i]);
            }
            i += 2;
            j++;
        }
    }

    public void cancelButton(){
        Stage stage = (Stage) pathList.getScene().getWindow();
        stage.close();
    }

}
