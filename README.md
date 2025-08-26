# TicketPOS - Sistema de Punto de Venta

## Descripción

TicketPOS es una aplicación Android completa para sistemas de punto de venta (POS) diseñada para pequeñas y medianas empresas. La aplicación incluye todas las funcionalidades esenciales para gestionar ventas, inventario, reportes y configuración del negocio.

## Características Principales

### 🛒 Gestión de Ventas
- **Ventas rápidas** con búsqueda de productos por nombre, SKU o código de barras
- **Carrito de compras** con gestión de cantidades y descuentos
- **Múltiples métodos de pago** (efectivo, tarjeta, transferencia)
- **Cálculo automático** de impuestos y totales
- **Recibos personalizables** con logo y información de la empresa
- **Ventas en espera** y anulación de transacciones

### 📦 Control de Inventario
- **Gestión completa de productos** con SKU, códigos de barras y categorías
- **Control de stock** con alertas de inventario bajo
- **Categorización** y etiquetado de productos
- **Importación/exportación** de inventario (CSV, Excel)
- **Ajustes de stock** con historial de cambios
- **Gestión de proveedores** y costos

### 📊 Reportes y Analytics
- **Reportes de ventas** por período, usuario y método de pago
- **Análisis de inventario** con productos más vendidos
- **Reportes de clientes** y comportamiento de compra
- **Métricas financieras** y análisis de rentabilidad
- **Exportación** en múltiples formatos (PDF, Excel, CSV)
- **Reportes programados** y envío automático por email

### 👥 Gestión de Usuarios
- **Sistema de autenticación** con roles y permisos
- **Múltiples usuarios** (admin, manager, cashier)
- **Control de acceso** y auditoría de acciones
- **Bloqueo automático** por intentos fallidos de login

### ⚙️ Configuración Avanzada
- **Personalización de empresa** (nombre, logo, impuestos)
- **Configuración de impresoras** térmicas y de red
- **Métodos de pago** configurables
- **Respaldo automático** de base de datos
- **Sincronización** con sistemas externos
- **Multiidioma** (Español, Inglés)

## Arquitectura Técnica

### 🏗️ Patrón de Diseño
- **MVVM (Model-View-ViewModel)** con LiveData
- **Repository Pattern** para acceso a datos
- **Dependency Injection** con Hilt
- **Navigation Component** para navegación entre pantallas

### 🗄️ Base de Datos
- **Room Database** con entidades optimizadas
- **Relaciones** entre productos, ventas, clientes y usuarios
- **Migrations** automáticas de esquema
- **Backup/restore** de datos

### 🔌 Tecnologías Utilizadas
- **Kotlin** como lenguaje principal
- **Android Jetpack** (Lifecycle, Navigation, Room)
- **Material Design 3** para la interfaz de usuario
- **Coroutines** para operaciones asíncronas
- **ViewBinding** para binding de vistas
- **Retrofit** para APIs (futuro)
- **Glide** para carga de imágenes

### 📱 Compatibilidad
- **Android API 24+** (Android 7.0+)
- **Orientación** portrait y landscape
- **Tablets** y teléfonos optimizados
- **Modo offline** completo

## Estructura del Proyecto

```
app/
├── src/main/
│   ├── java/com/ticketpos/app/
│   │   ├── data/                    # Capa de datos
│   │   │   ├── dao/                # Data Access Objects
│   │   │   ├── database/           # Base de datos Room
│   │   │   ├── entity/             # Entidades de datos
│   │   │   └── repository/         # Repositorios
│   │   ├── ui/                     # Capa de presentación
│   │   │   ├── auth/               # Autenticación
│   │   │   ├── inventory/          # Gestión de inventario
│   │   │   ├── main/               # Actividad principal
│   │   │   ├── reports/            # Reportes y analytics
│   │   │   ├── sale/               # Gestión de ventas
│   │   │   ├── settings/           # Configuración
│   │   │   └── splash/             # Pantalla de inicio
│   │   ├── service/                # Servicios en background
│   │   └── util/                   # Utilidades y helpers
│   ├── res/                        # Recursos de la aplicación
│   │   ├── drawable/               # Imágenes y drawables
│   │   ├── layout/                 # Layouts XML
│   │   ├── menu/                   # Menús
│   │   ├── navigation/             # Navegación
│   │   ├── values/                 # Strings, colores, estilos
│   │   └── xml/                    # Preferencias y otros XML
│   └── AndroidManifest.xml         # Manifesto de la aplicación
├── build.gradle                    # Configuración del módulo app
└── proguard-rules.pro             # Reglas de ofuscación
```

