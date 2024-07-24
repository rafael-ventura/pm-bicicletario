package com.example.bicicletario.bicicletario.infraestructure;
import com.example.bicicletario.bicicletario.domain.Email;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class EmailRepository {
    private final Map<Integer, Email> emailStorage = new HashMap<>();

    public Email save(Email email) {
        int id = email.getId();
        email.setId(id);
        emailStorage.put(id, email);
        return email;
    }

    public Email findById(int id) {
        return emailStorage.get(id);
    }

    public Map<Integer, Email> findAll() {
        return new HashMap<>(emailStorage);
    }
}
