package sample;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Niklas on 2016-05-25.
 */
public class GenerateFiles {
    String user = System.getProperty("user.home");
    String projectFolder = "BackUpHelper";
    File txt;
    public void generateFiles(String source, String target) throws IOException{
        makeProjectFolder();
        makeRunFile();
        writePathTXT(source, target);
        makeJavaFile();
        readFile();
    }

    private void makeProjectFolder() throws IOException{

        File fileFolder = new File(user + File.separator + projectFolder);
        if(!fileFolder.exists()){
            System.out.println(fileFolder.toString());
            Files.createDirectory((fileFolder).toPath());
        }
    }

    private void makeRunFile() throws IOException{

        File run = new File(user+ File.separator + projectFolder + File.separator + "run.bat");
        if(!run.exists()){
            List<String> lines = Arrays.asList("@echo off","javac CopyDirectories.java"
                    , "javaw CopyDirectories", "exit");
            Files.createFile(run.toPath());
            Files.write(run.toPath(), lines, Charset.forName("UTF-8"));
        }

    }

    private void makeJavaFile() throws IOException{
        System.out.println(System.getProperty("user.dir"));
        File javaFile = new File(user + File.separator + projectFolder +File.separator + "CopyDirectories.java");
        if(!javaFile.exists()) {
               List<String> lines = Arrays.asList("import java.io.*;", "import java.nio.file.Files;", "import java.text.DateFormat;", "import java.text.SimpleDateFormat;",
                       "import java.util.Date;", "public class CopyDirectories  {", "    public static void main(String[] args) {",
                       "        String user = System.getProperty(\"user.home\");",
                       "        String projectFolder = \"BackUpHelper\";", "        File txt;",
                       "        String[] paths = new String[50];", "        CopyDirectories cd = new CopyDirectories();",
                       "        DateFormat dateFormat = new SimpleDateFormat(\"yyMMdd-HH.mm\");",
                       "        Date date = new Date();",
                       "        txt = new File(user + File.separator +", "                projectFolder + File.separator + \"paths.txt\");",
                       "        try{", "            BufferedReader br = new BufferedReader(new FileReader(txt));",
                       "         String line = null;", "            int i = 0;", "            while ((line = br.readLine()) != null) {",
                       "                paths[i] = line;", "               i++;", "            }",
                       "            br.close();", "        }catch(IOException e){", "            e.printStackTrace();",
                       "        }", "        File source;", "        File target;", "        int j = 0;", "       while( j < paths.length && paths[j] != null) {",
                       "            source = new File(paths[j]);", "            target = new File(paths[j + 1] + File.separator + dateFormat.format(date));",
                       "            try {", "                Files.createDirectory(target.toPath());", "            } catch (IOException e) {",
                       "                e.printStackTrace();", "            }", "            try {",
                       "                cd.copyDirectory(source, target);", "            } catch (IOException e) {",
                       "                e.printStackTrace();", "            }", "            j += 2;", "        }", "    }",
                       "    private void copyDirectory(File source, File target) throws IOException {",
                       "        if (source.isDirectory()) {", "            if (!target.exists()) {",
                       "                target.mkdir();", "            }", "            String[] sourceChild = source.list();",
                       "            for (int i = 0; i < sourceChild.length; i++) {", "                System.out.println(sourceChild[i]);",
                       "                copyDirectory(new File(source, sourceChild[i]),", "                        new File(target, sourceChild[i]));",
                       "            }", "        } else {", "            InputStream in = new FileInputStream(source);",
                       "            OutputStream out = new FileOutputStream(target);", "            byte[] buf = new byte[1024];",
                       "            int len;", "            while ((len = in.read(buf)) > 0) {", "                out.write(buf, 0, len);",
                       "            }", "            in.close();", "            out.close();", "        }",
                       "    }", "}");
            Files.write(javaFile.toPath(), lines, Charset.forName("UTF-8"));
        }
    }

    private void writePathTXT(String source, String target) throws IOException{
         txt = new File(user + File.separator +
                projectFolder + File.separator + "paths.txt");
        if(!txt.exists()){
            Files.createFile(txt.toPath());

        }
        List<String> lines = Arrays.asList(source.replace("\\", "\\\\"), target.replace("\\","\\\\"));
        Files.write(txt.toPath(), lines, StandardOpenOption.APPEND);
    }
    private void readFile() throws IOException{
    BufferedReader br = new BufferedReader(new FileReader(txt));

    String line = null;
    while ((line = br.readLine()) != null) {
        System.out.println(line);
    }

    br.close();
    }
}
