package top.appx.dao;

import com.github.pagehelper.PageInfo;
import top.appx.entity.CollectParam;

import java.io.Serializable;
import java.util.List;

public interface BaseDao<T> {
    int deleteByPrimaryKey(Serializable id);
    void deleteByPrimaryKeys(List<Long> ids);
    void insert(T record);
    int insertSelective(T record);
    T selectByPrimaryKey(Serializable id);
    int updateByPrimaryKeySelective(T record);
    int updateByPrimaryKeyWithBLOBs(T record);
    int updateByPrimaryKey(T record);

    List<T> find();
}
