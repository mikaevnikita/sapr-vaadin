package ru.mikaev.sapr.ui.preprocessor;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import ru.mikaev.sapr.mapping.PreprocessorDataMapper;
import ru.mikaev.sapr.service.PreprocessorDataService;

@SpringComponent
@UIScope
@Route("preprocessor-ui")
public class PreprocessorUi extends VerticalLayout {
    private final PreprocessorDataService dataService;
    private final PreprocessorDataMapper mapper;

    public PreprocessorUi(PreprocessorDataService dataService,
                          PreprocessorDataMapper mapper) {
        this.dataService = dataService;
        this.mapper = mapper;
    }
}
