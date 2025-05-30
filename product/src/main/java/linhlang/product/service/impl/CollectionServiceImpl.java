package linhlang.product.service.impl;

import linhlang.commons.aop.annotation.LockBusiness;
import linhlang.commons.exceptions.BusinessException;
import linhlang.commons.model.PageData;
import linhlang.product.constants.Errors;
import linhlang.product.controller.request.CommonSearchReq;
import linhlang.product.controller.request.SaveCollectionReq;
import linhlang.product.model.Collection;
import linhlang.product.repository.CollectionRepository;
import linhlang.product.service.CollectionService;
import linhlang.product.service.mapper.CollectionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {

    private final CollectionMapper collectionMapper;
    private final CollectionRepository collectionRepository;

    @Override
    public Collection create(SaveCollectionReq request) {
        Collection collection = collectionMapper.toEntity(request);
        collectionRepository.save(collection);
        return collection;
    }

    @Override
    @LockBusiness(business = "update_collection", key = "#id", timeout = 60)
    public Collection update(String id, SaveCollectionReq request) {
        Collection dbValue = collectionRepository.load(id);
        if (dbValue == null) {
            throw new BusinessException(Errors.COLLECTION_NOTFOUND);
        }

        collectionMapper.update(dbValue, request);
        collectionRepository.save(dbValue);
        return dbValue;
    }

    @Override
    public Collection detail(String id) {
        Collection dbValue = collectionRepository.load(id);
        if (dbValue == null) {
            throw new BusinessException(Errors.COLLECTION_NOTFOUND);
        }

        return dbValue;
    }

    @Override
    public PageData<Collection> search(CommonSearchReq req) {
        return collectionRepository.query(req);
    }

    @Override
    public Collection delete(String id) {
        Collection collection = detail(id);
        collectionRepository.delete(id);
        return collection;
    }
}
