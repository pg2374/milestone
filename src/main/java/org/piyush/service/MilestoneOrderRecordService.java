package org.piyush.service;

import org.piyush.models.MilestoneOrderRecord;
import org.piyush.repository.MilestoneOrderRepository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.List;

public class MilestoneOrderRecordService {

    private final MilestoneOrderRepository repository;

    public MilestoneOrderRecordService(MilestoneOrderRepository repository) {
        this.repository = repository;
    }

    public List<MilestoneOrderRecord> findByPartitionId(String indexField, String value) {
        return repository.queryByIndex(indexField, value);
    }

    // Factory method to wire dependencies
    public static MilestoneOrderRecordService defaultInstance() {
        DynamoDbClient dynamoDbClient = DynamoDbClient.create();
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();

        DynamoDbTable<MilestoneOrderRecord> table = enhancedClient.table(
                "oo_milestone_dynamo_db",
                TableSchema.fromBean(MilestoneOrderRecord.class)
        );

        return new MilestoneOrderRecordService(new MilestoneOrderRepository(table));
    }
}