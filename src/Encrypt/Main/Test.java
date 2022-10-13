package Encrypt.Main;

import Encrypt.Utils.Encryptor;

public class Test {
    public static void main(String[] args) {
        String src = "F:\\Test\\file";
        String des = "F:\\Test\\file\\abcEnc.txt";
        String des2 = "F:\\Test\\file\\abc2.txt";
        Encryptor.DESdecrypt("12345678",src);
    }
}
