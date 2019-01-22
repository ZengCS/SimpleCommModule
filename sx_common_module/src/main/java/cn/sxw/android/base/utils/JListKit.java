package cn.sxw.android.base.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * List 工具
 */
public final class JListKit {
    public static final String Split_Char = ",";

    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<E>();
    }

    public static <E> ArrayList<E> newArrayList(E... elements) {
        ArrayList<E> list = new ArrayList<E>();
        Collections.addAll(list, elements);
        return list;
    }

    public static <E> ArrayList<E> newArrayListWithCapacity(int initialArraySize) {
        return new ArrayList<E>(initialArraySize);
    }

    public static <E> LinkedList<E> newLinkedList() {
        return new LinkedList<E>();
    }

    public static <E> LinkedList<E> newLinkedList(Iterable<? extends E> elements) {
        LinkedList<E> list = newLinkedList();
        for (E element : elements) {
            list.add(element);
        }
        return list;
    }

    public static boolean isEmpty(Collection<?> list) {
        if (list != null && list.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isNotEmpty(Collection<?> list) {
        return !isEmpty(list);
    }

    /**
     * @Description:把数组转换为一个用逗号分隔的字符串 ，以便于用in+String 查询
     */
    public static String converToString(String[] ig) {
        return converToString(ig, Split_Char);
    }

    /**
     * @Description:把数组转换为一个用逗号分隔的字符串 ，以便于用in+String 查询
     */
    public static String converToString(String[] ig, String splitChar) {
        if (splitChar == null) {
            splitChar = Split_Char;
        }
        StringBuilder sb = new StringBuilder();
        if (ig != null && ig.length > 0) {
            for (int i = 0; i < ig.length; i++) {
                sb.append(ig[i]).append(splitChar);
            }
        }
        String str = sb.toString();
        if (str.endsWith(splitChar)) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    /**
     * @Description:把一个字符串转化为List<String>
     */
    public static List<String> converToArray(String str) {
        return converToArray(str, Split_Char);
    }

    /**
     * @Description:把一个字符串转化为List<String>
     */
    public static List<String> converToArray(String str, String splitChar) {
        if (splitChar == null) {
            splitChar = Split_Char;
        }
        if (str != null) {
            String[] strs = str.split(splitChar);

            return new ArrayList<String>(Arrays.asList(strs));
        } else {
            return new ArrayList<String>();
        }
    }

    /**
     * @Description:把list转换为一个用逗号分隔的字符串
     */
    @SuppressWarnings("all")
    public static String listToString(List list, String splitChar) {
        if (splitChar == null) {
            splitChar = Split_Char;
        }
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    sb.append(list.get(i) + splitChar);
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        String sbStr = sb.toString();
        if (sbStr.endsWith(splitChar)) {
            sbStr = sbStr.substring(0, sbStr.length() - 1);
        }
        return sbStr;
    }

    /**
     * 列表去重
     */
    public static List<String> removeDuplicate(List<String> list) {
        Set set = new LinkedHashSet<String>();
        set.addAll(list);
        list.clear();
        list.addAll(set);
        return list;
    }
}
