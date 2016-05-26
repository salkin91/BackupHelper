package sample;

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
        String projectFolder = "BackUpHelper";
        File txt;
        String[] paths = new String[50];
        CopyDirectories cd = new CopyDirectories();
        DateFormat dateFormat = new SimpleDateFormat("yyMMdd-HH.mm");
        Date date = new Date();
        txt = new File(user + File.separator +
                projectFolder + File.separator + "paths.txt");

        try{
            BufferedReader br = new BufferedReader(new FileReader(txt));
            String line = null;
            int i = 0;
            while ((line = br.readLine()) != null) {
                paths[i] = line;
                i++;
            }
            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        File source;
        File target;
        Long[] timestamps = new Long[paths.length/2];
        try{
            timestamps = cd.readTimeStamp();
        }catch (IOException e){
            e.printStackTrace();
        }
        int j = 0;
        int i = 0;

        File timeStampFile = new File(System.getProperty("user.home") + File.separator + "BackUpHelper" + File.separator + "timestamps.txt");
        if(timeStampFile.exists()){
            timeStampFile.delete();
        }
        while( j < paths.length && paths[j] != null) {
            source = new File(paths[j]);
            target = new File(paths[j + 1] + File.separator + dateFormat.format(date));
            try{
                cd.writeTimeStamp(source, timeStampFile);
            }catch (IOException e){
                e.printStackTrace();
            }
            if(timestamps[i] != source.lastModified()){
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
                System.out.println(sourceChild[i]);
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
    private void writeTimeStamp(File source, File timeStamp) throws IOException{
        if(!timeStamp.exists()){
            Files.createFile(timeStamp.toPath());
        }
        Long lastModified = source.lastModified();
        lastModified.toString();
        List<String> lines = Arrays.asList(lastModified.toString());
        Files.write(timeStamp.toPath(), lines, StandardOpenOption.APPEND);
    }
    private Long[] readTimeStamp() throws IOException{
        Long[] ts = new Long[50];
        File timeStamp = new File(System.getProperty("user.home") + File.separator + "BackUpHelper" + File.separator + "timestamps.txt");
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
}
