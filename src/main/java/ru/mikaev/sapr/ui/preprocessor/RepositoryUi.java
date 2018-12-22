package ru.mikaev.sapr.ui.preprocessor;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import ru.mikaev.sapr.domain.PreprocessorData;
import ru.mikaev.sapr.dto.PreprocessorDataDto;
import ru.mikaev.sapr.mapping.PreprocessorDataMapper;
import ru.mikaev.sapr.service.PreprocessorDataService;

import java.util.ArrayList;
import java.util.List;

@SpringComponent
@UIScope
@Route("preprocessor")
public class RepositoryUi extends VerticalLayout {
    private final Grid<PreprocessorDataDto> grid;
    private final PreprocessorDataService dataService;
    private final PreprocessorDataMapper mapper;

    public RepositoryUi(PreprocessorDataService dataService, PreprocessorDataMapper mapper) {
        this.grid = new Grid<>(PreprocessorDataDto.class);
        this.dataService = dataService;
        this.mapper = mapper;

        grid.setSizeFull();
        grid.addColumn(PreprocessorDataDto::getDataName).setHeader("Name");
        grid.addColumn(PreprocessorDataDto::getCreationDateTime).setHeader("Creation");

        HorizontalLayout mainLayout = new HorizontalLayout();
        VerticalLayout leftPanel = new VerticalLayout();
        VerticalLayout rightPanel = new VerticalLayout();

        leftPanel.add(grid);
        mainLayout.add(leftPanel);
        mainLayout.add(rightPanel);

        updatePreprocessorData();

        add(mainLayout);
    }

    private void updatePreprocessorData() {
        final List<PreprocessorData> dataList = dataService.findAll();
        final List<PreprocessorDataDto> dtos = new ArrayList<>();
        for(PreprocessorData data: dataList){
            dtos.add(mapper.toDto(data));
        }
        grid.setItems(dtos);
    }
}
