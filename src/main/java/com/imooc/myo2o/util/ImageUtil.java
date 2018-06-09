package com.imooc.myo2o.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.web.util.UriUtils;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.SimpleTimeZone;

public class ImageUtil {
    private static String basePath;
    private static String base;
    private static String basePath1;
    static {
        try {
            basePath = UriUtils.decode(Thread.currentThread().getContextClassLoader().getResource("").getPath(),"utf-8");
            base=basePath.substring(1,basePath.length()-1);
            basePath1 = UriUtils.decode("G:\\BaiduNetdiskDownload\\02JavaEE中级\\pengwei-o2o\\myo2os\\src\\test\\resources","utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static final SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random r=new Random();
    public static String generateThumbnail(InputStream thumbnailInputStream,String fileName, String targetAddr){
        String realFileName = getRandomFileName();
        String extension = getFileExtension(fileName);
        makeDirPath(targetAddr);
        String relativeAddr  =targetAddr +realFileName + extension;
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        try {
            Thumbnails.of(thumbnailInputStream).size(200, 200)
                    .watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(base + "/waterremark.png")),0.25f)
                    .outputQuality(0.8f).toFile(dest);
        }catch (IOException e) {
            // TODO: handle exceptionr
            e.printStackTrace();
        }
        return relativeAddr;
    }

    /**
     * 创建目标路径所涉及到的目录，即/home/work/o2o/xxx.jpg,
     * 那么 home work o2o 这三个文件夹都得自动创建
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        // TODO Auto-generated method stub
        String realFileParentPath = PathUtil.getImgBasePath()+targetAddr;
        File dirPath = new File(realFileParentPath);
        if(!dirPath.exists()) {
            dirPath.mkdirs();
        }
    }

    /**
     * 获取输入文件流的扩展名
     * @param cFile
     * @return
     */
    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 生成随机文件名，当前年月日+五位随机数
     * @return
     */
    public static String getRandomFileName() {
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
