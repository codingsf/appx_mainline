package top.appx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.appx.dao.TransferDao;
import top.appx.dao.UserDao;
import top.appx.entity.Transfer;
import top.appx.entity.User;
import top.appx.entity.vo.TransferVO;
import top.appx.exception.MsgException;
import top.appx.exception.NotEnoughMoneyException;
import top.appx.service.TransferService;

import java.util.Date;
import java.util.List;

@Service
public class TransferServiceImpl implements TransferService {
    @Autowired
    private TransferDao transferDao;

    @Autowired
    private UserDao userDao;

    @Override
    public PageInfo<TransferVO> findPage(Integer pageNum, Integer pageSize) throws Exception {
        PageHelper.startPage(pageNum,pageSize);
        List<TransferVO> list = transferDao.findVO();
        return new PageInfo<>(list);
    }

    @Transactional
    @Override
    public void transfer(long sUserId, long dUserId, double money, String title, String description) {
        if(money<=0){
            throw new MsgException();
        }
        if(sUserId==dUserId){
            throw new MsgException();
        }

        Transfer transfer = new Transfer();
        transfer.setsUserId(sUserId);
        transfer.setdUserId(dUserId);
        transfer.setMoney(money);
        transfer.setTitle(title);
        transfer.setDescription(description);
        transfer.setCreateTime(new Date());
        transferDao.save(transfer);

        User sUser = userDao.selectByPrimaryKey(sUserId);
        if(sUser.getMoney()<money){
            throw new NotEnoughMoneyException();
        }
        userDao.addMoney(sUserId,money*-1);
        userDao.addMoney(dUserId,money);

    }

    @Override
    public List<TransferVO> findUserId(Long userId,long top) {
        return transferDao.findUserId(userId,top);
    }
}
