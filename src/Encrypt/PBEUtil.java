package Encrypt;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.FileOutputStream;
import java.util.Random;

/*PBE加密算法：

 */
public class PBEUtil {
    public static String Encrypt(String password,String srcFile,String desFile){
        //由口令生成密钥
        try {
            //由口令生成密钥
            char[] pwd = password.toCharArray();
            PBEKeySpec pbks = new PBEKeySpec(pwd);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(pbks);
            //salt
            byte[] salt = new byte[8];
            Random r = new Random();
            r.nextBytes(salt);
            //初始化密码器
            Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
            PBEParameterSpec ps = new PBEParameterSpec(salt,1000);
            cipher.init(Cipher.ENCRYPT_MODE,key,ps);
            //获取明文,加密
            byte ptext[] = srcFile.getBytes("UTF-8");
            byte ctext[] = cipher.doFinal(ptext);
            FileOutputStream fos = new FileOutputStream(desFile);
            fos.write(salt);
            fos.write(ctext);
        }catch (Exception e){
            System.out.println("key generate failed!");
            e.printStackTrace();
        }
        return null;
    }
}
