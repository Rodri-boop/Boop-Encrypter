# 🔐 Gestor de Contraseñas Cifradas en Java

Este es un gestor de contraseñas en **Java** que utiliza **AES-256** para cifrar y almacenar de forma segura las credenciales del usuario en un archivo local.

## 🚀 Características

✅ **Cifrado AES-256** para máxima seguridad.  
✅ **Almacenamiento persistente** de contraseñas cifradas.  
✅ **Menú interactivo en consola** para administrar credenciales.  
✅ **Carga y reutilización de clave AES segura**.  

## 📂 Estructura del Proyecto
📦 password-manager 

  ┣ 📜 Encryptor.java # Cifrado y descifrado de datos con AES-256  
  ┣ 📜 KeyManager.java # Generación y almacenamiento seguro de la clave AES  
  ┣ 📜 PasswordManager.java # Gestión de contraseñas con menú en consola   
  ┣ 📜 passwords.dat # Archivo cifrado donde se almacenan las contraseñas   
  ┣ 📜 key.aes # Clave AES en Base64 (archivo generado automáticamente)  
  
## 📦 Instalación

1. **Clona el repositorio**  
   ```bash
   git clone https://github.com/tu-usuario/Boop-Encrypter.git
   cd Boop-Encrypter
2. **Compila y ejecuta el programa**
   ```bash
    javac *.java
    java PasswordManager
# 📖 Uso
Al iniciar el programa, verás un menú como este:
   ```markdown
      🔐 GESTOR DE CONTRASEÑAS
      1. Agregar contraseña
      2. Recuperar contraseña
      3. Listar servicios
      4. Salir
      Selecciona una opción:
```
- Agregar contraseña: Introduce el nombre del servicio y su contraseña.

- Recuperar contraseña: Introduce el servicio para ver su contraseña descifrada.

- Listar servicios: Muestra todos los servicios almacenados.


# 🔒 Seguridad
- AES-256 para cifrar contraseñas.

- Clave AES única, almacenada en key.aes.

- Próximas mejoras: clave maestra, autenticación y base de datos cifrada.

# 📜 Licencia
Este proyecto está bajo la Licencia MIT.

