# Generar instalador Windows (PerfectStrangers)

Este script crea un instalador para Windows que incluye la aplicación empaquetada y una JRE embebida, de modo que el usuario final no necesita instalar Java.

Requisitos:
- JDK instalado con la herramienta `jpackage` disponible en el PATH (JDK 14+). Usar la misma versión mayor que la `jre-image` incluida si es posible.
- Carpeta `jre-image` en la raíz del proyecto (ya está incluida en este repositorio).
- `mvn` (Maven) en PATH para construir el JAR sombreado.

Uso:

1. Abrir PowerShell o CMD en la raíz del proyecto.
2. Ejecutar:

```bat
build_installer.bat
```

El script hará `mvn -DskipTests package` para generar el JAR sombreado en `target/` y luego ejecutará `jpackage` para crear un instalador `.exe` dentro de la carpeta `installer/`.

Notas:
- Si `jpackage` no está disponible, instala un JDK que lo incluya o usa la alternativa de Inno Setup (no incluida).
- Si quieres un icono personalizado, convierte un `png` a `ico` y actualiza el script para pasar `--icon path\to\icon.ico`.
- El instalador genera accesos directos en el menú inicio y acceso directo en el escritorio (opciones por defecto de `jpackage`).
