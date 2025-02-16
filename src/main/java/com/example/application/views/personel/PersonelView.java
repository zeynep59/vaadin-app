package com.example.application.views.personel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.client.RestTemplate;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import com.example.application.models.Personel;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
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
    private final Grid<Personel> grid = new Grid<>(Personel.class, false); // Disable auto-columns
    private List<Personel> allPersonels; 

    public PersonelView() {

        TextField searchField = new TextField();
        searchField.setPlaceholder("Search by name");
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setValueChangeMode(ValueChangeMode.EAGER); 
        searchField.addValueChangeListener(e -> updateGrid(e.getValue())); 

        
        add(searchField);

        // Fetch all personels from the backend
        allPersonels = fetchPersonels();

        grid.addColumn(Personel::getName).setHeader("Name");
        grid.addColumn(Personel::getSurname).setHeader("Surname");

        add(grid);

        updateGrid("");
    }

    private List<Personel> fetchPersonels() {
        String url = "http://localhost:8282/api/personel"; 
        Personel[] personelArray = restTemplate.getForObject(url, Personel[].class);
        return Arrays.asList(personelArray);
    }

    private void updateGrid(String searchText) {
        // Filter personels based on the search text
        List<Personel> filteredPersonels = allPersonels.stream()
                .filter(personel -> matchesSearch(personel, searchText))
                .collect(Collectors.toList());

        // Update the grid according to search
        grid.setItems(filteredPersonels);
    }

    private boolean matchesSearch(Personel personel, String searchText) {
        // Check if the personel matches the search text
        if (searchText == null || searchText.isEmpty()) {
            return true;
        }
        String lowerCaseSearchText = searchText.toLowerCase();
        return personel.getName().toLowerCase().contains(lowerCaseSearchText) ;
    }
}