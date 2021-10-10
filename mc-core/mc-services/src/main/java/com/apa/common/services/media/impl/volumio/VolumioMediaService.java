package com.apa.common.services.media.impl.volumio;

import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.repositories.VolumioMediaRepository;
import com.apa.common.services.AbstractMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class VolumioMediaService extends AbstractMediaService<VolumioMedia> {

    private final VolumioMediaRepository volumioMediaRepository;

    @Override
    @Transactional
    public VolumioMedia save(VolumioMedia volumioMedia) {
        return volumioMediaRepository.save(volumioMedia);
    }

    @Override
    public boolean existAndEquals(VolumioMedia media) {
        return volumioMediaRepository.findById(media.getTrackUri())
                .map(m -> m.equals(media))
                .orElse(false);
    }
}
