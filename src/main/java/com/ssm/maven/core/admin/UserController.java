package com.ssm.maven.core.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssm.maven.core.entity.PageBean;
import com.ssm.maven.core.entity.User;
import com.ssm.maven.core.service.UserService;
import com.ssm.maven.core.util.MD5Util;
import com.ssm.maven.core.util.ResponseUtil;
import com.ssm.maven.core.util.StringUtil;
import com.ssm.maven.core.util.XSSFilterUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;
    private static final Logger log = Logger.getLogger(UserController.class);// 日志文件

    /**
     * 登录
     *
     * @param user
     * @param request
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public String login(HttpServletRequest request, HttpServletResponse response) {
    	JSONObject result = new JSONObject();
    	String userName = request.getParameter("userName");
    	String password = request.getParameter("password");
    	String code = request.getParameter("code");
    	HttpSession session = request.getSession();
    	if(StringUtil.isEmpty(code)) {
    		result.put("msg", "2");
    		return result.toString();
    	} else if (!code.equals(session.getAttribute("code"))) {
    		result.put("msg", "3");
    		return result.toString();
    	}
    	User user = new User();
    	user.setUserName(userName);
    	user.setPassword(password);
        User resultUser = userService.login(user);
        if (resultUser == null) {
        	result.put("msg", "0");
            return result.toString();
        } else {
            session.setAttribute("currentUser", resultUser);
            MDC.put("userName", user.getUserName());
            result.put("msg", "1");
            return result.toString();
        }
    }


    /**
     * 修改密码
     *
     * @param user
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/modifyPassword")
    public String modifyPassword(User user, HttpServletResponse response) throws Exception {
        String MD5pwd = MD5Util.MD5Encode(user.getPassword(), "UTF-8");
        user.setPassword(MD5pwd);
        int resultTotal = userService.updateUser(user);
        JSONObject result = new JSONObject();
        if (resultTotal > 0) {
            result.put("success", true);
        } else {
            result.put("success", false);
        }
        log.info("request: user/modifyPassword , user: " + user.toString());
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 退出系统
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/logout")
    public String logout(HttpSession session) throws Exception {
        session.invalidate();
        log.info("request: user/logout");
        return "redirect:/login.jsp";
    }

    /**
     * @param page
     * @param rows
     * @param s_user
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    public String list(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "rows", required = false) String rows, User s_user, HttpServletResponse response) throws Exception {
        PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userName", StringUtil.formatLike(s_user.getUserName()));
        map.put("start", pageBean.getStart());
        map.put("size", pageBean.getPageSize());
        List<User> userList = userService.findUser(map);
        Long total = userService.getTotalUser(map);
        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(userList);
        result.put("rows", jsonArray);
        result.put("total", total);
        log.info("request: user/list , map: " + map.toString());
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 增加用户
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    @ResponseBody
    public String addUser(HttpServletRequest request, HttpServletResponse response) {
    	//获取用户名
    	String userName = request.getParameter("username");
    	//获取用户密码
    	String password = request.getParameter("password");
    	JSONObject result = new JSONObject();
    	//判断用户名、密码是否为空
    	if(StringUtil.isEmpty(userName)) {
    		result.put("msg", "0");
    		return result.toString();
    	}
    	if(StringUtil.isEmpty(password)) {
    		result.put("msg", "1");
    		return result.toString();
    	}
    	//判断是否含有危险字符
    	if(XSSFilterUtil.doFilter(userName)) {
    		result.put("msg", "2");
    		return result.toString();
    	}
    	if(XSSFilterUtil.doFilter(password)) {
    		result.put("msg", "3");
    		return result.toString();
    	}
    	//设置返回数
    	int resultTotal = 0;
		if(StringUtil.isNotEmpty(userName) && StringUtil.isNotEmpty(password)) {
			User user = new User();
			user.setUserName(userName);
			user.setPassword(MD5Util.MD5Encode(password, "UTF-8"));
			user.setRoleName("系统管理员");
			log.debug("user: " + user.toString());
			resultTotal = userService.addUser(user);
	        if (resultTotal > 0) {
	            result.put("msg", "4");
	        } else {
	            result.put("msg", "5");
	            return result.toString();
	        }
		}
		return result.toString();
    }
    
    /**
     * 添加或修改管理员
     *
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/save")
    public String save(User user, HttpServletResponse response) throws Exception {
        int resultTotal = 0;
        String MD5pwd = MD5Util.MD5Encode(user.getPassword(), "UTF-8");
        user.setPassword(MD5pwd);
        if (user.getId() == null) {
            resultTotal = userService.addUser(user);
        } else {
            resultTotal = userService.updateUser(user);
        }
        JSONObject result = new JSONObject();
        if (resultTotal > 0) {
            result.put("success", true);
        } else {
            result.put("success", false);
        }
        log.debug("request: user/save , user: " + user.toString());
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 删除管理员
     *
     * @param ids
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/delete")
    public String delete(@RequestParam(value = "ids") String ids, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        String[] idsStr = ids.split(",");
        for (int i = 0; i < idsStr.length; i++) {
            userService.deleteUser(Integer.parseInt(idsStr[i]));
        }
        result.put("success", true);
        log.info("request: user/delete , ids: " + ids);
        ResponseUtil.write(response, result);
        return null;
    }
}
