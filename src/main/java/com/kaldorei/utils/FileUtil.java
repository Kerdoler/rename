package com.kaldorei.utils;

import org.apache.log4j.Logger;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Properties;

/**
 * 文件工具类
 *
 * @author kerdoler_li
 */
public class FileUtil {

    private static Logger logger = Logger.getLogger(FileUtil.class);

    /**
     * 获取文件的创建时间
     *
     * @param filePath
     * @return
     */
    public static Date getFileCreateInfo(String filePath) throws Exception {

        File file = new File(filePath);
        Path path = file.toPath();
        BasicFileAttributes basicFileInfo = Files.readAttributes(path, BasicFileAttributes.class);

        // 文件的创建时间
        Date fileCreateDate = null;

        // 创建时间
//        Instant instantcreationTime = basicFileInfo.creationTime().toInstant();
//        String formatinstantcreationTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss").withZone(ZoneId.systemDefault()).format(instantcreationTime);
//        logger.info("文件创建时间：" + formatinstantcreationTime);

        // 最后修改时间
        Instant lastModifiedTimeInstant = basicFileInfo.lastModifiedTime().toInstant();
        if (lastModifiedTimeInstant != null) {
            fileCreateDate = new Date(lastModifiedTimeInstant.toEpochMilli());
            logger.info("文件最后修改时间：" + new SimpleDateFormat("yyyy:MM:dd hh:mm:ss").format(fileCreateDate));
        }

        return fileCreateDate;
    }

    /**
     * 读取配置文件
     * @param key
     * @return
     */
    public static String loadProperty(String key) {
        Properties properties = new Properties();
        String value = "";
        try {
            properties = PropertiesLoaderUtils.loadAllProperties("props.properties");
            value = new String(properties.getProperty(key).getBytes("utf-8"),"utf-8");
        } catch (Exception e) {
            logger.error("读取配置文件异常", e);
        }
        return value;
    }

    public static void main(String[] args) {
        System.out.println(loadProperty("FOLDER_PATH"));
    }
}
