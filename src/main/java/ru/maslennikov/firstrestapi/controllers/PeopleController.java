package ru.maslennikov.firstrestapi.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.maslennikov.firstrestapi.dto.PersonDTO;
import ru.maslennikov.firstrestapi.models.Person;
import ru.maslennikov.firstrestapi.servicies.PeopleService;
import ru.maslennikov.firstrestapi.util.PersonErrorResponse;
import ru.maslennikov.firstrestapi.util.PersonNotCreatedException;
import ru.maslennikov.firstrestapi.util.PersonNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/people")
public class PeopleController {
    private final PeopleService peopleService;
    private final ModelMapper modelMapper;

    @Autowired
    public PeopleController(PeopleService peopleService, ModelMapper modelMapper) {
        this.peopleService = peopleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<PersonDTO> findAllPeople() {
        return peopleService.findAll().stream().map(this::convertToPersonDTO)
                .collect(Collectors.toList());//повторить как это записывается
    }

    @GetMapping("/{id}")
    public PersonDTO findPeopleById(@PathVariable("id") int id) {
        Optional<Person> person = peopleService.findById(id);

        return modelMapper.map(person.orElseThrow(PersonNotFoundException::new), PersonDTO.class);
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> createPeople(@RequestBody @Valid PersonDTO personDTO,
                                               BindingResult bindingResult) throws PersonNotCreatedException {
    if (bindingResult.hasErrors()) {
        StringBuilder errors = new StringBuilder();
        List<FieldError> allErrors = bindingResult.getFieldErrors();
        for (FieldError error : allErrors) {
            errors.append(error.getField()).append(": ")
                    .append(error.getDefaultMessage()).append(";");
        }
        throw new PersonNotCreatedException(errors.toString());
    }

    peopleService.save(convertToPerson(personDTO));//основная часть кода

    return ResponseEntity.ok(HttpStatus.OK);
    }


    @ExceptionHandler(PersonNotFoundException.class)
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e) {
        PersonErrorResponse pe = new PersonErrorResponse(
                "This person with this id not found!",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(pe, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(PersonNotCreatedException.class)
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotCreatedException e) {
        PersonErrorResponse pe = new PersonErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(pe, HttpStatus.BAD_REQUEST);

    }

    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    private PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }

}
