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

/**
 * Servicio para reproducir archivos de audio (MP3 o WAV)
 * Maneja la reproducción de alertas del sistema
 */
public class SoundService {
    
    private static volatile Thread audioThread = null;
    
    /**
     * Reproduce un archivo de audio
     * @param nombreArchivo nombre del archivo (ej: "PerfectStrangers alert.mp3")
     */
    public static void reproducirSonido(String nombreArchivo) {
        // Ejecutar en hilo separado para no bloquear UI
        audioThread = new Thread(() -> {
            try {
                // Intenta reproducir como WAV/AIFF primero
                reproducirWAV(nombreArchivo);
            } catch (Exception e1) {
                try {
                    // Si falla, intenta reproducir como MP3 usando proceso externo
                    reproducirMP3Externo(nombreArchivo);
                } catch (Exception e2) {
                    System.err.println("Error reproduciendo sonido: " + nombreArchivo);
                    e2.printStackTrace();
                }
            }
        });
        
        audioThread.setDaemon(true);
        audioThread.start();
    }
    
    /**
     * Reproduce un archivo WAV/AIFF desde los recursos
     */
    private static void reproducirWAV(String nombreArchivo) throws Exception {
        String rutaArchivo = obtenerRutaArchivo(nombreArchivo);
        
        if (rutaArchivo == null) {
            throw new FileNotFoundException("Archivo de audio no encontrado: " + nombreArchivo);
        }
        
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(rutaArchivo));
        AudioFormat format = audioInputStream.getFormat();
        
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine audioLine = (SourceDataLine) AudioSystem.getLine(info);
        
        audioLine.open(format);
        audioLine.start();
        
        byte[] buffer = new byte[4096];
        int bytesRead;
        
        while ((bytesRead = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
            audioLine.write(buffer, 0, bytesRead);
        }
        
        audioLine.drain();
        audioLine.close();
        audioInputStream.close();
    }
    
    /**
     * Reproduce un archivo MP3 usando proceso externo
     * Busca el archivo en los recursos del JAR
     */
    private static void reproducirMP3Externo(String nombreArchivo) throws Exception {
        String rutaArchivo = obtenerRutaArchivo(nombreArchivo);
        
        if (rutaArchivo == null) {
            throw new FileNotFoundException("Archivo de audio no encontrado: " + nombreArchivo);
        }
        
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                String script = "$player = New-Object -ComObject WMPlayer.OCX; "
                    + "$media = $player.newMedia($args[0]); "
                    + "$player.currentPlaylist.clear(); "
                    + "$null = $player.currentPlaylist.appendItem($media); "
                    + "$player.controls.play(); "
                    + "while ($player.playState -ne 1 -and $player.playState -ne 8) { Start-Sleep -Milliseconds 200 }";
                ProcessBuilder pb = new ProcessBuilder(
                    "powershell", "-NoProfile", "-ExecutionPolicy", "Bypass", "-Command", script, rutaArchivo
                );
                pb.start().waitFor();
            } else {
                // En Linux/Mac, usar ffplay o similar
                ProcessBuilder pb = new ProcessBuilder("ffplay", "-nodisp", "-autoexit", rutaArchivo);
                pb.start().waitFor();
            }
        } catch (Exception e) {
            System.err.println("No se pudo reproducir MP3: " + e.getMessage());
            throw e;
        }
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
     * Busca en: 1) Carpeta de recursos /audio/, 2) Carpeta del paquete, 3) Sistema de archivos
     */
    private static String obtenerRutaArchivo(String nombreArchivo) {
        // 0. Carpeta del proyecto durante desarrollo
        try {
            Path pathDesarrollo = Path.of(System.getProperty("user.dir"), "src", "main", "java", "com", "mycompany", "perfectstrangers", nombreArchivo);
            if (Files.exists(pathDesarrollo)) {
                return pathDesarrollo.toAbsolutePath().toString();
            }
        } catch (Exception e) {}

        // 1. Intenta buscar en /audio/ (JAR o classpath)
        try {
            URL url = SoundService.class.getResource("/audio/" + nombreArchivo);
            if (url != null) {
                return URLDecoder.decode(url.getPath(), "UTF-8");
            }
        } catch (Exception e) {}
        
        // 2. Intenta buscar en la carpeta del paquete (misma carpeta que los .java)
        try {
            URL url = SoundService.class.getResource(nombreArchivo);
            if (url != null) {
                String path = URLDecoder.decode(url.getPath(), "UTF-8");
                // En Windows, remover el primer "/" si es así (ej: /C:/ruta/...)
                if (path.startsWith("/") && path.length() > 2 && path.charAt(2) == ':') {
                    path = path.substring(1);
                }
                return path;
            }
        } catch (Exception e) {}
        
        // 3. Intenta buscar en el sistema de archivos directo
        // (para cuando se ejecuta desde el IDE durante desarrollo)
        try {
            String classPath = SoundService.class.getProtectionDomain()
                .getCodeSource().getLocation().getPath();
            classPath = URLDecoder.decode(classPath, "UTF-8");
            
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
        } catch (Exception e) {}
        
        return null;
    }
}
