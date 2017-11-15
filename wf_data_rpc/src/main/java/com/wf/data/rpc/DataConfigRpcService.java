package com.wf.data.rpc;

import com.wf.data.rpc.dto.DataConfigDto;

/**
 * @author: lcs
 * @date: 2017/11/15
 */
public interface DataConfigRpcService {

    /**
     * 根据name获取配置项
     *
     * @param name
     * @return null / BaseConfig
     */
    DataConfigDto findByName(String name);

    /**
     * 根据name channel获取配置项
     * @param name
     * @param channel
     * @return
     */
    DataConfigDto findByNameAndChannel(String name, Long channel);

    /**
     * 根据name获取配置项的值
     *
     * @param name
     * @return null / BaseConfig
     */
    String getStringValueByName(String name);


    /**
     * 根据name获取配置项的值
     *
     * @param name
     * @return Boolean
     */
    boolean getBooleanValueByName(String name);


    /**
     * 根据name获取配置项的值
     *
     * @param name
     * @return 0d / double
     */
    double getDoubleValueByName(String name);


    /**
     * 根据name获取配置项的值
     *
     * @param name
     * @return 0 / float
     */
    float getFloatValueByName(String name);


    /**
     * 根据name获取配置项的值
     *
     * @param name
     * @return 0 / int
     */
    int getIntValueByName(String name);


    /**
     * 根据name获取配置项的值
     *
     * @param name
     * @return 0 / long
     */
    long getLongValueByName(String name);

}
