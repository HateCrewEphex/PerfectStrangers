package com.mycompany.perfectstrangers;

public class MPModoPDVQuickRun {

    public static void main(String[] args) {
        MercadoPagoAPI api = new MercadoPagoAPI();
        String resultado = api.activarModoPDV();
        System.out.println(resultado);
    }
}
