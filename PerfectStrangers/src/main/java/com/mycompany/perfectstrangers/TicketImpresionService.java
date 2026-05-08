package com.mycompany.perfectstrangers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.List;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.JOptionPane;

public final class TicketImpresionService {

    private static final float ANCHO_MM = 58.0f;
    private static final float PUNTOS_POR_MM = 72.0f / 25.4f;
    private static final float ANCHO_PUNTOS = ANCHO_MM * PUNTOS_POR_MM;
    // Aumentamos ligeramente el margen para evitar recortes en el borde derecho
    private static final float MARGEN_PUNTOS = 6.0f;
    private static final float GAP_COLUNAS = 6.0f;
    private static final int MAX_CARACTERES = 48;
    private static final Font FUENTE_NORMAL = new Font("Monospaced", Font.PLAIN, 8);
    private static final Font FUENTE_TITULO = new Font("Monospaced", Font.BOLD, 8);
    private static final Font FUENTE_TOTAL = new Font("Monospaced", Font.BOLD, 9);

    private TicketImpresionService() {
    }

    public static void imprimirTexto(String texto) throws PrinterException {
        PrintService servicio = resolverServicioImpresion();
        if (servicio == null) {
            throw new PrinterException("No se encontró una impresora disponible.");
        }

        List<String> lineas = normalizarLineas(texto);
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintService(servicio);
        PageFormat pageFormat = crearPageFormat(job, lineas);
        job.setPrintable(new TicketPrintable(lineas), pageFormat);
        job.print();
    }

    private static PrintService resolverServicioImpresion() {
        PrintService[] servicios = PrintServiceLookup.lookupPrintServices(null, null);
        if (servicios == null || servicios.length == 0) {
            return null;
        }

        if (servicios.length == 1) {
            return servicios[0];
        }

        PrintService porDefecto = PrintServiceLookup.lookupDefaultPrintService();
        PrintService seleccion = (PrintService) JOptionPane.showInputDialog(
            null,
            "Selecciona la impresora para el ticket:",
            "Imprimir ticket",
            JOptionPane.QUESTION_MESSAGE,
            null,
            servicios,
            porDefecto != null ? porDefecto : servicios[0]
        );

        return seleccion != null ? seleccion : porDefecto != null ? porDefecto : servicios[0];
    }

    private static PageFormat crearPageFormat(PrinterJob job, List<String> lineas) {
        double altoEstimado = Math.max(220.0, (lineas.size() + 4) * 11.0 + 20.0);

        Paper paper = new Paper();
        paper.setSize(ANCHO_PUNTOS, altoEstimado);
        paper.setImageableArea(MARGEN_PUNTOS, MARGEN_PUNTOS,
                ANCHO_PUNTOS - (MARGEN_PUNTOS * 2.0),
                altoEstimado - (MARGEN_PUNTOS * 2.0));

        PageFormat pageFormat = job.defaultPage();
        pageFormat.setOrientation(PageFormat.PORTRAIT);
        pageFormat.setPaper(paper);
        return pageFormat;
    }

    private static List<String> normalizarLineas(String texto) {
        List<String> resultado = new ArrayList<>();
        if (texto == null || texto.isBlank()) {
            return resultado;
        }

        String[] lineasOriginales = texto.replace("\r", "").split("\n");
        for (String linea : lineasOriginales) {
            if (linea == null || linea.isBlank()) {
                resultado.add("");
                continue;
            }
            envolverLinea(linea, resultado);
        }
        return resultado;
    }

    private static void envolverLinea(String linea, List<String> resultado) {
        String texto = linea;
        while (texto.length() > MAX_CARACTERES) {
            int corte = texto.lastIndexOf(' ', MAX_CARACTERES);
            if (corte <= 0) {
                corte = MAX_CARACTERES;
            }
            resultado.add(texto.substring(0, corte).trim());
            texto = texto.substring(corte).trim();
        }
        if (!texto.isEmpty()) {
            resultado.add(texto);
        }
    }

    private static String recortarParaAncho(String texto, java.awt.FontMetrics metrics, float anchoMaximo) {
        String limpio = texto == null ? "" : texto.trim();
        if (metrics.stringWidth(limpio) <= anchoMaximo) {
            return limpio;
        }

        StringBuilder sb = new StringBuilder(limpio);
        while (sb.length() > 0 && metrics.stringWidth(sb.toString() + "...") > anchoMaximo) {
            sb.setLength(sb.length() - 1);
        }
        return sb.length() == 0 ? "" : sb.toString() + "...";
    }

