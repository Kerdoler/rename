package com.kaldorei.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.kaldorei.Main;
import org.apache.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 获取照片信息的工具类
 *
 * @author kerdoler_li
 */
public class PhotoExifUtil {

    private static Logger logger = Logger.getLogger(PhotoExifUtil.class);

    /**
     * 通过照片的EXIF信息获取拍摄时间
     *
     * @param filePath
     * @throws Exception
     */
    public static Date getPhotoExif(String filePath) throws Exception {
        File file = new File(filePath);

        // 照片的拍摄时间
        Date photoCreateDate = null;

        // 照片文件的元信息
        Metadata metadata = ImageMetadataReader.readMetadata(file);

        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                if ("Date/Time Original".equals(tag.getTagName())) {
                    logger.info("照片EXIF信息标签: " + tag.getTagName() + " - " + tag.getDescription());
                    photoCreateDate = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss").parse(tag.getDescription());
                    return photoCreateDate;
                }
            }
        }

        return photoCreateDate;
    }

}