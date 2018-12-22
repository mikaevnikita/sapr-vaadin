package ru.mikaev.sapr.ui.preprocessor;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.apache.commons.lang3.StringUtils;
import ru.mikaev.sapr.domain.PreprocessorData;
import ru.mikaev.sapr.dto.PreprocessorDataDto;
import ru.mikaev.sapr.mapping.PreprocessorDataMapper;
import ru.mikaev.sapr.service.PreprocessorDataService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
