package Encrypt;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.ZipOutputStream;

public class MainGUI extends JFrame {
    public static void main(String[] args) {
        new MainGUI();
    }

    private String msg = "\n使用说明:\n1,先点击选择文件按钮,在弹出窗口中找到并且选中文件,或者手动输入文件所在绝对路径;" +
            "\n2,输入密码，英文字母数字组合八位以上;" +
            "\n3,加密前可以自行勾选“备份原文件”，若选择备份，会在选择文件同目录下生成zip格式的备份文件;" +
            "\n    否则会被加密后的文件替代;" +
            "\n4,点击加密即可对文件进行加密，会在选中的文件同目录下生成加密文件;" +
            "\n    选择加密文件输入正确密码，点击解密即可以解密,若密码错误会导致解密结果乱码;";

    private Container container = getContentPane();
    private JLabel fileurlLable = new JLabel("请选择文件或者手动输入文件路径:");
    private JTextField fileurl = new JTextField();
    private JLabel pwdLable = new JLabel("请输入密码(英文字母或数字，八位以上！):");
    private JTextField pwd = new JTextField();
    private JLabel result = new JLabel("请选择文件，可点击-说明-按钮查看详细步骤");
    private  JButton selectBtn = new JButton("选择文件");
    private JButton encryptBtn = new JButton("加密");
    private JButton decryptBtn = new JButton("解密");
    private JButton cancelBtn = new JButton("清空");
    private JButton infoBtn = new JButton("说明");

    private JCheckBox checkBox = new JCheckBox("备份原文件");
    private boolean backup = false;

    public MainGUI(){
        setTitle("加解密文件");
        setBounds(600,200,700,300);
        container.setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init();
        setVisible(true);
    }

    private void init(){
        /*输入部分--Center*/
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(null);
        fileurlLable.setBounds(10, 20, 300, 25);
        pwdLable.setBounds(10, 60, 300, 25);
        result.setBounds(10,100,690,100);
        fieldPanel.add(fileurlLable);
        fieldPanel.add(pwdLable);
        fieldPanel.add(result);
        fileurl.setBounds(320, 20, 360, 25);
        pwd.setBounds(220, 60, 320, 25);
        selectBtn.setBounds(220,20,100,25);
        checkBox.setBounds(560,60,100,25);
        fieldPanel.add(checkBox);
        fieldPanel.add(selectBtn);
        fieldPanel.add(fileurl);
        fieldPanel.add(pwd);
        container.add(fieldPanel, "Center");

        /*按钮部分--South*/
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(encryptBtn);
        buttonPanel.add(decryptBtn);
        buttonPanel.add(cancelBtn);
        buttonPanel.add(infoBtn);
        container.add(buttonPanel, "South");
        listerner();
    }

    private void listerner() {
        selectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                jfc.showDialog(new JLabel(),"选择");
                File file = jfc.getSelectedFile();
                try {
                        fileurl.setText(file.getAbsolutePath());
                        result.setText("已选择：" + jfc.getSelectedFile().getName());
                }catch (Exception e1){
                    JOptionPane.showMessageDialog(null,"未选择文件"+e1);
                }
            }
        });

        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(checkBox.isSelected())
                    backup = true;
                if(!checkBox.isSelected()){
                    backup = false;
                }
            }
        });

        encryptBtn.addActionListener(new ActionListener() {
                                         @Override
                                         public void actionPerformed(ActionEvent e) {
                                             String input = fileurl.getText();
                                             if (null == input || input.trim().length() == 0||pwd.getText()==null||pwd.getText().trim().length()==0) {
                                                 JOptionPane.showMessageDialog(null, "路径以及密码不能为空");
                                             }
                                             else {
                                                 if(pwd.getText().trim().length()<8){
                                                     JOptionPane.showMessageDialog(null,"密码必须八位以上！");
                                                 }
                                                 try {
                                                     //  Block of code to try
                                                     if(backup==true){
                                                         File file = new File(input);
                                                         int index = input.lastIndexOf("\\");
                                                         String des = input.substring(0,index)+"\\"+file.getName()+".zip";
                                                         FileOutputStream fos = new FileOutputStream(des);
                                                         ZipOutputStream zos = new ZipOutputStream(fos);
                                                         zipCompress.zipFile(file,file.getName(),zos);
                                                         zos.close();
                                                         fos.close();
                                                         result.setText("已经备份压缩至："+des);
                                                     }
                                                     Encryptor.DESencrypt(pwd.getText(),input);
                                                     result.setText("已经加密文件："+input);

                                                 } catch (Exception exception) {
                                                     //  Block of code to handle errors
                                                     JOptionPane.showMessageDialog(null, "所选择文件不存在或者打开失败！"+exception);
                                                 }
                                             }
                                         }
                                     }
        );
        decryptBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = fileurl.getText();
                if (null == input || input.trim().length() == 0||pwd.getText()==null||pwd.getText().trim().length()==0) {
                    JOptionPane.showMessageDialog(null, "路径以及密码不能为空");
                }
                else {
                    if(pwd.getText().trim().length()<8){
                        JOptionPane.showMessageDialog(null,"密码必须八位以上！");
                    }
                    int key = Integer.parseInt(pwd.getText());
                    try {
                        //  Block of code to try
                        Encryptor.DESdecrypt(pwd.getText(),input);
                        result.setText("已经解密："+input);

                    } catch (Exception exception) {
                        //  Block of code to handle errors
                        JOptionPane.showMessageDialog(null, "所选择文件不存在或者并非加密文件,打开失败！！"+exception);
                    }
                }
            }
        });


        /** 清空输入信息 */
        cancelBtn.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        fileurl.setText("");
                        pwd.setText("");
                        result.setText("请选择文件，可点击-说明-按钮查看详细步骤");
                    }
                });

        //关于本项目的介绍页面：
        infoBtn.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog(null, "网络编程作业二\n可以对文件数据进行加解密，采用了Java的swing来创建交互界面;\n"+msg+"\n\n关于：\n网络201张进华\nemail:2006200014@e.gzhu.edu.cn\nGithub:\nhttps://github.com/myli0724/HomeWork04-14");
                    }
                }
        );
    }
}

