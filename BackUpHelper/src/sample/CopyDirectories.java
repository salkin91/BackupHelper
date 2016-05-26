package sample;

import java.io.*;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


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
        int j = 0;
        while( j < paths.length && paths[j] != null) {
            source = new File(paths[j]);
            target = new File(paths[j + 1] + File.separator + dateFormat.format(date));
            
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

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }
}
