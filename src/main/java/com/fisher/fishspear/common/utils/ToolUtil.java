package com.fisher.fishspear.common.utils;

import com.baomidou.mybatisplus.activerecord.Model;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class ToolUtil {

    /**
     * 对象是否不为空
     */
    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }

    /**
     * 对象是否为空
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object o) {
        if (o == null) {
            return true;
        }
        if (o instanceof String) {
            if (o.toString().trim().equals("")) {
                return true;
            }
        } else if (o instanceof List) {
            if (((List) o).size() == 0) {
                return true;
            }
        } else if (o instanceof Map) {
            if (((Map) o).size() == 0) {
                return true;
            }
        } else if (o instanceof Set) {
            if (((Set) o).size() == 0) {
                return true;
            }
        } else if (o instanceof Object[]) {
            if (((Object[]) o).length == 0) {
                return true;
            }
        } else if (o instanceof int[]) {
            if (((int[]) o).length == 0) {
                return true;
            }
        } else if (o instanceof long[]) {
            if (((long[]) o).length == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否有空对象
     */
    public static boolean isOneEmpty(Object... objects) {
        for (Object object : objects) {
            boolean empty = isEmpty(object);
            if (empty) {
                return true;
            }
        }
        return false;
    }

    /**
     * map转list
     *
     * @param map
     * @return
     */
    public static List mapTransitionList(Map map) {
        List list = new ArrayList();
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            list.add(entry.getKey());
            list.add(entry.getValue());
        }
        return list;
    }

    /**
     * 对传感器编号排序 格式 1-1前 1-2后
     * 排序规则 先按第一个数字排序；再按第二个数字排序
     */
    public static int sortByCode(String code1, String code2) {
        char[] chars1 = code1.toCharArray();
        char[] chars2 = code2.toCharArray();
        if (Integer.parseInt(chars1[0] + "") < Integer.parseInt(chars2[0] + "")) {
            return -1;
        } else if (Integer.parseInt(chars1[0] + "") > Integer.parseInt(chars2[0] + "")) {
            return 1;
        } else {
            if (Integer.parseInt(chars1[2] + "") < Integer.parseInt(chars2[2] + "")) {
                return -1;
            } else if (Integer.parseInt(chars1[2] + "") > Integer.parseInt(chars2[2] + "")) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    /**
     * shiro密码加密工具类
     *
     * @param credentials 密码
     * @param saltSource  密码盐
     * @return
     */
    public static String md5(String credentials, String saltSource) {
        ByteSource salt = ByteSource.Util.bytes(saltSource);
        return new SimpleHash(Md5Hash.ALGORITHM_NAME, credentials, salt, 1024).toString();
    }

    /**
     * 获取随机盐值
     *
     * @param length
     * @return
     */
    public static String getRandomSalt(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 获取随机数字验证码
     *
     * @param length
     * @return
     */
    public static String getRandomNumber(int length) {
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * base64转码
     * @param s
     * @return
     */
    public static String base64Decode(String s) {
        byte[] b = null;
        String r = null;
        try {
            b = s.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
            r = new BASE64Encoder().encode(b);
        }
        return r.replaceAll("[\\s*\t\n\r]", "");
    }

    /**
     * MD5加密
     * @param s
     * @return
     */
    public static String md5(String s) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(s.getBytes());
        return new BigInteger(1, md.digest()).toString(16);
    }

    /**
     * 获取指定包名下所有class
     * @param pck
     * @return
     */
    public static Set<Class<? extends Model>> getClasses(String pck) {
        List<ClassLoader> classLoadersList = new LinkedList<>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(pck))));
        Set<Class<? extends Model>> classes = reflections.getSubTypesOf(Model.class);
        return classes;
    }

    /**
     * 获取table_name 和 Class对应关系
     * @param pck
     * @return
     */
    public static Map<String, Class> getTableClass(String pck) {
        Set<Class<? extends Model>> classes = getClasses(pck);
        Map<String, Class> map = new HashMap<>();
        for (Class<?> clazz : classes) {
            try {
                String tableName = (String) clazz.getDeclaredField("TABLE_NAME").get(null);
                map.put(tableName, clazz);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * 获取指定包内的所有model的静态属性值
     * @param pck
     * @return
     */
    public static Map<String, List<String>> getStaticFields(String pck) {
        Set<Class<? extends Model>> classes = getClasses(pck);
        Map<String, List<String>> map = new HashMap<>();
        for (Class<?> clazz : classes) {
            Field[] fields = clazz.getDeclaredFields();
            List<String> list = new ArrayList<>();
            String tableName = null;
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())) {
                    try {
                        if (field.getName().equals("TABLE_NAME")) {
                            tableName = (String) field.get(null);
                        }
                        list.add((String) field.get(null));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            map.put(tableName, list);
        }
        return map;
    }


    public static void main(String[] args) throws Exception {
//        List list =  new ArrayList<BizLabQuality>();
//        list.add(new BizLabQuality().setQualified(10).setTotal(11));
//        list.add(new BizLabQuality().setQualified(10).setTotal(11));
//        compare(list,"qualified","total");

//        String sss = "eyJUb2tlbklEIjoiMSIsIlJuZCI6IjEiLCJEYXRhIjpbeyJUYWJsZU5hbWUiOiJUYWJsZV9DbGllbnRVc2VySW5mbyIsIkZpZWxkcyI6IkZpZWxkX1VzZXJJZCxGaWVsZF9LZXlDb2RlIiwiQ29uZGl0aW9ucyI6IiIsIkxpbWl0IjoiIiwiT3JkZXJCeXMiOm51bGx9LHsiVGFibGVOYW1lIjoiVGFibGVfQWdlbmN5SW5mbyIsIkZpZWxkcyI6IkZpZWxkX0FnZW5jeUlkIiwiQ29uZGl0aW9ucyI6IiIsIkxpbWl0IjoiIiwiT3JkZXJCeXMiOm51bGx9XSwiQXBwSUQiOiIxIn0=";
//        String ss = "c203a86b645bab971918fa318dbc107c";
//        String s = "{\"TokenID\":\"1\",\"Rnd\":\"1\",\"Data\":[{\"TableName\":\"Table_ClientUserInfo\",\"Fields\":\"Field_UserId,Field_KeyCode\",\"Conditions\":\"\",\"Limit\":\"\",\"OrderBys\":null},{\"TableName\":\"Table_AgencyInfo\",\"Fields\":\"Field_AgencyId\",\"Conditions\":\"\",\"Limit\":\"\",\"OrderBys\":null}],\"AppID\":\"1\"}";
//        String base64 = base64Decode(s);
//        String md5 = md5(base64);
//        System.out.println(base64);
//        System.out.println(sss.equals(base64));
//        System.out.println(md5);
//        System.out.println(md5.equals(ss));

//        Map<String, List<String>> map = getStaticFields("com.fisher.fishspear.jlzf.model");
//        System.out.println(map);

        Map<String, Class> map = getTableClass("com.fisher.fishspear.jlzf.model");
        for (Class c : map.values()) {
            Object o = c.newInstance();
            Field[] fields = c.getDeclaredFields();
            for (Field field : fields) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    String type = field.getGenericType().getTypeName();
                    if (type.contains("Integer") || type.contains("int")) {
                        field.set(o, new Integer(1));
                    } else if (type.contains("String")) {
                        field.set(o, "2");
                    } else if (type.contains("Double") || type.contains("double")) {
                        field.set(o, 3d);
                    } else if (type.contains("Boolean") || type.contains("boolean")) {
                        field.set(o, false);
                    } else if (type.contains("Date")) {

                    }
                }
            }
            System.out.println(o);
        }

    }
}
