package com.jia.store.item;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ItemSerializer extends JsonSerializer<Item> {
    @Override
    public void serialize(Item item,
                          JsonGenerator gen,
                          SerializerProvider serializerProvider) throws IOException {
        Map<String, Object> resposne = new HashMap<>();
        resposne.put("id", item.getId());
        resposne.put("name", item.getName());
        gen.writeObject(resposne);
    }
}
