package org.piyush.repository;

import org.piyush.models.MilestoneOrderRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MilestoneOrderRepository {
    private static final Logger logger = LoggerFactory.getLogger(MilestoneOrderRepository.class);
    private final DynamoDbTable<MilestoneOrderRecord> table;

    public MilestoneOrderRepository(DynamoDbTable<MilestoneOrderRecord> table) {
        this.table = table;
    }

    private static final Map<String, String> FIELD_TO_INDEX_MAP = Map.ofEntries(
            Map.entry("accountNumber", "accountNumberIndex"),
            Map.entry("productOrderId", "productOrderIdIndex")
//            Map.entry("productOrderType", "productOrderTypeIndex")
//            Map.entry("channel", "channelIndex"),
//            Map.entry("errorCode", "errorCodeIndex"),
//            Map.entry("logId", "logIdIndex"),
//            Map.entry("milestone", "milestoneIndex"),
//            Map.entry("orderItemId", "orderItemIdIndex"),
//            Map.entry("productType", "productTypeIndex"),
//            Map.entry("statusDetail", "statusDetailIndex"),
//            Map.entry("status", "statusIndex"),
//            Map.entry("subscriptionId", "subscriptionIndex"),
//            Map.entry("timeStamp", "timeStampIndex")
    );

    public MilestoneOrderRecord queryById(String milestoneId) {
        try {
            Iterator<MilestoneOrderRecord> results = table.query(r -> r
                    .queryConditional(QueryConditional.keyEqualTo(k -> k
                            .partitionValue(milestoneId)))
            ).items().iterator();

            if (results.hasNext()) {
                MilestoneOrderRecord task = results.next();
                logger.info("Record retrieved successfully with ID: {}", milestoneId);
                return task;
            } else {
                throw new RuntimeException(String.format("Record not found with id [%s]", milestoneId));
            }
        } catch (DynamoDbException e) {
            throw new RuntimeException(String.format("Failed to retrieve Record with ID [%s]", milestoneId), e);
        }
    }

    public List<MilestoneOrderRecord> queryByIndex(String fieldName, String value) {
        String indexName = FIELD_TO_INDEX_MAP.get(fieldName);
        if (indexName == null) {
            throw new IllegalArgumentException("No GSI defined for field: " + fieldName);
        }

        QueryConditional condition = QueryConditional.keyEqualTo(Key.builder().partitionValue(value).build());
        DynamoDbIndex<MilestoneOrderRecord> index = table.index(indexName);

        return index.query(r -> r.queryConditional(condition))
                .stream()
                .flatMap(p -> p.items().stream())
                .toList();
    }
}
