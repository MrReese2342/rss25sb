package fr.univrouen.rss25sb.repository;

import fr.univrouen.rss25sb.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Item findById(int id);
    boolean existsById(int id);
}
