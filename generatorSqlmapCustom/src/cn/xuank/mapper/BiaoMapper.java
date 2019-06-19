package cn.xuank.mapper;

import cn.xuank.pojo.Biao;
import cn.xuank.pojo.BiaoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BiaoMapper {
    int countByExample(BiaoExample example);

    int deleteByExample(BiaoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Biao record);

    int insertSelective(Biao record);

    List<Biao> selectByExample(BiaoExample example);

    Biao selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Biao record, @Param("example") BiaoExample example);

    int updateByExample(@Param("record") Biao record, @Param("example") BiaoExample example);

    int updateByPrimaryKeySelective(Biao record);

    int updateByPrimaryKey(Biao record);
}