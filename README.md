## 埋点项目

* channel id 定义在base项目的ChannelConstants类中。
* 子channel id在重构209,base库channel_info表中

### channel id demo
```
    /**
     * 默认渠道
     */
    Long DEFAULT_CHANNEL = 100000L;

    /**
     * 金山渠道
     */
    Long JS_CHANNEL = 100001L;

    /**
     * android渠道
     */
    Long ANDROID_CHANNEL = 200001L;
    
    /**
     * IOS渠道
     */
    Long IOS_CHANNEL = 300001L;

    /**
     * 多多渠道
     */
    Long JDD_CHANNEL = 100000L;

    /**
     * 微拓
     */
    Long WEITUO_CHANNEL = 100004L;

    /**
     * 逗游
     */
    Long DOYO_CHANNEL = 100002L;

    /**
     * 微拓Android APP子渠道
     */
    Long WEITUO_ANDROID_CHANNEL = 200001016L;
```

* author ping