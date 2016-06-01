package sample;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Niklas on 2016-05-25.
 */
public class GenerateFiles {
    String user = System.getProperty("user.home");
    String os = System.getProperty("os.name");
    String projectFolder = "BackupHelper";
    String projectSubFolder = "bin";
    File txt;
    public void generateFiles(String source, String target) throws IOException{
        makeProjectFolder();
        if(os.contains("Win")){
            makeRunFileWin();
        }
        else makeRunFileUnix();

        writePathTXT(source, target);
        writeTimeStamp(source);
        try{
            exportResource("CopyDirectories.jar");

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void makeProjectFolder() throws IOException{

        File fileFolder = new File(user + File.separator + projectFolder);
        if(!fileFolder.exists()){
            Files.createDirectory((fileFolder).toPath());
        }
        File fileSubFolder = new File(fileFolder.toString() + File.separator + projectSubFolder);
        if(!fileSubFolder.exists()){
            Files.createDirectories(fileSubFolder.toPath());
        }
    }

    private void makeRunFileWin() throws IOException{

        File run = new File(user+ File.separator + projectFolder + File.separator + "run.bat");
        if(!run.exists()){
            List<String> lines = Arrays.asList("@echo off",
                    ":start","java -jar bin"+ File.separator + "CopyDirectories.jar",
                    "TIMEOUT /T 60 /NOBREAK", "goto start");
            Files.createFile(run.toPath());
            Files.write(run.toPath(), lines, Charset.forName("UTF-8"));
        }

    }
    private void makeRunFileUnix() throws IOException{
        File run = new File(user+ File.separator + projectFolder + File.separator + "run.sh");
        if(!run.exists()){
            List<String> lines = Arrays.asList("#!/bin/bash",
                    "while true", "do", "java -jar bin"+ File.separator + "CopyDirectories.jar", "sleep 60", "done");
            Files.createFile(run.toPath());
            Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
            perms.add(PosixFilePermission.OWNER_EXECUTE);
            perms.add(PosixFilePermission.OWNER_WRITE);
            perms.add(PosixFilePermission.OWNER_READ);
            perms.add(PosixFilePermission.OTHERS_EXECUTE);
            perms.add(PosixFilePermission.OTHERS_WRITE);
            perms.add(PosixFilePermission.OTHERS_READ);
            perms.add(PosixFilePermission.GROUP_READ);
            perms.add(PosixFilePermission.GROUP_WRITE);
            perms.add(PosixFilePermission.GROUP_EXECUTE);
            Files.setPosixFilePermissions(run.toPath(), perms);
            Files.write(run.toPath(), lines, Charset.forName("UTF-8"));
        }
    }

    private void exportResource(String resourceName) throws Exception {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        try {
            stream = Main.class.getResourceAsStream(resourceName);
            if(stream == null) {
                throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            resStreamOut = new FileOutputStream(System.getProperty("user.home") + File.separator +
                    projectFolder + File.separator + projectSubFolder + File.separator + resourceName);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            stream.close();
            resStreamOut.close();
        }
    }

    public void writePathTXT(String source, String target) throws IOException{
         txt = new File(user + File.separator +
                projectFolder + File.separator + projectSubFolder + File.separator + "paths.txt");
        if(!txt.exists()){
            Files.createFile(txt.toPath());

        }
        List<String> lines = Arrays.asList(source.replace("\\", "\\\\"), target.replace("\\","\\\\"));
        Files.write(txt.toPath(), lines, StandardOpenOption.APPEND);
    }

    public void writeTimeStamp(String source) throws IOException{
        File sourceFile = new File(source);
        File timeStamps = new File(user + File.separator + projectFolder +
                File.separator + projectSubFolder + File.separator + "timestamps.txt");
        if(!timeStamps.exists()){
            Files.createFile(timeStamps.toPath());
        }
        Long timestamp = sourceFile.lastModified();
        Files.write(timeStamps.toPath(), (timestamp.toString() + "\n").getBytes(), StandardOpenOption.APPEND);
    }
}
