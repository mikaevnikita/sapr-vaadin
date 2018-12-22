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
@Route("preprocessor")
public class RepositoryUi extends VerticalLayout {
    private final Grid<PreprocessorDataDto> grid;
    private final PreprocessorDataService dataService;
    private final PreprocessorDataMapper mapper;

    private final ConfirmDialog deleteConfirmDialog;
    private final TextField dataNameTextField;

    public RepositoryUi(PreprocessorDataService dataService,
                        PreprocessorDataMapper mapper) {
        this.dataService = dataService;
        this.mapper = mapper;

        grid = new Grid<>();
        dataNameTextField = new TextField();

        deleteConfirmDialog = new ConfirmDialog();
        deleteConfirmDialog.setHeader("Confirm delete");
        deleteConfirmDialog.setText("Are you sure you want to delete the data set?");
        deleteConfirmDialog.setConfirmText("Delete");
        deleteConfirmDialog.setCancelText("Cancel");
        deleteConfirmDialog.setRejectable(true);
        deleteConfirmDialog.setRejectText("Cancel");
        deleteConfirmDialog.addConfirmListener(e -> {
            PreprocessorDataDto selectedItem =
                    grid.getSelectionModel().getFirstSelectedItem().get();
            dataService.deleteData(selectedItem);
            deleteConfirmDialog.close();
            updatePreprocessorData();
        });
        deleteConfirmDialog.addRejectListener(e -> deleteConfirmDialog.close());
        deleteConfirmDialog.setConfirmButtonTheme("error primary");

        grid.addColumn(PreprocessorDataDto::getDataName).setHeader("Name");
        grid.addColumn(PreprocessorDataDto::getCreationDateTime).setHeader("Creation");
        grid.addColumn(dto -> dto.getConstruction().getRods().size()).setHeader("Rods");

        VerticalLayout mainLayout = new VerticalLayout();
        HorizontalLayout createPanel = getCreatePanel();

        mainLayout.add(createPanel, grid);

        add(mainLayout);

        updatePreprocessorData();
    }

    private void updatePreprocessorData() {
        final String dataName = dataNameTextField.getValue();
        final List<PreprocessorData> dataList;

        if (StringUtils.isBlank(dataName)) {
            dataList = dataService.findAll();
        } else {
            dataList = dataService.findLikeDataName(dataName);
        }

        final List<PreprocessorDataDto> dtos = new ArrayList<>();
        for (PreprocessorData data : dataList) {
            dtos.add(mapper.fromPreprocessorData(data));
        }
        grid.setItems(dtos);
    }

    private void deleteData() {
        final Optional<PreprocessorDataDto> selectedItem =
                grid.getSelectionModel().getFirstSelectedItem();
        if (!selectedItem.isPresent()) {
            Notification.show("You must select a data set!");
        } else {
            deleteConfirmDialog.open();
        }
    }

    private HorizontalLayout getCreatePanel() {
        HorizontalLayout createPanel = new HorizontalLayout();

        Button createButton = new Button("Create");
        createButton.addClickListener(event -> {
            dataService.createData(dataNameTextField.getValue());
            updatePreprocessorData();
        });

        Button deleteButton = new Button("Delete");
        deleteButton.addClickListener(event -> deleteData());

        Button updateButton = new Button("Update");

        dataNameTextField.setPlaceholder("Filter by name..");
        dataNameTextField.setValueChangeMode(ValueChangeMode.EAGER);
        dataNameTextField.addValueChangeListener(e -> updatePreprocessorData());

        createPanel.add(dataNameTextField, createButton, deleteButton, updateButton);

        return createPanel;
    }
}
