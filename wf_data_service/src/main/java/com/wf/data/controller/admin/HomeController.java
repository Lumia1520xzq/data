package com.wf.data.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.cache.ehcache.EhcacheManager;
import com.wf.core.extjs.model.MenuModel;
import com.wf.core.extjs.model.TreeModel;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.Global;
import com.wf.core.utils.http.HttpClientUtils;
import com.wf.core.utils.type.StringUtils;
import org.jasig.cas.client.util.AssertionHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取菜单
 */
@RestController
@RequestMapping("/admin/home")
public class HomeController {

    @Autowired
    private EhcacheManager ehcacheManager;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**菜单本地缓存时间(秒)*/
    private static final int MENU_LOCAL_CACHE_SECONDS = 300;

    @RequestMapping("/loginout")
    public Object loginout(HttpServletRequest request) {
        request.getSession().invalidate();
        ehcacheManager.removeAll();
        return true;
    }

    /**
     * 左侧一级菜单
     *
     * @return
     */
    @RequestMapping("/listModule")
    public Object listModule(@RequestBody JSONObject root) {
        return getMenu(root.getString("root"));
    }

    private Object getMenu(String menuParCode) {
        String longName = AssertionHolder.getAssertion().getPrincipal().getName();
        String menuCacheKey = longName + "-" + menuParCode;
        Object json = ehcacheManager.get(menuCacheKey);
        if (json == null) {
            String url = Global.getConfig("mp.url");
            if (StringUtils.isBlank(url)) {
                throw new RuntimeException("can not find property 'mp.url' in 'application.properties.'");
            }
            if (url.contains("?")) {
                if (!url.endsWith("&")) {
                    url += "&";
                }
            } else {
                url += "?";
            }
            url += "loginName=" + longName + "&parentMenuCode=" + menuParCode;
            String body = HttpClientUtils.post(url, logger);
            if (StringUtils.isBlank(body)) {
                throw new RuntimeException("send HTTP Request to get menu data failed. request:" + url);
            }
            ehcacheManager.set(menuCacheKey, body, MENU_LOCAL_CACHE_SECONDS);
            json = body;
        }

        List<TreeModel> list = GfJsonUtil.parseArray(json.toString(), TreeModel.class);
        List<MenuModel> maps = new ArrayList<>();
        for (TreeModel tm : list) {
            MenuModel mm = new MenuModel();
            mm.setId(tm.getCode());
            mm.setCode(tm.getCode());
            mm.setText(tm.getText());
            mm.setExpanded(tm.isExpanded());
            mm.setIcon(tm.getIcon());
            mm.setLeaf(tm.isLeaf());
            mm.setModuleLink(tm.getModuleLink());
            maps.add(mm);
        }
        return maps;
    }

    /**
     * 左侧二级菜单树
     *
     * @return
     */
    @RequestMapping("/listMenu")
    public Object listMenu(String code) {
        return getMenu(code);
    }
}
