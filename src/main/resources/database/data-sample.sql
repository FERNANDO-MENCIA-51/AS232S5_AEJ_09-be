-- =====================================================
-- Datos de ejemplo para testing
-- Backend de Integración: AI Detection & NASA APOD
-- =====================================================

-- =====================================================
-- Datos de ejemplo para ai_detections
-- =====================================================
INSERT INTO ai_detections (
    text_content, 
    lang, 
    is_ai_generated, 
    ai_probability, 
    confidence_score, 
    classification, 
    analysis_date, 
    status
) VALUES 
    (
        'This is a sample text written by a human for testing purposes. It contains personal thoughts and experiences.',
        'en',
        false,
        0.1500,
        0.8500,
        'HUMAN_WRITTEN',
        CURRENT_TIMESTAMP,
        'A'
    ),
    (
        'The quick brown fox jumps over the lazy dog in a computational manner, demonstrating algorithmic efficiency.',
        'en',
        true,
        0.9200,
        0.9200,
        'AI_GENERATED',
        CURRENT_TIMESTAMP,
        'A'
    ),
    (
        'Machine learning algorithms can process vast amounts of data efficiently through neural network architectures.',
        'en',
        true,
        0.8800,
        0.7800,
        'AI_GENERATED',
        CURRENT_TIMESTAMP,
        'A'
    ),
    (
        'I love spending time with my family on weekends, especially when we go to the park.',
        'en',
        false,
        0.2000,
        0.8800,
        'HUMAN_WRITTEN',
        CURRENT_TIMESTAMP,
        'A'
    ),
    (
        'Este texto fue escrito por una persona real con experiencias auténticas y emociones genuinas.',
        'es',
        false,
        0.1800,
        0.8200,
        'HUMAN_WRITTEN',
        CURRENT_TIMESTAMP,
        'A'
    ),
    (
        'La inteligencia artificial representa un avance tecnológico significativo en el procesamiento de información.',
        'es',
        true,
        0.8500,
        0.8500,
        'AI_GENERATED',
        CURRENT_TIMESTAMP,
        'A'
    ),
    (
        'This text contains both human-written content and AI-generated sections, making it difficult to classify.',
        'en',
        null,
        0.5000,
        0.6000,
        'MIXED_CONTENT',
        CURRENT_TIMESTAMP,
        'A'
    ),
    (
        'This record has been deleted for testing purposes.',
        'en',
        false,
        0.2500,
        0.7500,
        'HUMAN_WRITTEN',
        CURRENT_TIMESTAMP,
        'I'
    );

-- =====================================================
-- Datos de ejemplo para nasa_apod
-- =====================================================
INSERT INTO nasa_apod (
    requested_date,
    title,
    explanation,
    url,
    hdurl,
    media_type,
    apod_date,
    copyright,
    service_version,
    query_status,
    status
) VALUES 
    (
        '2024-01-15',
        'The Magnificent Spiral Galaxy M51',
        'A beautiful spiral galaxy captured by Hubble Space Telescope. Also known as the Whirlpool Galaxy, M51 is interacting with a smaller companion galaxy.',
        'https://apod.nasa.gov/apod/image/2401/m51_hubble_960.jpg',
        'https://apod.nasa.gov/apod/image/2401/m51_hubble_4096.jpg',
        'image',
        '2024-01-15',
        'NASA, ESA, Hubble Heritage Team',
        'v1',
        'SUCCESS',
        'A'
    ),
    (
        '2024-02-20',
        'Star Formation in the Eagle Nebula',
        'This nebula shows the birth of new stars in our galaxy. The famous "Pillars of Creation" are visible in this stunning image.',
        'https://apod.nasa.gov/apod/image/2402/eagle_nebula_960.jpg',
        'https://apod.nasa.gov/apod/image/2402/eagle_nebula_4096.jpg',
        'image',
        '2024-02-20',
        'NASA, ESA, STScI',
        'v1',
        'SUCCESS',
        'A'
    ),
    (
        '2024-03-10',
        'Perseverance Rover: Mars Landscape',
        'A panoramic view of the Martian surface from Perseverance rover exploring Jezero Crater.',
        'https://apod.nasa.gov/apod/image/2403/mars_perseverance_960.jpg',
        'https://apod.nasa.gov/apod/image/2403/mars_perseverance_4096.jpg',
        'image',
        '2024-03-10',
        'NASA/JPL-Caltech',
        'v1',
        'SUCCESS',
        'A'
    ),
    (
        '2024-04-05',
        'Solar Eclipse Time-lapse',
        'A stunning time-lapse video of the total solar eclipse captured from multiple locations.',
        'https://www.youtube.com/embed/eclipse2024',
        null,
        'video',
        '2024-04-05',
        'NASA Goddard',
        'v1',
        'SUCCESS',
        'A'
    ),
    (
        '2024-05-12',
        'The Andromeda Galaxy',
        'Our nearest large galactic neighbor, the Andromeda Galaxy (M31), in all its glory.',
        'https://apod.nasa.gov/apod/image/2405/andromeda_960.jpg',
        'https://apod.nasa.gov/apod/image/2405/andromeda_4096.jpg',
        'image',
        '2024-05-12',
        'Adam Block, Mt. Lemmon SkyCenter, U. Arizona',
        'v1',
        'SUCCESS',
        'A'
    ),
    (
        '2024-04-01',
        'Deleted APOD Record',
        'This is a deleted record for testing restore functionality.',
        'https://apod.nasa.gov/apod/image/sample_deleted.jpg',
        'https://apod.nasa.gov/apod/image/sample_deleted_hd.jpg',
        'image',
        '2024-04-01',
        'NASA',
        'v1',
        'SUCCESS',
        'I'
    );

