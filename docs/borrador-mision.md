Este archivo es un borrador, omitir informacion.
# LLM Kala

## Project Overview

Aplicacion Quarkus-Java para la respuesta de mensajes sobre proudctos de Kala y brindar informacion de productos y servicios ofrecidos y información de contacto a los clientes.

## Functions

- **posts**: Registros de posts, los posts registrados son aquellos autorizados para generar respuesta automatica por parte del agente.
- **comments**: Visualizacion de los comentarios realizados por parte de los clientes y la respectiva respuesta por parte del usuario o el agente.
- **chat**: Interfaz para chatear con el agente de kala.

## Productos

- CAMISAS, BODIES

### posts

Informacion de post.

- **post_id**: id del post generado por la red social.
- **product**: Producto
- **register_date**: Fecha y hora de registro.

Los posts se pueden agregar.

### Comments

Informacion de los comments

- **comment_id**: id del comentario.
- **post_id**: id del post generado por la red social.
- **comment.from.id**: id del cliente que genero el post.
- **comment.message**: mensaje del cliente.
- **parent_id**: id del comentarios padre.
- **response.message**: Mensaje de los comentarios.
- **status**: Estatus del mensaje

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

### Chat

- Endpoit para chatear con el agente Kala, se envia parametro 'query' y se envia 'response' con la respuesta.

## Flujos

### Responder comentarios

- Carga de los post que estan en estado 'ACTIVE' desde el repositorio.
- Carga de los comentarios relacionados por el **post_id** y en estado 'PENDING'.
- Envia el valor de 'message' del comentario al agente y se espera la respuesta.
- Guardar la respuesta del agente en la estructura del comentario en **response.message** y se modifica el estado del comentario a 'PROCESSED'.