    private static String centrar(String texto, java.awt.FontMetrics metrics, float anchoMaximo) {
        String limpio = recortarParaAncho(texto, metrics, anchoMaximo);
        int anchoTexto = metrics.stringWidth(limpio);
        int espacio = Math.max(0, (int) ((anchoMaximo - anchoTexto) / 2.0f));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < espacio / Math.max(1, metrics.charWidth(' ')); i++) {
            sb.append(' ');
        }
        sb.append(limpio);
        return sb.toString();
    }

    private static String formatoIzquierdaDerecha(String izquierda, String derecha, java.awt.FontMetrics metrics, float anchoMaximo) {
        String der = derecha == null ? "" : derecha.trim();
        String izq = recortarParaAncho(izquierda, metrics, Math.max(0.0f, anchoMaximo - metrics.stringWidth(der) - GAP_COLUNAS));
        float anchoIzq = metrics.stringWidth(izq);
        float anchoDer = metrics.stringWidth(der);
        float espacios = Math.max(GAP_COLUNAS, anchoMaximo - anchoIzq - anchoDer);
        StringBuilder sb = new StringBuilder();
        sb.append(izq);
        int cantidadEspacios = Math.max(1, (int) Math.round(espacios / Math.max(1, metrics.charWidth(' '))));
        for (int i = 0; i < cantidadEspacios; i++) {
            sb.append(' ');
        }
        sb.append(der);
        return sb.toString();
    }

    private static final class TicketPrintable implements Printable {

        private final List<String> lineas;

        private TicketPrintable(List<String> lineas) {
            this.lineas = lineas;
        }

        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            if (pageIndex > 0) {
                return NO_SUCH_PAGE;
            }

            Graphics2D g2 = (Graphics2D) graphics.create();
            try {
                g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                // Forzar texto negro para impresión (más contraste en térmicas)
                g2.setColor(Color.BLACK);
                java.awt.FontMetrics metrics = g2.getFontMetrics(FUENTE_NORMAL);
                int altoLinea = metrics.getHeight();
                int y = altoLinea;
                float anchoUtil = (float) pageFormat.getImageableWidth();

                for (String linea : lineas) {
                    if (linea == null) {
                        y += altoLinea;
                        continue;
                    }

                    String limpia = linea.trim();
                    if (limpia.isEmpty()) {
                        y += altoLinea / 2;
                        continue;
                    }

                    if (limpia.startsWith("TITLE|")) {
                        g2.setFont(FUENTE_TITULO);
                        java.awt.FontMetrics m = g2.getFontMetrics(g2.getFont());
                        String texto = limpia.substring(6).trim();
                        g2.drawString(centrar(texto, m, anchoUtil), 0, y);
                    } else if (limpia.startsWith("SUBTITLE|")) {
                        g2.setFont(FUENTE_TITULO);
                        java.awt.FontMetrics m = g2.getFontMetrics(g2.getFont());
                        String texto = limpia.substring(9).trim();
                        g2.drawString(centrar(texto, m, anchoUtil), 0, y);
                    } else if (limpia.equals("SEP")) {
                        g2.setFont(FUENTE_NORMAL);
                        g2.drawString(rellenar('-', Math.max(18, (int) (anchoUtil / Math.max(1, metrics.charWidth('-'))))), 0, y);
                    } else if (limpia.startsWith("SECTION|")) {
                        g2.setFont(FUENTE_TOTAL);
                        String texto = limpia.substring(8).trim();
                        g2.drawString(texto, 0, y);
                    } else if (limpia.startsWith("ITEM|")) {
                        g2.setFont(FUENTE_NORMAL);
                        java.awt.FontMetrics mItem = g2.getFontMetrics(g2.getFont());
                        String[] partes = limpia.split("\\|", 3);
                        String izquierda = partes.length > 1 ? partes[1] : "";
                        String derecha = partes.length > 2 ? partes[2] : "";
                        // Dibujar columna izquierda y derecha por separado para evitar recortes
                        String izquierdaRecortada = recortarParaAncho(izquierda, mItem, Math.max(0.0f, anchoUtil - mItem.stringWidth(derecha) - GAP_COLUNAS));
                        g2.drawString(izquierdaRecortada, 0, y);
                        g2.drawString(derecha, (int) (anchoUtil - mItem.stringWidth(derecha)), y);
                    } else if (limpia.startsWith("DATA|")) {
                        g2.setFont(FUENTE_NORMAL);
                        java.awt.FontMetrics mData = g2.getFontMetrics(g2.getFont());
                        String[] partes = limpia.split("\\|", 3);
                        String etiqueta = partes.length > 1 ? partes[1] : "";
                        String valor = partes.length > 2 ? partes[2] : "";
                        String etiquetaRecortada = recortarParaAncho(etiqueta, mData, Math.max(0.0f, anchoUtil - mData.stringWidth(valor) - GAP_COLUNAS));
                        g2.drawString(etiquetaRecortada, 0, y);
                        g2.drawString(valor, (int) (anchoUtil - mData.stringWidth(valor)), y);
                    } else if (limpia.startsWith("TOTAL|")) {
                        g2.setFont(FUENTE_TOTAL);
                        java.awt.FontMetrics mTotal = g2.getFontMetrics(g2.getFont());
                        String[] partes = limpia.split("\\|", 3);
                        String etiqueta = partes.length > 1 ? partes[1] : "";
                        String valor = partes.length > 2 ? partes[2] : "";
                        String etiquetaRecortada = recortarParaAncho(etiqueta, mTotal, Math.max(0.0f, anchoUtil - mTotal.stringWidth(valor) - GAP_COLUNAS));
                        g2.drawString(etiquetaRecortada, 0, y);
                        g2.drawString(valor, (int) (anchoUtil - mTotal.stringWidth(valor)), y);
                    } else if (limpia.startsWith("FOOTER|")) {
                        g2.setFont(FUENTE_NORMAL);
                        java.awt.FontMetrics mFooter = g2.getFontMetrics(g2.getFont());
                        String texto = limpia.substring(7).trim();
                        g2.drawString(centrar(texto, mFooter, anchoUtil), 0, y);
                    } else {
                        g2.setFont(FUENTE_NORMAL);
                        g2.drawString(limpia, 0, y);
                    }
                    y += altoLinea;
                }

                return PAGE_EXISTS;
            } finally {
                g2.dispose();
            }
        }

        private String rellenar(char caracter, int ancho) {
            StringBuilder sb = new StringBuilder(ancho);
            for (int i = 0; i < ancho; i++) {
                sb.append(caracter);
            }
            return sb.toString();
        }
    }
}