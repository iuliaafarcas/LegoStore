package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import net.rgielen.fxweaver.core.FxmlView;
import org.example.model.LegoStore;
import org.example.service.LegoStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Component
@FxmlView("/mainUi.fxml")
public class LegoStoreController {
    @Autowired
    public LegoStoreController(LegoStoreService legoStoreService) {
        this.legoStoreService = legoStoreService;

    }

    @FXML
    public void initialize() {
        data = FXCollections.observableList(legoStoreService.getList());
        idTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityTableColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceTableColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        numberOfPiecesTableColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfPieces"));
        tableView.setItems(data);
    }

    public void addLego() {
        LegoStore newLego = new LegoStore(null, nameField.getText(),
                Integer.valueOf(numberOfPiecesField.getText()),
                Integer.valueOf(priceField.getText()),
                Integer.valueOf(quantityField.getText())
                );
        try {
            LegoStore savedLego = legoStoreService.addLego(newLego);
            data.add(savedLego);
        } catch (Exception e) {
            showError(e, "Lego not added!");
        }
    }
    private int getPositionInListForId(Long id) {
        return IntStream.range(0, data.size())
                .filter(position -> data.get(position).getId().equals(id))
                .findFirst()
                .getAsInt();

    }

    public void updateLego() {
        try {
            LegoStore newLego = new LegoStore(Long.valueOf(idUpdateField.getText()),
                    nameUpdateField.getText(),
                    Integer.valueOf(numberOfPiecesUpdateField.getText()),
                    Integer.valueOf(priceUpdateField.getText()),
                    Integer.valueOf(quantityUpdateField.getText())
                   );
            LegoStore savedLego = legoStoreService.updateLego(newLego);
            int legoIndex= getPositionInListForId(Long.valueOf(idUpdateField.getText()));
            data.set(legoIndex, savedLego);
        } catch (Exception e) {
            showError(e, "Lego not updated!");
        }
    }

    public void deleteLego() {
        try {
            Long id = Long.valueOf(idDeleteField.getText());
            legoStoreService.deleteLego(id);
            data.remove(getPositionInListForId(id));
            idDeleteField.clear();
        } catch (Exception e) {
            showError(e, "Lego not deleted!");
        }
    }

    public void logOut() {
        SecurityContextHolder.clearContext();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Logged out");
        alert.setContentText("You've been logged out");
        alert.show();
    }


    private void showError(Exception e, String title) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(e.getMessage());
        alert.show();
    }

    private final LegoStoreService legoStoreService;
    @FXML
    private TableView<LegoStore> tableView;

    @FXML
    private TableColumn<LegoStore, Long> idTableColumn;

    @FXML
    private TableColumn<LegoStore, String> nameTableColumn;
    @FXML
    private TableColumn<LegoStore, Integer> quantityTableColumn;

    @FXML
    private TableColumn<LegoStore, Integer> priceTableColumn;
    @FXML
    private TableColumn<LegoStore, Integer> numberOfPiecesTableColumn;


    @FXML
    private TextField idDeleteField;
    @FXML
    private TextField idUpdateField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField numberOfPiecesField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField quantityField;

    @FXML
    private TextField nameUpdateField;
    @FXML
    private TextField priceUpdateField;
    @FXML
    private TextField numberOfPiecesUpdateField;
    @FXML
    private TextField quantityUpdateField;

    private ObservableList<LegoStore> data;

}
