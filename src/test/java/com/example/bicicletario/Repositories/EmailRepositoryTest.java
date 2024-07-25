package com.example.bicicletario.Repositories;

import com.example.bicicletario.bicicletario.domain.Email;
import com.example.bicicletario.bicicletario.infraestructure.EmailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EmailRepositoryTest {

    private EmailRepository emailRepository;

    @BeforeEach
    void setUp() {
        emailRepository = new EmailRepository();
    }

    @Test
    void testSaveAndFindById() {
        Email email = new Email();
        email.setId(1); // Set ID to a specific value

        Email savedEmail = emailRepository.save(email);

        assertNotNull(savedEmail);
        assertEquals(1, savedEmail.getId());
        Email retrievedEmail = emailRepository.findById(savedEmail.getId());
        assertNotNull(retrievedEmail);
        assertEquals(savedEmail.getId(), retrievedEmail.getId());
    }

    @Test
    void testFindAll() {
        Email email1 = new Email();
        email1.setId(1);
        emailRepository.save(email1);

        Email email2 = new Email();
        email2.setId(2);
        emailRepository.save(email2);

        Map<Integer, Email> allEmails = emailRepository.findAll();
        assertNotNull(allEmails);
        assertEquals(2, allEmails.size());
        assertTrue(allEmails.containsKey(1));
        assertTrue(allEmails.containsKey(2));
    }
}
