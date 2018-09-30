package com.david.utils;

import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.util.CollectionUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.zip.GZIPInputStream;

/**
 * 一个公共的工具类
 */
public class CommonUtils {
    /**
     * 获取一个32位的随机数 16位数字组成。并去掉“-”并且转换为大写
     */
    public static String getUUIDRandomNum() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    /**
     * 获取一个随机生成四位数字
     */
    public static String generateFourNum() {
        String[] beforeShuffle = new String[]{"2", "3", "4", "5", "6", "7",
                "8", "9", "1"};
        List list = Arrays.asList(beforeShuffle);
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
        }
        String afterShuffle = sb.toString();
        String result = afterShuffle.substring(5, 9);
        return result;
    }

    //生成随机数字和字母,
    public static String getStringRandom(int length) {
        String val = "";
        Random random = new Random();
        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
    
    /**
     * 将map中的数据直接封装到一个javabean中
     */
    public static <T> T toBean(Map map, Class<T> clazz) {
        try {
            /**
             * 创建指定类型的JavaBean对象
             */
            /*
             * 1. 通过参数clazz创建实例 2. 使用BeanUtils.populate把map的数据封闭到bean中
             */
            T bean = clazz.newInstance();
            ConvertUtils.register(new DateConverter(), java.util.Date.class);
            // 将数据封装在javabean中
            org.apache.commons.beanutils.BeanUtils.populate(bean, map);
            // 返回javabean对象
            return bean;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;

    }

    /**
     * Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map
     *
     * @param obj
     */
    public static Map<String, Object> transBean2Map(Object obj)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException {

        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();

            // 过滤class属性
            if (!key.equals("class")) {
                // 得到property对应的getter方法
                Method getter = property.getReadMethod();
                Object value = getter.invoke(obj);

                map.put(key, value);
            }

        }
        return map;
    }

    /**
     * 拷贝实体，source,target不允许为空
     *
     * @param source
     * @param target
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static void copyProperties(Object source, Object target)
            throws IllegalAccessException, InvocationTargetException {
        org.apache.commons.beanutils.BeanUtils.copyProperties(source, target);
    }

    /**
     * 拷贝实体集合，sourceList，targetList不允许为空
     *
     * @param sourceList
     * @param targetList
     * @throws Exception
     */
    public static void copyPropertiesList(List sourceList, List targetList) throws Exception {
        if (CollectionUtils.isEmpty(sourceList) || CollectionUtils.isEmpty(targetList)) {
            throw new Exception();
        }
        sourceList.forEach(items -> {
            Object target = new Object();
            try {
                org.apache.commons.beanutils.BeanUtils.copyProperties(items, target);
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            targetList.add(target);
        });
    }

    /**
     * Map --> Bean 2: 利用org.apache.commons.beanutils 工具类实现 Map --> Bean
     *
     * @param map
     * @param obj
     */
    public static void transMap2Bean2(Map<String, Object> map, Object obj)
            throws InvocationTargetException, IllegalAccessException {
        if (map == null || obj == null) {
            return;
        }
        org.apache.commons.beanutils.BeanUtils.populate(obj, map);
    }

    /**
     * Map --> Bean 1: 利用Introspector,PropertyDescriptor实现 Map --> Bean
     *
     * @param map
     * @param obj
     */
    public static void transMap2Bean(Map<String, Object> map, Object obj)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (map.containsKey(key)) {
                Object value = map.get(key);
                // 得到property对应的setter方法
                Method setter = property.getWriteMethod();
                setter.invoke(obj, value);
            }
        }
    }

    /**
     * 生成上传文件的名称。利用UUID解决文件重名覆盖的问题。abc.mp3  则为 407b756ad-604c-4032-8773-141016726b80.mp3
     */
    public static String getFileName(String primitiveFileName) {
        // 使用uuid生成文件名
        String fileName = UUID.randomUUID().toString();
        // 获取文件后缀
        String suffix = primitiveFileName.substring(primitiveFileName.lastIndexOf("."));
        return fileName + suffix;
    }

    /**
     * 获取文件的后缀
     */
    public static String getSuffixFileName(String primitiveFileName) {
        // 获取文件后缀
        String suffix = primitiveFileName.substring(primitiveFileName.lastIndexOf("."));
        return suffix;
    }

    /*
     * Java文件操作 获取文件扩展名
     *
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }
    /*
     * Java文件操作 获取不带扩展名的文件名
     *
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }
    
    public static String decompress(String str) {
		if (str == null || str.length() == 0) {
			return str;
		}
		ByteArrayInputStream bis = null;
		ByteArrayOutputStream bos = null;
		GZIPInputStream is = null;
		byte[] buf = null;
		try {
			bis = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
			bos = new ByteArrayOutputStream();
			// 使用默认缓冲区大小创建新的输入流
			is = new GZIPInputStream(bis); 
			buf = new byte[1024];
			int len = 0;
			while ((len = is.read(buf)) != -1) { // 将未压缩数据读入字节数组
				// 将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此byte数组输出流
				bos.write(buf, 0, len);
			}
			is.close(); bis.close(); bos.close();
			return new String(bos.toByteArray()); // 通过解码字节将缓冲区内容转换为字符串
		} catch (Exception ex) {
			return str;
		} finally {
			bis = null; bos = null;
			is = null; buf = null;
		}
	}
    
    public static void main(String[] args)
            throws InvocationTargetException, IllegalAccessException, IntrospectionException {
//		ResponseEntity entity = new ResponseEntity("uid", "986945193");
//		Map<String, Object> map = transBean2Map(entity);
//		System.out.println(FastJsonUtil.toJSONString(map));
//        System.out.print(getExtensionName("abc.mp4"));
    	/*for (int i = 0; i < 10; i++) {
    		System.out.println(getStringRandom(16));
		}*/
    	System.out.println(Runtime.getRuntime().availableProcessors());
    	System.out.println(getStringRandom(16));
    }
}
