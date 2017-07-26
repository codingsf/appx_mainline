package top.appx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.appx.dao.ArticleDao;
import top.appx.dao.ArticleGroupDao;
import top.appx.entity.Article;
import top.appx.entity.ArticleGroup;
import top.appx.entity.User;
import top.appx.service.ArticleService;
import top.appx.util.StringUtil;

import java.util.Date;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private ArticleGroupDao articleGroupDao;


    @Override
    public PageInfo<Article> findPage(Integer pageNum, Integer pageSize) throws Exception {
        PageHelper.startPage(pageNum,pageSize);
        Article article = new Article();
        List<Article> articleList = articleDao.find();
        return new PageInfo<Article>(articleList);
    }

    @Override
    public Article findById(Long id) {
        return articleDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(Article article) {

        if(article.getArticleGroupId()!=null){
            ArticleGroup articleGroup = articleGroupDao.selectByPrimaryKey(article.getArticleGroupId());
            article.setArticleGroupFlag(articleGroup.getFlag());
        }
        else if(!StringUtil.isNullOrEmpty(article.getArticleGroupFlag())){
            ArticleGroup articleGroup = articleGroupDao.findByFlag(article.getArticleGroupFlag());
            article.setArticleGroupId(articleGroup.getId());
        }
        article.setCreateTime(new Date());
        articleDao.insert(article);


    }

    @Override
    public boolean saveIfNotExistUrl(Article article) {
        if(!articleDao.exist(article)){
            save(article);
            return true;
        }
        return false;
    }

    @Override
    public boolean existUrl(String url) {
        return articleDao.existUrl(url);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        articleDao.deleteByPrimaryKeys(ids);
    }

    @Override
    public List<Article> index() {
        return articleDao.index();
    }

    @Override
    public List<Article> findByArticleGroupId(Long id) {
        return articleDao.findByArticleGroupId(id);
    }
}
