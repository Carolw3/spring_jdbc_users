-- Elimina la tabla si ja existeix
DROP TABLE IF EXISTS users;

-- Crear la tabla amb les especificacions indicades
CREATE TABLE users (  -- CREATE TABLE IF NOT EXISTS users(  --
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    descripcion TEXT NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(15) NOT NULL,
    image VARCHAR(500),
    ultimAcces TIMESTAMP NULL,
    dataCreated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    dataUpdated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);