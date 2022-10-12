package Encrypt;

public class Test {
    public static void main(String[] args) {
        String src = "F:\\Test\\file\\abc.txt";
        String des = "F:\\Test\\file\\abcEnc.txt";
        String des2 = "F:\\Test\\file\\abc2.txt";
        System.out.println(DESUtil.encryptFile("12345678",src,des));
        System.out.println(DESUtil.decryptFile("12345678",des,des2));
    }
}
