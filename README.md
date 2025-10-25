
# 📱 Beneficio Joven – Repo 1: **Aplicación Móvil (Android)**

Este repositorio contiene la **app Android** del sistema de cupones para el programa **Beneficio Joven** del Gobierno Municipal de Atizapán de Zaragoza.  
La app permite a las y los jóvenes **autenticarse**, **explorar comercios**, **consultar cupones**, **ver código QR/código único** y **gestionar favoritos**.

El proyecto busca impulsar la participación juvenil y facilitar el acceso a beneficios que promuevan el bienestar, la economía local y las oportunidades para los jóvenes de Atizapán.

----------

## 🌟 Componentes principales del proyecto completo

1.  **Aplicación móvil (Kotlin – Android)** ← _este repo_
    
    -   Código: [https://github.com/PeritiaCodex/BeneficioJoven](https://github.com/PeritiaCodex/BeneficioJoven)
        
    -   Registro e inicio de sesión.
        
    -   Consulta de cupones y promociones disponibles.
        
    -   Canje digital mediante **QR** o **clave única**.
        
    -   Perfil con preferencias básicas y **favoritos** locales.
        
2.  **Panel de administración (HTML, CSS, JS)**
    
    -   Código: [https://github.com/A01801461/beneficio_joven_paneles](https://github.com/A01801461/beneficio_joven_paneles)
        
    -   Gestión de usuarios (jóvenes registrados).
      
    -   Alojado en: [https://beneficiojoven.site](https://beneficiojoven.site)
        
    -   Alta/edición de comercios.
        
    -   Creación y seguimiento de cupones.
        
    -   Reportes para el municipio.
        
3.  **Servidor Backend (API + lógica de negocio)**
    
    -   Código: [https://github.com/A01801461/beneficio_joven_backend](https://github.com/A01801461/beneficio_joven_backend)
        
    -   Alojado en: [https://bj-api.site](https://bj-api.site/)
        
    -   Autenticación de usuarios y roles.
        
    -   Ciclo de vida de cupones (creación, validación, redención).
        
    -   API REST para app móvil y panel.
        

----------

## 👥 Autores

-   **Astrid Guadalupe Navarro Rojas** — [A01769650@tec.mx](mailto:A01769650@tec.mx)
    
-   **Daniel Díaz Romero** — [A01801486@tec.mx](mailto:A01801486@tec.mx)
    
-   **David Alejandro Pérez Tabarés** — [A01800971@tec.mx](mailto:A01800971@tec.mx)
    
-   **Isaac Abud León** — [A01801461@tec.mx](mailto:A01801461@tec.mx)
    
-   **Juan Manuel Torres Rottonda** — [A01800476@tec.mx](mailto:A01800476@tec.mx)
    
-   **Luis Ángel Godínez González** — [A01752310@tec.mx](mailto:A01752310@tec.mx)
    


----------

## 🚀 Detalles de la App

La app está construida con **Kotlin** y **Jetpack Compose**, consumiendo el backend vía **Retrofit/OkHttp** con respuestas **JSON**. Usa **Coil** (con `coil-svg`) para imágenes y un almacenamiento **Room en memoria** para favoritos durante la sesión.

### **Características principales**

-   **Autenticación** (login/registro) con **JWT** emitido por la API.
    
-   **Exploración por comercio** y **lista de cupones** por negocio.
    
-   **Detalle de cupón** con **QR** y texto de código.
    
-   **Favoritos locales** (persistencia en memoria para esta versión).
    
-   **UI moderna** con **Jetpack Compose** y **Navigation Compose**.
    

### **Pantallas principales**

Pantalla

Descripción

**Login / Registro**

Autenticación y alta de usuarios.

**Negocios**

Catálogo agrupado por comercio.

**Cupones**

Listado por comercio, con filtros visuales.

**Detalle de cupón**

Logo, vigencia, **QR** y código.

**Perfil / Términos**

Preferencias básicas y legales.

----------

## 🛠️ Requisitos y configuración

-   **Android Studio** (Giraffe o superior recomendado).
    
-   **JDK 17** (según versión de Gradle/AGP).
    
-   **Dispositivo/Emulador** con Android 8.0+ (API 26+) recomendado.
    

### **Base URL del backend**

La base del backend **debe terminar con `/`**:

-   **Producción** (preconfigurada):  
    `https://bj-api.site/beneficioJoven/`
    
-   **Desarrollo local**:
    
    -   Emulador: `http://10.0.2.2:3000/beneficioJoven/`
        
    -   Dispositivo real (misma red): `http://<IP_PC>:3000/beneficioJoven/`
        

> Si usas **HTTP** local (no HTTPS), habilita tráfico claro:  
> `AndroidManifest.xml`
> 
> ```xml
> <application
>   android:usesCleartextTraffic="true"
>   android:networkSecurityConfig="@xml/network_security_config" ... />
> 
> ```
> 
> `res/xml/network_security_config.xml` → permite `10.0.2.2` o tu IP.

### **Dependencias clave**

-   Retrofit 2 + OkHttp + Gson
    
-   Coil + **coil-svg** (para **.svg**)
    
-   Room (en memoria) para favoritos
    
-   Coroutines + StateFlow
    
-   Navigation Compose
    

----------

## ▶️ Cómo ejecutar la app (local / producción)

1.  **Clona** este repositorio y **abre en Android Studio**.
    
2.  Verifica/ajusta la `BASE_URL` en el cliente Retrofit si vas a usar **local**.
    
3.  **Sincroniza Gradle**.
    
4.  Ejecuta en **emulador** o **dispositivo**.
    

> Para probar con el backend local, asegúrate de tener el servidor en `http://localhost:3000` y usa `10.0.2.2` en la app (emulador).  
> Para producción prueba con `https://bj-api.site/beneficioJoven/`.

----------

## 🧩 Estructura del código (Android)

```
mx.itesm.beneficiojoven/
├─ view/
│  ├─ MainActivity.kt
│  └─ ui/
│     ├─ nav/ (AppNavHost, rutas)
│     └─ screens/ (Login, Register, Businesses, CouponScreen, Terms, etc.)
├─ vm/ (ViewModels)
│  ├─ AuthViewModel.kt
│  ├─ CouponViewModels.kt
│  └─ FavoritesVM.kt
├─ model/
│  ├─ Models.kt (User, Role, Coupon, Merchant)
│  └─ data/
│     ├─ remote/ (RetrofitClient, BackendApi, DTOs, mappers)
│     └─ local/ (Room en memoria: AppDatabase, Dao, Repository)

```

----------

## 🔌 Integración con la API (repo externo)

La documentación detallada del **backend** (endpoints, contratos, ejemplos) vive en el repositorio del servidor:  
**API**: [https://github.com/A01801461/beneficio_joven_backend](https://github.com/A01801461/beneficio_joven_backend) — Alojado en **[https://bj-api.site](https://bj-api.site/)**


----------

## 🧪 Pruebas rápidas (con backend local)

-   **Cupones**
    
    ```bash
    curl http://localhost:3000/beneficioJoven/coupons
    
    ```
    
-   **Login**
    
    ```bash
    curl -i -X POST http://localhost:3000/beneficioJoven/auth/login \
      -H "Content-Type: application/json" \
      -d '{"email":"ana.perez+001@ejemplo.com","password":"BjTest#123"}'
    
    ```
    

----------

## 🔒 Seguridad y Privacidad

-   El **JWT** se almacena en memoria (sesión) y se añade a `Authorization: Bearer <token>` en cada petición.
    
-   No se guarda **PII** sensible en el dispositivo: los **favoritos** se guardan en **Room en memoria** para esta versión.
    
-   Para entornos productivos, usar siempre **HTTPS** y políticas de red estrictas (Network Security Config).
    
-   Esta versión puede tener medidas relajadas para facilitar pruebas integradas con el backend y panel.
    

----------

## 🧯 Solución de problemas

-   **HTTP 400 al iniciar sesión**  
    Enviar **JSON** (`Content-Type: application/json`) con campos **exactos**: `email`, `password`.  
    Evita `x-www-form-urlencoded` (los correos con `+` pueden romperse).
    
-   **HTTP 401**  
    Credenciales inválidas. Verifica usuario/contraseña.
    

-   **Backend local desde emulador**  
    Usa `http://10.0.2.2:3000/beneficioJoven/` y habilita tráfico claro si no es HTTPS.
    

----------

## 📄 Licencia

Este proyecto es propiedad del **Gobierno Municipal de Atizapán de Zaragoza**.  
Su uso está limitado a fines institucionales y no puede ser distribuido ni comercializado sin autorización expresa.
