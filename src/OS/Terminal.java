package OS;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Scanner;

//-------------------------------parser class------------------------------------

class Parser {
    private static String commandName;
    private static String[] args = null;

    //This method will divide the input into commandName and args
//where "input" is the string command entered by the user
    public  boolean parse(String input) {

        String[] array = input.split(" ");

        if (array.length == 1) {
            commandName = array[0];
        } else if (array.length > 1) {

            args = new String[array.length - 1]; //set number of args

            commandName = array[0];

            for (int i = 1; i < array.length; i++) {
                args[i - 1] = array[i];//first argument will be after the command
            }
        }
        return true;

    }

    public String getCommandName() {
        return commandName;
    }

    public String[] getArgs() {
        return args;
    }
    public void argsToNull(){
        try {
            Arrays.fill(args,null);
            args=null;
        }catch (Exception e){}

    }
    public static void printArgs() {
        if (args == null) {
            System.out.println("No argument");
        } else {
            for (int i = 0; i < args.length; i++) {
                System.out.println(args[i]);
            }
        }
    }
}

//---------------------------------------------------------------------
public class Terminal {
    public Parser parser=new Parser();
    private static String path="D:/OS"; //default path
    //Implement each command in a method, for example:
    //================================================
    public String echo(String s)
    {
        return s;
    }
    //==========================================
    public String pwd(){return path;}
    //-----------------------------------------------
    public void touch(String nPath) {
        Path p= Paths.get(nPath);
        //if it is a long path
        File file;
        if(nPath.contains(":")){

            file = new File(nPath);
        }
        else{
            file = new File(path +"/"+ nPath);
        }
        try {
            file.createNewFile();
        }catch (Exception e){
            System.out.println("Error : Command not found or invalid parameters are entered!");
        }
        }
//===============================================================================================
    public void cd(){
        path=System.getProperty("user.home");
    }
    public void cd(String in){
        if(in.equals("..")){
            File f=new File(path);
            File parent=f.getParentFile();
            path=parent.getPath();
        }
        else if(in.contains(":") &&!in.contains("."))
        {
            File f=new File(in);
            if(f.exists())
                {
                    path=in;
                }
            else{System.out.println("Invalid path");}


        }
        else {
            if (!in.contains(".")) {
                in = path + "/" + in;
                File f = new File(in);
                if (f.exists()) {
                    path = in;
                } else {
                    System.out.println("Invalid path");
                }
            }
            else if(!in.equals("")) {System.out.println("path should be a directory");}
        }
    }
//===============================================================================================
    //copy data from file to another and if second file not file create it and copy data
    public void cp(String fileName1,String pathFile2)
    {
        Path p1=Paths.get(path+"/"+fileName1);
        Path p2=Paths.get(pathFile2);
        try {
            Files.copy(p1, p2, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (Exception e)
        {
            System.out.println("Error : Command not found or invalid parameters are entered!");
        }
    }
//===============================================================================================
public String ls() {
    String[] filesNames;
    File file= new File(path);
    filesNames= file.list();
    Arrays.sort(filesNames);
    String list="";
    for(int i=0;i<filesNames.length;i++)
    {
        list+=filesNames[i]+"\n";
    }
    return list;
}

//===============================================================================================
    public String ls_r() {
        String[] filesNames;
        File file= new File(path);
        filesNames= file.list();
        Arrays.sort(filesNames);
        String list="";
        for(int i=filesNames.length-1;i>=0;i--)
        {
                list+=filesNames[i]+"\n";
        }
        return list;
    }
//===========================================================================================
    //we will copy dir1 into dir2
public void cp_r(String dir1, String dir2) {
        dir1=path+"/"+dir1;
        dir2=path+"/"+dir2;
        File f1=new File(dir1);
        File f2=new File(dir2);
    try {
        FileUtils.copyDirectory(f1,f2);
    } catch (IOException e) {
        System.out.println("Error : Command not found or invalid parameters are entered!");
    }

}
//===========================================================================================
    public String cat(String[] filename){

        String s;
        if(filename.length==1) {
            s = fileRead(filename[0], path);
        }
        else
        {
            s =fileRead(filename[0],path)+fileRead(filename[1],path);
        }
        return s;
    }

//=============================================================================================
public void rm(String fileName){
    try
    {
        File file= new File(path+"/"+fileName);
        if(file.delete())                      //returns Boolean value
        {
            //delete file
        }
        else
        {
            System.out.println("Error : Command not found or invalid parameters are entered!");
        }
    }
    catch(Exception e)
    {
        System.out.println("Error : Command not found or invalid parameters are entered!");
    }

}
//===============================================================================================
    public void mkdir(String[]args){
        for(int i=0;i<args.length;i++)
        {
            if(!args[i].contains(":"))
            {
                args[i]=path+"/"+args[i];
            }
        }
        for(int i=0;i<args.length;i++)
        {
            new File(args[i]).mkdirs();

        }
    }
//===============================================================================================
    public void rmdir(String in) {
        if(in.equals("*")) {
            File dir=new File(path);
            File[] files =dir.listFiles();
            for(File f : files){
                if (f.isDirectory())
                {
                    try {
                        if (Files.list(Paths.get(f.getPath())).findAny().isEmpty()) {
                            Files.delete(Paths.get(f.getPath()));
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println("Error : Command not found or invalid parameters are entered!");
                    }
                }
            }

        }
        else
        {
            if(!in.contains(":"))
            {
                in=path+"/"+in;
            }
            try {
                if (Files.list(Paths.get(in)).findAny().isEmpty()) {
                    Files.delete(Paths.get(in));
                }
            }
            catch (Exception e)
            {
                System.out.println("Error : Command not found or invalid parameters are entered!");
            }
        }
    }
//===============================================================================================
    //read data from file

    public static String fileRead(String fileName,String currentPath) {
        String s = "";
        try {
            FileReader f = new FileReader(currentPath+"/"+fileName);
            int ch;
            while ((ch = f.read()) != -1) {
                s += (char) ch; //int to char
            }
            f.close();
        } catch (Exception ex) {
            System.out.println("Error : Command not found or invalid parameters are entered!");
        }
        return s;
    }
//=======================================================================================
    //write data in a file
    public static void fileWrite(String fileName, String write,String currentPath) {

        try {
            FileWriter f = new FileWriter(currentPath +"/"+ fileName);
            f.write(write);
            f.close();
        } catch (Exception ex) {
            System.out.println("Error : Command not found or invalid parameters are entered!");
        }
    }
    //=====================================================================================


//This method will choose the suitable command method to be called
    public void chooseCommandAction(){

        String command=parser.getCommandName();
        String[] args=parser.getArgs();
        try {
            switch (command) {

                case "pwd":
                    if (args == null)
                        System.out.println(pwd());
                    else
                        System.out.println("Error : Command not found or invalid parameters are entered!");
                    break;
                case "echo":
                        System.out.println(echo(args[0]));
                    break;
                case "touch":
                    touch(args[0]);
                    break;
                case "cd":
                    if (args == null) {
                        cd();
                    } else {
                        cd(args[0]);
                    }
                    break;
                case "ls":
                    if (args != null)
                        System.out.println(ls_r());
                    else {
                        System.out.println(ls());
                    }
                    break;
                case "mkdir":
                    mkdir(args);
                    break;
                case "rmdir":
                    rmdir(args[0]);
                    break;
                case "cp":

                        if (args[0].equals("-r")) {
                            cp_r(args[1], args[2]);
                        } else {
                            cp(args[0], args[1]);
                        }

                    break;
                case "rm":
                    rm(args[0]);
                    break;
                case "cat":
                    System.out.println(cat(args));
                    break;
                case "exit":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Error : Command not found or invalid parameters are entered!");
                    break;
            }
        }
        catch (Exception e){
            System.out.println("Error : Command not found or invalid parameters are entered!");
        }
        parser.argsToNull();
    }
    public static void main(String[] args) throws IOException {
        Terminal t=new Terminal();
        Scanner s=new Scanner(System.in);
        String in;
        while(true){
            in= s.nextLine();
            t.parser.parse(in);
            t.chooseCommandAction();
        }
    }

}
