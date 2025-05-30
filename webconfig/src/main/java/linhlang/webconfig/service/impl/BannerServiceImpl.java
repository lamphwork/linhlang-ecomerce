package linhlang.webconfig.service.impl;

import linhlang.commons.exceptions.BusinessException;
import linhlang.commons.model.PageData;
import linhlang.commons.model.QueryRequest;
import linhlang.webconfig.constants.Errors;
import linhlang.webconfig.controller.request.SaveBannerRequest;
import linhlang.webconfig.model.Banner;
import linhlang.webconfig.repository.BannerRepository;
import linhlang.webconfig.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;

    @Override
    public String createBanner(SaveBannerRequest req) {
        Banner banner = new Banner(
                UUID.randomUUID().toString(),
                req.getImageUrl(),
                req.getOrderIndex()
        );

        bannerRepository.save(banner);
        return banner.getId();
    }

    @Override
    public String updateBanner(String id, SaveBannerRequest req) {
        Banner banner = bannerRepository.load(id);
        if (banner == null) {
            throw new BusinessException(Errors.BANNER_NOTFOUND);
        }

        banner.setImageUrl(req.getImageUrl());
        banner.setOrderIndex(req.getOrderIndex());
        bannerRepository.save(banner);
        return id;
    }

    @Override
    public String deleteBanner(String id) {
        Banner banner = bannerRepository.load(id);
        if (banner == null) {
            throw new BusinessException(Errors.BANNER_NOTFOUND);
        }

        bannerRepository.delete(id);
        return id;
    }

    @Override
    public PageData<Banner> query(QueryRequest request) {
        return bannerRepository.queryAll(request);
    }
}
