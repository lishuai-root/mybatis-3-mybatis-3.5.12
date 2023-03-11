package test.java.mybatis;

import org.apache.ibatis.annotations.Mapper;
import test.java.pojo.User;

/**
 * @description:
 * @author: LISHUAI
 * @createDate: 2023/3/5 17:45
 * @version: 1.0
 */

public interface MyBatisDebug {

  User queryOneUser(String userId);
}
