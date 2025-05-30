package linhlang.product.service.impl;

import linhlang.commons.exceptions.BusinessException;
import linhlang.commons.model.PageData;
import linhlang.commons.model.QueryRequest;
import linhlang.product.constants.Errors;
import linhlang.product.controller.request.CommonSearchReq;
import linhlang.product.controller.request.SaveProviderReq;
import linhlang.product.model.Provider;
import linhlang.product.repository.ProviderRepository;
import linhlang.product.service.ProviderService;
import linhlang.product.service.mapper.ProviderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService {

    private final ProviderMapper providerMapper;
    private final ProviderRepository providerRepository;

    @Override
    public PageData<Provider> query(CommonSearchReq request) {
        return providerRepository.query(request);
    }

    @Override
    public Provider create(SaveProviderReq provider) {
        Provider sameName = providerRepository.findSameName(provider.name());
        if (sameName != null) {
            throw new BusinessException(Errors.PRODUCT_EXISTED);
        }

        Provider newProvider = providerMapper.toEntity(provider);
        providerRepository.save(newProvider);
        return newProvider;
    }

    @Override
    public Provider update(String id, SaveProviderReq req) {
        Provider provider = providerRepository.load(id);
        if (provider == null) {
            throw new BusinessException(Errors.PROVIDER_NOTFOUND);
        }

        providerMapper.update(provider, req);
        providerRepository.save(provider);
        return provider;
    }

    @Override
    public void delete(String id) {
        Provider provider = providerRepository.load(id);
        if (provider == null) {
            throw new BusinessException(Errors.PROVIDER_NOTFOUND);
        }

        providerRepository.delete(id);
    }
}
