package top.appx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.appx.dao.ArticleGroupDao;
import top.appx.entity.ArticleGroup;
import top.appx.entity.vo.ArticleGroupVO;
import top.appx.exception.ArticleGroupFlagExistException;
import top.appx.exception.ArticleGroupNameExistException;
import top.appx.service.ArticleGroupService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class ArticleGroupServiceImpl implements ArticleGroupService {
    @Autowired
    private ArticleGroupDao articleGroupDao;

    @Override
    public void save(ArticleGroup articleGroup) {

        if(articleGroupDao.findByName(articleGroup.getName())!=null){
            throw new ArticleGroupNameExistException();
        }
        if(articleGroupDao.findByFlag(articleGroup.getFlag())!=null){
            throw new ArticleGroupFlagExistException();
        }

        articleGroup.setCreateTime(new Date());
        articleGroupDao.insert(articleGroup);
    }

    @Override
    public PageInfo<ArticleGroup> findPage(ArticleGroup search, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<ArticleGroup> articleGroupList = articleGroupDao.find(search);
        return new PageInfo<ArticleGroup>(articleGroupList);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        articleGroupDao.deleteByPrimaryKeys(ids);
    }

    @Override
    public List<ArticleGroupVO> index(Long userId) {
        return articleGroupDao.index(userId);
    }


    @Override
    public ArticleGroup findById(Long id) {
        return articleGroupDao.selectByPrimaryKey(id);
    }

    @Override
    public ArticleGroupVO selectVO(long articleGroupId, Long userId) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("articleGroupId",articleGroupId);
        return articleGroupDao.selectVOByMap(map);
    }

    @Override
    public void subscribe(Long id, long userId, boolean subscribe) {
        if(subscribe){
            articleGroupDao.subscribe(id,userId);
        }else{
            articleGroupDao.unsubscribe(id,userId);
        }
    }
}
