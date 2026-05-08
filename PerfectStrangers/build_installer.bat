@echo off
rem Build installer for PerfectStrangers using jpackage
rem Requirements: JDK with jpackage on PATH (same major version as runtime-image),
rem the project built (target/PerfectStrangers-1.0-SNAPSHOT-shaded.jar) and a valid runtime image in ./jre-image

setlocal enabledelayedexpansion

echo Building project jar with Maven...
mvn -DskipTests package || (echo Maven build failed && exit /b 1)

set APP_NAME=PerfectStrangers
set MAIN_JAR=PerfectStrangers-1.0-SNAPSHOT-shaded.jar
set MAIN_CLASS=com.mycompany.perfectstrangers.PerfectStrangers
set RUNTIME_IMAGE=%CD%\jre-image
set INPUT_DIR=%CD%\target
set DEST_DIR=%CD%\installer

if not exist "%RUNTIME_IMAGE%" (
  echo Runtime image not found at %RUNTIME_IMAGE%
  echo You can generate one with jlink, or place a JRE here.
  pause
  exit /b 1
)

set ICON_FILE=%CD%\dist\PerfectStrangers.ico

echo Running jpackage...
if exist "%ICON_FILE%" (
  echo Usando icono: %ICON_FILE%
  jpackage --type exe --input "%INPUT_DIR%" --name "%APP_NAME%" --main-jar "%MAIN_JAR%" --main-class "%MAIN_CLASS%" --app-version 1.0 --vendor "Perfect Strangers" --runtime-image "%RUNTIME_IMAGE%" --icon "%ICON_FILE%" --win-shortcut --win-menu --dest "%DEST_DIR%"
) else (
  echo Icono no encontrado en %ICON_FILE%. Generando sin icono...
  jpackage --type exe --input "%INPUT_DIR%" --name "%APP_NAME%" --main-jar "%MAIN_JAR%" --main-class "%MAIN_CLASS%" --app-version 1.0 --vendor "Perfect Strangers" --runtime-image "%RUNTIME_IMAGE%" --win-shortcut --win-menu --dest "%DEST_DIR%"
)

if %ERRORLEVEL% equ 0 (
  echo Installer created in %DEST_DIR%
  dir "%DEST_DIR%"
) else (
  echo jpackage failed with exit code %ERRORLEVEL%
)

endlocal

pause
