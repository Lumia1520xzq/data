package com.wf.data.common.constants;

/**
 * @author shihui
 * @date 2018/3/16
 */
public interface UserClassifyContents {
    /*用户分类标签*/

    //所有用户
    int USERGROUP_ALL = 0;

    //新用户
    int USERGROUP_NEWUSER = 1;

    //老用户-15天内活跃
    int USERGROUP_FIFTEEN_ACTIVE = 2;

    //老用户-15天内未活跃
    int USERGROUP_FIFTEEN_NO_ACTIVE = 3;

}
