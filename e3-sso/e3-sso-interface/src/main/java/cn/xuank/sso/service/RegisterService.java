package cn.xuank.sso.service;

import cn.xuank.common.utils.E3Result;
import cn.xuank.pojo.TbUser;

public interface RegisterService {

	E3Result checkData(String param, int type);
	E3Result register(TbUser user);
}
