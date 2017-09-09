package top.appx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.appx.config.AppxConfig;
import top.appx.dao.ArticleDao;
import top.appx.dao.ArticleGroupDao;
import top.appx.entity.Article;
import top.appx.entity.ArticleGroup;
import top.appx.entity.vo.ArticleDetailVO;
import top.appx.entity.vo.ArticleIndexVO;
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


    @Autowired
    private AppxConfig appxConfig;

    private void delRedisByArticleId(long id){
        String key = appxConfig.getDomain()+ "_articleDetailVO_id_"+id;
        String key2 = appxConfig.getDomain()+"_article_id_"+id;
        redisTemplate.delete(key);
        redisTemplate.delete(key2);
    }




    @Override
    public PageInfo<Article> findPage(Object search,Integer pageNum, Integer pageSize) throws Exception {
        PageHelper.startPage(pageNum,pageSize);
        Article article = new Article();
        List<Article> articleList = articleDao.find(search);
        return new PageInfo<Article>(articleList);
    }

    @Override
    public PageInfo<ArticleIndexVO> findPageVO(Object search,Integer pageNum, Integer pageSize) throws Exception {
        PageHelper.startPage(pageNum,pageSize);
        Article article = new Article();
        List<ArticleIndexVO> articleList = articleDao.findVO(search);
        return new PageInfo<ArticleIndexVO>(articleList);
    }
   // private RedisTemp

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Article findById(Long id) {
        String key = appxConfig.getDomain()+ "_articleDetailVO_id_"+id;
        Object object = redisTemplate.opsForValue().get(key);
        if(object==null){
            Article article =articleDao.selectByPrimaryKey(id);
            redisTemplate.opsForValue().set(key,article);
            return article;
        }else{
            return (Article)object;
        }

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
        ids.forEach(id -> {
            delRedisByArticleId(id);
        });
        articleDao.deleteByPrimaryKeys(ids);

    }

    @Override
    public List<ArticleIndexVO> index() {
        return articleDao.index();
    }

    @Override
    public List<ArticleIndexVO> articles() {
        return articleDao.articles();
    }

    @Override
    public List<Article> findByArticleGroupId(Long id) {
        PageHelper.startPage(1,20);
        return articleDao.findByArticleGroupId(id);
    }

    @Override
    public ArticleDetailVO detail(Long id) {
        String key = appxConfig.getDomain()+ "_articleDetailVO_id_"+id;
        Object object = redisTemplate.opsForValue().get(key);
        if(object==null){
            ArticleDetailVO article =articleDao.detail(id);
            redisTemplate.opsForValue().set(key,article);
            return article;
        }else{
            return (ArticleDetailVO)object;
        }

       // return articleDao.detail(id);
    }

    @Override
    public void update(Article article) {
        delRedisByArticleId(article.getId());
        articleDao.updateByPrimaryKey(article);
    }


}
