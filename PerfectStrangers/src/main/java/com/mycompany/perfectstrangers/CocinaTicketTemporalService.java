package com.mycompany.perfectstrangers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Buffer interno para enviar a cocina solo las capturas nuevas de una mesa con cuenta abierta.
 * No reemplaza la orden de BD; solo guarda el ticket temporal que cocina debe ver.
 */
public final class CocinaTicketTemporalService {

    public static final class TicketItem {
        private final int idProducto;
        private final String nombreProducto;
        private final int cantidad;
        private final String notas;
        private final double precioUnitario;

        public TicketItem(int idProducto, String nombreProducto, int cantidad, double precioUnitario, String notas) {
            this.idProducto = idProducto;
            this.nombreProducto = nombreProducto;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
            this.notas = notas;
        }

        public int getIdProducto() {
            return idProducto;
        }

        public String getNombreProducto() {
            return nombreProducto;
        }

        public int getCantidad() {
            return cantidad;
        }

        public String getNotas() {
            return notas;
        }

        public double getPrecioUnitario() {
            return precioUnitario;
        }
    }

    public static final class TicketCocina {
        private final long idTicket;
        private final int idOrden;
        private final int mesa;
        private final Timestamp fechaHora;
        private final List<TicketItem> items;

        private TicketCocina(long idTicket, int idOrden, int mesa, Timestamp fechaHora, List<TicketItem> items) {
            this.idTicket = idTicket;
            this.idOrden = idOrden;
            this.mesa = mesa;
            this.fechaHora = fechaHora;
            this.items = items;
        }

        public long getIdTicket() {
            return idTicket;
        }

        public int getIdOrden() {
            return idOrden;
        }

        public int getMesa() {
            return mesa;
        }

        public Timestamp getFechaHora() {
            return fechaHora;
        }

        public List<TicketItem> getItems() {
            return new ArrayList<>(items);
        }
    }

    private static final AtomicLong SECUENCIA = new AtomicLong(1L);
    private static final List<TicketCocina> TICKETS = new ArrayList<>();

    private CocinaTicketTemporalService() {
    }

    public static synchronized long registrarTicket(int idOrden, int mesa, List<TicketItem> items) {
        long idTicket = SECUENCIA.getAndIncrement();
        Timestamp fechaHora = new Timestamp(System.currentTimeMillis());
        List<TicketItem> copia = new ArrayList<>(items);
        TICKETS.add(new TicketCocina(idTicket, idOrden, mesa, fechaHora, copia));
        TICKETS.sort(Comparator.comparing(TicketCocina::getFechaHora));
        return idTicket;
    }

    public static synchronized List<TicketCocina> obtenerTicketsPendientes() {
        List<TicketCocina> copia = new ArrayList<>(TICKETS);
        copia.sort(Comparator.comparing(TicketCocina::getFechaHora));
        return copia;
    }

    public static synchronized boolean consumirTicket(long idTicket) {
        return TICKETS.removeIf(ticket -> ticket.getIdTicket() == idTicket);
    }

    public static synchronized void limpiar() {
        TICKETS.clear();
    }
}