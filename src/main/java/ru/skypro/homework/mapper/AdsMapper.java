package ru.skypro.homework.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.dto.FullAdsDto;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Image;


@Mapper(componentModel = "spring", uses = {ImageToString.class})
public interface AdsMapper {


    @Mapping(target = "pk", source = "id")
    @Mapping(target = "image", source = "image")//uses = ImageToString.class
    AdsDto adsToDTO(Ads ads);

    @InheritInverseConfiguration
    Ads adsToEntity(CreateAdsDto dto);
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "image", source = "image")//uses = ImageToString.class
    FullAdsDto adsToFullAdsDTO(Ads ads);
}
