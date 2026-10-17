/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dialog;

import interface_noyau_fonctionnel.InterfaceNoyauFonctionnel;
import java.awt.EventQueue;
import java.time.LocalDate;
import presentation.FrameReservation;

public class DialogReservation {

    private FrameReservation frameReservation;
    private InterfaceNoyauFonctionnel inf;
    
    private LocalDate selectedDate;
    private String selectedTime;
    private int selectedNbPersons;
    private int selectedTable;

    public DialogReservation(InterfaceNoyauFonctionnel inf) {
        this.inf = inf;
    }

    public void initDialog() {
        frameReservation = new FrameReservation();
        frameReservation.initFrame();
        frameReservation.setDialog(this);
        frameReservation.setVisible(true);
    }

    public void handleDateSelectedEvent(LocalDate date) {
        selectedDate = date;
        selectedTime = null;
        selectedNbPersons = 0;
        frameReservation.clearTimeSelection();
        frameReservation.clearNbPersonsSelection();
        frameReservation.setNbPersonsStepEnabled(false);
        resetTableStep();
        frameReservation.setTimeStepEnabled(date != null);
    }

    public void handleTimeSelectedEvent(String time) {
        selectedTime = time;
        frameReservation.setNbPersonsStepEnabled(true);
        updateAvailableTables();
    }

    public void handleNumOfPersonsSelectedEvent(int nbPersons) {
        selectedNbPersons = nbPersons;
        updateAvailableTables();
    }

    public void handleTableSelectedEvent(int numTable) {
        selectedTable = numTable;
        frameReservation.setValidationEnabled(true);
    }

    public void handleCancelEvent() {
        frameReservation.clearDate();
    }

    public void handleValidationEvent() {
        String formattedDate = selectedDate.format(
                java.time.format.DateTimeFormatter.ofPattern("d MMMM yyyy", java.util.Locale.FRENCH));
        frameReservation.showConfirmation("Réservation validée pour le " + formattedDate
                + " à " + selectedTime + " pour " + selectedNbPersons
                + " personnes à la Table " + selectedTable + ".");
    }
    
    
    
    private void updateAvailableTables() {
        resetTableStep();
        if (selectedNbPersons > 0) {
            String[] availableTables = inf.trouverTableDisponible(
                    selectedDate.getDayOfMonth(), selectedDate.getMonthValue(),
                    selectedNbPersons, selectedTime);
            frameReservation.showAvailableTables(availableTables);
            frameReservation.setTableStepEnabled(true);
        }
    }

//Pour reset l'étape de selection de la table
    private void resetTableStep() {
        selectedTable = 0;
        frameReservation.clearAvailableTables();
        frameReservation.setTableStepEnabled(false);
        frameReservation.setValidationEnabled(false);
    }

    public static void main(String[] args) {
        DialogReservation dialog = new DialogReservation(new InterfaceNoyauFonctionnel());
        EventQueue.invokeLater(() -> {
            dialog.initDialog();
        });
    }

}
