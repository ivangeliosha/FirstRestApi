package ru.maslennikov.firstrestapi.servicies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.maslennikov.firstrestapi.models.Person;
import ru.maslennikov.firstrestapi.repositories.PeopleRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PeopleService {
    private final PeopleRepository peopleRepository;
    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public Optional<Person> findById(int id) {
        return peopleRepository.findById(id);
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }
    @Transactional
    public void save(Person person) {
        enrichPerson(person);
        peopleRepository.save(person);
    }
    private void enrichPerson(Person person) {
        person.setCreatedAt(LocalDateTime.now());
        person.setUpdatedAt(LocalDateTime.now());
        person.setCreatedWho("ADMIN");
    }


}
