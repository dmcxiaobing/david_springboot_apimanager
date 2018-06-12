package com.david.web.controller;

/**
 * @author ：David
 * @weibo ：http://weibo.com/mcxiaobing
 * @github: https://github.com/QQ986945193
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 首页转发controller
 */
@Controller
public class IndexController {

    /**
     * 跳转至指定页面
     */
    @RequestMapping("go.do")
    public String go(@RequestParam String page) {
        return page;
    }
}