## Entidades de Datos

### 📋 Producto
- ID, nombre, descripción, SKU, código de barras
- Categoría, marca, precio, costo, stock
- Unidad de medida, peso, dimensiones
- Impuestos, estado activo/inactivo

### 🛍️ Venta
- Número de venta, fecha, cliente
- Usuario (cajero), método de pago
- Subtotal, impuestos, descuentos, total
- Estado de pago, recibos impresos

### 👤 Usuario
- Credenciales, rol, permisos
- Información personal y de contacto
- Estado activo/bloqueado, intentos de login

### 🏪 Cliente
- Código, información personal
- Historial de compras, límite de crédito
- Tipo de cliente (regular, VIP, mayorista)

## Funcionalidades Destacadas

### 🔍 Búsqueda Inteligente
- Búsqueda por nombre, SKU o código de barras
- Escáner de códigos de barras integrado
- Sugerencias y autocompletado
- Historial de búsquedas recientes

### 💳 Procesamiento de Pagos
- Múltiples métodos de pago
- Cálculo automático de cambio
- Integración con terminales de tarjeta
- Comprobantes de pago

### 📈 Reportes en Tiempo Real
- Dashboard con métricas clave
- Gráficos y visualizaciones
- Filtros por fecha, categoría, usuario
- Exportación y compartir

### 🔒 Seguridad
- Autenticación robusta
- Encriptación de datos sensibles
- Auditoría de transacciones
- Respaldo automático

## Instalación y Configuración

### 📋 Requisitos Previos
- Android Studio Arctic Fox o superior
- Android SDK API 24+
- Gradle 7.0+
- JDK 11+

