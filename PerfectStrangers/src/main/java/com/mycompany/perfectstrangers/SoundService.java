package com.mycompany.perfectstrangers;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Servicio para reproducir archivos de audio WAV
 * Maneja la reproducción de alertas del sistema
 */
public class SoundService {
    
    private static volatile Thread audioThread = null;
    
    /**
     * Reproduce un archivo de audio
     * @param nombreArchivo nombre del archivo (ej: "PerfectStrangers-alert.wav")
     */
    public static void reproducirSonido(String nombreArchivo) {
        audioThread = new Thread(() -> {
            try {
                reproducirWAV(nombreArchivo);
            } catch (Exception e) {
                System.err.println("Error reproduciendo sonido: " + nombreArchivo);
                System.err.println(e.getMessage());
            }
        });
        
        audioThread.setDaemon(true);
        audioThread.start();
    }
    
    /**
     * Reproduce un archivo WAV desde el classpath o el sistema de archivos.
     */
    private static void reproducirWAV(String nombreArchivo) throws Exception {
        try (AudioInputStream audioInputStream = abrirAudioInputStream(nombreArchivo)) {
            AudioFormat formatoBase = audioInputStream.getFormat();
            AudioFormat formatoReproduccion = obtenerFormatoReproduccion(formatoBase);
            if (formatoReproduccion.matches(formatoBase)) {
                reproducirStream(audioInputStream, formatoReproduccion);
            } else {
                try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(formatoReproduccion, audioInputStream)) {
                    reproducirStream(audioStream, formatoReproduccion);
                }
            }
        }
    }

    private static void reproducirStream(AudioInputStream audioStream, AudioFormat formatoReproduccion) throws Exception {
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, formatoReproduccion);
        SourceDataLine audioLine = (SourceDataLine) AudioSystem.getLine(info);

        try {
            audioLine.open(formatoReproduccion);
            audioLine.start();

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = audioStream.read(buffer, 0, buffer.length)) != -1 && !Thread.currentThread().isInterrupted()) {
                audioLine.write(buffer, 0, bytesRead);
            }

            audioLine.drain();
        } finally {
            audioLine.stop();
            audioLine.close();
        }
    }

    private static AudioInputStream abrirAudioInputStream(String nombreArchivo) throws Exception {
        URL resourceUrl = SoundService.class.getResource("/audio/" + nombreArchivo);
        if (resourceUrl != null) {
            return AudioSystem.getAudioInputStream(resourceUrl);
        }

        String rutaArchivo = obtenerRutaArchivo(nombreArchivo);
        if (rutaArchivo == null) {
            throw new FileNotFoundException("Archivo de audio no encontrado: " + nombreArchivo);
        }

        try {
            return AudioSystem.getAudioInputStream(new File(rutaArchivo));
        } catch (UnsupportedAudioFileException ex) {
            throw new IllegalArgumentException("Formato de audio no soportado: " + nombreArchivo, ex);
        }
    }

    private static AudioFormat obtenerFormatoReproduccion(AudioFormat formatoBase) {
        if (AudioSystem.isLineSupported(new DataLine.Info(SourceDataLine.class, formatoBase))) {
            return formatoBase;
        }

        return new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            formatoBase.getSampleRate(),
            16,
            formatoBase.getChannels(),
            formatoBase.getChannels() * 2,
            formatoBase.getSampleRate(),
            false
        );
    }
    
    /**
     * Detiene el sonido que se está reproduciendo
     */
    public static void detenerSonido() {
        if (audioThread != null && audioThread.isAlive()) {
            audioThread.interrupt();
        }
    }
    
    /**
     * Obtiene la ruta del archivo de audio
     * Busca en: 1) Carpeta del paquete, 2) Sistema de archivos compilado
     */
    private static String obtenerRutaArchivo(String nombreArchivo) {
        // 0. Carpeta del proyecto durante desarrollo
        try {
            Path pathDesarrollo = Path.of(System.getProperty("user.dir"), "src", "main", "java", "com", "mycompany", "perfectstrangers", nombreArchivo);
            if (Files.exists(pathDesarrollo)) {
                return pathDesarrollo.toAbsolutePath().toString();
            }
        } catch (RuntimeException e) {
            return null;
        }

        // 1. Intenta buscar en la carpeta del paquete (misma carpeta que los .java)
        URL url = SoundService.class.getResource(nombreArchivo);
        if (url != null) {
            try {
                String path = URLDecoder.decode(url.getPath(), "UTF-8");
                // En Windows, remover el primer "/" si es así (ej: /C:/ruta/...)
                if (path.startsWith("/") && path.length() > 2 && path.charAt(2) == ':') {
                    path = path.substring(1);
                }
                return path;
            } catch (java.io.UnsupportedEncodingException e) {
                return null;
            }
        }
        
        // 2. Intenta buscar en el sistema de archivos directo
        // (para cuando se ejecuta desde el IDE durante desarrollo)
        String classPath = SoundService.class.getProtectionDomain()
            .getCodeSource().getLocation().getPath();
        try {
            classPath = URLDecoder.decode(classPath, "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }

        // En Windows, remover el primer "/"
        if (classPath.startsWith("/") && classPath.length() > 2 && classPath.charAt(2) == ':') {
            classPath = classPath.substring(1);
        }

        // Si es un archivo classes, buscar en src/main/java
        String basePath = classPath;
        if (basePath.contains("target/classes")) {
            basePath = basePath.replace("target/classes", 
                "src/main/java/com/mycompany/perfectstrangers");
        }

        File audioFile = new File(basePath, nombreArchivo);
        if (audioFile.exists()) {
            return audioFile.getAbsolutePath();
        }
        
        return null;
    }
}
