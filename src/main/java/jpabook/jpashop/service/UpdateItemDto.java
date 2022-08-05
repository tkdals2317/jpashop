package jpabook.jpashop.service;

import jpabook.jpashop.controller.BookForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateItemDto {
    private String name;
    private int price;
    private int stockQuantity;

    public static UpdateItemDto of(BookForm form){
        UpdateItemDto updateItemDto = new UpdateItemDto();
        updateItemDto.setName(form.getName());
        updateItemDto.setPrice(form.getPrice());
        updateItemDto.setStockQuantity(form.getStockQuantity());
        return updateItemDto;
    }
}
