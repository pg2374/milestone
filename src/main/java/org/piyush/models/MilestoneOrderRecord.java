package org.piyush.models;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

@DynamoDbBean
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class MilestoneOrderRecord {
    private String orderMilestoneIdentifier;
    private String accountNumber;
    private String channel;
    private String logId;
    private String milestone;
    private String orderItemId;
    private String productOrderId;
    private String productOrderType;
    private String productType;
    private String status;
    private String statusDetail;
    private String subscriptionId;
    private String timeStamp;
    private String errorCode;

    @DynamoDbAttribute("orderMilestoneIdentifier")
    @DynamoDbPartitionKey
    public String getOrderMilestoneIdentifier() {
        return orderMilestoneIdentifier;
    }

    @DynamoDbAttribute("accountNumber")
    @DynamoDbSecondaryPartitionKey(indexNames = {"accountNumberIndex"})
    public String getAccountNumber() {
        return accountNumber;
    }

    @DynamoDbAttribute("channel")
    public String getChannel() {
        return channel;
    }

    @DynamoDbAttribute("logId")
    public String getLogId() {
        return logId;
    }

    @DynamoDbAttribute("milestone")
    public String getMilestone() {
        return milestone;
    }

    @DynamoDbAttribute("orderItemId")
    public String getOrderItemId() {
        return orderItemId;
    }

    @DynamoDbAttribute("productOrderId")
    @DynamoDbSecondaryPartitionKey(indexNames = {"productOrderIdIndex"})
    public String getProductOrderId() {
        return productOrderId;
    }

    @DynamoDbAttribute("productOrderType")
    public String getProductOrderType() {
        return productOrderType;
    }

    @DynamoDbAttribute("productType")
    public String getProductType() {
        return productType;
    }

    @DynamoDbAttribute("status")
    public String getStatus() {
        return status;
    }

    @DynamoDbAttribute("statusDetail")
    public String getStatusDetail() {
        return statusDetail;
    }

    @DynamoDbAttribute("subscriptionId")
    public String getSubscriptionId() {
        return subscriptionId;
    }

    @DynamoDbAttribute("timeStamp")
    public String getTimeStamp() {
        return timeStamp;
    }

    @DynamoDbAttribute("errorCode")
    public String getErrorCode() {
        return errorCode;
    }
}