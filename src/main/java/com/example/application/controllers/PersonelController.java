package com.example.application.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.application.models.Personel;
import com.example.application.services.PersonelService;



@RestController
public class PersonelController {

    private final PersonelService personelService;


    public PersonelController(PersonelService personelService) {
        this.personelService = personelService;
        personelService.addDummyPersonel();

    }

    @GetMapping("/api/personel")
    public List<Personel> getAllPersonel() {
        return personelService.getAllPersonel();
    }
    
    @PostMapping("/api/personel")
public ResponseEntity<String> postMethodName(@RequestBody Personel personel) {
        personelService.createPersonel(personel.getName(), personel.getSurname(), personel.getTc());
    return ResponseEntity.ok("New personel added successfully");
}


}
