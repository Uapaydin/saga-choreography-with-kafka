package com.utku.saga.model;

import lombok.*;

/**
 * @author tcuapaydin
 * @created 13/06/2022 - 14:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class RemoteKafkaCallPackage extends RemoteCallPackage {
    private Object kafkaMessage;
}
