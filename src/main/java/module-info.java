module AgendaCheck {

    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires poi.ooxml;
    requires poi;
    requires jfreechart;
    requires java.desktop;

    exports org.AgendaCheck.UserGI to javafx.graphics;
    exports org.AgendaCheck.UserGI.controllers to javafx.fxml;

    opens org.AgendaCheck.UserGI.controllers;


}