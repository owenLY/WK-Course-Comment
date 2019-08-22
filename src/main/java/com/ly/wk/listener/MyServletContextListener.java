package com.ly.wk.listener;

import com.ly.wk.service.BannerSerice;
import com.ly.wk.service.CourseService;
import com.ly.wk.utils.SpringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class MyServletContextListener implements ServletContextListener {

    /**
     * servlet容器初始化完成时，将banners,courseListVo查出存入redis中
     *
     *　注意:由于servlet容器和bean容器是两个不同的容器,所以 @Autowired只能在bean容器管理的范围内使用,
     *      但是可以通过spring提供的 WebApplicationContextUtils来获取所需要的bean
     * @param sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        CourseService courseService= WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext())
                .getBean(CourseService.class);
        BannerSerice bannerSerice = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext())
                .getBean(BannerSerice.class);

        courseService.queryCourseList();
        bannerSerice.queryBanner();

        System.out.println("将ApplicationContext注入");
        ApplicationContext applicationContext=
                WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
        SpringUtils.setApplicationContext(applicationContext);  //将ApplicationContext添加到SpringUtils工具中
    }
}
