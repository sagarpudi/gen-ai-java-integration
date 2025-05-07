package com.epam.training.gen.ai.chat.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Represents a response object containing information about a vector retrieved from the Qdrant vector database.
 * <p>
 * This includes the vector's unique identifier, associated metadata (payload), similarity score,
 * and the version of the vector.
 * </p>
 */
@Setter
@Getter
@Builder
public class QdrantPointResponse {
    /**
     * Unique identifier for the vector.
     */
    private String id;

    /**
     * Metadata or contextual information stored along with the vector.
     * This can include any additional attributes used to describe or filter the vector.
     */
    private Map<String, Object> payload;

    /**
     * Similarity score between the query embedding and this vector.
     * Higher scores indicate greater similarity.
     */
    private Float score;

    /**
     * Version of the vector record in Qdrant. Can be used for optimistic locking or tracking updates.
     */
    private long version;
}
