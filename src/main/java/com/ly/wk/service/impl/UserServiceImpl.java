package com.ly.wk.service.impl;

import com.ly.wk.config.DefaultParamBean;
import com.ly.wk.mapper.UserMapper;
import com.ly.wk.modle.User;
import com.ly.wk.service.UserService;
import com.ly.wk.service.YunpianService;
import com.ly.wk.utils.Md5Util;
import com.ly.wk.utils.UUidUtil;
import com.ly.wk.vo.PersonalDataVo;
import com.ly.wk.vo.ResultVo;
import com.ly.wk.vo.Status;
import com.ly.wk.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    private Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired private UserMapper userMapper;
    @Autowired private DefaultParamBean defaultParamBean;
    @Autowired YunpianService yunpianService;
    @Autowired ValueOperations<String,Object> valueOperations;

    @Override
    public ResultVo register(UserVo userVo) {
        String code1=userVo.getCode();  //获取随机盐值
        String code2= (String) valueOperations.get(userVo.getMobile());
        if(code1==null || code2==null || !code1.equals(code2)){     //比较前台和缓存中的验证码
            System.err.println("error");
            return ResultVo.of(Status.FAILURE,"注册失败");
        }
        String uid=UUidUtil.getUUid();
        userVo.setUid(uid);

        String salt=Md5Util.getSalt();
        userVo.setPassword(Md5Util.digest(userVo.getPassword(),salt));  //对密码进行加密
        userVo.setSalt(salt);

        userVo.setCreateTime(new Date(System.currentTimeMillis()));
        userVo.setAvatar("avatar/abcadfdajdfdak-fafdaf.jpg");
        userVo.setNickname("小哥的昵称不见了");

        int result=userMapper.addUser(userVo);
        if(result==1){
            return ResultVo.of(Status.SUCCESS,"注册成功,3秒后跳转至登录页");
        }
        else {
            return ResultVo.of(Status.FAILURE,"注册失败");
        }
    }

    @Override
    public User login(User user) {
        if(user==null)
            return null;
        String username=user.getUsername();
        String salt=userMapper.querySalt(username);
        if(salt!=null){
            String password=Md5Util.digest(user.getPassword(),salt);
            user.setPassword(password);
            User u=userMapper.queryUser(user);
            u.setAvatar(defaultParamBean.getImagePath()+u.getAvatar());
            if(u!=null){
                return u;
            }
        }
        return null;
    }

    @Override
    public boolean sendCode(String mobile) {
        String code= UUidUtil.get4Number();
        String text="【云片网】您的验证码是"+code;
        boolean flag=yunpianService.sendMessage(text,mobile);
        if(flag){
            valueOperations.set(mobile,code,5, TimeUnit.MINUTES);   //将验证码暂时存入存储,5分钟有效
            return true;
        }
        else
            return false;
    }


    @Override
    public boolean verifyUsername(String username) {
        return userMapper.getByUsername(username)==0?true:false;
    }

    @Override
    public boolean verifyEmail(String email) {
        return userMapper.getByEmail(email)==0?true:false;
    }

    @Override
    public boolean verifyMobile(String mobile) {
        return userMapper.getByMobile(mobile)==0?true:false;
    }

    @Override
    public ResultVo updatePersonalData(MultipartFile file, PersonalDataVo personalDataVo) {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String birthday=personalDataVo.getBirthday();
        if(birthday!=null) {
            try {
                personalDataVo.setBirth(format.parse(birthday));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        String avatar=null;
        if(!file.isEmpty()){
            String name=file.getOriginalFilename();
            String suffix=name.substring(name.indexOf("."));
            avatar="avatar/"+UUidUtil.get6Character()+"-"+UUidUtil.get4Number()+suffix;
        }
        personalDataVo.setAvatar(avatar);
        logger.info(personalDataVo.toString());
        int result=userMapper.update(personalDataVo);

        String parentPath="/usr/local/tomcat/apache-tomcat-9.0.14/webapps";
        String childPath;

        if(result==1 && !file.isEmpty()){
            if(!file.isEmpty()){
                childPath=defaultParamBean.getImagePath()+avatar;
                File f=new File(parentPath+childPath);
                try {
                    file.transferTo(f);
                    logger.info("上传成功:\t"+f.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return ResultVo.of(Status.SUCCESS,"修改成功",childPath);
            }

            return ResultVo.of(Status.SUCCESS,"修改成功");
        }

        return ResultVo.of(Status.FAILURE,"修改失败");
    }

    @Override
    public PersonalDataVo showData(String uid) {
        PersonalDataVo personalData=userMapper.queryByUid(uid);

        int gender=personalData.getGender();
        if(gender==1)
            personalData.setMan(true);

        String avatar=personalData.getAvatar();
        if(avatar!=null) {
            personalData.setAvatar(defaultParamBean.getImagePath() + personalData.getAvatar());
        }
        return personalData;
    }
}
