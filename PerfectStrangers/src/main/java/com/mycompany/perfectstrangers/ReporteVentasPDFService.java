package com.mycompany.perfectstrangers;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

//marca de agua
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfGState;

public class ReporteVentasPDFService {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ReporteVentasPDFService.class.getName());

    public enum TipoReporte {
        GENERAL,
        RESUMEN_CATEGORIAS,
        DETALLE_PRODUCTOS
    }

    public static class FiltroReporte {
        public final Date fechaInicio;
        public final Date fechaFin;
        public final String metodoPago;
        public final String mesero;

        public FiltroReporte(Date fechaInicio, Date fechaFin, String metodoPago, String mesero) {
            this.fechaInicio = fechaInicio;
            this.fechaFin = fechaFin;
            this.metodoPago = metodoPago;
            this.mesero = mesero;
        }
    }

    public static void generarReporte(File destino, FiltroReporte filtro, TipoReporte tipo, String etiquetaPeriodo) throws Exception {
        Document doc = new Document(PageSize.A4.rotate(), 30, 30, 24, 24);
        try (FileOutputStream fos = new FileOutputStream(destino)) {
            PdfWriter writer = PdfWriter.getInstance(doc, fos);
            writer.setPageEvent(new MarcaAguaImagen("logo.png"));
            //writer.setPageEvent(new MarcaAguaImagen("C:/reportes/logo.png"));
            /*writer.setPageEvent(new MarcaAguaImagen(
                getClass().getResource("/img/logo.png")
            ));*/
            doc.open();

            try {
                Font titulo = new Font(Font.HELVETICA, 18, Font.BOLD);
                Font subtitulo = new Font(Font.HELVETICA, 11, Font.NORMAL);
                doc.add(new Paragraph("PerfectStrangers - Reporte de Ventas", titulo));
                doc.add(new Paragraph("Tipo: " + tipoToTexto(tipo), subtitulo));
                doc.add(new Paragraph("Periodo: " + etiquetaPeriodo, subtitulo));
                doc.add(new Paragraph("Metodo: " + (filtro.metodoPago == null ? "Todos" : filtro.metodoPago), subtitulo));
                doc.add(new Paragraph("Mesero: " + (filtro.mesero == null ? "Todos" : filtro.mesero), subtitulo));
                doc.add(new Paragraph(" "));

                switch (tipo) {
                    case GENERAL -> {
                        agregarReporteGeneral(doc, filtro);
                        doc.newPage();
                    }
                    case RESUMEN_CATEGORIAS -> {
                        agregarReporteCategorias(doc, filtro);
                        doc.newPage();
                    }
                    case DETALLE_PRODUCTOS -> {
                        agregarReporteProductos(doc, filtro);
                        doc.newPage();
                    }
                }
            } finally {
                if (doc.isOpen()) {
                    doc.close();
                }
            }
        }
    }

    private static void agregarReporteGeneral(Document doc, FiltroReporte filtro) throws Exception {
        String sqlBase = "FROM pagos pg " +
            "JOIN ordenes o ON pg.id_orden = o.id_orden " +
            "JOIN empleados e ON o.id_empleado = e.id_empleado ";

        List<Object> params = new ArrayList<>();
        String where = construirWherePagos(filtro, "pg", "e", params);

        String sqlTotales = "SELECT COUNT(*) AS operaciones, COALESCE(SUM(pg.monto_pagado),0) AS total, COALESCE(AVG(pg.monto_pagado),0) AS ticket_promedio " +
            sqlBase + where;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sqlTotales)) {

            setParams(pst, params);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    doc.add(new Paragraph("Resumen General", new Font(Font.HELVETICA, 13, Font.BOLD)));
                    doc.add(new Paragraph("Operaciones: " + rs.getInt("operaciones")));
                    doc.add(new Paragraph(String.format("Total cobrado: $%.2f", rs.getDouble("total"))));
                    doc.add(new Paragraph(String.format("Ticket promedio: $%.2f", rs.getDouble("ticket_promedio"))));
                    doc.add(new Paragraph(" "));
                }
            }
        }

        String sqlMetodo = "SELECT pg.metodo_pago, COUNT(*) AS operaciones, COALESCE(SUM(pg.monto_pagado),0) AS total " +
            sqlBase + where +
            " GROUP BY pg.metodo_pago ORDER BY total DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sqlMetodo)) {

            setParams(pst, params);
            try (ResultSet rs = pst.executeQuery()) {
                PdfPTable tabla = new PdfPTable(3);
                tabla.setWidthPercentage(100);
                tabla.setWidths(new float[]{2f, 1f, 1.5f});
                agregarCabecera(tabla, "Metodo");
                agregarCabecera(tabla, "Operaciones");
                agregarCabecera(tabla, "Total");

                boolean hayDatos = false;
                while (rs.next()) {
                    hayDatos = true;
                    tabla.addCell(rs.getString("metodo_pago"));
                    tabla.addCell(String.valueOf(rs.getInt("operaciones")));
                    tabla.addCell(String.format("$%.2f", rs.getDouble("total")));
                }

                doc.add(new Paragraph("Desglose por Metodo de Pago", new Font(Font.HELVETICA, 13, Font.BOLD)));
                if (hayDatos) {
                    doc.add(tabla);
                } else {
                    doc.add(new Paragraph("Sin datos para los filtros seleccionados."));
                }
            }
        }
    }

    private static void agregarReporteCategorias(Document doc, FiltroReporte filtro) throws Exception {
        List<Object> params = new ArrayList<>();
        String where = construirWherePagos(filtro, "pg", "e", params);

        String sql = "SELECT pr.categoria, COALESCE(SUM(d.cantidad),0) AS unidades, " +
            "COALESCE(SUM(d.cantidad * d.precio_unitario),0) AS total " +
            "FROM ( " +
            "   SELECT DISTINCT o.id_orden " +
            "   FROM pagos pg " +
            "   JOIN ordenes o ON pg.id_orden = o.id_orden " +
            "   JOIN empleados e ON o.id_empleado = e.id_empleado " +
            where +
            ") ord " +
            "JOIN detalle_orden d ON ord.id_orden = d.id_orden " +
            "JOIN productos pr ON d.id_producto = pr.id_producto " +
            "WHERE pr.categoria IN ('Platillo', 'Bebidas') " +
            "GROUP BY pr.categoria " +
            "ORDER BY total DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            setParams(pst, params);
            try (ResultSet rs = pst.executeQuery()) {
                PdfPTable tabla = new PdfPTable(3);
                tabla.setWidthPercentage(100);
                tabla.setWidths(new float[]{2f, 1f, 1.5f});
                agregarCabecera(tabla, "Categoria");
                agregarCabecera(tabla, "Unidades");
                agregarCabecera(tabla, "Venta Total");

                boolean hayDatos = false;
                while (rs.next()) {
                    hayDatos = true;
                    tabla.addCell(rs.getString("categoria"));
                    tabla.addCell(String.valueOf(rs.getInt("unidades")));
                    tabla.addCell(String.format("$%.2f", rs.getDouble("total")));
                }

                doc.add(new Paragraph("Resumen de Ventas: Platillos vs Bebidas", new Font(Font.HELVETICA, 13, Font.BOLD)));
                if (hayDatos) {
                    doc.add(tabla);
                } else {
                    doc.add(new Paragraph("Sin datos para los filtros seleccionados."));
                }
            }
        }
    }

    private static void agregarReporteProductos(Document doc, FiltroReporte filtro) throws Exception {
        List<Object> params = new ArrayList<>();
        String where = construirWherePagos(filtro, "pg", "e", params);

        String sql = "SELECT pr.categoria, pr.nombre, " +
            "COALESCE(SUM(d.cantidad),0) AS unidades, " +
            "COALESCE(SUM(d.cantidad * d.precio_unitario),0) AS total, " +
            "COALESCE(AVG(d.precio_unitario),0) AS precio_promedio " +
            "FROM ( " +
            "   SELECT DISTINCT o.id_orden " +
            "   FROM pagos pg " +
            "   JOIN ordenes o ON pg.id_orden = o.id_orden " +
            "   JOIN empleados e ON o.id_empleado = e.id_empleado " +
            where +
            ") ord " +
            "JOIN detalle_orden d ON ord.id_orden = d.id_orden " +
            "JOIN productos pr ON d.id_producto = pr.id_producto " +
            "WHERE pr.categoria IN ('Platillo', 'Bebidas') " +
            "GROUP BY pr.id_producto, pr.categoria, pr.nombre " +
            "ORDER BY pr.categoria, total DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            setParams(pst, params);
            try (ResultSet rs = pst.executeQuery()) {
                PdfPTable tabla = new PdfPTable(5);
                tabla.setWidthPercentage(100);
                tabla.setWidths(new float[]{1.3f, 2.8f, 1f, 1.5f, 1.4f});
                agregarCabecera(tabla, "Categoria");
                agregarCabecera(tabla, "Producto");
                agregarCabecera(tabla, "Unidades");
                agregarCabecera(tabla, "Total");
                agregarCabecera(tabla, "Precio Prom.");

                boolean hayDatos = false;
                while (rs.next()) {
                    hayDatos = true;
                    tabla.addCell(rs.getString("categoria"));
                    tabla.addCell(rs.getString("nombre"));
                    tabla.addCell(String.valueOf(rs.getInt("unidades")));
                    tabla.addCell(String.format("$%.2f", rs.getDouble("total")));
                    tabla.addCell(String.format("$%.2f", rs.getDouble("precio_promedio")));
                }

                doc.add(new Paragraph("Detalle de Ventas por Producto (Platillos y Bebidas)", new Font(Font.HELVETICA, 13, Font.BOLD)));
                if (hayDatos) {
                    doc.add(tabla);
                } else {
                    doc.add(new Paragraph("Sin datos para los filtros seleccionados."));
                }
            }
        }
    }

    private static String construirWherePagos(FiltroReporte filtro, String aliasPago, String aliasEmpleado, List<Object> params) {
        StringBuilder where = new StringBuilder(" WHERE DATE(" + aliasPago + ".fecha_pago) BETWEEN ? AND ? ");
        params.add(filtro.fechaInicio);
        params.add(filtro.fechaFin);

        if (filtro.metodoPago != null && !filtro.metodoPago.isBlank()) {
            where.append(" AND ").append(aliasPago).append(".metodo_pago = ? ");
            params.add(filtro.metodoPago);
        }

        if (filtro.mesero != null && !filtro.mesero.isBlank()) {
            where.append(" AND ").append(aliasEmpleado).append(".nombre = ? ");
            params.add(filtro.mesero);
        }

        return where.toString();
    }

    private static void setParams(PreparedStatement pst, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            pst.setObject(i + 1, params.get(i));
        }
    }

    private static void agregarCabecera(PdfPTable tabla, String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, new Font(Font.HELVETICA, 10, Font.BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(6);
        tabla.addCell(cell);
    }

    private static String tipoToTexto(TipoReporte tipo) {
        return switch (tipo) {
            case GENERAL -> "General";
            case RESUMEN_CATEGORIAS -> "Resumen platillos y bebidas";
            case DETALLE_PRODUCTOS -> "Detalle por producto";
        };
    }
    
    static class MarcaAguaImagen extends PdfPageEventHelper {

        private Image imagen;

        public MarcaAguaImagen(String rutaImagen) {
            try {
                // Modificado para obtener la imagen desde los recursos del proyecto (jar)
                java.net.URL url = getClass().getResource("/com/mycompany/perfectstrangers/logo150.png");
                if (url != null) {
                    imagen = Image.getInstance(url);
                } else {
                    // Fallback local file si no se encuentra en el classpath
                    imagen = Image.getInstance(rutaImagen);
                }
                
                imagen.scaleToFit(400, 400);
                imagen.setAlignment(Image.UNDERLYING);

            } catch (Exception e) {
                logger.warning("No se pudo cargar la imagen para la marca de agua: " + e.getMessage());
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            if (imagen == null) return;
            try {
                PdfContentByte canvas = writer.getDirectContentUnder();

                PdfGState gs1 = new PdfGState();
                gs1.setFillOpacity(0.15f);
                canvas.setGState(gs1);

                float x = (document.getPageSize().getWidth() - imagen.getScaledWidth()) / 2;
                float y = (document.getPageSize().getHeight() - imagen.getScaledHeight()) / 2;

                imagen.setAbsolutePosition(x, y);
                canvas.addImage(imagen);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
