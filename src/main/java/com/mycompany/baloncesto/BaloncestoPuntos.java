
package com.mycompany.baloncesto;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class BaloncestoPuntos extends JPanel {
    
    private static String archivo = "EstadisticasBaloncesto.xlsx";
    

    public BaloncestoPuntos() {
        initComponents();
        Boton_calcular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcularPuntos();
            }
        });
        
    }
    
    private void calcularPuntos() {
        try {
            int tirosMetidosDe2 = (int) spinner_metidos_2.getValue();
            int tirosMetidosDe3 = (int) spinner_metidos_3.getValue();
            int tiros_hechos_de_2 = (int) spinner_hechos_2.getValue();
            int tiros_hechos_de_3 = (int) spinner_hechos_3.getValue();
            int tiros_libres_metidos = (int) spinner_metidos_libres.getValue();
            int tiros_libres_hechos = (int) spinner_libres_hechos.getValue();
            int puntos_campo = (tirosMetidosDe2 * 2) + (tirosMetidosDe3 * 3);

            double tirosIntentados = tiros_hechos_de_2 + tiros_hechos_de_3;
            double fgPorcentaje = 0;
            double efgPorcentaje = 0;
            double tsPorcentaje = 0;
            

            if (tirosIntentados > 0) {
                fgPorcentaje = ((double) (tirosMetidosDe2 + tirosMetidosDe3) / (tiros_hechos_de_2 + tiros_hechos_de_3)) * 100;
                efgPorcentaje = ((double) (tirosMetidosDe2 + tirosMetidosDe3 + (0.5 * tirosMetidosDe3)) / (tiros_hechos_de_2 + tiros_hechos_de_3)) * 100;
                tsPorcentaje = ((double) (puntos_campo + tiros_libres_metidos) / (2* (tiros_hechos_de_2 + tiros_hechos_de_3 + (0.44 * tiros_libres_hechos)))) * 100;
            }

            String jugador = Campo_rellenar_nombre.getText().trim();

            if (jugador.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ingrese el nombre del jugador para continuar.");
                return;
            }

            guardarEnExcel(jugador,tirosMetidosDe2 , tirosMetidosDe3 , tiros_hechos_de_2 , tiros_hechos_de_3, fgPorcentaje, efgPorcentaje, tiros_libres_hechos , tiros_libres_metidos, tsPorcentaje);
            resetearCampos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrio un error , revise que introdujo los datos correctamente");
        }
    }

    private void guardarEnExcel(String jugador, int tirosDe2, int tirosDe3, int tiros_hechos_de_2, int tiros_hechos_de_3, double fgPorcentaje, double efgPorcentaje,int tiros_libres_hechos, int tiros_libres_metidos, double tsPorcentaje) {
        File fichero = new File(archivo);
        Workbook workbook;
        Sheet sheet;

        try {
            if (fichero.exists()) {
                
                FileInputStream fis = new FileInputStream(fichero);
                workbook = new XSSFWorkbook(fis);
                sheet = workbook.getSheetAt(0);
            } else {
                
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("Estadisticas");
                
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Jugador");
                headerRow.createCell(1).setCellValue("Tiros hechos de 2");
                headerRow.createCell(2).setCellValue("Tiros metidos de 2");
                headerRow.createCell(3).setCellValue("Tiros hechos de 3");
                headerRow.createCell(4).setCellValue("Tiros metidos de 3");
                headerRow.createCell(5).setCellValue("FG%");
                headerRow.createCell(6).setCellValue("eFG%");
                headerRow.createCell(7).setCellValue("Tiros libres hechos");
                headerRow.createCell(8).setCellValue("Tiros libres metidos");
                headerRow.createCell(9).setCellValue("TS%");
                
            }

            int ultimalinea = sheet.getLastRowNum();
            Row row = sheet.createRow(ultimalinea + 1);

            row.createCell(0).setCellValue(jugador);
            row.createCell(1).setCellValue(tiros_hechos_de_2);
            row.createCell(2).setCellValue(tirosDe2);
            row.createCell(3).setCellValue(tiros_hechos_de_3);
            row.createCell(4).setCellValue(tirosDe3);
            row.createCell(5).setCellValue(fgPorcentaje);
            row.createCell(6).setCellValue(efgPorcentaje);
            row.createCell(7).setCellValue(tiros_libres_hechos);
            row.createCell(8).setCellValue(tiros_libres_metidos);
            row.createCell(9).setCellValue(tsPorcentaje);
            
            Row filaMedia = sheet.createRow(ultimalinea + 2);
            filaMedia.createCell(0).setCellValue("Media");

            for (int col = 1; col <= 9; col++) {
                double suma = 0;
                int totalFilas = 0;

                for (int i = 1; i <= ultimalinea + 1; i++) {
                    Row fila = sheet.getRow(i);
                    if (fila != null && fila.getCell(col) != null) {
                        try {
                            suma += fila.getCell(col).getNumericCellValue();
                            totalFilas++;
                        } catch (Exception e) {

                        }
                    }
                }

                if (totalFilas > 0) {
                    filaMedia.createCell(col).setCellValue(suma / totalFilas);
                } else {
                    filaMedia.createCell(col).setCellValue("0.00");
                }
            }

            FileOutputStream fos = new FileOutputStream(fichero);
            workbook.write(fos);

            fos.close();
            workbook.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar en Excel: ");
        }
    }

    private void resetearCampos() {
        Campo_rellenar_nombre.setText("");
        spinner_metidos_2.setValue(0);
        spinner_metidos_3.setValue(0);
        spinner_hechos_2.setValue(0);
        spinner_hechos_3.setValue(0);
        spinner_metidos_libres.setValue(0);
        spinner_libres_hechos.setValue(0);
    }

    public static void main(String[] args) {
        
        JFrame frame = new JFrame("Puntuación Baloncesto");

        BaloncestoPuntos panel = new BaloncestoPuntos();

        frame.setContentPane(panel);

        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Tiros_metidos_de_dos = new javax.swing.JLabel();
        spinner_metidos_2 = new javax.swing.JSpinner();
        Tiros_metidos_de_3 = new javax.swing.JLabel();
        spinner_metidos_3 = new javax.swing.JSpinner();
        Nombre_jugador = new javax.swing.JLabel();
        Campo_rellenar_nombre = new javax.swing.JTextField();
        Boton_calcular = new javax.swing.JButton();
        Tiros_hechos_de_2 = new javax.swing.JLabel();
        spinner_hechos_2 = new javax.swing.JSpinner();
        Tiros_hechos_de_3 = new javax.swing.JLabel();
        spinner_hechos_3 = new javax.swing.JSpinner();
        tiros_libres_metidos = new javax.swing.JLabel();
        spinner_metidos_libres = new javax.swing.JSpinner();
        tiros_libres_hechos = new javax.swing.JLabel();
        spinner_libres_hechos = new javax.swing.JSpinner();

        Tiros_metidos_de_dos.setText("Tiros metidos de 2");

        Tiros_metidos_de_3.setText("Tiros metidos de 3");

        Nombre_jugador.setText("Nombre del jugador");

        Boton_calcular.setText("Calcular");

        Tiros_hechos_de_2.setText("Tiros hechos de 2");

        Tiros_hechos_de_3.setText("Tiros hechos de 3");

        tiros_libres_metidos.setText("Tiros libres metidos");

        tiros_libres_hechos.setText("Tiros libres hechos");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Boton_calcular, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
            .addGroup(layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Nombre_jugador)
                        .addGap(49, 49, 49)
                        .addComponent(Campo_rellenar_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Tiros_hechos_de_3)
                            .addComponent(tiros_libres_metidos, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(tiros_libres_hechos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spinner_hechos_3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spinner_metidos_libres, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spinner_libres_hechos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Tiros_metidos_de_dos, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49)
                        .addComponent(spinner_metidos_2, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Tiros_metidos_de_3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(spinner_metidos_3, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Tiros_hechos_de_2)
                        .addGap(65, 65, 65)
                        .addComponent(spinner_hechos_2, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(115, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Nombre_jugador)
                    .addComponent(Campo_rellenar_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Tiros_metidos_de_dos)
                    .addComponent(spinner_metidos_2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Tiros_hechos_de_2)
                    .addComponent(spinner_hechos_2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Tiros_metidos_de_3)
                    .addComponent(spinner_metidos_3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Tiros_hechos_de_3)
                    .addComponent(spinner_hechos_3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tiros_libres_metidos)
                    .addComponent(spinner_metidos_libres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tiros_libres_hechos)
                    .addComponent(spinner_libres_hechos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                .addComponent(Boton_calcular, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Boton_calcular;
    private javax.swing.JTextField Campo_rellenar_nombre;
    private javax.swing.JLabel Nombre_jugador;
    private javax.swing.JLabel Tiros_hechos_de_2;
    private javax.swing.JLabel Tiros_hechos_de_3;
    private javax.swing.JLabel Tiros_metidos_de_3;
    private javax.swing.JLabel Tiros_metidos_de_dos;
    private javax.swing.JSpinner spinner_hechos_2;
    private javax.swing.JSpinner spinner_hechos_3;
    private javax.swing.JSpinner spinner_libres_hechos;
    private javax.swing.JSpinner spinner_metidos_2;
    private javax.swing.JSpinner spinner_metidos_3;
    private javax.swing.JSpinner spinner_metidos_libres;
    private javax.swing.JLabel tiros_libres_hechos;
    private javax.swing.JLabel tiros_libres_metidos;
    // End of variables declaration//GEN-END:variables

}
