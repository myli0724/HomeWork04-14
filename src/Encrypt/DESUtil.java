package Encrypt;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.security.Key;
import java.util.Base64;

/*
DES加密算法:
(String desFile) DESUtil.encryptFile(pwd,srcFile,desFile);
(String desFile) DESUtil.decryptFile(pwd,srcFile,DesFile);
 */
public class DESUtil {

    /*偏移变量，固定占8位字节,根据需要设置*/
    private final static String IV_PARAMETER = "12345678";
    /*加密算法-DES*/
    private static final String ALGORITHM = "DES";
    /* 加密/解密算法-工作模式-填充模式*/
    private static final String CIPER_ALOGRITHM = "DES/CBC/PKCS5Padding";
    /*默认编码-UTF-8*/
    private static final String CHARSET = "utf-8";

    /*生成key*/
    private static Key generateKey(String pwd){
        try {
            DESKeySpec dks = new DESKeySpec(pwd.getBytes(CHARSET));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            return keyFactory.generateSecret(dks);
        } catch (Exception e){
            System.out.println("generateKey failed!");
            e.printStackTrace();
        }
        return null;
    }

    /*DES加密字符串*/
    public static String encrypt(String pwd, String data){
        if(pwd == null||pwd.length()<8){
            throw new RuntimeException("key 长度不能小于八位！");
        }
        if(data == null){
            return null;
        }
        try{
            Key secretKey = generateKey(pwd);
            Cipher cipher = Cipher.getInstance(CIPER_ALOGRITHM);
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            cipher.init(Cipher.ENCRYPT_MODE,secretKey,iv);
            byte[] bytes = cipher.doFinal(data.getBytes(CHARSET));

            return new String(Base64.getEncoder().encode(bytes));
        } catch (Exception e){
            System.out.println(" encrypt the string failed!");
            e.printStackTrace();
        }
        return null;
    }
    /*DES加密文件*/
    public static String encryptFile(String pwd,String srcFile,String desFile){
        if(pwd==null||pwd.length()<8){
            throw new RuntimeException("key 长度不能小于八位！");
        }
        try{
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            Cipher cipher = Cipher.getInstance(CIPER_ALOGRITHM);
            cipher.init(Cipher.ENCRYPT_MODE,generateKey(pwd),iv);
            InputStream is = new FileInputStream(srcFile);
            OutputStream os = new FileOutputStream(desFile);
            CipherInputStream cis = new CipherInputStream(is,cipher);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = cis.read(buffer))>0){
                os.write(buffer,0,r);
            }
            cis.close();
            is.close();
            os.close();
            return desFile;
        }catch (Exception e){
            System.out.println("encryptFile failed!");
            e.printStackTrace();
        }
        return null;
    }

    /*DES-解密*/
    public static String decryptFile(String pwd,String src,String des){
        if(pwd==null||pwd.length()<8){
            throw new RuntimeException("key 不能小于八位!");
        }
        try {
            File file = new File(des);
            if(!file.exists()){
                file.getParentFile().mkdir();
                file.createNewFile();
            }
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            Cipher cipher = Cipher.getInstance(CIPER_ALOGRITHM);
            cipher.init(Cipher.DECRYPT_MODE,generateKey(pwd),iv);
            InputStream is = new FileInputStream(src);
            OutputStream os = new FileOutputStream(des);
            CipherOutputStream cos = new CipherOutputStream(os,cipher);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = is.read(buffer))>=0){
                cos.write(buffer,0,r);
            }
            cos.close();
            is.close();
            os.close();
            return des;
        }catch (Exception e){
            System.out.println("decryptFile failed!");
            e.printStackTrace();
        }
        return null;
    }
}
