package org.piyush.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.piyush.models.MilestoneOrderRecord;
import org.piyush.service.MilestoneOrderRecordService;

import java.util.List;
import java.util.Map;

public class MilestoneLambdaHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    private final MilestoneOrderRecordService orderService;

    public MilestoneLambdaHandler() {
        this.orderService = MilestoneOrderRecordService.defaultInstance();
    }

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        try {
            Map<String, String> indexAndValue = extractIndexAndValue(input);
            List<MilestoneOrderRecord> records = orderService.findByPartitionId(indexAndValue.get("inputIndex"), indexAndValue.get("value"));

            return Map.of(
                    "inputIndex", indexAndValue.get("inputIndex"),
                    "value", indexAndValue.get("value"),
                    "count", records.size(),
                    "records", records
            );
        } catch (Exception e) {
            return Map.of(
                    "error", e.getClass().getSimpleName(),
                    "message", e.getMessage()
            );
        }
    }

    private Map<String, String> extractIndexAndValue(Map<String, Object> input) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String payload;

        if (input.containsKey("Records")) {
            payload = (String) ((Map<String, Object>) ((List<?>) input.get("Records")).get(0)).get("body");
        } else if (input.containsKey("body")) {
            payload = (String) input.get("body");
        } else {
            payload = mapper.writeValueAsString(input);
        }

        JsonNode root = mapper.readTree(payload);
        JsonNode item = root.isArray() ? root.get(0) : root;

        return Map.of(
                "inputIndex", item.get("inputIndex").asText(),
                "value", item.get("value").asText()
        );
    }
}