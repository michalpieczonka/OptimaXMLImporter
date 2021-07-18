package pl.apisnet.backEND;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptimaTest {
    Optima mainOptima = new Optima("Admin","","Test","C:/Program Files (x86)/Comarch ERP Optima");

    @Test
    void connectToOptima() {
        assertAll(
                () -> assertNull(mainOptima.getOptimaProgramID()),
                () ->  mainOptima.connectToOptima(),
                () -> assertNotNull(mainOptima.getOptimaProgramID())
        );
    }

    @Test
    void disconnectFromOptima() {
        mainOptima.disconnectFromOptima();
        assertNull(mainOptima.getOptimaProgramID());
    }

    @Test
    void getOptimaConnection() {
        mainOptima.disconnectFromOptima();
        assertFalse(mainOptima.checkOptimaConnection());
    }

    @Test
    void getOptimaProgramID() {
        mainOptima.connectToOptima();

    }
}