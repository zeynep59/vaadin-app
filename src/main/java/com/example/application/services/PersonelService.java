package com.example.application.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.application.models.Personel;
import com.example.application.repositories.PersonelRepository;

@Service
public class PersonelService {

    private final PersonelRepository personelRepository;

    public PersonelService(PersonelRepository personelRepository){
            this.personelRepository = personelRepository;
    }

    public List<Personel> getAllPersonel(){
        return personelRepository.findAll();
    }

    public void createPersonel(String name, String surname, String tc){
        Personel personel = new Personel();
        personel.setName(name);
        personel.setSurname(surname);
        personel.setTc(tc);
        personelRepository.save(personel);
        
    }
    public void addDummyPersonel() {
        for (int i = 1; i <= 10; i++) {
            Personel personel = new Personel();
            personel.setName("Personel" + i);
            personel.setSurname("lastname " + i);
            personel.setTc(i + "64374563" + i);
            personelRepository.save(personel);
        }
    }


}
