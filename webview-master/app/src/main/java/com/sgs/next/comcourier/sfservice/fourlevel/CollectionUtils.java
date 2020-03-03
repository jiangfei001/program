package com.sgs.next.comcourier.sfservice.fourlevel;

import java.util.Collection;
import java.util.Map;

/**
 * Created by 01211403(范建星) on 2016/10/14.
 */
public class CollectionUtils {

    /**
     * 判断集合是否为空
     *
     * @param collection
     * @return
     */
    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断集合是否为空
     *
     * @param collection
     * @return
     */
    public static boolean isEmpty(Map collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * <p>Checks if an array of Objects is empty or {@code null}.</p>
     *
     * @param array the array to test
     * @return {@code true} if the array is empty or {@code null}
     * @since 2.1
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }


    public static int size(Collection collection) {
        return collection == null ? 0 : collection.size();
    }

    public static int size(Object[] array) {
        return array == null ? 0 : array.length;
    }

}
