package com.imooc.myo2o.util;

public class PathUtil {
    /*
     * 根据不同的操作系统，设置储存图片文件不同的根目录
     */
    private static String separator = System.getProperty("file.separator");
    public static String getImgBasePath(){
        String os = System.getProperty("os.name");
        String basePath="";
        if(os.toLowerCase().startsWith("win")){
            basePath = "D:/projectdev/image";
        }else{
            basePath = "/home/imooc/image";
        }
        basePath = basePath.replace("/", separator);
        return basePath;
    }

    public static String getShopImagePath(long shopId){
        String imagePath = "/upload/item/shop"+shopId+"/";
        return imagePath.replace("/", separator);
    }
}
