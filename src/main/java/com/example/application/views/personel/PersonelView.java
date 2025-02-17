package com.example.application.views.personel;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import com.example.application.models.Personel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Personel")
@Route("personel")
@Menu(order = 1, icon = LineAwesomeIconUrl.USERS_SOLID)
public class PersonelView extends VerticalLayout {

    private final RestTemplate restTemplate = new RestTemplate();
    private final Grid<Personel> grid = new Grid<>(Personel.class, false);
    private List<Personel> allPersonels;

    public PersonelView() {
        // search field
        TextField searchField = new TextField();
        searchField.setPlaceholder("Search by name");
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(e -> updateGrid(e.getValue()));

        // create columns
        grid.addColumn(Personel::getName).setHeader("Name");
        grid.addColumn(Personel::getSurname).setHeader("Surname");

        // refresh button
        Button refreshButton = new Button("Refresh", VaadinIcon.REFRESH.create());
        refreshButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        refreshButton.addClickListener(e -> {
            addNewPersonel(); 
            allPersonels = fetchPersonels(); 
            updateGrid(searchField.getValue()); 
        });
        
        // put the button
        HorizontalLayout buttonLayout = new HorizontalLayout(refreshButton);
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        add(searchField, grid, buttonLayout);

        allPersonels = fetchPersonels();
        updateGrid("");
    }

    private List<Personel> fetchPersonels() {
        String url = "http://localhost:8282/api/personel";
        Personel[] personelArray = restTemplate.getForObject(url, Personel[].class);
        return Arrays.asList(personelArray);
    }

    private void addNewPersonel() {
        // create new personel
        Random random = new Random();
        long tc = 10000000000L + (long) (random.nextDouble() * 90000000000L); //generate random tc

        Personel newPersonel = new Personel();
        newPersonel.setName("zeynep");
        newPersonel.setSurname("ztrk");
        newPersonel.setTc(String.valueOf(tc));

        String url = "http://localhost:8282/api/personel";
        ResponseEntity<String> response = restTemplate.postForEntity(url, newPersonel, String.class);


    }

    private void updateGrid(String searchText) {
        // search part
        List<Personel> filteredPersonels = allPersonels.stream()
                .filter(personel -> matchesSearch(personel, searchText))
                .collect(Collectors.toList());

        // update grid
        grid.setItems(filteredPersonels);
    }

    private boolean matchesSearch(Personel personel, String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            return true;
        }
        String lowerCaseSearchText = searchText.toLowerCase();
        return personel.getName().toLowerCase().contains(lowerCaseSearchText);
    }

}