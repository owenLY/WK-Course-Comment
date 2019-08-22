package com.ly.wk.service;

import java.util.Map;

public interface AlipayService {
    String alipay(String oid,double total_amount, String subject);
    boolean signVerified(Map<String,String> params,String alipay_public_key,String charset,String sign_type);
}
