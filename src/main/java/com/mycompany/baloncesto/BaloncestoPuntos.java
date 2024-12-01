
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
            int puntos = (puntos_campo + tiros_libres_metidos);
            int rebotes = (int) spinner_rebotes.getValue();
            int asistencias = (int) spinner_asistencias.getValue();
            int robos = (int) spinner_robos.getValue();
            int tapones = (int) spinner_tapones.getValue();
            int tapones_recibidos = (int) spinner_tapones_recibidos.getValue();
            int perdidas = (int) spinner_perdidas.getValue();
            int faltas_recibidas = (int) spinner_faltas_recibidas.getValue();
            int faltas_realizadas = (int) spinner_faltas_realizadas.getValue();
            int tiros_campo_fallados = (tiros_hechos_de_2 - tirosMetidosDe2) + (tiros_hechos_de_3 - tirosMetidosDe3);
            int tiros_libres_fallados = (tiros_libres_hechos - tiros_libres_metidos);

            double tirosIntentados = tiros_hechos_de_2 + tiros_hechos_de_3;
            double fgPorcentaje = 0;
            double efgPorcentaje = 0;
            double tsPorcentaje = 0;
            int valoracion = 0;
            

            if (tirosIntentados > 0) {
                fgPorcentaje = ((double) (tirosMetidosDe2 + tirosMetidosDe3) / (tiros_hechos_de_2 + tiros_hechos_de_3)) * 100;
                efgPorcentaje = ((double) (tirosMetidosDe2 + tirosMetidosDe3 + (0.5 * tirosMetidosDe3)) / (tiros_hechos_de_2 + tiros_hechos_de_3)) * 100;
                tsPorcentaje = ((double) (puntos_campo + tiros_libres_metidos) / (2* (tiros_hechos_de_2 + tiros_hechos_de_3 + (0.44 * tiros_libres_hechos)))) * 100;
                valoracion = ((int) (puntos + rebotes + asistencias+ robos + tapones + faltas_recibidas)-(tiros_campo_fallados + tiros_libres_fallados + perdidas + tapones_recibidos + faltas_realizadas));
            }

            String jugador = Campo_rellenar_nombre.getText().trim();

            if (jugador.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ingrese el nombre del jugador para continuar.");
                return;
            }

            guardarEnExcel(jugador,tirosMetidosDe2 , tirosMetidosDe3 , tiros_hechos_de_2 , tiros_hechos_de_3, fgPorcentaje, efgPorcentaje, tiros_libres_hechos , tiros_libres_metidos, tsPorcentaje, valoracion);
            resetearCampos();
            

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrio un error , revise que introdujo los datos correctamente");
        }
    }

    private void guardarEnExcel(String jugador, int tirosDe2, int tirosDe3, int tiros_hechos_de_2, int tiros_hechos_de_3, double fgPorcentaje, double efgPorcentaje,int tiros_libres_hechos, int tiros_libres_metidos, double tsPorcentaje, int valoracion) {
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
                headerRow.createCell(10).setCellValue("Valoracion");
                
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
            row.createCell(10).setCellValue(valoracion);
            
            Row filaMedia = sheet.createRow(ultimalinea + 2);
            filaMedia.createCell(0).setCellValue("Media");

            for (int col = 1; col <= 10; col++) {
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        tiros_libres_metidos = new javax.swing.JLabel();
        spinner_metidos_2 = new javax.swing.JSpinner();
        spinner_metidos_3 = new javax.swing.JSpinner();
        spinner_metidos_libres = new javax.swing.JSpinner();
        spinner_hechos_3 = new javax.swing.JSpinner();
        Tiros_hechos_de_3 = new javax.swing.JLabel();
        spinner_libres_hechos = new javax.swing.JSpinner();
        Tiros_hechos_de_2 = new javax.swing.JLabel();
        spinner_hechos_2 = new javax.swing.JSpinner();
        Tiros_metidos_de_dos = new javax.swing.JLabel();
        Tiros_metidos_de_3 = new javax.swing.JLabel();
        Nombre_jugador = new javax.swing.JLabel();
        Campo_rellenar_nombre = new javax.swing.JTextField();
        tiros_libres_hechos = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        Boton_calcular = new javax.swing.JButton();
        Tapones_recibidos = new javax.swing.JLabel();
        spinner_tapones_recibidos = new javax.swing.JSpinner();
        Perdidas = new javax.swing.JLabel();
        spinner_perdidas = new javax.swing.JSpinner();
        faltas_recibidas = new javax.swing.JLabel();
        spinner_faltas_recibidas = new javax.swing.JSpinner();
        asistencias = new javax.swing.JLabel();
        spinner_asistencias = new javax.swing.JSpinner();
        Tapones = new javax.swing.JLabel();
        spinner_tapones = new javax.swing.JSpinner();
        rebotes = new javax.swing.JLabel();
        Robos = new javax.swing.JLabel();
        spinner_robos = new javax.swing.JSpinner();
        spinner_rebotes = new javax.swing.JSpinner();
        faltas_realizadas = new javax.swing.JLabel();
        spinner_faltas_realizadas = new javax.swing.JSpinner();

        tiros_libres_metidos.setText("Tiros libres metidos");

        Tiros_hechos_de_3.setText("Tiros hechos de 3");

        Tiros_hechos_de_2.setText("Tiros hechos de 2");

        Tiros_metidos_de_dos.setText("Tiros metidos de 2");

        Tiros_metidos_de_3.setText("Tiros metidos de 3");

        Nombre_jugador.setText("Nombre del jugador");

        tiros_libres_hechos.setText("Tiros libres hechos");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(Nombre_jugador)
                        .addGap(49, 49, 49)
                        .addComponent(Campo_rellenar_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Tiros_hechos_de_3)
                            .addComponent(tiros_libres_metidos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tiros_libres_hechos, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spinner_hechos_3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spinner_metidos_libres, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spinner_libres_hechos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(Tiros_metidos_de_dos, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49)
                        .addComponent(spinner_metidos_2, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(Tiros_metidos_de_3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(spinner_metidos_3, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(Tiros_hechos_de_2)
                        .addGap(65, 65, 65)
                        .addComponent(spinner_hechos_2, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(154, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Nombre_jugador)
                    .addComponent(Campo_rellenar_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Tiros_metidos_de_dos)
                    .addComponent(spinner_metidos_2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Tiros_hechos_de_2)
                    .addComponent(spinner_hechos_2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Tiros_metidos_de_3)
                    .addComponent(spinner_metidos_3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Tiros_hechos_de_3)
                    .addComponent(spinner_hechos_3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tiros_libres_metidos)
                    .addComponent(spinner_metidos_libres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tiros_libres_hechos)
                    .addComponent(spinner_libres_hechos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(111, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab1", jPanel1);

        Boton_calcular.setText("Calcular");

        Tapones_recibidos.setText("Tapones recibidos");

        Perdidas.setText("Perdidas");

        faltas_recibidas.setText("Faltas recibidas");

        asistencias.setText("Asistencias");

        Tapones.setText("Tapones");

        rebotes.setText("Rebotes");

        Robos.setText("Robos");

        faltas_realizadas.setText("Faltas realizadas");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(Boton_calcular, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(faltas_realizadas, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(spinner_faltas_realizadas, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(Tapones_recibidos)
                                    .addComponent(Perdidas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(faltas_recibidas, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(spinner_tapones_recibidos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(spinner_perdidas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(spinner_faltas_recibidas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(Tapones)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(spinner_tapones, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(Robos)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(spinner_robos, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(asistencias, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rebotes))
                                .addGap(49, 49, 49)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(spinner_asistencias, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(spinner_rebotes, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(154, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rebotes)
                    .addComponent(spinner_rebotes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(asistencias)
                    .addComponent(spinner_asistencias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Robos)
                    .addComponent(spinner_robos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Tapones)
                    .addComponent(spinner_tapones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Tapones_recibidos)
                    .addComponent(spinner_tapones_recibidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Perdidas)
                    .addComponent(spinner_perdidas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(faltas_recibidas)
                    .addComponent(spinner_faltas_recibidas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(faltas_realizadas)
                    .addComponent(spinner_faltas_realizadas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Boton_calcular, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44))
        );

        jTabbedPane1.addTab("tab2", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 477, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(465, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(421, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Boton_calcular;
    private javax.swing.JTextField Campo_rellenar_nombre;
    private javax.swing.JLabel Nombre_jugador;
    private javax.swing.JLabel Perdidas;
    private javax.swing.JLabel Robos;
    private javax.swing.JLabel Tapones;
    private javax.swing.JLabel Tapones_recibidos;
    private javax.swing.JLabel Tiros_hechos_de_2;
    private javax.swing.JLabel Tiros_hechos_de_3;
    private javax.swing.JLabel Tiros_metidos_de_3;
    private javax.swing.JLabel Tiros_metidos_de_dos;
    private javax.swing.JLabel asistencias;
    private javax.swing.JLabel faltas_realizadas;
    private javax.swing.JLabel faltas_recibidas;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel rebotes;
    private javax.swing.JSpinner spinner_asistencias;
    private javax.swing.JSpinner spinner_faltas_realizadas;
    private javax.swing.JSpinner spinner_faltas_recibidas;
    private javax.swing.JSpinner spinner_hechos_2;
    private javax.swing.JSpinner spinner_hechos_3;
    private javax.swing.JSpinner spinner_libres_hechos;
    private javax.swing.JSpinner spinner_metidos_2;
    private javax.swing.JSpinner spinner_metidos_3;
    private javax.swing.JSpinner spinner_metidos_libres;
    private javax.swing.JSpinner spinner_perdidas;
    private javax.swing.JSpinner spinner_rebotes;
    private javax.swing.JSpinner spinner_robos;
    private javax.swing.JSpinner spinner_tapones;
    private javax.swing.JSpinner spinner_tapones_recibidos;
    private javax.swing.JLabel tiros_libres_hechos;
    private javax.swing.JLabel tiros_libres_metidos;
    // End of variables declaration//GEN-END:variables

}
