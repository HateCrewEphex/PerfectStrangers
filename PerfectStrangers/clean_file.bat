@echo off
setlocal enabledelayedexpansion

set "inputFile=src\main\java\com\mycompany\perfectstrangers\PSConOrder.java"
set "outputFile=%inputFile%.tmp"

(for /f "delims=" %%A in ('type "%inputFile%"') do (
    set "line=%%A"
    if not "!line!"=="        try {" (
        if not "!line!"=="            DBConnection.asegurarEstadoDetalleOrden();" (
            if not "!line!"=="        } catch (SQLException ex) {" (
                if not "!line:logger.log=!"=="!line!" (
                    if not "!line!"=="        }" (
                        echo !line!
                    ) else (
                        REM Skip closing brace only if preceded by logger.log
                        echo !line!
                    )
                ) else (
                    echo !line!
                )
            ) else (
                echo !line!
            )
        ) else (
            echo !line!
        )
    ) else (
        echo !line!
    )
)) > "%outputFile%"

move /y "%outputFile%" "%inputFile%"
echo Limpieza completada para PSConOrder.java
