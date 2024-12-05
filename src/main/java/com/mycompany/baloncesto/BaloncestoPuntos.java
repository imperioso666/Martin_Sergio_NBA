
package com.mycompany.baloncesto;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
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
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;



public class BaloncestoPuntos extends JPanel {
    
    private static String archivo = "EstadisticasBaloncesto.xlsx";
    
    

    public BaloncestoPuntos() {
    initComponents();

    combo_equipos.addItem("Los Angeles Lakers");
    combo_equipos.addItem("Golden State Warriors");

    combo_equipos.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            combo_jugadores.removeAllItems();
            String equipoSeleccionado = (String) combo_equipos.getSelectedItem();

            if (equipoSeleccionado.equals("Los Angeles Lakers")) {
                combo_jugadores.addItem("LeBron James");
                combo_jugadores.addItem("Anthony Davis");
                combo_jugadores.addItem("D'Angelo Russell");
                combo_jugadores.addItem("Rui Hachimura");
                combo_jugadores.addItem("Austin Reaves");
            } else if (equipoSeleccionado.equals("Golden State Warriors")) {
                combo_jugadores.addItem("Stephen Curry");
                combo_jugadores.addItem("Klay Thompson");
                combo_jugadores.addItem("Draymond Green");
                combo_jugadores.addItem("Andrew Wiggins");
                combo_jugadores.addItem("Kevon Looney");
            }
        }
    });

    JPanel contenido = new JPanel(); 
    contenido.setLayout(new GridBagLayout()); 
    contenido.add(jTabbedPane1); 
    add(contenido, BorderLayout.CENTER);

    Boton_calcular.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            calcularPuntos();
        }
    });
    Boton_grafica1.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String jugadorSeleccionado = (String) combo_jugadores.getSelectedItem();
            if (jugadorSeleccionado != null && !jugadorSeleccionado.isEmpty()) {
                generarGraficoDePuntos(jugadorSeleccionado);
            } else {
                JOptionPane.showMessageDialog(BaloncestoPuntos.this, "Seleccione un jugador.");
            }
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
            

            guardarEnExcel((String) combo_jugadores.getSelectedItem(), tirosMetidosDe2 , tirosMetidosDe3 , tiros_hechos_de_2 , tiros_hechos_de_3, fgPorcentaje, efgPorcentaje, tiros_libres_hechos , tiros_libres_metidos, tsPorcentaje, valoracion);
            resetearCampos();
            

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrio un error , revise que introdujo los datos correctamente");
        }
    }
    
    private void generarGraficoDePuntos(String jugador) {
        try {
            // Abre el archivo Excel
            String archivoExcel = "Estadisticas_" + combo_equipos.getSelectedItem() + ".xlsx"; // Nombre del archivo Excel basado en el equipo seleccionado
            FileInputStream fis = new FileInputStream(archivoExcel);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet(jugador);  // Asumiendo que los datos están en la primera hoja

            if (sheet == null) {
                JOptionPane.showMessageDialog(this, "No se encontraron datos para el jugador: " + jugador);
                return;
            }

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row fila = sheet.getRow(i);
                if (fila != null) {
                    String nombreJugador = fila.getCell(0).getStringCellValue();
                    if (nombreJugador.equalsIgnoreCase(jugador)) {
                        
                        double tirosMetidosDe2 = fila.getCell(2).getNumericCellValue();
                        double tirosMetidosDe3 = fila.getCell(4).getNumericCellValue();
                        double tirosLibresMetidos = fila.getCell(7).getNumericCellValue();
                        double puntos = (tirosMetidosDe2 * 2) + (tirosMetidosDe3 * 3) + (tirosLibresMetidos * 1);

                        String partido = "Partido " + (i);
                        dataset.addValue(puntos, "Puntos", partido);
                    }
                }
            }

           
            JFreeChart grafico = ChartFactory.createBarChart("Puntos por partido de " + jugador,"Partidos","Puntos",dataset);

            File directorio = new File("graficas");
            if (!directorio.exists()) {
                directorio.mkdir();
            }
            
            
            File archivoGrafico = new File(directorio, jugador + "_puntos.png");
            ChartUtils.saveChartAsPNG(archivoGrafico, grafico, 800, 600);

            JOptionPane.showMessageDialog(this, "Gráfico generado correctamente");
            workbook.close();
            fis.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo Excel o generar el gráfico: " + e.getMessage());
        }
    }

    private void guardarEnExcel(String jugador, int tirosDe2, int tirosDe3, int tiros_hechos_de_2, 
                            int tiros_hechos_de_3, double fgPorcentaje, double efgPorcentaje, 
                            int tiros_libres_hechos, int tiros_libres_metidos, double tsPorcentaje, 
                            int valoracion) {
    String archivo = "Estadisticas_" + combo_equipos.getSelectedItem() + ".xlsx";
    File fichero = new File(archivo);
    Workbook workbook;
    Sheet sheetJugador;
    Sheet sheetMedias;

    try {
        if (fichero.exists()) {
            FileInputStream fis = new FileInputStream(fichero);
            workbook = new XSSFWorkbook(fis);
        } else {
            workbook = new XSSFWorkbook();
        }

        if (workbook.getSheet(jugador) == null) {
            sheetJugador = workbook.createSheet(jugador);
            Row headerRow = sheetJugador.createRow(0);
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
            headerRow.createCell(10).setCellValue("Valoración");
        } else {
            sheetJugador = workbook.getSheet(jugador);
        }

        int ultimaLinea = sheetJugador.getLastRowNum();
        Row row = sheetJugador.createRow(ultimaLinea + 1);
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

        if (workbook.getSheet("Medias") == null) {
            sheetMedias = workbook.createSheet("Medias");
            Row mediaHeader = sheetMedias.createRow(0);
            mediaHeader.createCell(0).setCellValue("Media de equipo");
            mediaHeader.createCell(1).setCellValue("Tiros hechos de 2");
            mediaHeader.createCell(2).setCellValue("Tiros metidos de 2");
            mediaHeader.createCell(3).setCellValue("Tiros hechos de 3");
            mediaHeader.createCell(4).setCellValue("Tiros metidos de 3");
            mediaHeader.createCell(5).setCellValue("FG%");
            mediaHeader.createCell(6).setCellValue("eFG%");
            mediaHeader.createCell(7).setCellValue("Tiros libres hechos");
            mediaHeader.createCell(8).setCellValue("Tiros libres metidos");
            mediaHeader.createCell(9).setCellValue("TS%");
            mediaHeader.createCell(10).setCellValue("Valoración");
        } else {
            sheetMedias = workbook.getSheet("Medias");
        }

        int jugadoresContados = 0;
        double[] suma = new double[11];

        for (int i = 1; i <= sheetJugador.getLastRowNum(); i++) {
            Row fila = sheetJugador.getRow(i);
            if (fila != null) {
                for (int j = 1; j <= 10; j++) {
                    suma[j] += fila.getCell(j) != null ? fila.getCell(j).getNumericCellValue() : 0;
                }
                jugadoresContados++;
            }
        }

        int ultimaFilaMedias = sheetMedias.getLastRowNum();
        Row mediaRow = sheetMedias.createRow(ultimaFilaMedias + 1);
        mediaRow.createCell(0).setCellValue(combo_equipos.getSelectedItem().toString());

        for (int i = 1; i <= 10; i++) {
            double media = (jugadoresContados > 0) ? suma[i] / jugadoresContados : 0;
            mediaRow.createCell(i).setCellValue(media);
        }

        FileOutputStream fos = new FileOutputStream(fichero);
        workbook.write(fos);
        fos.close();
        workbook.close();

        JOptionPane.showMessageDialog(this, "Estadísticas guardadas en el archivo: " + archivo);

    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error al guardar en Excel: " + e.getMessage());
    }
}


    
    



    private void resetearCampos() {
        spinner_metidos_2.setValue(0);
        spinner_metidos_3.setValue(0);
        spinner_hechos_2.setValue(0);
        spinner_hechos_3.setValue(0);
        spinner_metidos_libres.setValue(0);
        spinner_libres_hechos.setValue(0);
        spinner_rebotes.setValue(0);
        spinner_asistencias.setValue(0);
        spinner_robos.setValue(0);
        spinner_tapones.setValue(0);
        spinner_tapones_recibidos.setValue(0);
        spinner_perdidas.setValue(0);
        spinner_faltas_recibidas.setValue(0);
        spinner_faltas_realizadas.setValue(0);
        
    }

    public static void main(String[] args) {
        
        JFrame frame = new JFrame("Puntuación Baloncesto");

        BaloncestoPuntos panel = new BaloncestoPuntos();

        frame.setContentPane(panel);

        frame.setSize(477, 523);
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
        tiros_libres_hechos = new javax.swing.JLabel();
        combo_equipos = new javax.swing.JComboBox<>();
        combo_jugadores = new javax.swing.JComboBox<>();
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
        Boton_grafica1 = new javax.swing.JButton();

        setMaximumSize(null);
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(477, 465));

        tiros_libres_metidos.setText("Tiros libres metidos");

        Tiros_hechos_de_3.setText("Tiros hechos de 3");

        Tiros_hechos_de_2.setText("Tiros hechos de 2");

        Tiros_metidos_de_dos.setText("Tiros metidos de 2");

        Tiros_metidos_de_3.setText("Tiros metidos de 3");

        tiros_libres_hechos.setText("Tiros libres hechos");

        combo_equipos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Equipos" }));

        combo_jugadores.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Jugadores" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(combo_equipos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(combo_jugadores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
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
                            .addComponent(spinner_hechos_2, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(154, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(combo_equipos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(combo_jugadores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(75, 75, 75)
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
                .addContainerGap(143, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Datos Equipo y jugador", jPanel1);

        Boton_calcular.setText("Generar Excel");

        Tapones_recibidos.setText("Tapones recibidos");

        Perdidas.setText("Perdidas");

        faltas_recibidas.setText("Faltas recibidas");

        asistencias.setText("Asistencias");

        Tapones.setText("Tapones");

        rebotes.setText("Rebotes");

        Robos.setText("Robos");

        faltas_realizadas.setText("Faltas realizadas");

        Boton_grafica1.setText("Generar gráfica");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Boton_calcular)
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
                                .addComponent(spinner_rebotes, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Boton_grafica1)
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(33, 33, 33)
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
                .addGap(38, 38, 38)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Boton_calcular, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Boton_grafica1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(86, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Mas datos", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jTabbedPane1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 532, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("Datos Equipo y jugador");
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Boton_calcular;
    private javax.swing.JButton Boton_grafica1;
    private javax.swing.JLabel Perdidas;
    private javax.swing.JLabel Robos;
    private javax.swing.JLabel Tapones;
    private javax.swing.JLabel Tapones_recibidos;
    private javax.swing.JLabel Tiros_hechos_de_2;
    private javax.swing.JLabel Tiros_hechos_de_3;
    private javax.swing.JLabel Tiros_metidos_de_3;
    private javax.swing.JLabel Tiros_metidos_de_dos;
    private javax.swing.JLabel asistencias;
    private javax.swing.JComboBox<String> combo_equipos;
    private javax.swing.JComboBox<String> combo_jugadores;
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
