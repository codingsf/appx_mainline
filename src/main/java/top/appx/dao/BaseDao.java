package top.appx.dao;

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
    List<T> find(Object search);
}
