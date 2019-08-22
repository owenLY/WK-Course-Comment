package com.ly.wk.service.impl;

import com.ly.wk.config.DefaultParamBean;
import com.ly.wk.mapper.BannerMapper;
import com.ly.wk.modle.Banner;
import com.ly.wk.service.BannerSerice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class BannerServiceImpl implements BannerSerice {
    @Autowired BannerMapper bannerMapper;
    @Autowired DefaultParamBean defaultParamBean;
    @Autowired ValueOperations<String,Object> valueOperations;

    @Override
    public List<Banner> queryBanner() {

        //查看redis中是否有缓存
        List<Banner> banners= (List<Banner>) valueOperations.get("banners");
        if(banners!=null)
            return banners;

        int start=0;
        int size=3;

        try{
            banners=bannerMapper.query(start,size);

            banners.stream().forEach((banner -> {
                String url=banner.getUrl();
                StringBuffer path=new StringBuffer(defaultParamBean.getImagePath());
                banner.setUrl(path.append(url).toString());
            }));
        }catch (Exception e){
            e.printStackTrace();
        }

        valueOperations.set("banners",banners,60*60*24, TimeUnit.SECONDS);
        return banners;
    }
}
