package ru.mikaev.sapr.ui.preprocessor;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import ru.mikaev.sapr.common.PreprocessorDataHolder;
import ru.mikaev.sapr.mapping.PreprocessorDataMapper;
import ru.mikaev.sapr.service.PreprocessorDataService;

import java.util.Objects;

@SpringComponent
@UIScope
@Route("preprocessor")
public class PreprocessorUi
        extends HorizontalLayout
        implements AfterNavigationObserver {
    private final PreprocessorDataService dataService;
    private final PreprocessorDataMapper mapper;

    private final PreprocessorDataHolder holder;

    public PreprocessorUi(PreprocessorDataService dataService,
                          PreprocessorDataMapper mapper,
                          PreprocessorDataHolder holder) {
        this.dataService = dataService;
        this.mapper = mapper;
        this.holder = holder;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        if (Objects.nonNull(holder.getPreprocessorData())) {
            Notification.show(holder.getPreprocessorData().getDataName());
        }
    }
}
