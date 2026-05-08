#!/usr/bin/env python3
"""
Convertidor simple de PNG a ICO usando solo librerías estándar.
Crea un ICO mínimo con estructura válida.
"""
import struct
import io

def png_to_ico(png_path, ico_path):
    """Convierte PNG a ICO usando una solución simple."""
    try:
        # Leer el PNG
        with open(png_path, 'rb') as f:
            png_data = f.read()
        
        # Para un ICO válido, necesitamos:
        # 1. ICO header (6 bytes): Reserved(0), Type(1), Count(1 uint16)
        # 2. Image dir entries (16 bytes cada una)
        # 3. Imagen (BITMAPINFOHEADER + pixels)
        
        # Estructura simple: crear un ICO con una entrada
        # ICO Header
        ico_header = struct.pack('<HHH', 0, 1, 1)  # Reserved, Type (1=ICO), Count
        
        # Para simplificar, usaremos el PNG como raw data en el ICO
        # Esta es una aproximación que algunos lectores pueden aceptar
        
        image_data = png_data
        image_size = len(image_data)
        image_offset = 6 + 16  # Header + 1 directory entry
        
        # Image directory entry: Width, Height, ColorCount, Reserved, Planes, BitCount, ImageSize, ImageOffset
        dir_entry = struct.pack('<BBBBHHII', 
            0,        # Width (0 = cualquier tamaño)
            0,        # Height (0 = cualquier tamaño)
            0,        # Color count (0 = no hay tabla de colores)
            0,        # Reserved
            1,        # Color planes
            32,       # Bits per pixel
            image_size,      # Image size
            image_offset     # Image offset
        )
        
        # Escribir el ICO
        with open(ico_path, 'wb') as f:
            f.write(ico_header)
            f.write(dir_entry)
            f.write(image_data)
        
        print(f"ICO creado exitosamente: {ico_path}")
        return True
    except Exception as e:
        print(f"Error: {e}")
        return False

if __name__ == "__main__":
    png_file = r"C:\Users\LordN\Desktop\PerfectStrangers\PerfectStrangers\PerfectStrangers\src\main\java\com\mycompany\perfectstrangers\Logo nuevo.png"
    ico_file = r"C:\Users\LordN\Desktop\PerfectStrangers\PerfectStrangers\PerfectStrangers\dist\PerfectStrangers.ico"
    
    png_to_ico(png_file, ico_file)
