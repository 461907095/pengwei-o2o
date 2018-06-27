package com.imooc.myo2o.util;

import com.google.code.kaptcha.Constants;

import javax.servlet.http.HttpServletRequest;

public class CodeUtil {
    public static boolean checkVerigyCode(HttpServletRequest request){
        //实际生成的验证码
        String verifyCodeExpected= (String) request.getSession().getAttribute(
                Constants.KAPTCHA_SESSION_KEY);
        String verifyCodeActual=HttpServletRequestUtil.getString(request,"verifyCodeActual");
        if(verifyCodeActual==null||!verifyCodeActual.equalsIgnoreCase(verifyCodeExpected)){
            return false;
        }
        return true;
    }
}
