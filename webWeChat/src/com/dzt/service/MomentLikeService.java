package com.dzt.service;

/**
 * @author Z
 */
public interface MomentLikeService {
    /**
     * 删除朋友圈对应信息
     * @param id
     */
    void removeByMomentId(Integer id);

    /**
     * 得到一个朋友圈的点赞数
     * @param id
     * @return
     */
    Integer getLikeCount(Integer id);

    /**
     * 查看是否已经将某个朋友圈点赞
     * @param momentId
     * @param userId
     * @return
     */
    boolean isLike(Integer momentId,Integer userId);

    /**
     * 添加一个点赞
     * @param momentId
     * @param userId
     */
    void addLike(Integer momentId,Integer userId);

    /**
     * 将用户的某条点赞删除
     * @param momentId
     * @param userId
     */
    void removeLike(Integer momentId,Integer userId);
}
