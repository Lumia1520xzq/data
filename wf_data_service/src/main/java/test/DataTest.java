package test;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

/**
 * @author: lcs
 * @date: 2017/12/21
 */
public class DataTest {
    public static void main(String[] args) throws Exception {
        List<Integer> deskTypes = Lists.newArrayList();
        deskTypes = Arrays.asList(1, 2, 3, 4,0);

        deskTypes.remove(0);
        System.out.println(deskTypes.toArray());

    }
}
