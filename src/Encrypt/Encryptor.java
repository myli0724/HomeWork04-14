package Encrypt;

import java.io.File;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Encryptor {

    private static BlockingQueue<String> waiting = new ArrayBlockingQueue<String>(1024);

    public static String DESencrypt(String pwd,String src){
        findFolder(src);
        /*thread*/
        MultiThread.DESEncThreads(waiting,pwd);
        return src;
    }

    public static String DESdecrypt(String pwd,String src){
        findFolder(src);
        MultiThread.DESdecThreads(waiting,pwd);
        return src;
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
            e.printStackTrace();
        }
    }
}
