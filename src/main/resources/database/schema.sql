
-- =====================================================
-- TABLA: ai_detections
-- Almacena los resultados de análisis de detección de IA usando RapidAPI
-- =====================================================
CREATE TABLE IF NOT EXISTS ai_detections (
    id BIGSERIAL PRIMARY KEY,
    text_content TEXT NOT NULL,
    lang VARCHAR(10) DEFAULT 'en' NOT NULL,
    is_ai_generated BOOLEAN,
    ai_probability DECIMAL(5,4) CHECK (ai_probability >= 0 AND ai_probability <= 1),
    confidence_score DECIMAL(5,4) CHECK (confidence_score >= 0 AND confidence_score <= 1),
    classification VARCHAR(50) NOT NULL,
    analysis_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(1) DEFAULT 'A' NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT chk_status_ai CHECK (status IN ('A', 'I')),
    CONSTRAINT chk_classification_ai CHECK (classification IN ('AI_GENERATED', 'HUMAN_WRITTEN', 'MIXED_CONTENT', 'UNCERTAIN'))
);

-- Índices optimizados para ai_detections
CREATE INDEX IF NOT EXISTS idx_ai_detections_status ON ai_detections(status) WHERE status = 'A';
CREATE INDEX IF NOT EXISTS idx_ai_detections_lang ON ai_detections(lang);
CREATE INDEX IF NOT EXISTS idx_ai_detections_classification ON ai_detections(classification);
CREATE INDEX IF NOT EXISTS idx_ai_detections_analysis_date ON ai_detections(analysis_date DESC);
CREATE INDEX IF NOT EXISTS idx_ai_detections_created_at ON ai_detections(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_ai_detections_probability ON ai_detections(ai_probability DESC);
CREATE INDEX IF NOT EXISTS idx_ai_detections_is_ai ON ai_detections(is_ai_generated) WHERE status = 'A';

-- Índice compuesto para consultas frecuentes
CREATE INDEX IF NOT EXISTS idx_ai_detections_status_date ON ai_detections(status, analysis_date DESC);
CREATE INDEX IF NOT EXISTS idx_ai_detections_lang_classification ON ai_detections(lang, classification);

-- Comentarios para documentación de ai_detections
COMMENT ON TABLE ai_detections IS 'Almacena resultados de análisis de detección de contenido generado por IA mediante RapidAPI';
COMMENT ON COLUMN ai_detections.id IS 'Identificador único del análisis';
COMMENT ON COLUMN ai_detections.text_content IS 'Texto analizado por la API de detección de IA';
COMMENT ON COLUMN ai_detections.lang IS 'Código de idioma del texto (ISO 639-1): es, en, fr, etc.';
COMMENT ON COLUMN ai_detections.is_ai_generated IS 'Indica si el texto fue generado por IA (true/false)';
COMMENT ON COLUMN ai_detections.ai_probability IS 'Probabilidad de que el texto sea generado por IA (0.0000 a 1.0000)';
COMMENT ON COLUMN ai_detections.confidence_score IS 'Nivel de confianza del análisis (0.0000 a 1.0000)';
COMMENT ON COLUMN ai_detections.classification IS 'Clasificación del contenido: AI_GENERATED, HUMAN_WRITTEN, MIXED_CONTENT, UNCERTAIN';
COMMENT ON COLUMN ai_detections.analysis_date IS 'Fecha y hora en que se realizó el análisis';
COMMENT ON COLUMN ai_detections.status IS 'Estado del registro: A=Activo, I=Inactivo (eliminado lógicamente)';
COMMENT ON COLUMN ai_detections.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN ai_detections.updated_at IS 'Fecha y hora de última actualización del registro';

-- =====================================================
-- TABLA: nasa_apod
-- Almacena información de las imágenes astronómicas del día de NASA
-- =====================================================
CREATE TABLE IF NOT EXISTS nasa_apod (
    id BIGSERIAL PRIMARY KEY,
    requested_date VARCHAR(10) NOT NULL,
    title TEXT NOT NULL,
    explanation TEXT,
    url TEXT NOT NULL,
    hdurl TEXT,
    media_type VARCHAR(50) NOT NULL DEFAULT 'image',
    apod_date DATE NOT NULL,
    copyright VARCHAR(500),
    service_version VARCHAR(10),
    query_status VARCHAR(50) DEFAULT 'SUCCESS' NOT NULL,
    status VARCHAR(1) DEFAULT 'A' NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT chk_status_nasa CHECK (status IN ('A', 'I')),
    CONSTRAINT chk_media_type_nasa CHECK (media_type IN ('image', 'video')),
    CONSTRAINT chk_query_status_nasa CHECK (query_status IN ('SUCCESS', 'ERROR', 'PENDING', 'TIMEOUT'))
);

-- Índices optimizados para nasa_apod
CREATE INDEX IF NOT EXISTS idx_nasa_apod_status ON nasa_apod(status) WHERE status = 'A';
CREATE INDEX IF NOT EXISTS idx_nasa_apod_date ON nasa_apod(apod_date DESC);
CREATE INDEX IF NOT EXISTS idx_nasa_apod_requested_date ON nasa_apod(requested_date);
CREATE INDEX IF NOT EXISTS idx_nasa_apod_media_type ON nasa_apod(media_type);
CREATE INDEX IF NOT EXISTS idx_nasa_apod_query_status ON nasa_apod(query_status);
CREATE INDEX IF NOT EXISTS idx_nasa_apod_created_at ON nasa_apod(created_at DESC);

-- Índice único para evitar duplicados de fecha activa
CREATE UNIQUE INDEX IF NOT EXISTS idx_nasa_apod_date_unique ON nasa_apod(apod_date) WHERE status = 'A';

-- Índice compuesto para consultas frecuentes
CREATE INDEX IF NOT EXISTS idx_nasa_apod_status_date ON nasa_apod(status, apod_date DESC);
CREATE INDEX IF NOT EXISTS idx_nasa_apod_media_status ON nasa_apod(media_type, status);

-- Comentarios para documentación de nasa_apod
COMMENT ON TABLE nasa_apod IS 'Almacena información de las imágenes astronómicas del día (Astronomy Picture of the Day) de NASA';
COMMENT ON COLUMN nasa_apod.id IS 'Identificador único del registro APOD';
COMMENT ON COLUMN nasa_apod.requested_date IS 'Fecha solicitada en formato YYYY-MM-DD';
COMMENT ON COLUMN nasa_apod.title IS 'Título de la imagen o video astronómico';
COMMENT ON COLUMN nasa_apod.explanation IS 'Explicación detallada del contenido astronómico';
COMMENT ON COLUMN nasa_apod.url IS 'URL del contenido en resolución estándar';
COMMENT ON COLUMN nasa_apod.hdurl IS 'URL de la imagen en alta definición (HD) - solo para imágenes';
COMMENT ON COLUMN nasa_apod.media_type IS 'Tipo de medio: image o video';
COMMENT ON COLUMN nasa_apod.apod_date IS 'Fecha del contenido astronómico (fecha real de la imagen/video)';
COMMENT ON COLUMN nasa_apod.copyright IS 'Información de derechos de autor y créditos';
COMMENT ON COLUMN nasa_apod.service_version IS 'Versión del servicio NASA APOD API utilizada';
COMMENT ON COLUMN nasa_apod.query_status IS 'Estado de la consulta: SUCCESS, ERROR, PENDING, TIMEOUT';
COMMENT ON COLUMN nasa_apod.status IS 'Estado del registro: A=Activo, I=Inactivo (eliminado lógicamente)';
COMMENT ON COLUMN nasa_apod.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN nasa_apod.updated_at IS 'Fecha y hora de última actualización del registro';

-- =====================================================
-- TRIGGERS para actualización automática de updated_at
-- =====================================================

-- Función genérica para actualizar updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger para ai_detections
DROP TRIGGER IF EXISTS trigger_ai_detections_updated_at ON ai_detections;
CREATE TRIGGER trigger_ai_detections_updated_at
    BEFORE UPDATE ON ai_detections
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Trigger para nasa_apod
DROP TRIGGER IF EXISTS trigger_nasa_apod_updated_at ON nasa_apod;
CREATE TRIGGER trigger_nasa_apod_updated_at
    BEFORE UPDATE ON nasa_apod
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- =====================================================
-- VISTAS útiles para consultas frecuentes
-- =====================================================

-- Vista de detecciones de IA activas
CREATE OR REPLACE VIEW v_ai_detections_active AS
SELECT 
    id,
    text_content,
    lang,
    is_ai_generated,
    ai_probability,
    confidence_score,
    classification,
    analysis_date,
    created_at
FROM ai_detections
WHERE status = 'A'
ORDER BY analysis_date DESC;

COMMENT ON VIEW v_ai_detections_active IS 'Vista de todas las detecciones de IA activas ordenadas por fecha de análisis';

-- Vista de APOD activos
CREATE OR REPLACE VIEW v_nasa_apod_active AS
SELECT 
    id,
    requested_date,
    title,
    explanation,
    url,
    hdurl,
    media_type,
    apod_date,
    copyright,
    query_status,
    created_at
FROM nasa_apod
WHERE status = 'A'
ORDER BY apod_date DESC;

COMMENT ON VIEW v_nasa_apod_active IS 'Vista de todos los registros APOD activos ordenados por fecha';

-- Vista de estadísticas de detección de IA
CREATE OR REPLACE VIEW v_ai_detection_stats AS
SELECT 
    lang,
    classification,
    COUNT(*) as total_detections,
    AVG(ai_probability) as avg_probability,
    AVG(confidence_score) as avg_confidence,
    COUNT(CASE WHEN is_ai_generated = true THEN 1 END) as ai_generated_count,
    COUNT(CASE WHEN is_ai_generated = false THEN 1 END) as human_written_count
FROM ai_detections
WHERE status = 'A'
GROUP BY lang, classification;

COMMENT ON VIEW v_ai_detection_stats IS 'Estadísticas agregadas de detecciones de IA por idioma y clasificación';


