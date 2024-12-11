package com.mycompany.baloncesto;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public class BaloncestoJFRAME extends javax.swing.JFrame {

    public BaloncestoJFRAME() {
        initComponents();
    
    // Aqui se define las acciones de los Jbutton 
    
    // este boton se encarga de hacer los calculos y meterlos en excel
    Boton_calcular.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            calcularPuntos();
        }
    });
    
    // este boton genera las graficas.
    Boton_grafica1.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String jugadorSeleccionado = (String) combo_jugadores.getSelectedItem();
            if (jugadorSeleccionado != null && !jugadorSeleccionado.isEmpty()) {
                generarGraficoDePuntos(jugadorSeleccionado);
                generarGraficoRebotes(jugadorSeleccionado);
                generarGraficoAsistencias(jugadorSeleccionado);
            } else {
                JOptionPane.showMessageDialog(BaloncestoJFRAME.this, "Seleccione un jugador.");
            }
        }
    });
    
    // este boton genera los PDF
    Boton_PDF_1.addActionListener(e -> {
    String jugador = (String) combo_jugadores.getSelectedItem();
    String equipo = (String) combo_equipos.getSelectedItem();
    generarPDF(jugador, equipo);
    });
    
    //Boton opcional para "resetear" los jspinner , es deicr ponerlos a 0 de nuevo.
    Boton_BORRAR.addActionListener(e -> {
    resetearCampos();
    });
    
    
     // para los menus desplegables.
    combo_equipos.addItem("Los Angeles Lakers");
    combo_equipos.addItem("Golden State Warriors");

    //con este action listener en combo equipos , segun el equipo escogido nos mostrara los jugadores asociados.
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
    configurarEtiquetas();
    configurarMenu();
}
   private List<MiComponente> etiquetas; // Lista para almacenar las etiquetas

        private void configurarEtiquetas() {
            
            etiquetas = new ArrayList<>();
            etiquetas.add(miComponente1);
            etiquetas.add(miComponente2);
            etiquetas.add(miComponente3);
            etiquetas.add(miComponente4);
            etiquetas.add(miComponente5);
            etiquetas.add(miComponente6);
            
        }

        private void actualizarTamañoFuente(int size) {
            for (MiComponente etiqueta : etiquetas) {
                etiqueta.cambiarTamaño(size);
            }
        }

        private void configurarMenu() {
            pequeño.addActionListener(e -> actualizarTamañoFuente(1));
            mediano.addActionListener(e -> actualizarTamañoFuente(2));
            grande.addActionListener(e -> actualizarTamañoFuente(3));

            // Establecer el botón "Mediano" como predeterminado
            mediano.setSelected(true);
        }

    // metodo para calcular los datos que necesitaremos posteriormente para introducir los resultados a excel
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

            
            double fgPorcentaje = 0;
            double efgPorcentaje = 0;
            double tsPorcentaje = 0;
            int valoracion = 0;
    
            
            fgPorcentaje = ((double) (tirosMetidosDe2 + tirosMetidosDe3) / (tiros_hechos_de_2 + tiros_hechos_de_3)) * 100;
            efgPorcentaje = ((double) (tirosMetidosDe2 + tirosMetidosDe3 + (0.5 * tirosMetidosDe3)) / (tiros_hechos_de_2 + tiros_hechos_de_3)) * 100;
            tsPorcentaje = ((double) (puntos_campo + tiros_libres_metidos) / (2* (tiros_hechos_de_2 + tiros_hechos_de_3 + (0.44 * tiros_libres_hechos)))) * 100;
            valoracion = ((int) (puntos + rebotes + asistencias+ robos + tapones + faltas_recibidas)-(tiros_campo_fallados + tiros_libres_fallados + perdidas + tapones_recibidos + faltas_realizadas));
            
            
            // llamada al metodo guardar en excel.
            guardarEnExcel((String) combo_jugadores.getSelectedItem(), tirosMetidosDe2 , tirosMetidosDe3 , tiros_hechos_de_2 , tiros_hechos_de_3, fgPorcentaje, efgPorcentaje, tiros_libres_hechos , tiros_libres_metidos, tsPorcentaje, valoracion,asistencias,rebotes);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrio un error , revise que introdujo los datos correctamente");
        }
    }
    // metodo para generar excels , donde pasaremos todos los datos que insertaremos por parametro.
      private void guardarEnExcel(String jugador, int tirosDe2, int tirosDe3, int tiros_hechos_de_2, 
                            int tiros_hechos_de_3, double fgPorcentaje, double efgPorcentaje, 
                            int tiros_libres_hechos, int tiros_libres_metidos, double tsPorcentaje, 
                            int valoracion,int asistencias,int rebotes) {
    
    //Codigo para generar el fichero excel
    String archivo = "Estadisticas_" + combo_equipos.getSelectedItem() + ".xlsx";
    File fichero = new File(archivo);
    Workbook workbook;
    Sheet sheetJugador;
    Sheet sheetMedias;

    try {
        //Codigo apra comprobar que existe el fichero , si existe actualiza el fichero y si no , crea un excel nuevo.
        if (fichero.exists()) {
            FileInputStream fis = new FileInputStream(fichero); // file input stream para leer el excel
            workbook = new XSSFWorkbook(fis);
        } else {
            workbook = new XSSFWorkbook();
        }

        //Codigo para comprobar que si existe o no la hoja dentro del excel con el nombre del jugador 
        // si no existe crea una hoja nueva con el nombre del jugador y ademas añade un encabezado a cada
        // columna del excel que trabajaremos.
        
        
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
            headerRow.createCell(11).setCellValue("Asistencias");
            headerRow.createCell(12).setCellValue("Rebotes");
        } else {
             // si existe recuperamos los datos de esa hoja
            sheetJugador = workbook.getSheet(jugador);
        }
        // con la variable ultimalinea obtenemos la ultima linea del excel. Insertamos los registros de cada jugador , previamente calculados en el metodo
        // calcularPuntos y pasados por parametro.
        int ultimaLinea = sheetJugador.getLastRowNum();
        Row row = sheetJugador.createRow(ultimaLinea + 1); // con esto creamos una linea mas despues de la ultima
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
        row.createCell(11).setCellValue(asistencias);
        row.createCell(12).setCellValue(rebotes);

        // Creamos o actualizamos la hoja de medias, igual que hacemos con jugadores 
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
            mediaHeader.createCell(11).setCellValue("Asistencias");
            mediaHeader.createCell(12).setCellValue("Rebotes");
        } else {
            sheetMedias = workbook.getSheet("Medias");
        }

        
        // Codigo para calcular la media del equipo donde se van acumulando los datos en el arreglo suma.
        int jugadoresContados = 0;
        double[] suma = new double[13];

        for (int i = 1; i <= sheetJugador.getLastRowNum(); i++) {
            Row fila = sheetJugador.getRow(i);
            if (fila != null) {
                for (int j = 1; j <= 12; j++) {
                    suma[j] += fila.getCell(j) != null ? fila.getCell(j).getNumericCellValue() : 0;
                }
                jugadoresContados++;
            }
        }
        
        // Codigo para guardar los datos en la hoja Medias creada en el excel. Lo guarda en una fila del excel despues de la ultima
        
        // divide la suma que hemos recolectado antes en el arreglo entre los jugadores ( variable jugadoresContados).
        int ultimaFilaMedias = sheetMedias.getLastRowNum();
        Row mediaRow = sheetMedias.createRow(ultimaFilaMedias + 1);
        mediaRow.createCell(0).setCellValue(combo_equipos.getSelectedItem().toString());

        for (int i = 1; i <= 12; i++) {
            double media = (jugadoresContados > 0) ? suma[i] / jugadoresContados : 0;
            mediaRow.createCell(i).setCellValue(media);
        }
        
        // Escribir y gaurdar el archivo
        FileOutputStream fos = new FileOutputStream(fichero);
        workbook.write(fos);
        fos.close();
        workbook.close();

        JOptionPane.showMessageDialog(this, "Estadísticas guardadas en el archivo: " + archivo);

    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error al guardar en Excel: " + e.getMessage());
    }
}
    // metodo para generar el grafico de putnos.
    private void generarGraficoDePuntos(String jugador) {
    try {
        
        // Leer el archivo de excel , combo equipos es el desplegables de equipos para seleccionar.
        String archivoExcel = "Estadisticas_" + combo_equipos.getSelectedItem() + ".xlsx";
        FileInputStream fis = new FileInputStream(archivoExcel);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet(jugador);
        
        // si no se encuentra ningun excel cone se nombre , muestra una ventana emergente con un mensaje.
        if (sheet == null) {
            JOptionPane.showMessageDialog(this, "No se encontraron datos para el jugador: " + jugador);
            return;
        }
        
        // dataset tendra los datos de los puntos del jugador en cada partido. Mediadataset es para calcular la media 
        // de los puntos que se muestra en el grafico como linea
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DefaultCategoryDataset mediaDataset = new DefaultCategoryDataset();

        
        
        // codigo para recorrer los datos del excel, recorrera cada fila oara obtener los datos del jugador.
        double suma_de_los_puntos = 0;
        int partidos = 0;
        
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row fila = sheet.getRow(i);
            if (fila != null) {
                String nombreJugador = fila.getCell(0).getStringCellValue();
                if (nombreJugador.equalsIgnoreCase(jugador)) {
                    // obtiene del excel los datos de las columnas con estos datos. puntos calcula el valor de los dobles triples etc
                    double tirosMetidosDe2 = fila.getCell(2).getNumericCellValue();
                    double tirosMetidosDe3 = fila.getCell(4).getNumericCellValue();
                    double tirosLibresMetidos = fila.getCell(8).getNumericCellValue();
                    double puntos = (tirosMetidosDe2 * 2) + (tirosMetidosDe3 * 3) + (tirosLibresMetidos * 1);
                    
                    // Se actualiza el dataset para añadir puntos para cada partido, con la variable partido indicamos luego en el grafico que partido es
                    // se incrementa con cada vuelta del codigo
                    String partido = "Partido " + (i);
                    dataset.addValue(puntos, "Puntos", partido);

                    suma_de_los_puntos += puntos;
                    partidos++;
                }
            }
        }
        // calculamos la media de puntos y añadimos en la variable mediaDataSet por cada partido del jugador.
        double mediaPuntos = suma_de_los_puntos / partidos;

        
        for (int i = 1; i <= partidos; i++) {
            String partido = "Partido " + i;
            mediaDataset.addValue(mediaPuntos, "Media", partido);
        }

        // Con este codigo creamos un grafico de barras de los puntos por partido.
        JFreeChart grafico = ChartFactory.createBarChart(
            "Puntos por partido de " + jugador, // titulo del grafico
            "Partidos", // horizontal
            "Puntos", // vertical
            dataset
        );
        
        // Codigo para superponer la linea de medias . plot lo que hace es obtener el area del grafico
        CategoryPlot plot = grafico.getCategoryPlot();
        // definimos la linea con line Renderer y superponemos ( con 1 indicamos la superposicion ) 
        LineAndShapeRenderer lineRenderer = new LineAndShapeRenderer();
        plot.setDataset(1, mediaDataset);
        plot.setRenderer(1, lineRenderer);

        // Configurar los colores y estilos de la línea
        lineRenderer.setSeriesPaint(0, Color.BLACK);
        lineRenderer.setSeriesStroke(0, new BasicStroke(2.0f)); // grosor de linea

        // Asegurar que la línea de la media se superpone al gráfico de barras
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        // Crear el directorio si no existe
        File directorio = new File("graficas/" + jugador);
        if (!directorio.exists()) {
            directorio.mkdir();
        }

        // Guardar el gráfico como imagen
        File archivoGrafico = new File(directorio, jugador + "_puntos.png");
        ChartUtils.saveChartAsPNG(archivoGrafico, grafico, 800, 600);

        JOptionPane.showMessageDialog(this, "Gráfico de puntos generado correctamente");
        workbook.close();
        fis.close();

    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error al leer el archivo Excel o generar el gráfico: " + e.getMessage());
    }
}

    // METODO PARA GENERAR GRAFICOS DE REBOTES.
    private void generarGraficoRebotes(String jugador) {
    try {
        
        String archivoExcel = "Estadisticas_" + combo_equipos.getSelectedItem() + ".xlsx";
        FileInputStream fis = new FileInputStream(archivoExcel);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet(jugador);

        if (sheet == null) {
            JOptionPane.showMessageDialog(this, "No se encontraron datos para el jugador: " + jugador);
            return;
        }
        
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        
        for (int i = 1; i <= sheet.getLastRowNum(); i++) { 
            Row fila = sheet.getRow(i);
            if (fila != null) {
                double rebotes = fila.getCell(12).getNumericCellValue();
                String partido = "Partido " + i;
                dataset.addValue(rebotes, "Rebotes", partido);
            }
        }
        
        // Crear grafico de lineas
        JFreeChart chart = ChartFactory.createLineChart(
            "Rebotes por partido de " + jugador, 
            "Partidos", 
            "Rebotes", 
            dataset
        );
        
        File directorioJugador = new File("graficas/" + jugador);
        if (!directorioJugador.exists()) {
            directorioJugador.mkdirs(); 
        }

        File archivoGrafico = new File(directorioJugador, jugador + "_rebotes.png");
        ChartUtils.saveChartAsPNG(archivoGrafico, chart, 800, 600);

        JOptionPane.showMessageDialog(this, "Gráfico de rebotes generado correctamente:");

        workbook.close();
        fis.close();

    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error al generar el gráfico de rebotes: " + e.getMessage());
    }
}
    private void generarGraficoAsistencias(String jugador) {
    try {
        // Ruta del archivo Excel
        String archivoExcel = "Estadisticas_" + combo_equipos.getSelectedItem() + ".xlsx";
        FileInputStream fis = new FileInputStream(archivoExcel);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet(jugador);

        // Validar que haya datos para el jugador
        if (sheet == null) {
            JOptionPane.showMessageDialog(this, "No se encontraron datos para el jugador: " + jugador);
            return;
        }

        // Crear dataset para asistencias
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // recorrer las filas del excel
        for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Iterar desde la primera fila
            Row fila = sheet.getRow(i);
            if (fila != null) {
                // Generar etiquetas para los partidos
                String partido = "Partido " + (i + 1); // Etiqueta de partidos
                double asistencias = fila.getCell(11).getNumericCellValue(); // Asistencias (Índice 11)
                dataset.addValue(asistencias, "Asistencias", partido);
            }
        }

        // Crear gráfico de barras
        JFreeChart chart = ChartFactory.createBarChart(
            "Asistencias por partido de " + jugador, // Título
            "Partidos", // Eje X
            "Asistencias", // Eje Y
            dataset
        );

        
        File directorioJugador = new File("graficas/" + jugador);
        if (!directorioJugador.exists()) {
            directorioJugador.mkdirs(); // Crear directorio si no existe
        }

        File archivoGrafico = new File(directorioJugador, jugador + "_asistencias.png");
        ChartUtils.saveChartAsPNG(archivoGrafico, chart, 800, 600);

        JOptionPane.showMessageDialog(this, "Gráfico de asistencias generado correctamente.");

        
        workbook.close();
        fis.close();
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error al generar el gráfico de asistencias: " + ex.getMessage());
    }
}
    public void generarPDF(String jugador, String equipo) {
    try {
        // Codigo para crear la carpeta donde se almacena los pdf 
        File directorioPDF = new File("PDF");
        if (!directorioPDF.exists()) {
            directorioPDF.mkdirs();
        }

        // Codigo para crear el fichero pdf
        String archivoPDF = "PDF/" + jugador + ".pdf";
        FileOutputStream fileOutputStream = new FileOutputStream(new File(archivoPDF));
        PdfWriter pdfWriter = new PdfWriter(fileOutputStream);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        // margenes para pdf
        pdfDocument.setDefaultPageSize(PageSize.A4);
        document.setMargins(20, 20, 20, 20);

        // titulo para el pdf
        document.add(new Paragraph("Reporte del Jugador: " + jugador)
                .setFont(PdfFontFactory.createFont())
                .setFontSize(14).setBold().setTextAlignment(TextAlignment.CENTER));

        // subtitulo con nombre del equipo
        document.add(new Paragraph("Equipo: " + equipo)
                .setFont(PdfFontFactory.createFont())
                .setFontSize(12).setBold().setTextAlignment(TextAlignment.CENTER));

        // insertar los graficos del pdf , recuperando de la carpeta graficas
        String puntosGrafico = "graficas/" + jugador + "/" + jugador + "_puntos.png";
        Image imgPuntos = new Image(ImageDataFactory.create(puntosGrafico));
        imgPuntos.scaleToFit(300, 150);
        imgPuntos.setTextAlignment(TextAlignment.CENTER);
        document.add(new Paragraph("Gráfico de Puntos").setFontSize(10).setTextAlignment(TextAlignment.CENTER));
        document.add(imgPuntos);

        String rebotesGrafico = "graficas/" + jugador + "/" + jugador + "_rebotes.png";
        Image imgRebotes = new Image(ImageDataFactory.create(rebotesGrafico));
        imgRebotes.scaleToFit(300, 150);
        imgRebotes.setTextAlignment(TextAlignment.CENTER);
        document.add(new Paragraph("Gráfico de Rebotes").setFontSize(10).setTextAlignment(TextAlignment.CENTER));
        document.add(imgRebotes);

        String asistenciasGrafico = "graficas/" + jugador + "/" + jugador + "_asistencias.png";
        Image imgAsistencias = new Image(ImageDataFactory.create(asistenciasGrafico));
        imgAsistencias.scaleToFit(300, 150);
        imgAsistencias.setTextAlignment(TextAlignment.CENTER);
        document.add(new Paragraph("Gráfico de Asistencias").setFontSize(10).setTextAlignment(TextAlignment.CENTER));
        document.add(imgAsistencias);

        // Leer estadísticas del archivo Excel
        String archivoExcel = "Estadisticas_" + equipo + ".xlsx";
        FileInputStream fis = new FileInputStream(archivoExcel);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet(jugador);

        // con este codigo extraemos los datos de fg% , efg% y ts%
        if (sheet != null) {
            Row fila = sheet.getRow(1);
            if (fila != null) {
                double fg = fila.getCell(5).getNumericCellValue();
                double efg = fila.getCell(6).getNumericCellValue();
                double ts = fila.getCell(9).getNumericCellValue();

                // Calcular la media de triples metidos
                double sumaTriples = 0;
                int partidos = 0;

                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row filaDatos = sheet.getRow(i);
                    if (filaDatos != null) {
                        double triplesMetidos = filaDatos.getCell(4).getNumericCellValue(); // Columna 4 para triples
                        sumaTriples += triplesMetidos;
                        partidos++;
                    }
                }

                double mediaTriples = sumaTriples / partidos;

                // con div creamos dos contenedores para mostrar los datos en dos columnas y asi no tener el modelo de tabla
                Div contenedorEstadisticas = new Div();
                contenedorEstadisticas.setWidth(UnitValue.createPercentValue(100)); // Ancho del contenedor

                // Primera columna
                Div columna1 = new Div();
                columna1.setWidth(UnitValue.createPercentValue(50)); // Ancho del 50% para la columna 1
                columna1.add(new Paragraph("FG%: " + String.format("%.2f%%", fg)).setFontSize(10));
                columna1.add(new Paragraph("eFG%: " + String.format("%.2f%%", efg)).setFontSize(10));

                // Segunda columna
                Div columna2 = new Div();
                columna2.setWidth(UnitValue.createPercentValue(50)); // Ancho del 50% para la columna 2
                columna2.add(new Paragraph("TS%: " + String.format("%.2f%%", ts)).setFontSize(10));
                columna2.add(new Paragraph("Media triples: " + String.format("%.2f", mediaTriples)).setFontSize(10));

                // Añadir columnas al div
                contenedorEstadisticas.add(columna1);
                contenedorEstadisticas.add(columna2);

                // Añadir estadísticas al documento
                document.add(new Paragraph("Otras estadísticas:").setFontSize(12).setBold());
                document.add(contenedorEstadisticas);
            }
        }

        workbook.close();
        fis.close();
        document.close();

        JOptionPane.showMessageDialog(this, "PDF generado correctamente: " + archivoPDF);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al generar el PDF: " + e.getMessage());
    }
}

    // metodo para resetear los campos jspinner a 0
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        spinner_metidos_2 = new javax.swing.JSpinner();
        spinner_metidos_3 = new javax.swing.JSpinner();
        spinner_metidos_libres = new javax.swing.JSpinner();
        spinner_hechos_3 = new javax.swing.JSpinner();
        spinner_libres_hechos = new javax.swing.JSpinner();
        spinner_hechos_2 = new javax.swing.JSpinner();
        combo_equipos = new javax.swing.JComboBox<>();
        combo_jugadores = new javax.swing.JComboBox<>();
        miComponente1 = new com.mycompany.baloncesto.MiComponente();
        miComponente2 = new com.mycompany.baloncesto.MiComponente();
        miComponente3 = new com.mycompany.baloncesto.MiComponente();
        miComponente4 = new com.mycompany.baloncesto.MiComponente();
        miComponente5 = new com.mycompany.baloncesto.MiComponente();
        miComponente6 = new com.mycompany.baloncesto.MiComponente();
        jPanel3 = new javax.swing.JPanel();
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
        Boton_PDF_1 = new javax.swing.JButton();
        Boton_BORRAR = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        tamaño = new javax.swing.JMenu();
        pequeño = new javax.swing.JMenuItem();
        mediano = new javax.swing.JMenuItem();
        grande = new javax.swing.JMenuItem();
        condiciones_servicio = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        combo_equipos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Equipos" }));

        combo_jugadores.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Jugadores" }));

        miComponente1.setText("Tiros metidos de 2");

        miComponente2.setText("Tiros hechos de 2");

        miComponente3.setText("Tiros metidos de 3");

        miComponente4.setText("Tiros hechos de 3");

        miComponente5.setText("Tiros libres metidos");

        miComponente6.setText("Tiros libres hechos");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(combo_equipos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(combo_jugadores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(miComponente4, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(miComponente5, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(miComponente6, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(miComponente2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(miComponente3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE))
                            .addComponent(miComponente1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 93, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spinner_hechos_2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spinner_metidos_2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spinner_metidos_3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spinner_hechos_3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spinner_metidos_libres, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spinner_libres_hechos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(85, 85, 85))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(combo_equipos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(combo_jugadores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(68, 68, 68)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(miComponente1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinner_metidos_2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spinner_hechos_2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(miComponente2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(miComponente3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinner_metidos_3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(miComponente4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinner_hechos_3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(miComponente5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinner_metidos_libres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(miComponente6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinner_libres_hechos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(87, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Datos Equipo y jugador", jPanel2);

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

        Boton_PDF_1.setText("Generar PDF");

        Boton_BORRAR.setText("Borrar");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(faltas_realizadas, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(spinner_faltas_realizadas, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(Tapones_recibidos)
                                    .addComponent(Perdidas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(faltas_recibidas, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(spinner_tapones_recibidos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(spinner_perdidas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(spinner_faltas_recibidas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(Tapones)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(spinner_tapones, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(Robos)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(spinner_robos, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(asistencias, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rebotes))
                                .addGap(49, 49, 49)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(spinner_asistencias, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(spinner_rebotes, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(Boton_calcular)
                        .addGap(18, 18, 18)
                        .addComponent(Boton_grafica1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Boton_PDF_1)
                        .addGap(18, 18, 18)
                        .addComponent(Boton_BORRAR)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rebotes)
                    .addComponent(spinner_rebotes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(asistencias)
                    .addComponent(spinner_asistencias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Robos)
                    .addComponent(spinner_robos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Tapones)
                    .addComponent(spinner_tapones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Tapones_recibidos)
                    .addComponent(spinner_tapones_recibidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Perdidas)
                    .addComponent(spinner_perdidas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(faltas_recibidas)
                    .addComponent(spinner_faltas_recibidas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(faltas_realizadas)
                    .addComponent(spinner_faltas_realizadas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(55, 55, 55)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Boton_calcular, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Boton_grafica1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Boton_PDF_1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Boton_BORRAR, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Mas datos", jPanel3);

        tamaño.setText("Cambiar tamaño");

        pequeño.setText("pequeño");
        pequeño.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pequeñoActionPerformed(evt);
            }
        });
        tamaño.add(pequeño);

        mediano.setText("mediano");
        tamaño.add(mediano);

        grande.setText("grande");
        tamaño.add(grande);

        jMenuBar1.add(tamaño);

        condiciones_servicio.setText("Condiciones del servicio");
        jMenuBar1.add(condiciones_servicio);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 502, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 505, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pequeñoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pequeñoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pequeñoActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BaloncestoJFRAME().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Boton_BORRAR;
    private javax.swing.JButton Boton_PDF_1;
    private javax.swing.JButton Boton_calcular;
    private javax.swing.JButton Boton_grafica1;
    private javax.swing.JLabel Perdidas;
    private javax.swing.JLabel Robos;
    private javax.swing.JLabel Tapones;
    private javax.swing.JLabel Tapones_recibidos;
    private javax.swing.JLabel asistencias;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> combo_equipos;
    private javax.swing.JComboBox<String> combo_jugadores;
    private javax.swing.JMenu condiciones_servicio;
    private javax.swing.JLabel faltas_realizadas;
    private javax.swing.JLabel faltas_recibidas;
    private javax.swing.JMenuItem grande;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JMenuItem mediano;
    private com.mycompany.baloncesto.MiComponente miComponente1;
    private com.mycompany.baloncesto.MiComponente miComponente2;
    private com.mycompany.baloncesto.MiComponente miComponente3;
    private com.mycompany.baloncesto.MiComponente miComponente4;
    private com.mycompany.baloncesto.MiComponente miComponente5;
    private com.mycompany.baloncesto.MiComponente miComponente6;
    private javax.swing.JMenuItem pequeño;
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
    private javax.swing.JMenu tamaño;
    // End of variables declaration//GEN-END:variables
}
