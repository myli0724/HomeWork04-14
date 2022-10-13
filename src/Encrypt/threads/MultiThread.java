package Encrypt.threads;

import Encrypt.Utils.DESUtil;

import java.io.File;
import java.util.concurrent.BlockingQueue;

/*
多线程-生产者消费者模型处理；
*/
public class MultiThread {
    public static void DESEncThreads(BlockingQueue<String> queue,String pwd){
//        Runnable producer = () -> {
//            while (true) {
//                try {
//                    Object object=new Object();
//                    queue.put(object);
//                    System.out.println("生产了:"+object.toString());
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        new Thread(producer).start();
//        new Thread(producer).start();

        Runnable consumer = () -> {
            while (!queue.isEmpty()) {
                try {
                    String file = queue.take();
                    String des = file.substring(0,file.lastIndexOf("."))+"-Enc"+file.substring(file.lastIndexOf("."));
                    DESUtil.encryptFile(pwd,file,des);
                    System.out.println("处理了文件:"+file);
                    File file1 = new File(file);
                    file1.delete();
                } catch (InterruptedException e) {
                    System.out.println("多线程处理文件失败！");
                    e.printStackTrace();
                    throw new RuntimeException("多线程处理文件失败！\n"+e);
                }
            }
        };
        //三个线程；
        new Thread(consumer).start();
        new Thread(consumer).start();
        new Thread(consumer).start();
    }

    public static void DESdecThreads(BlockingQueue<String> queue,String pwd){
        Runnable decConsumer = () -> {
            while (!queue.isEmpty()) {
                try {
                    String file = queue.take();
                    String des = file.substring(0,file.lastIndexOf(".")-4)+file.substring(file.lastIndexOf("."));
                    DESUtil.decryptFile(pwd,file,des);
                    System.out.println("解密了文件:"+file);
                    File file1 = new File(file);
                    file1.delete();
                } catch (InterruptedException e) {
                    System.out.println("多线程处理文件失败！");
                    e.printStackTrace();
                    throw new RuntimeException("多线程处理文件失败！\n"+e);
                }
            }
        };
        //三个线程；
        new Thread(decConsumer).start();
        new Thread(decConsumer).start();
        new Thread(decConsumer).start();
    }
}
