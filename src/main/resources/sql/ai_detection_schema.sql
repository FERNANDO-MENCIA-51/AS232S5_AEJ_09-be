-- Tabla para guardar consultas de detección de IA
-- Optimizada para manejar respuestas de RapidAPI AI Detection
CREATE TABLE IF NOT EXISTS ai_detection_queries (
    id SERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    lang VARCHAR(10) DEFAULT 'en',
    ai_probability DECIMAL(5,4) CHECK (ai_probability >= 0 AND ai_probability <= 1),
    classification VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para mejorar rendimiento en consultas frecuentes
CREATE INDEX IF NOT EXISTS idx_ai_detection_lang ON ai_detection_queries(lang);
CREATE INDEX IF NOT EXISTS idx_ai_detection_classification ON ai_detection_queries(classification);
CREATE INDEX IF NOT EXISTS idx_ai_detection_created_at ON ai_detection_queries(created_at);
CREATE INDEX IF NOT EXISTS idx_ai_detection_probability ON ai_detection_queries(ai_probability);

-- Comentarios para documentar la tabla
COMMENT ON TABLE ai_detection_queries IS 'Almacena consultas realizadas a la API de detección de IA';
COMMENT ON COLUMN ai_detection_queries.text IS 'Texto analizado por la API de detección de IA';
COMMENT ON COLUMN ai_detection_queries.lang IS 'Idioma del texto (es, en, etc.)';
COMMENT ON COLUMN ai_detection_queries.ai_probability IS 'Probabilidad de que el texto sea generado por IA (0.0 a 1.0)';
COMMENT ON COLUMN ai_detection_queries.classification IS 'Clasificación: AI_GENERATED, HUMAN_WRITTEN, MIXED_CONTENT, etc.';
COMMENT ON COLUMN ai_detection_queries.created_at IS 'Fecha y hora de creación del registro';
