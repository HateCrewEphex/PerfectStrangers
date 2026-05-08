# Script para crear acceso directo al escritorio de PerfectStrangers
# Ubicación del instalador
$installerPath = "C:\Users\LordN\Desktop\PerfectStrangers\PerfectStrangers\PerfectStrangers\dist\installer\PerfectStrangers-1.0.exe"

# Ubicación del escritorio del usuario actual
$desktopPath = [System.Environment]::GetFolderPath("Desktop")

# Ruta del acceso directo
$shortcutPath = "$desktopPath\PerfectStrangers.lnk"

# Crear el objeto WshShell
$shell = New-Object -ComObject WScript.Shell

# Crear el acceso directo
$shortcut = $shell.CreateShortcut($shortcutPath)
$shortcut.TargetPath = $installerPath
$shortcut.WindowStyle = 1  # Normal window
$shortcut.Description = "Instalador de PerfectStrangers - Restaurante"
$shortcut.IconLocation = "$PSScriptRoot\src\main\java\com\mycompany\perfectstrangers\Logo nuevo.png"
$shortcut.Save()

Write-Host "Acceso directo creado en: $shortcutPath" -ForegroundColor Green