### 🚀 Pasos de Instalación

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/israelicky/ticketpos.git
   cd ticketpos
   ```

2. **Abrir en Android Studio**
   - Abrir Android Studio
   - Seleccionar "Open an existing project"
   - Navegar a la carpeta del proyecto

3. **Sincronizar dependencias**
   - Esperar a que Gradle sincronice
   - Resolver cualquier dependencia faltante

4. **Configurar dispositivo/emulador**
   - Conectar dispositivo Android o crear emulador
   - Habilitar depuración USB en el dispositivo

5. **Ejecutar la aplicación**
   - Presionar F5 o botón "Run"
   - Seleccionar dispositivo objetivo

### ⚙️ Configuración Inicial

1. **Primer inicio**
   - La aplicación se ejecutará en modo demo
   - Usuario: `admin`, Contraseña: `admin123`

2. **Configuración de empresa**
   - Ir a Configuración > Empresa
   - Ingresar nombre, impuestos, moneda

3. **Configuración de impresora**
   - Ir a Configuración > Impresora
   - Seleccionar tipo y configurar conexión

4. **Importar productos**
   - Ir a Inventario > Importar
   - Seleccionar archivo CSV o Excel

## Uso de la Aplicación

### 🛒 Realizar una Venta

1. **Iniciar nueva venta**
   - Tocar botón "Nueva Venta" en pantalla principal
   - O usar atajo de teclado

2. **Agregar productos**
   - Buscar por nombre, SKU o escanear código de barras
   - Seleccionar cantidad y agregar al carrito
   - Repetir para todos los productos

3. **Aplicar descuentos**
   - Tocar botón "Descuento" en carrito
   - Ingresar porcentaje o monto fijo

4. **Procesar pago**
   - Revisar total y tocar "Pago"
   - Seleccionar método de pago
   - Confirmar transacción

5. **Imprimir recibo**
   - Recibo se imprime automáticamente
   - Opción de enviar por email o compartir

### 📦 Gestionar Inventario

1. **Agregar producto**
   - Ir a Inventario > Agregar Producto
   - Completar información requerida
   - Guardar y agregar stock inicial

2. **Editar producto**
   - Buscar producto en lista
   - Tocar en producto para editar
   - Modificar información y guardar

3. **Ajustar stock**
   - Seleccionar producto
   - Tocar "Ajustar Stock"
   - Ingresar cantidad y razón

4. **Importar productos**
   - Preparar archivo CSV/Excel
   - Ir a Inventario > Importar
   - Seleccionar archivo y mapear campos

### 📊 Generar Reportes

1. **Seleccionar tipo de reporte**
   - Ventas, inventario, clientes, financiero

2. **Definir período**
   - Fecha de inicio y fin
   - Aplicar filtros adicionales

3. **Generar reporte**
   - Tocar "Generar Reporte"
   - Esperar procesamiento

4. **Exportar/compartir**
   - PDF, Excel, CSV
   - Email, impresión, compartir

## Personalización

### 🎨 Temas y Colores
- Colores personalizables en `colors.xml`
- Temas Material Design 3
- Modo oscuro/claro automático

### 📝 Textos y Mensajes
- Todos los textos en `strings.xml`
- Soporte multiidioma
- Mensajes personalizables

### 🖨️ Recibos
- Encabezado y pie personalizables
- Logo de empresa
- Información de contacto
- Términos y condiciones

## Mantenimiento

### 💾 Respaldo de Datos
- Respaldo automático diario
- Respaldo manual en cualquier momento
- Restauración desde archivo de respaldo
- Sincronización con servicios en la nube

### 🔄 Actualizaciones
- Verificación automática de actualizaciones
- Notificaciones de nuevas versiones
- Proceso de actualización guiado
- Rollback en caso de problemas

### 🐛 Solución de Problemas
- Logs detallados del sistema
- Modo debug para desarrolladores
- Reportes de errores automáticos
- Base de conocimientos integrada

## Contribución

### 🤝 Cómo Contribuir

1. **Fork del proyecto**
2. **Crear rama de feature**
   ```bash
   git checkout -b feature/nueva-funcionalidad
   ```
3. **Realizar cambios**
4. **Commit con mensaje descriptivo**
   ```bash
   git commit -m "Agregar nueva funcionalidad de reportes"
   ```
5. **Push a la rama**
   ```bash
   git push origin feature/nueva-funcionalidad
   ```
6. **Crear Pull Request**

### 📝 Estándares de Código

- **Kotlin** como lenguaje principal
- **Kotlin Coding Conventions** oficiales
- **Material Design** para UI/UX
- **Clean Architecture** principles
- **Unit tests** para lógica de negocio
- **UI tests** para flujos críticos

## Licencia

Este proyecto está bajo la licencia **MIT**. Ver el archivo `LICENSE` para más detalles.

## Soporte

### 📧 Contacto
- **Email**: soporte@ticketpos.com
- **Documentación**: https://docs.ticketpos.com
- **Comunidad**: https://community.ticketpos.com

### 🆘 Problemas Comunes

1. **La aplicación no inicia**
   - Verificar permisos de Android
   - Limpiar caché de la aplicación
   - Reinstalar la aplicación

2. **Error de base de datos**
   - Verificar espacio en dispositivo
   - Restaurar desde respaldo
   - Contactar soporte técnico

3. **Problemas de impresión**
   - Verificar conexión de impresora
   - Revisar configuración de impresora
   - Probar con impresora de prueba

## Roadmap

### 🚀 Próximas Versiones

#### v1.1 (Q2 2024)
- [ ] Integración con APIs de proveedores
- [ ] Sistema de cupones y promociones
- [ ] Gestión de empleados y comisiones
- [ ] Reportes avanzados con gráficos

#### v1.2 (Q3 2024)
- [ ] Aplicación web complementaria
- [ ] Sincronización multi-dispositivo
- [ ] Integración con contabilidad
- [ ] Sistema de lealtad de clientes

#### v2.0 (Q4 2024)
- [ ] Inteligencia artificial para predicciones
- [ ] Análisis de comportamiento de clientes
- [ ] Integración con e-commerce
- [ ] API pública para desarrolladores

### 💡 Ideas Futuras
- **Machine Learning** para predicción de demanda
- **Blockchain** para transacciones seguras
- **IoT** para sensores de inventario
- **Realidad Aumentada** para picking de productos

## Agradecimientos

- **Google** por Android y Material Design
- **JetBrains** por Kotlin
- **Comunidad Android** por librerías y herramientas
- **Usuarios beta** por feedback y sugerencias

---

**TicketPOS** - Transformando el comercio minorista, una transacción a la vez.

*Desarrollado con ❤️ para pequeñas y medianas empresas*
