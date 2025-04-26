module lead.cheese {
    requires javafx.controls;
    requires javafx.fxml;
    requires metadata.extractor;
    requires im4java;
    requires java.desktop;

    opens lead.cheese to javafx.fxml;
    exports lead.cheese;
    exports lead.cheese.controller;
    opens lead.cheese.controller to javafx.fxml;
    exports lead.cheese.model;
    exports lead.cheese.view;
}