package com.epam.training.gen.ai.chat.model;

import io.qdrant.client.grpc.JsonWithInt;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Request DTO for sending point data to Qdrant.
 * Contains:
 * - UUID identifier
 * - Vector values (list of floats)
 * - Optional payload (metadata as key-value)
 */
@Setter
@Getter
@Builder
public class QdrantPointRequest {

    /**
     * Unique identifier for the vector.
     */
    private UUID id;

    /**
     * The vector representation (embedding).
     */
    private List<Float> vector;

    /**
     * Metadata or contextual payload associated with the vector.
     */
    private Map<String, JsonWithInt.Value> payload;
}
