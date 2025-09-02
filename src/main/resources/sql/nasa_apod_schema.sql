-- Crear tabla para NASA APOD queries
CREATE TABLE IF NOT EXISTS nasa_apod_queries (
    id BIGSERIAL PRIMARY KEY,
    requested_date VARCHAR(10),
    title TEXT,
    explanation TEXT,
    image_url TEXT,
    hd_image_url TEXT,
    media_type VARCHAR(50),
    copyright TEXT,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_nasa_apod_requested_date ON nasa_apod_queries(requested_date);
CREATE INDEX IF NOT EXISTS idx_nasa_apod_status ON nasa_apod_queries(status);
CREATE INDEX IF NOT EXISTS idx_nasa_apod_created_at ON nasa_apod_queries(created_at);
CREATE INDEX IF NOT EXISTS idx_nasa_apod_media_type ON nasa_apod_queries(media_type);

-- Comentarios para documentar la tabla
COMMENT ON TABLE nasa_apod_queries IS 'Tabla para almacenar consultas y resultados del NASA APOD API';
COMMENT ON COLUMN nasa_apod_queries.id IS 'Identificador único de la consulta';
COMMENT ON COLUMN nasa_apod_queries.requested_date IS 'Fecha solicitada en formato YYYY-MM-DD';
COMMENT ON COLUMN nasa_apod_queries.title IS 'Título de la imagen astronómica';
COMMENT ON COLUMN nasa_apod_queries.explanation IS 'Explicación de la imagen';
COMMENT ON COLUMN nasa_apod_queries.image_url IS 'URL de la imagen en resolución estándar';
COMMENT ON COLUMN nasa_apod_queries.hd_image_url IS 'URL de la imagen en alta definición';
COMMENT ON COLUMN nasa_apod_queries.media_type IS 'Tipo de media (image, video)';
COMMENT ON COLUMN nasa_apod_queries.copyright IS 'Información de copyright';
COMMENT ON COLUMN nasa_apod_queries.status IS 'Estado de la consulta (SUCCESS, ERROR, etc.)';
COMMENT ON COLUMN nasa_apod_queries.created_at IS 'Fecha y hora de creación del registro';