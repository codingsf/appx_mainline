package top.appx.service;

import com.github.pagehelper.PageInfo;
import top.appx.entity.vo.TransferVO;

import java.util.List;

public interface TransferService {
    PageInfo<TransferVO> findPage(Integer pageNum , Integer pageSize) throws Exception;
    void transfer(long sUserId,long dUserId,double money,String title,String description);

    List<TransferVO> findUserId(Long id,long top);
}
