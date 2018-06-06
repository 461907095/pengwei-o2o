package com.imooc.myo2o.util;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestUtil {

    /**
     * 转哈un为int
     * @param request
     * @param key
     * @return
     */
    public static int getInt(HttpServletRequest request ,String key){
        try{
            return Integer.decode(request.getParameter(key));
        }catch (Exception e){
            return -1;
        }
    }

    public static Long getLong(HttpServletRequest request ,String key){
        try{
            return Long.decode(request.getParameter(key));
        }catch (Exception e){
            return -1l;
        }
    }

    public static Double getDouble(HttpServletRequest request ,String key){
        try{
            return Double.valueOf(request.getParameter(key));
        }catch (Exception e){
            return -1.0;
        }
    }

    public static boolean getBoolean(HttpServletRequest request ,String key){
        try{
            return Boolean.valueOf(request.getParameter(key));
        }catch (Exception e){
            return false;
        }
    }

    public static String getString(HttpServletRequest request,String key){
        try{
            String result=request.getParameter(key);
            if(result!=null){
                result=result.trim();
            }
            if("".equals(result)){
                result=null;
            }
            return  result;
        }catch (Exception e){
            return  null;
        }
    }
}
