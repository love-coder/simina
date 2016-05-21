package org.xidian.test;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/*  
 * 简易记事本  
 */  
public class MenuTest {  
    private Frame f;  
    private MenuBar menuBar;  
    private Menu menu;  
    private MenuItem openItem, saveItem, closeItem;  
    private TextArea textArea;  
  
    private FileDialog openDia, saveDia;// 保存文件的对话框  
  
    private File file;  
  
    MenuTest() {  
        init();  
    }  
  
    public void init() {  
        f = new Frame("my frame");// 初始化窗口  
        menuBar = new MenuBar();// 初始化菜单栏  
        menu = new Menu("操作选项");// 初始化文件菜单  
        openItem = new MenuItem("打开");// 初始化打开条目  
        saveItem = new MenuItem("保存");// 初始化保存条目  
        closeItem = new MenuItem("关闭");// 初始化关闭条目  
        
        textArea = new TextArea();// 初始化文本区域  
  
        openDia = new FileDialog(f, "我要打开", FileDialog.LOAD);// 打开，对话框的模式  
        saveDia = new FileDialog(f, "我要保存", FileDialog.SAVE);// 保存，对话框的模式  
  
        // 设置属性  
        f.setBounds(100, 100, 600, 600);  
  
        // 关联控件  
        f.setMenuBar(menuBar);  
        menuBar.add(menu);  
        menu.add(openItem);  
        menu.add(saveItem);  
        menu.add(closeItem);  
        f.add(textArea);  
  
        // 加载事件  
        myEvent();  
  
        // 设置显示  
        f.setVisible(true);  
    }  
  
    /*  
     * 时间处理方法  
     */  
    private void myEvent() {  
        // 设置窗体的关闭事件  
        f.addWindowListener(new WindowAdapter() {  
  
            @Override  
            public void windowClosing(WindowEvent e) {  
                System.exit(0);  
            }  
  
        });  
  
        // 设置菜单中的关闭事件  
        closeItem.addActionListener(new ActionListener() {  
  
            public void actionPerformed(ActionEvent e) {  
                System.exit(0);  
            }  
        });  
  
        // 设置菜单中打开事件  
        openItem.addActionListener(new ActionListener() {  
  
            @Override  
            public void actionPerformed(ActionEvent e) {  
                openDia.setVisible(true);// 设置打开对话框显示  
                // 获取目录  
                String dirPath = openDia.getDirectory();  
                // 获取文件名  
                String name = openDia.getFile();  
                // 点击对话栏的“取消”按钮，设定  
                if (dirPath == null || name == null) {  
                    return;  
                }  
                // 清空文本区域  
                textArea.setText("");  
  
                // 把获取到的文件封装到File对象中  
                file = new File(dirPath, name);  
  
                // 开始将文件中的内容读取到文本区域中  
                BufferedReader bfr = null;  
                try {  
                    bfr = new BufferedReader(new FileReader(file));// 建立读取流  
                    String line = "";  
                    while ((line = bfr.readLine()) != null) {  
                        textArea.append(line + "\r\n");  
                    }  
                } catch (FileNotFoundException e1) {  
                    new RuntimeException("文件不存在");  
                } catch (IOException e1) {  
                    new RuntimeException("读取文件失败");  
                } finally {  
                    if (bfr != null) {  
                        try {  
                            bfr.close();  
                        } catch (IOException e1) {  
                            new RuntimeException("关闭资源失败");  
                        }  
                    }  
                }  
            }  
        });  
  
        // 设置保存事件  
        saveItem.addActionListener(new ActionListener() {  
  
            @Override  
            public void actionPerformed(ActionEvent e) {  
                if (file == null) {  
                    saveDia.setVisible(true);// 设置保存对话框显示  
                    // 设置保存路径  
                    String dirPath = saveDia.getDirectory();// 得到目录  
                    String name = saveDia.getFile();// 得到文件  
  
                    // 点击取消按钮  
                    if (dirPath == null || name == null) {  
                        return;  
                    }  
                    file = new File(dirPath, name);  
                }  
  
                // 开始保存文件  
                BufferedWriter bfw = null;  
                try {  
                    bfw = new BufferedWriter(new FileWriter(file));// 建立写入流  
                    String text = textArea.getText();// 得到需要写入文件的内容  
                    bfw.write(text);// 写内容  
                    bfw.flush();// 刷新缓冲区  
                } catch (IOException e1) {  
                    new RuntimeException("写入文件失败");  
                } finally {  
                    if (bfw != null) {  
                        try {  
                            bfw.close();  
                        } catch (IOException e1) {  
                            new RuntimeException("关闭资源失败");  
                        }  
                    }  
                }  
            }  
        });  
    }  
  
    public static void main(String[] args) {  
        new MenuTest();  
    }  
}