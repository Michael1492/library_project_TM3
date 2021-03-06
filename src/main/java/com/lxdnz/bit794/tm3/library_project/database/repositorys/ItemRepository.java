package com.lxdnz.bit794.tm3.library_project.database.repositorys;

import com.lxdnz.bit794.tm3.library_project.system.model.concrete.Item;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemRepository extends CrudRepository<Item, Long> {

    List<Item> findByTitle(String title);
    Item findOne(long id);
    List<Item> findAllByTitleIgnoreCaseContaining(String string);
    List<Item> findAllByCreatorIgnoreCaseContaining(String string);
    List<Item> findAllByTitleIgnoreCaseContainingOrIgnoreCaseCreatorContaining(String title, String creator);

}
