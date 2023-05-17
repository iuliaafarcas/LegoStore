package org.example.service;

import jakarta.transaction.Transactional;
import org.example.aspects.RequiresAuthentication;
import org.example.aspects.RequiresRole;
import org.example.model.LegoStore;
import org.example.repository.LegoStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LegoStoreService {

    private final LegoStoreRepository repository;

    @Autowired
    public LegoStoreService( LegoStoreRepository repository) {

        this.repository = repository;
    }

    @RequiresAuthentication
    public LegoStore addLego(LegoStore item) {
        return this.repository.save(item);
    }

    @RequiresRole("ADMIN")
    public void deleteLego(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("LegoStore with id " + id + " does NOT exist!");
        }
        repository.deleteById(id);

    }

    @RequiresRole("ADMIN")
    @Transactional
    public LegoStore updateLego(LegoStore item) {
        LegoStore newItem = this.repository.findById(item.getId()).orElseThrow(() -> new RuntimeException("Game with id " + item.getId() + " not found"));

        newItem.setName(item.getName());
        newItem.setQuantity(item.getQuantity());
        newItem.setPrice(item.getPrice());
        newItem.setNumberOfPieces(item.getNumberOfPieces());
        return repository.save(newItem);
    }

    public List<LegoStore> getList() {
        return this.repository.findAll();
    }

}
