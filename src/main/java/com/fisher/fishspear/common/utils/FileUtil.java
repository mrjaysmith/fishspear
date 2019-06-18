package com.fisher.fishspear.common.utils;

import com.fisher.fishspear.common.exception.BizExceptionEnum;
import com.fisher.fishspear.common.exception.BussinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

public class FileUtil {

    private static Logger log = LoggerFactory.getLogger(FileUtil.class);

    private static final String FOLDER_SEPARATOR = "/";
    private static final char EXTENSION_SEPARATOR = '.';

    /**
     * NIO way
     */
    public static byte[] toByteArray(String filename) {

        File f = new File(filename);
        if (!f.exists()) {
            log.error("文件未找到！" + filename);
            throw new BussinessException(BizExceptionEnum.FILE_NOT_FOUND);
        }
        FileChannel channel = null;
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(f);
            channel = fs.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            while ((channel.read(byteBuffer)) > 0) {
                // do nothing
                // System.out.println("reading");
            }
            return byteBuffer.array();
        } catch (IOException e) {
            throw new BussinessException(BizExceptionEnum.FILE_READING_ERROR);
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                throw new BussinessException(BizExceptionEnum.FILE_READING_ERROR);
            }
            try {
                fs.close();
            } catch (IOException e) {
                throw new BussinessException(BizExceptionEnum.FILE_READING_ERROR);
            }
        }
    }

    /**
     * 将数据写入文件
     *
     * @param path
     * @param data
     */
    public static boolean saveToFile(String path, String data) {
        File file = new File(path);
        if (!file.exists())
            file.mkdirs();
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(path);
            bw = new BufferedWriter(fw);
            bw.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) bw.close();
                if (fw != null) fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * @param filePath 指定的文件路径
     * @param isNew    true：新建、false：不新建
     * @return 存在返回TRUE，不存在返回FALSE
     * @desc:判断指定路径是否存在，如果不存在，根据参数决定是否新建
     * @autor:chenssy
     * @date:2014年8月7日
     */
    public static boolean isExist(String filePath, boolean isNew) {
        File file = new File(filePath);
        if (!file.exists() && isNew) {
            return file.mkdirs();    //新建文件路径
        }
        return false;
    }

    /**
     * 获取指定文件的大小
     *
     * @param file
     * @return
     * @throws Exception
     * @author:chenssy
     * @date : 2016年4月30日 下午9:10:12
     */
    @SuppressWarnings("resource")
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
        }
        return size;
    }

    /**
     * 删除所有文件，包括文件夹
     *
     * @param dirpath
     * @author : chenssy
     * @date : 2016年5月23日 下午12:41:08
     */
    public void deleteAll(String dirpath) {
        File path = new File(dirpath);
        try {
            if (!path.exists())
                return;// 目录不存在退出
            if (path.isFile()) // 如果是文件删除
            {
                path.delete();
                return;
            }
            File[] files = path.listFiles();// 如果目录中有文件递归删除文件
            for (int i = 0; i < files.length; i++) {
                deleteAll(files[i].getAbsolutePath());
            }
            path.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制文件或者文件夹
     *
     * @param inputFile   源文件
     * @param outputFile  目的文件
     * @param isOverWrite 是否覆盖文件
     * @throws IOException
     * @author : chenssy
     * @date : 2016年5月23日 下午12:41:59
     */
    public static void copy(File inputFile, File outputFile, boolean isOverWrite)
            throws IOException {
        if (!inputFile.exists()) {
            throw new RuntimeException(inputFile.getPath() + "源目录不存在!");
        }
        copyPri(inputFile, outputFile, isOverWrite);
    }

    /**
     * 复制文件或者文件夹
     *
     * @param inputFile   源文件
     * @param outputFile  目的文件
     * @param isOverWrite 是否覆盖文件
     * @throws IOException
     * @author : chenssy
     * @date : 2016年5月23日 下午12:43:24
     */
    private static void copyPri(File inputFile, File outputFile, boolean isOverWrite) throws IOException {
        if (inputFile.isFile()) {        //文件
            copySimpleFile(inputFile, outputFile, isOverWrite);
        } else {
            if (!outputFile.exists()) {        //文件夹
                outputFile.mkdirs();
            }
            // 循环子文件夹
            for (File child : inputFile.listFiles()) {
                copy(child, new File(outputFile.getPath() + "/" + child.getName()), isOverWrite);
            }
        }
    }

    /**
     * 复制单个文件
     *
     * @param inputFile   源文件
     * @param outputFile  目的文件
     * @param isOverWrite 是否覆盖
     * @throws IOException
     * @author : chenssy
     * @date : 2016年5月23日 下午12:44:07
     */
    private static void copySimpleFile(File inputFile, File outputFile,
                                       boolean isOverWrite) throws IOException {
        if (outputFile.exists()) {
            if (isOverWrite) {        //可以覆盖
                if (!outputFile.delete()) {
                    throw new RuntimeException(outputFile.getPath() + "无法覆盖！");
                }
            } else {
                // 不允许覆盖
                return;
            }
        }
        InputStream in = new FileInputStream(inputFile);
        OutputStream out = new FileOutputStream(outputFile);
        byte[] buffer = new byte[1024];
        int read = 0;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        in.close();
        out.close();
    }

    /**
     * 获取文件的MD5
     *
     * @param file 文件
     * @return
     * @author : chenssy
     * @date : 2016年5月23日 下午12:50:38
     */
    public static String getFileMD5(File file) {
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    /**
     * 获取文件的后缀
     *
     * @param file 文件
     * @return
     * @author : chenssy
     * @date : 2016年5月23日 下午12:51:59
     */
    public static String getFileSuffix(String file) {
        if (file == null) {
            return null;
        }
        int extIndex = file.lastIndexOf(EXTENSION_SEPARATOR);
        if (extIndex == -1) {
            return null;
        }
        int folderIndex = file.lastIndexOf(FOLDER_SEPARATOR);
        if (folderIndex > extIndex) {
            return null;
        }
        return file.substring(extIndex + 1);
    }

    /**
     * 文件重命名
     *
     * @param oldPath 老文件
     * @param newPath 新文件
     * @author : chenssy
     * @date : 2016年5月23日 下午12:56:05
     */
    public boolean renameDir(String oldPath, String newPath) {
        File oldFile = new File(oldPath);// 文件或目录
        File newFile = new File(newPath);// 文件或目录

        return oldFile.renameTo(newFile);// 重命名
    }

    /**
     * 图片转base64
     *
     * @param img
     * @return
     */
    public static String encodeImageToBase64(BufferedImage img) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();
        BASE64Encoder encoder = new BASE64Encoder();
        String base64Img = encoder.encodeBuffer(bytes);
        base64Img = base64Img.replaceAll("\n", "").replaceAll("\r", "");
        return base64Img;
    }

    public static boolean generateImage(String imgStr, String imgFilePath) {// 对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) // 图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] bytes = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(bytes);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        String s = "iVBORw0KGgoAAAANSUhEUgAABwgAAAHgCAYAAACxVDlTAAAVTklEQVR42u3d5//X8xrA8d8fcM7NM4hsGdmj0tAeNLRUJGW0yGqoqKg453Scc2jY0hYVDZWmVLS1aFAqWoi0jFK/4+v+6XFdt3ic3/N6fJ//wPvu6/pen6I/nXJtMQAAAAAAAFAyFHkEAAAAAAAAEAgBAAAAAAAAgRAAAAAAAAAQCAEAAAAAAACBEAAAAAAAABAIAQAAAAAAAIEQAAAAAAAAEAgBAAAAAAAAgRAAAAAAAAAQCAEAAAAAAACBEAAAAAAAABAIAQAAAAAAQCAEAAAAAAAABEIAAAAAAABAIAQAAAAAAAAEQgAAAAAAAEAgBAAAAAAAAARCAAAAAAAAQCAEAAAAAAAABEIAAAAAAABAIAQAAAAAAAAEQgAAAAAAAEAgBAAAAAAAAIEQAAAAAAAAEAgBAAAAAAAAgRAAAAAAAAAQCAEAAAAAAACBEAAAAAAAABAIAQAAAAAAAIEQAAAAAAAAEAgBAAAAAAAAgRAAAAAAAAAQCAEAAAAAAACBEAAAAAAAAARCAAAAAAAAQCAEAAAAAAAABEIAAAAAAABAIAQAAAAAAAAEQgAAAAAAAEAgBAAAAAAAAARCAAAAAAAAQCAEAAAAAAAABEIAAAAAAABAIAQAAAAAAAAEQgAAAAAAABAIPQQAAAAAAAAIhAAAAAAAAIBACAAAAAAAAAiEAAAAAAAAgEAIAAAAAAAACIQAAAAAAACAQAgAAAAAAAAIhAAAAAAAAIBACAAAAAAAAAiEAAAAAAAAgEAIAAAAAAAACIQAAAAAAAAgEAIAAAAAAAACIQAAAAAAACAQAgAAAAAAAAIhAAAAAAAAIBACAAAAAAAAAiEAAAAAAAAgEAIAAAAAAAACIQAAAAAAACAQAgAAAAAAAAIhAAAAAAAAIBACAAAAAACAQAgAAAAAAAAIhAAAAAAAAIBACAAAAAAAAAiEAAAAAAAAgEAIAAAAAAAACIQAAAAAAACAQAgAAAAAAAAIhAAAAAAAAIBACAAAAAAAAAiEAAAAAAAAgEAIAAAAAAAAAiEAAAAAAAAgEAIAAAAAAAACIQAAAAAAACAQAgAAAAAAAAIhAAAAAAAAIBACAAAAAAAAAiEAAAAAAAAgEAIAAAAAAAACIQAAAAAAACAQAgAAAAAAAAIhAAAAAAAACIQAAAAAAACAQAgAAAAAAAAIhAAAAAAAAIBACAAAAAAAAPzeA+GgP5QqBgAAAAAAAEqGouKiomIAAAAAAACgZCga9MdSxQAAAAAAAEDJ4BuEAAAAAAAAUJK+QegRAAAAAAAAQCAEAAAAAAAABEIAAAAAAABAIAQAAAAAAAAEQgAAAAAAAEAgBAAAAAAAAARCAAAAAAAAQCAEAAAAAAAABEIAAAAAAABAIAQAAAAAAAAEQgAAAAAAAEAgBAAAAAAAAIEQAAAAAAAAEAgBAAAAAAAAgRAAAAAAAAAQCAEAAAAAAACBEAAAAAAAABAIAQAAAAAAAIEQAAAAAAAAEAgBAAAAAAAAgRAAAAAAAAAQCAEAAAAAAACBEAAAAAAAAARCAAAAAAAAQCAEAAAAAAAABEIAAAAAAABAIAQAAAAAAAAEQgAAAAAAAEAgBAAAAAAAAARCAAAAAAAAQCAEAAAAAAAABEIAAAAAAABAIAQAAAAAAAAEQgAAAAAAABAIAQAAAAAAAIEQAAAAAAAAEAgBAAAAAAAAgRAAAAAAAAAQCAEAAAAAAACBEAAAAAAAABAIAQAAAAAAAIEQAAAAAAAAEAgBAAAAAAAAgRAAAAAAAAAQCAEAAAAAAEAg9BAAAAAAAAAgEAIAAAAAAAACIQAAAAAAACAQAgAAAAAAAAIhAAAAAAAAIBACAAAAAAAAAiEAAAAAAAAgEAIAAAAAAAACIQAAAAAAACAQAgAAAAAAAAIhAAAAAAAAIBACAAAAAACAQAgAAAAAAAAIhAAAAAAAAIBACAAAAAAAAAiEAAAAAAAAgEAIAAAAAAAACIQAAAAAAACAQAgAAAAAAAAIhAAAAAAAAIBACAAAAAAAAAiEAAAAAAAAgEAIAAAAAAAAAiEAAAAAAAAgEAIAAAAAAAACIQAAAAAAACAQAgAAAAAAAAIhAAAAAAAAIBACAAAAAAAAAiEAAAAAAAAgEAIAAAAAAAACIQAAAAAAACAQAgAAAAAAAAIhAAAAAAAACIQAAAAAAACAQAgAAAAAAAAIhAAAAAAAAIBACAAAAAAAAAiEAAAAAAAAgEAIAAAAAAAACIQAAAAAAACAQAgAAAAAAAAIhAAAAAAAAIBACAAAAAAAAAiEAAAAAAAAIBACAAAAAAAAAiEAAAAAAAAgEAIAAAAAAAACIQAAAAAAACAQAgAAAAAAAAIhAAAAAAAAIBACAAAAAAAAAiEAAAAAAAAgEAIAAAAAAAACIQAAAAAAACAQAgAAAAAAgEDoEQAAAAAAAEAgBAAAAAAAAARCAAAAAAAAQCAEAAAAAAAABEIAAAAAAABAIAQAAAAAAAAEQgAAAAAAAEAgBAAAAAAAAARCAAAAAAAAQCAEAAAAAAAABEIAAAAAAABAIAQAAAAAAACBEAAAAAAAABAIAQAAAAAAAIEQAAAAAAAAEAgBAAAAAAAAgRAAAAAAAAAQCAEAAAAAAACBEAAAAAAAABAIAQAAAAAAAIEQAAAAAAAAEAgBAAAAAAAAgRAAAAAAAAAEQgAAAAAAAEAgBAAAAAAAAARCAAAAAAAAQCAEAAAAAAAABEIAAAAAAABAIAQAAAAAAAAEQgAAAAAAAEAgBAAAAAAAAARCAAAAAAAAQCAEAAAAAAAABEIAAAAAAAAQCAEAAAAAAACBEAAAAAAAABAIAQAAAAAAAIEQAAAAAAAAEAgBAAAAAACA30cgHPRUqWIAAAAAAACgZCgqLvwAAAAAAACAEsE/CAEAAAAAAKAk/YPQnVUAAAAAAAAoQd8g9AgAAAAAAAAgEAIAAAAAAAACIQAAAAAAACAQAgAAAAAAAAIhAAAAAAAAIBACAAAAAAAAAiEAAAAAAAAgEAIAAAAAAAACIQAAAAAAACAQAgAAAAAAAAIhAAAAAAAAIBACAAAAAACAQAgAAAAAAAAIhAAAAAAAAIBACAAAAAAAAAiEAAAAAAAAgEAIAAAAAAAACIQAAAAAAACAQAgAAAAAAAAIhAAAAAAAAIBACAAAAAAAAAiEAAAAAAAAgEAIAAAAAAAAAiEAAAAAAAAgEAIAAAAAAAACIQAAAAAAACAQAgAAAAAAAAIhAAAAAAAAIBACAAAAAAAAAiEAAAAAAAAgEAIAAAAAAAACIQAAAAAAACAQAgAAAAAAAAIhAAAAAAAACIQAAAAAAACAQAgAAAAAAAAIhAAAAAAAAIBACAAAAAAAAAiEAAAAAAAAgEAIAAAAAAAACIQAAAAAAACAQAgAAAAAAAAIhAAAAAAAAIBACAAAAAAAAAiEAAAAAAAAIBB6CAAAAAAAABAIAQAAAAAAAIEQAAAAAAAAEAgBAAAAAAAAgRAAAAAAAAAQCAEAAAAAAACBEAAAAAAAABAIAQAAAAAAAIEQAAAAAAAAEAgBAAAAAAAAgRAAAAAAAAAQCAEAAAAAAEAgBAAAAAAAAARCAAAAAAAAQCAEAAAAAAAABEIAAAAAAABAIAQAAAAAAAAEQgAAAAAAAEAgBAAAAAAAAARCAAAAAAAAQCAEAAAAAAAABEIAAAAAAABAIAQAAAAAAACBEAAAAAAAABAIAQAAAAAAAIEQAAAAAAAAEAgBAAAAAAAAgRAAAAAAAAAQCAEAAAAAAACBEAAAAAAAABAIAQAAAAAAAIEQAAAAAAAAEAgBAAAAAAAAgRAAAAAAAAAEQgAAAAAAAEAgBAAAAAAAAARCAAAAAAAAQCAEAAAAAAAABEIAAAAAAABAIAQAAAAAAAAEQgAAAAAAAEAgBAAAAAAAAARCAAAAAAAAQCAEAAAAAAAABEIAAAAAAAAQCAEAAAAAAACBEAAAAAAAABAIAQAAAAAAAIEQAAAAAAAAEAgBAAAAAAAAgRAAAAAAAAAQCAEAAAAAAACBEAAAAAAAABAIAQAAAAAAAIEQAAAAAAAAEAgBAAAAAABAIPQIAAAAAAAAIBACAAAAAAAAAiEAAAAAAAAgEAIAAAAAAAACIQAAAAAAACAQAgAAAAAAAAIhAAAAAAAAIBACAAAAAAAAAiEAAAAAAAAgEAIAAAAAAAACIQAAAAAAACAQAgAAAAAAgEAIAAAAAAAACIQAAAAAAACAQAgAAAAAAAAIhAAAAAAAAIBACAAAAAAAAAiEAAAAAAAAgEAIAAAAAAAACIQAAAAAAACAQAgAAAAAAAAIhAAAAAAAAIBACAAAAAAAAAIhAAAAAAAAIBACAAAAAAAAAiEAAAAAAAAgEAIAAAAAAAACIQAAAAAAACAQAgAAAAAAAAIhAAAAAAAAIBACAAAAAAAAAiEAAAAAAAAgEAIAAAAAAAACIQAAAAAAAAiEAAAAAAAAgEAIAAAAAAAACIQAAAAAAACAQAgAAAAAAAAIhAAAAAAAAIBACAAAAAAAAAiEAAAAAAAAgEAIAAAAAAAACIQAAAAAAACAQAgAAAAAAAAIhAAAAAAAACAQAgAAAAAAAAIhAAAAAAAAIBACAAAAAAAAAiEAAAAAAAAgEAIAAAAAAAACIQAAAAAAACAQAgAAAAAAAAIhAAAAAAAAIBACAAAAAAAAAiEAAAAAAAAgEAIAAAAAAAACIQAAAAAAAAiEAAAAAAAAgEAIAAAAAAAACIQA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";
        generateImage(s, "C:\\Users\\wb\\Desktop\\test.png");
    }
}