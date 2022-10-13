package Encrypt.Utils;

import Encrypt.threads.MultiThread;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Encryptor {

    private static BlockingQueue<String> waiting = new ArrayBlockingQueue<String>(1024);

    public static String DESencrypt(String pwd,String src){
        try {
            findFolder(src);
            /*thread*/
            MultiThread.DESEncThreads(waiting, pwd);
            return src;
        }catch (Exception e){
            System.out.println("打开文件失败或者加密过程出错！请检查路径");
            throw new RuntimeException("打开文件失败或者加密过程出错！请检查路径!\n"+e);
        }
    }

    public static String DESdecrypt(String pwd,String src){
        try {
            findFolder(src);
            MultiThread.DESdecThreads(waiting, pwd);
            return src;
        }catch (Exception e){
            System.out.println("打开文件失败或者解密过程出错！请检查路径");
            throw new RuntimeException("打开文件失败或者解密过程出错！请检查路径!\n"+e);
        }
    }


    /*递归遍历文件夹，将全部文件的路径存入waiting的阻塞队列里，再由MultiThread的消费者多线程模型由队列里取出进行多线程处理；*/
    private static void findFolder(String src){
        try {

            File file = new File(src);
            if(!file.exists()){
                System.out.println("open file failed!");
                throw new RuntimeException("open file failed!");
            }
            if(file.isHidden()){
                System.out.println("file is hidden!");
                throw new RuntimeException("file is hidden!");
            }
            if(file.isDirectory()){
                File[] children = file.listFiles();
                for(File childFile:children){
                    findFolder(childFile.getAbsolutePath());
                }
                return;
            }
            waiting.put(src);
        }catch (Exception e){
            System.out.println("遍历文件夹失败！");
            throw new RuntimeException("遍历文件夹失败\n"+e);
        }
    }
}
