
🌟 EmotiApp - Backend
EmotiApp es una API REST desarrollada con Kotlin y Spring Boot para el seguimiento del bienestar emocional. 
Permite a los usuarios registrar sus estados de ánimo y recibir recomendaciones personalizadas basadas en el catálogo de emociones.

🛠️ Tecnologías y Requisitos
Lenguaje: Kotlin

Framework: Spring Boot

Base de Datos: PostgreSQL (Dockerizado)

Pruebas: JUnit 5 & Mockito

Herramientas requeridas: Docker, JDK 17+, Postman.

🚀 Guía de Instalación y Ejecución
1. Levantar la Base de Datos con Docker
   El proyecto incluye un archivo docker-compose.yml para gestionar la persistencia. 
2. Ejecuta el siguiente comando en la raíz del proyecto:

    docker-compose up -d

2. Ejecutar la Aplicación
   Una vez que el contenedor de la base de datos esté activo, 
   inicia el servidor mediante Gradle:

   ./gradlew bootRun o dandole en el boton de correr en la parte de arriba.

La aplicación estará disponible en http://localhost:8080.

🧪 Pruebas y Cobertura (Coverage)
Ejecutar los Tests
Para validar la integridad del código, ejecuta:

./gradlew test o dando click derecho sobre los tests y buscar la opcion"Run tests with Coverage".

Los porcentajes aparecerán en la ventana lateral de Coverage del IDE.

📬 Uso de la Colección de Endpoints (Postman)
En la raíz del repositorio se encuentra el archivo: EmotiApp.postman_collection.json.

Pasos para probar la API:
Importar: Abre Postman, haz clic en Import y selecciona el archivo .json del proyecto.

Configuración: Asegúrate de tener la app corriendo en el puerto 8080.

Flujo Principal:

POST /users/register: Registra un nuevo usuario usando RegisterRequest.

GET /emotions: Obtiene el catálogo de emociones.

POST /emotion-selection/user/{id}: Registra el estado de ánimo y recibe una recomendación vinculada.

📂 Estructura Destacada
Mappers: Implementación del patrón Mapper para separar entidades de respuestas (UsersMapper, RecommendationMapper).

Models: Gestión estricta de RegisterRequest y UsersResponse para seguridad de datos.

Docker: Configuración lista para despliegue inmediato en entornos locales.
