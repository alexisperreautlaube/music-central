package com.apa.common.mapper;

import com.apa.common.entities.enums.RatingType;
import com.apa.common.entities.media.Rating;
import com.apa.common.entities.media.VolumioMedia;
import com.apa.common.entities.util.MediaReference;
import com.apa.core.dto.media.RatingDto;

public class RatingMapper {
    public static Rating toRating(RatingDto ratingDto, RatingType ratingType) {
        return Rating.builder()
                .mediaReference(MediaReference.builder()
                        .id(ratingDto.getTrackId())
                        .clazz(getClass(ratingType))
                        .build())
                .rating(ratingDto.getRating())
                .rateDate(ratingDto.getRateDate())
                .ratingType(ratingType)
                .build();
    }

    private static String getClass(RatingType ratingType) {
        if (RatingType.VOLUMIO_MANUAL.equals(ratingType)) {
            return VolumioMedia.class.getName();
        }
        throw new RuntimeException("unsuported Rating type");
    }
}
