package io.renren.common.utils;

import java.lang.reflect.Field;  
import java.lang.reflect.Method;  
import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.util.HashMap;  
import java.util.Locale;  
import java.util.Map;  
  
public class BeanRefUtil {  
    /** 
     * ȡBean�����Ժ�ֵ��Ӧ��ϵ��MAP 
     *  
     * @param bean 
     * @return Map 
     */  
    public static Map<String, String> getFieldValueMap(Object bean) {  
        Class<?> cls = bean.getClass();  
        Map<String, String> valueMap = new HashMap<String, String>();  
        Method[] methods = cls.getDeclaredMethods();  
        Field[] fields = cls.getDeclaredFields();  
        for (Field field : fields) {  
            try {  
                String fieldType = field.getType().getSimpleName();  
                String fieldGetName = parGetName(field.getName());  
                if (!checkGetMet(methods, fieldGetName)) {  
                    continue;  
                }  
                Method fieldGetMet = cls.getMethod(fieldGetName, new Class[] {});  
                Object fieldVal = fieldGetMet.invoke(bean, new Object[] {});  
                String result = null;  
                if ("Date".equals(fieldType)) {  
                    result = fmtDate((Date) fieldVal);  
                } else {  
                    if (null != fieldVal) {  
                        result = String.valueOf(fieldVal);  
                    }  
                }  
//              String fieldKeyName = parKeyName(field.getName());  
                valueMap.put(field.getName(), result);  
            } catch (Exception e) {  
                continue;  
            }  
        }  
        return valueMap;  
    }  
  
    /** 
     * set���Ե�ֵ��Bean 
     *  
     * @param bean 
     * @param valMap 
     */  
    public static void setFieldValue(Object bean, Map<String, String> valMap) {  
        Class<?> cls = bean.getClass();  
        // ȡ��bean������з���  
        Method[] methods = cls.getDeclaredMethods();  
        Field[] fields = cls.getDeclaredFields();  
  
        for (Field field : fields) {  
            try {  
                String fieldSetName = parSetName(field.getName());  
                if (!checkSetMet(methods, fieldSetName)) {  
                    continue;  
                }  
                Method fieldSetMet = cls.getMethod(fieldSetName,  
                        field.getType());  
//              String fieldKeyName = parKeyName(field.getName());  
                String  fieldKeyName = field.getName();  
                String value = valMap.get(fieldKeyName);  
                if (null != value && !"".equals(value)) {  
                    String fieldType = field.getType().getSimpleName();  
                    if ("String".equals(fieldType)) {  
                        fieldSetMet.invoke(bean, value);  
                    } else if ("Date".equals(fieldType)) {  
                        Date temp = parseDate(value);  
                        fieldSetMet.invoke(bean, temp);  
                    } else if ("Integer".equals(fieldType)  
                            || "int".equals(fieldType)) {  
                        Integer intval = Integer.parseInt(value);  
                        fieldSetMet.invoke(bean, intval);  
                    } else if ("Long".equalsIgnoreCase(fieldType)) {  
                        Long temp = Long.parseLong(value);  
                        fieldSetMet.invoke(bean, temp);  
                    } else if ("Double".equalsIgnoreCase(fieldType)) {  
                        Double temp = Double.parseDouble(value);  
                        fieldSetMet.invoke(bean, temp);  
                    } else if ("Boolean".equalsIgnoreCase(fieldType)) {  
                        Boolean temp = Boolean.parseBoolean(value);  
                        fieldSetMet.invoke(bean, temp);  
                    } else {  
                        System.out.println("not supper type" + fieldType);  
                    }  
                }  
            } catch (Exception e) {  
                continue;  
            }  
        }  
    }  
  
    /** 
     * ��ʽ��stringΪDate 
     *  
     * @param datestr 
     * @return date 
     */  
    public static Date parseDate(String datestr) {  
        if (null == datestr || "".equals(datestr)) {  
            return null;  
        }  
        try {  
            String fmtstr = null;  
            if (datestr.indexOf(':') > 0) {  
                fmtstr = "yyyy-MM-dd HH:mm:ss";  
            } else {  
                fmtstr = "yyyy-MM-dd";  
            }  
            SimpleDateFormat sdf = new SimpleDateFormat(fmtstr, Locale.UK);  
            return sdf.parse(datestr);  
        } catch (Exception e) {  
            return null;  
        }  
    }  
  
    /** 
     * ����ת��ΪString 
     *  
     * @param date 
     * @return date string 
     */  
    public static String fmtDate(Date date) {  
        if (null == date) {  
            return null;  
        }  
        try {  
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",  
                    Locale.US);  
            return sdf.format(date);  
        } catch (Exception e) {  
            return null;  
        }  
    }  
  
    /** 
     * �ж��Ƿ����ĳ���Ե� set���� 
     *  
     * @param methods 
     * @param fieldSetMet 
     * @return boolean 
     */  
    public static boolean checkSetMet(Method[] methods, String fieldSetMet) {  
        for (Method met : methods) {  
            if (fieldSetMet.equals(met.getName())) {  
                return true;  
            }  
        }  
        return false;  
    }  
  
    /** 
     * �ж��Ƿ����ĳ���Ե� get���� 
     *  
     * @param methods 
     * @param fieldGetMet 
     * @return boolean 
     */  
    public static boolean checkGetMet(Method[] methods, String fieldGetMet) {  
        for (Method met : methods) {  
            if (fieldGetMet.equals(met.getName())) {  
                return true;  
            }  
        }  
        return false;  
    }  
  
    /** 
     * ƴ��ĳ���Ե� get���� 
     *  
     * @param fieldName 
     * @return String 
     */  
    public static String parGetName(String fieldName) {  
        if (null == fieldName || "".equals(fieldName)) {  
            return null;  
        }  
        int startIndex = 0;  
        if (fieldName.charAt(0) == '_')  
            startIndex = 1;  
        return "get"  
                + fieldName.substring(startIndex, startIndex + 1).toUpperCase()  
                + fieldName.substring(startIndex + 1);  
    }  
  
    /** 
     * ƴ����ĳ���Ե� set���� 
     *  
     * @param fieldName 
     * @return String 
     */  
    public static String parSetName(String fieldName) {  
        if (null == fieldName || "".equals(fieldName)) {  
            return null;  
        }  
        int startIndex = 0;  
        if (fieldName.charAt(0) == '_')  
            startIndex = 1;  
        return "set"  
                + fieldName.substring(startIndex, startIndex + 1).toUpperCase()  
                + fieldName.substring(startIndex + 1);  
    }  
  
    /** 
     * ��ȡ�洢�ļ����ƣ�����parGetName�� 
     *  
     * @param fieldName 
     * @return ȥ����ͷ��get 
     */  
    public static String parKeyName(String fieldName) {  
        String fieldGetName = parGetName(fieldName);  
        if (fieldGetName != null && fieldGetName.trim() != ""  
                && fieldGetName.length() > 3) {  
            return fieldGetName.substring(3);  
        }  
        return fieldGetName;  
    }  
  
}  