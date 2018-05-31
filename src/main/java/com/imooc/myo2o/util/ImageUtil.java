package com.imooc.myo2o.util;

import com.sun.jndi.toolkit.url.Uri;
import com.sun.jndi.toolkit.url.UrlUtil;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.util.UriUtils;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.SimpleTimeZone;

public class ImageUtil {
    private static String basePath=Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static final SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random r=new Random();
    public static String generateThumbnail(CommonsMultipartFile thumbnail,String targetAddr){
        String realFileName=getRandomFileName();
        String extension=getFileExtension(thumbnail);
        makeDirPath(targetAddr);
        String relativeAddr=targetAddr+realFileName+extension;
        File dest=new File(PathUtil.getImageBasePath()+relativeAddr);
        try{
            Thumbnails.of(thumbnail.getInputStream()).size(200,200)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(UriUtils.decode(basePath,"utf-8") + "/waterremark.png")),
                            0.25f).outputQuality(0.8f).toFile(dest);
        }catch (Exception e){
            e.printStackTrace();
        }
        return relativeAddr;
    }

    private static void makeDirPath(String targetAddr) {
        String realBasePath = PathUtil.getImageBasePath();
        File dirPath=new File(realBasePath);
        if(!dirPath.exists()){
            dirPath.mkdirs();
        }
    }

    /**
     * 获取输入文件流的扩展名
     * @param cFile
     * @return
     */
    private static String getFileExtension(CommonsMultipartFile cFile) {
        String originalFilename = cFile.getOriginalFilename();
        return originalFilename.substring(originalFilename.lastIndexOf("."));
    }

    /**
     * 生成随机文件名，当前年月日+五位随机数
     * @return
     */
    private static String getRandomFileName() {
        int randum=r.nextInt(89999)+10000 ;
        String nowTimeStr=sDateFormat.format(new Date());
        return nowTimeStr+randum;
    }


    public static void main(String[] args) throws IOException {
        Thumbnails.of(new File("G:/new.png")).size(200, 200)
                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(UriUtils.decode(basePath,"utf-8") + "/waterremark.png")),
                        0.25f).outputQuality(0.8f).toFile("G:/news.png");
    }
}
