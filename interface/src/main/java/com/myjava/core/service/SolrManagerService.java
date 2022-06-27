package com.myjava.core.service;

import java.util.List;

public interface SolrManagerService {
    void saveItemToSolr(List items);

    void deleteItemByGoodsId(List goodsIds);
}