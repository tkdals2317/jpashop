package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    /**
     * 변경 감지를 통한 수정
     * 필요한 아이템만 골라서 변경 가능하다
     */
    @Transactional
    public void updateItem(Long itemId, UpdateItemDto itemDto) {
        Item item = itemRepository.findOne(itemId);
        item.change(itemDto.getName(), itemDto.getPrice(), itemDto.getStockQuantity());
    }
    
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long id) {
        return itemRepository.findOne(id);
    }
}
