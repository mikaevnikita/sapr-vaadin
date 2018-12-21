package ru.mikaev.sapr.ui.preprocessor;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import ru.mikaev.sapr.dto.PreprocessorDataDto;
import ru.mikaev.sapr.service.PreprocessorDataService;

@SpringComponent
@UIScope
public class RepositoryUi extends VerticalLayout {
    private final Grid<PreprocessorDataDto> grid;
    private final PreprocessorDataService dataService;

    public RepositoryUi(PreprocessorDataService dataService) {
        this.grid = new Grid<>(PreprocessorDataDto.class);
        this.dataService = dataService;
        add(grid);
        listPreprocessorData();
    }

    private void listPreprocessorData() {
        grid.setItems(dataService.findAll());
    }
}
