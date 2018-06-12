package com.david.web.controller;

/**
 * @author ：David
 * @weibo ：http://weibo.com/mcxiaobing
 * @github: https://github.com/QQ986945193
 */

/**
 * 首页转发controller
 */
@Controller
public class IndexController {

    /**
     *
     * 跳转至指定页面
     */
    @RequestMapping("go.do")
    public String go(@RequestParam String page) {
        return page;
    }
}
