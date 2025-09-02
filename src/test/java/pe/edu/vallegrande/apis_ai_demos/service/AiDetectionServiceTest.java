package pe.edu.vallegrande.apis_ai_demos.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import pe.edu.vallegrande.apis_ai_demos.dto.AiDetectionResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class AiDetectionServiceTest {

    @Autowired
    private AiDetectionService aiDetectionService;

    @Test
    public void testDetectAi_HumanText() {
        String humanText = "Sports and games are recommended for everyone. These serve as an exercise that must form a part of our daily routine if we want to stay fit and active. The key to staying fit is following a healthy lifestyle that includes a healthy diet and exercise.";
        
        Mono<AiDetectionResponse> result = aiDetectionService.detectAi(humanText);
        
        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    System.out.println("Response: " + response);
                    return response != null && response.getText() != null;
                })
                .verifyComplete();
    }

    @Test
    public void testDetectAi_PotentialAiText() {
        String potentialAiText = "Artificial intelligence (AI) is a rapidly evolving field that encompasses machine learning, natural language processing, and computer vision. AI systems can process vast amounts of data and identify patterns that humans might miss. These technologies are being implemented across various industries to improve efficiency and decision-making processes.";
        
        Mono<AiDetectionResponse> result = aiDetectionService.detectAi(potentialAiText);
        
        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    System.out.println("Response: " + response);
                    return response != null && response.getText() != null;
                })
                .verifyComplete();
    }
}
