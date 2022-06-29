package com.myjava.core.service;

import com.myjava.core.pojo.good.Goods;
import com.myjava.core.pojo.request.GoodsEntity;
import com.myjava.core.pojo.request.PageRequest;
import com.myjava.core.pojo.response.PageResponse;
import org.springframework.stereotype.Service;

@Service
public interface GoodsService {

    void addGood(GoodsEntity entity);

    PageResponse<Goods> mangerGetPage(PageRequest request);

    PageResponse<Goods> sellerGetPage(PageRequest<String> request, String sellerId);

    GoodsEntity getOneById(Long id);

    void updateGoods(GoodsEntity entity);

    void delete(Long[] ids);

    void updateStatus(Long[] ids, String status);

    void sellerSubmitAudit(Long[] ids);

    void auditAccept(Long[] ids);

    void rejectApply(Long[] ids);
}
