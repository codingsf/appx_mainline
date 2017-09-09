package top.appx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import top.appx.entity.Transfer;
import top.appx.entity.vo.TransferVO;

import java.util.List;

@Component
@Mapper
public interface TransferDao extends BaseDao<Transfer> {
    List<Transfer> find();
    List<TransferVO> findVO();
    void save(Transfer transfer);

    List<TransferVO> findUserId(@Param("userId") Long userId,@Param("top") long top);
}
