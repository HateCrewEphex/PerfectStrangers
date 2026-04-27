#!/usr/bin/env python3
import re

files_to_clean = [
    r'src\main\java\com\mycompany\perfectstrangers\PSConOrder.java',
    r'src\main\java\com\mycompany\perfectstrangers\PSTOrden.java'
]

for filepath in files_to_clean:
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # Remove the try-catch block with asegurarEstadoDetalleOrden
        pattern = r'\s*try\s*\{\s*DBConnection\.asegurarEstadoDetalleOrden\(\);\s*\}\s*catch\s*\(SQLException\s+ex\)\s*\{\s*logger\.log\([^)]+\);\s*\}\s*'
        content = re.sub(pattern, '', content)
        
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)
        
        print(f"✓ Limpieza completada: {filepath}")
    except Exception as e:
        print(f"✗ Error procesando {filepath}: {e}")
