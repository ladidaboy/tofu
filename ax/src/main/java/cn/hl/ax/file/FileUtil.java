package cn.hl.ax.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {
    public static void createFile(File file) throws IOException {
        if (!file.exists()) {
            String _folder_ = file.getAbsolutePath();
            _folder_.replaceAll("\\\\", "/");
            _folder_ = _folder_.substring(0, _folder_.lastIndexOf("/"));
            File folder = new File(_folder_);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            file.createNewFile();
        }
    }

    /**
     * 读取文件
     */
    public static String readFile(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (FileReader reader = new FileReader(file); BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }
        return content.toString();
    }

    /**
     * 复制文件
     */
    public static void copyFile(File file, String newPath) throws Exception {
        if (file.exists()) {
            InputStream input = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(newPath + file.getName());
            byte[] buffer = new byte[8192];
            int length = 0;
            while ((length = input.read(buffer)) != -1) {
                fos.write(buffer, 0, length);
            }
            fos.flush();
            fos.close();
            input.close();
        }
    }

    /**
     * 追加写文件(UTF-8)
     */
    public static void append2File(String fileName, String content) throws Exception {
        File file = new File(fileName);
        createFile(file);
        FileOutputStream fos = new FileOutputStream(file, true);
        fos.write(content.getBytes("UTF-8"));
        fos.flush();
        fos.close();
        /*FileWriter fw = new FileWriter(file, true);
        fw.write(content);
        fw.close();*/
    }

    /**
     * 写文件
     */
    public static void write2File(String fileName, String content) throws Exception {
        File file = new File(fileName);
        createFile(file);
        /*FileOutputStream fos = new FileOutputStream(file);
        fos.write(content.getBytes("UTF-8"));
        fos.flush();
        fos.close();*/
        FileWriter fw = new FileWriter(file);
        fw.write(content);
        fw.close();
    }

    public static void main(String[] args) {
        try {
            String filename = "_TestFileUtil.txt";
            write2File(filename, "WriteMode写入模式\r\n");
            append2File(filename, "AppendMode追加模式\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
