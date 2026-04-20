# LLM Kala

## Project Overview

Aplicación Quarkus-Java para responder mensajes sobre productos de Kala, brindar información de productos y servicios ofrecidos, así como información de contacto a los clientes.

## Funcionalidades

- **posts**: Registro de publicaciones autorizadas para generar respuestas automáticas por parte del agente.
- **comments**: Visualización de los comentarios realizados por los clientes y la respectiva respuesta, que puede ser generada tanto por el agente como por un usuario humano.
- **chat**: Interfaz para chatear con el agente de Kala, disponible para administradores y personal de soporte.

## Productos

Actualmente, la aplicación gestiona únicamente dos productos:
- CAMISAS
- BODIES

### posts

Información de post:
- **post_id**: ID del post generado por la red social.
- **product**: Producto asociado.
- **register_date**: Fecha y hora de registro.

Los posts se pueden agregar al sistema.

### comments

Información de los comentarios:
- **comment_id**: ID del comentario.
- **post_id**: ID del post relacionado.
- **comment.from.id**: ID del cliente que generó el comentario.
- **comment.message**: Mensaje del cliente.
- **parent_id**: ID del comentario padre (si aplica).
- **response.message**: Respuesta al comentario, que puede ser generada por el agente o por un usuario humano.
- **status**: Estado del mensaje.

Ejemplo:
```json
{
  "comment_id": "122099626484879870_1997339107843022",
  "post_id": "1012349128633841_122099626484879870",
  "comment": {
    "from": {
      "id": "26073109655722298",
      "name": "Daniel Berrocal"
    },
    "message": "Informacion sobre esto",
    "created_time": 1775070355
  },
  "response": {
    "message": null
  },
  "status": "PROCESSED"
}
```

### chat

- Endpoint para chatear con el agente Kala. Se envía el parámetro 'query' y se recibe 'response' con la respuesta. Este endpoint está dirigido a administradores y personal de soporte.

## Flujos

### Responder comentarios

- Se cargan los posts en estado 'ACTIVE' desde el repositorio.
- Se cargan los comentarios relacionados por **post_id** y en estado 'PENDING'.
- El mensaje del comentario se envía al agente y se espera la respuesta.
- La respuesta puede ser revisada y autorizada por un usuario antes de ser enviada.
- Una vez autorizada, la respuesta se guarda en **response.message** y el estado del comentario se actualiza a 'PROCESSED'.
