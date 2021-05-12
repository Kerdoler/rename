package com.kaldorei;

import com.kaldorei.utils.FileUtil;
import com.kaldorei.utils.PhotoExifUtil;
import org.apache.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 入口方法
 *
 * @author kerdoler_li
 */
public class Main {

    private Logger logger = Logger.getLogger(Main.class);

//    /**
//     * 准备修改文件所在的文件夹的完整路径
//     */
//    private static String FOLDER_PATH = "D:\\111";
//
//    /**
//     * 重命名的文件名日期格式
//     */
//    private static String RENAME_FORMAT = "yyyy-MM-dd hh.mm.ss";


    /**
     * 递归遍历文件夹修改文件名称
     */
    private void fileRename(String folderPath) {

        logger.info("----------");

        // 根据文件夹完整路径创建文件夹File对象
        File folder = new File(folderPath);
        logger.info("开始遍历文件夹:" + folder.getAbsolutePath());

        // 该目录如果存在
        if (folder.exists()) {

            // 读取目录下的文件
            File[] fileArray = folder.listFiles();

            if (fileArray == null || fileArray.length == 0) {
                logger.info("文件夹" + folder + "为空");
            } else {
                // 遍历文件夹下的文件
                for (File file : fileArray) {
                    logger.info("-");

                    if (file.isDirectory()) {
                        // 如果file对象是文件夹，则继续递归遍历

                        logger.info("检测到文件夹:" + file.getAbsolutePath() + "，继续递归遍历");
                        fileRename(file.getAbsolutePath());
                    } else {
                        // 如果file对象是文件，则开始重命名

                        // 当前文件所在路径
                        File currentFolderPath = file.getParentFile();

                        // 当前文件名
                        String currentFileName = file.getName();

                        // 当前文件后缀名
                        String fileSuffix = "";
                        if (currentFileName.lastIndexOf(".") != -1) {
                            fileSuffix = currentFileName.substring(currentFileName.lastIndexOf("."), currentFileName.length());
                        }

                        // 重命名后的文件名
                        String renameFileName = null;

                        // 文件的创建时间
                        Date fileCreateDate = null;

                        logger.info("即将修改文件：" + currentFolderPath + "\\" + currentFileName);
                        try {
                            // 1.优先通过EXIF获取拍照时间
                            fileCreateDate = PhotoExifUtil.getPhotoExif(currentFolderPath + "\\" + currentFileName);

                            // 2.如果无法通过EXIF获取拍照时间，则通过获取windows的文件修改时间进行文件重命名
                            if (fileCreateDate == null) {
                                fileCreateDate = FileUtil.getFileCreateInfo(currentFolderPath + "\\" + currentFileName);
                            }

                            // 3.根据文件创建时间定义修改后的文件名
                            if (fileCreateDate != null) {
                                renameFileName = new SimpleDateFormat(FileUtil.loadProperty("RENAME_FORMAT")).format(fileCreateDate);
                                // 文件所在文件夹路径 + 新文件名 + 后缀
                                File renameFile = new File(currentFolderPath + "\\" + renameFileName + fileSuffix);
                                // 重命名
                                file.renameTo(renameFile);
                                logger.info("修改后的文件路径+文件名：" + renameFile);
                            } else {
                                logger.info("文件名未修改，仍为：" + currentFileName);
                            }
                        } catch (Exception e) {
                            logger.error("文件名未修改，仍为：" + currentFileName, e);
                        }
                    }
                }
            }
        } else {
            logger.info("文件所在路径不存在!");
        }
    }


    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.fileRename(FileUtil.loadProperty("FOLDER_PATH"));
    }

}
