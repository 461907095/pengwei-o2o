package com.imooc.myo2o.web.shopadmin;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.myo2o.dto.ImageHolder;
import com.imooc.myo2o.dto.ProductExection;
import com.imooc.myo2o.entity.Product;
import com.imooc.myo2o.entity.ProductCategory;
import com.imooc.myo2o.entity.Shop;
import com.imooc.myo2o.enums.ProductStateEnum;
import com.imooc.myo2o.exceptions.ProductOperationException;
import com.imooc.myo2o.service.ProductCategoryService;
import com.imooc.myo2o.service.ProductService;
import com.imooc.myo2o.util.CodeUtil;
import com.imooc.myo2o.util.HttpServletRequestUtil;
import org.apache.ibatis.annotations.Param;
import org.omg.PortableServer.REQUEST_PROCESSING_POLICY_ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.sql.CommonDataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class ProductManagementController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;
    //支持上传图片详情图的最大数量
    private static  final  int IMAGEMAXCOUNT=6;

    /**
     * 通过店铺id获取该店铺下的商品列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/getproductlistbyshop" ,method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getProductListByShop(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<String, Object>();
        //获取前台传过来的页码
        int pageIndex=HttpServletRequestUtil.getInt(request,"pageIndex");
        //获取前台传过来的每页要求返回的商品数上限
        int pageSize=HttpServletRequestUtil.getInt(request,"pageSize");
        //从当前session中获取店铺信息,主要是shopId
        Shop currentShop= (Shop) request.getSession().getAttribute("currentShop");
        //控制判断
        if((pageIndex>-1)&&(pageSize>-1)&&(currentShop!=null)&&(currentShop.getShopId()!=null)){
            //获取传入的需要检索的条件，包括是否需要从某个商品类i别以及模糊查找上屏去筛选某个店铺下面的商品列
            //筛选的调节可以进行排列组合
            long productCategoryId=HttpServletRequestUtil.getLong(request,"productCategoryId");
            String productName=HttpServletRequestUtil.getString(request,"productName");
            Product productCondition=compactProductCondition(currentShop.getShopId(),productCategoryId,productName);
            //传入查询条件以及分页信息进行查询，返回响应商品列表以及总数
            ProductExection pe=productService.getProductList(productCondition,pageIndex,pageSize);
            modelMap.put("productList",pe.getProductList());
            modelMap.put("count",pe.getCount());
            modelMap.put("success",true);
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }


    @RequestMapping(value = "/getproductbyid",method = RequestMethod.GET)
    @ResponseBody
    private Map<String ,Object> getProductById(@RequestParam long productId){
        Map<String ,Object> modelMap=new HashMap<String, Object>();
        //非空判断
        if(productId>-1){
            //获取商品信息
            Product product=productService.getProductById(productId);
            //获取该店铺下的商品类别列表
            List<ProductCategory> productCategoryList=productCategoryService
                    .getProductCategoryList(product.getShop().getShopId());
            modelMap.put("product",product);
            modelMap.put("productCategoryList",productCategoryList);
            modelMap.put("success",true);
        }else{
            modelMap.put("success",true);
            modelMap.put("errMsg","empty productId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/modifyproduct",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> modifyProduct(HttpServletRequest request){
       Map<String,Object> modelMap=new HashMap<String, Object>();
       //是商品编辑时候调用还是上下家的时候调用
        //若为前者进行验证码判断，后者则跳过验证码判断
        boolean statusChange=HttpServletRequestUtil.getBoolean(request,"statusChange");
        //验证码判断
        if(!statusChange&&!CodeUtil.checkVerigyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入了错误的验证码");
            return modelMap;
        }
        //接受前端参数的变量的初始化，包括商品，缩略图，详情图列表实体类
        ObjectMapper mapper=new ObjectMapper();
        MultipartHttpServletRequest multipartRequest=null;
        Product product=null;
        ImageHolder thumbnail=null;
        List<ImageHolder> productImgList=new ArrayList<ImageHolder>();
        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
                request.getSession().getServletContext());
        //若请求中存在文件流,则取出相关的文件(包括缩略图和详情ｔｕ
        // )
        try{
            if(multipartResolver.isMultipart(request)){
                multipartRequest= (MultipartHttpServletRequest) request;
                thumbnail= handleImage(multipartRequest,thumbnail, productImgList);
            }
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }
        try{
            String productStr=HttpServletRequestUtil.getString(request,"productStr");
            //尝试获取前端传过来的表单String 流并将其zhuanuanchengproduct
            product=mapper.readValue(productStr,Product.class);
        } catch (Exception e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }
        //非空判断
        if(product!=null){
            try{
                //从session中获取当前店铺的id并复制给product,减少对前端数据的依赖
                Shop currentShop= (Shop) request.getSession().getAttribute("currentShop");
                product.setShop(currentShop);
                //开始进行商品信息变更操作
                ProductExection pe = productService.modifyProduct(product, thumbnail, productImgList);
                if(pe.getState()==ProductStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else{
                    modelMap.put("success",false);
                    modelMap.put("errMsg",pe.getStateInfo());
                }
            }catch (RuntimeException e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
                return modelMap;
            }
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入商品信息");
        }
        return modelMap;
    }


    @RequestMapping(value = "/addproduct",method = RequestMethod.POST)
    @ResponseBody
    private Map<String ,Object> addProduct(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<String, Object>();
        //验证码校验
        if(!CodeUtil.checkVerigyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入了错误的验证码");
            return modelMap;
        }
        //结合艘前端参数的变量的初始化，包括商品，缩略图，详情图列表实体类
        ObjectMapper mapper=new ObjectMapper();
        Product product=null;
        String productStr=HttpServletRequestUtil.getString(request,"productStr");
        MultipartHttpServletRequest multipartRequest=null;
        ImageHolder thumbnail=null;
        List<ImageHolder> productImgList=new ArrayList<ImageHolder>();
        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
                request.getSession().getServletContext()
        );
        try {
            //若请求中存在文件六，则取出相关的文件（包括缩略图和详情图）
            if(multipartResolver.isMultipart(request)){
                multipartRequest= (MultipartHttpServletRequest) request;
                thumbnail=handleImage(multipartRequest,thumbnail, productImgList);
            }else{
                modelMap.put("success",false);
                modelMap.put("errMsg","上传图片不能为空" );
                return modelMap;
            }
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }
        try{
            //尝试获取前端传过来的表单string流并将其转换为Product实体类
            product=mapper.readValue(productStr,Product.class);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }
        //若Product信息，缩略图以及详情图列表为非空，则开始进行商品添加操作
        if(product!=null&&thumbnail!=null&&productImgList.size()>0){
            try{
                //从session中获取当前店铺的Id并赋值给product减少对前端数据的依赖
                Shop currentShop= (Shop) request.getSession().getAttribute("currentShop");//currentShop
                Shop shop=new Shop();
                shop.setShopId(currentShop.getShopId());
                product.setShop(shop);
                //执行添加操作
                ProductExection pe=productService.addProduct(product,thumbnail,productImgList);
                if(pe.getState()==ProductStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else{
                    modelMap.put("success",false);
                    modelMap.put("errMsg",pe.getStateInfo());
                }

            }catch (ProductOperationException e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
                return modelMap;
            }
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入商品信息");
        }
        return modelMap;
    }

    /**
     * 封装商品查询条件到Product实力中去
     * @param shopId
     * @param productCategoryId
     * @param productName
     * @return
     */
    private Product compactProductCondition(Long shopId, long productCategoryId, String productName) {
        Product productCondition=new Product();
        Shop shop=new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);
        //若有指定类别的要求则添加进去
        if(productCategoryId!=-1L){
            ProductCategory productCategory=new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        //若有商品名模糊查询的要求则添加及其怒
        if(productName!=null){
            productCondition.setProductName(productName);
        }
        return productCondition;
    }


    private ImageHolder handleImage(MultipartHttpServletRequest request,ImageHolder thumbnail, List<ImageHolder> productImgList) throws IOException {
        //取出缩略图并构建ＩｍａｇｅＨｏｌｄｅｒ对象
        //取出缩略图并构建ImageHolder对象
        CommonsMultipartFile thumbnailFile= (CommonsMultipartFile) request.getFile("thumbnail");
        thumbnail=new ImageHolder(thumbnailFile.getInputStream(),thumbnailFile.getOriginalFilename());
        //取出详情图列表并构建List<ImageHolder>列表对象，最多支持六张图片上传
        for (int i=0;i<IMAGEMAXCOUNT;i++){
            CommonsMultipartFile productImgFile= (CommonsMultipartFile) request
                    .getFile("productImg"+i);
            if(productImgFile!=null){
                //若取出的滴i个详情图文件流不为空，则将其添加如详情图片列表
                ImageHolder productImg=new ImageHolder(
                        productImgFile.getInputStream(),productImgFile.getOriginalFilename());
                productImgList.add(productImg);
            }else{
                //若取出的第i个详情图文件六为空，则终止上传
                break;
            }
        }
        return thumbnail;
    }


}
