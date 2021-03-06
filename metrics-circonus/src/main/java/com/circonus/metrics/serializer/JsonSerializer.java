package com.circonus.metrics.serializer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.circonus.metrics.model.CirconusCounter;
import com.circonus.metrics.model.CirconusGauge;
import com.circonus.metrics.model.CirconusHistogram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Serialize Circonus time series object into json
 *
 * @see <a href="https://login.circonus.com/user/docs/Data/CheckTypes#HTTPTrap">HTTPTrap docs</a>
 */
public class JsonSerializer implements Serializer {
  private static final JsonFactory JSON_FACTORY = new JsonFactory();
  private static final ObjectMapper MAPPER = new ObjectMapper(JSON_FACTORY);
  private static final Logger LOG = LoggerFactory.getLogger(JsonSerializer.class);

  private JsonGenerator jsonOut;
  private ByteArrayOutputStream outputStream;

  public void startObject() throws IOException {
    outputStream = new ByteArrayOutputStream(2048);
    jsonOut = JSON_FACTORY.createGenerator(outputStream);
    jsonOut.writeStartObject();
  }

  public void appendGauge(CirconusGauge gauge) throws IOException {
    jsonOut.writeFieldName(gauge.metric());
    MAPPER.writeValue(jsonOut, gauge);
  }

  public void appendCounter(CirconusCounter counter) throws IOException {
    jsonOut.writeFieldName(counter.metric());
    MAPPER.writeValue(jsonOut, counter);
  }

  public void appendHistogram(CirconusHistogram hist) throws IOException {
    jsonOut.writeFieldName(hist.metric());
    MAPPER.writeValue(jsonOut, hist);
  }

  public void endObject() throws IOException {
    jsonOut.writeEndObject();
    jsonOut.flush();
    outputStream.close();
  }

  public String getAsString() throws UnsupportedEncodingException {
    return outputStream.toString("UTF-8");
  }
}
