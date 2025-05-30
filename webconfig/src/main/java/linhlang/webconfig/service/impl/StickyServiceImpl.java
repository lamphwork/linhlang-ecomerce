package linhlang.webconfig.service.impl;

import linhlang.commons.exceptions.BusinessException;
import linhlang.commons.model.PageData;
import linhlang.commons.model.QueryRequest;
import linhlang.webconfig.constants.Errors;
import linhlang.webconfig.controller.request.SaveStickyRequest;
import linhlang.webconfig.model.Sticky;
import linhlang.webconfig.repository.StickyRepository;
import linhlang.webconfig.service.StickyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class StickyServiceImpl implements StickyService {

    private final StickyRepository stickyRepository;

    @Override
    public String create(SaveStickyRequest req) {
        Sticky sticky = new Sticky(
                UUID.randomUUID().toString(),
                req.getTitle(),
                req.getContent(),
                Optional.ofNullable(req.getVisible()).orElse(true)
        );
        stickyRepository.save(sticky);
        return sticky.getId();
    }

    @Override
    public String update(String id, SaveStickyRequest req) {
        Sticky sticky = stickyRepository.load(id);
        if (sticky == null) {
            throw new BusinessException(Errors.STICKY_NOTFOUND);
        }

        sticky.setContent(req.getContent());
        sticky.setTitle(req.getTitle());
        Optional.ofNullable(req.getVisible()).ifPresent(sticky::setVisible);
        stickyRepository.save(sticky);
        return id;
    }

    @Override
    public String delete(String id) {
        Sticky sticky = stickyRepository.load(id);
        if (sticky == null) {
            throw new BusinessException(Errors.STICKY_NOTFOUND);
        }

        stickyRepository.delete(id);
        return id;
    }

    @Override
    public PageData<Sticky> query(QueryRequest request) {
        return  stickyRepository.queryAll(request);
    }
}
