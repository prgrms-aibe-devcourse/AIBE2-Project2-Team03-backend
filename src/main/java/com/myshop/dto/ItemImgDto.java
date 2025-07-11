package com.myshop.dto;

import com.myshop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImgDto {

    private Long id;
    private String imgName; // 이미지 파일명
    private String oriImgName; // 원본 이미지 파일명
    private String imgUrl; // 이미지 조회 경로
    private String repImgYn; // 대표 이미지 여부

    private static ModelMapper mapper = new ModelMapper();

    public static ItemImgDto of(ItemImg itemImg) {
        return mapper.map(itemImg, ItemImgDto.class);
    }
}
