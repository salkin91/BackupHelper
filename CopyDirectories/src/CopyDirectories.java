

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class CopyDirectories  {

    public static void main(String[] args) {

        String user = System.getProperty("user.home");
        String projectFolder = "BackupHelper" + File.separator + "bin";
        String[] paths = new String[50];
        CopyDirectories cd = new CopyDirectories();
        DateFormat dateFormat = new SimpleDateFormat("yyMMdd-HH.mm");
        Date date = new Date();

        try{
            paths = cd.readPaths(user, projectFolder);
        }catch(IOException e){
            e.printStackTrace();
        }

        File timeStampFile = new File(System.getProperty("user.home") + File.separator + projectFolder + File.separator + "timestamps.txt");
        Long[] oldTimestamps = new Long[paths.length/2];
        Long[] newTimestamps = new Long[paths.length/2];
        try{
            oldTimestamps = cd.readOldTimeStamps(projectFolder);
            newTimestamps = cd.readNewTimeStamps(paths);
            cd.deleteTimestampFile(timeStampFile);
        }catch (IOException e){
            e.printStackTrace();
        }


        int j = 0;
        int i = 0;
        while( j < paths.length && paths[j] != null) {
            File source = new File(paths[j]);
            File target = new File(paths[j + 1] + File.separator + dateFormat.format(date));
            try{
                cd.writeTimeStamp(newTimestamps[i], timeStampFile);
            }catch (IOException e){
                e.printStackTrace();
            }
            if(oldTimestamps[i] < newTimestamps[i]){
                try {
                    Files.createDirectory(target.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    cd.copyDirectory(source, target);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            i++;
            j += 2;
        }
    }

    private void copyDirectory(File source, File target) throws IOException {
        if (source.isDirectory()) {
            if (!target.exists()) {
                target.mkdir();
            }
            String[] sourceChild = source.list();
            for (int i = 0; i < sourceChild.length; i++) {
                copyDirectory(new File(source, sourceChild[i]),
                        new File(target, sourceChild[i]));
            }
        } else {
            InputStream in = new FileInputStream(source);
            OutputStream out = new FileOutputStream(target);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }

    private void deleteTimestampFile(File timeStampFile) throws IOException {
        if(timeStampFile.exists()){
            timeStampFile.delete();
        }
    }

    private String[] readPaths(String user, String projectFolder) throws IOException {
        String[] paths = new String[50];
        File txt = txt = new File(user + File.separator +
                projectFolder + File.separator + "paths.txt");
        BufferedReader br = new BufferedReader(new FileReader(txt));
        String line = null;
        int i = 0;
        while ((line = br.readLine()) != null) {
            paths[i] = line;
            i++;
        }
        br.close();
        return paths;
    }

    private void writeTimeStamp(Long lastModified, File timeStamp) throws IOException{
        if(!timeStamp.exists()){
            Files.createFile(timeStamp.toPath());
        }
        List<String> lines = Arrays.asList(lastModified.toString());
        Files.write(timeStamp.toPath(), lines, StandardOpenOption.APPEND);
    }
    private Long[] readOldTimeStamps(String projectFolder) throws IOException{
        Long[] ts = new Long[50];
        File timeStamp = new File(System.getProperty("user.home") + File.separator + projectFolder + File.separator + "timestamps.txt");
        BufferedReader br = new BufferedReader(new FileReader(timeStamp));
        String line = null;
        int i = 0;
        while ((line = br.readLine()) != null) {
            ts[i] = Long.parseLong(line);
            i++;
        }
        br.close();
        return ts;
    }

    private Long[] readNewTimeStamps(String[] paths){
        Long[] timeStamps = new Long[25];
        Long tmp;
        int j = 0;
        for(int i = 0; i < paths.length; i += 2){
            File[] files = new File(paths[i]).listFiles();
            tmp = files[0].lastModified();
            for(File file : files){
                if(tmp < file.lastModified()){
                    tmp = file.lastModified();
                }
            }
            timeStamps[j] = tmp;
            j++;
        }
        return timeStamps;

    }
}
