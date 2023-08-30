package com.sap.cc.ascii;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sap.cc.InvalidRequestException;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.mockwebserver.MockWebServer;

public class AsciiArtServiceClientTest {

  public static final AsciiArtRequest WITH_VALID_ARGS = new AsciiArtRequest("HelloWorld", "3");
  public static final AsciiArtRequest WITH_UNKNOWN_FONT_ID = new AsciiArtRequest("handleThis", "9");

  private AsciiArtServiceUrlProvider asciiArtServiceUrlProvider = Mockito.mock(AsciiArtServiceUrlProvider.class);
  private AsciiArtServiceClient asciiArtServiceClient;
  public static MockWebServer mockBackEnd;
  private ObjectMapper objectMapper = new ObjectMapper();

  @BeforeAll
  static void setUp() throws IOException {
    mockBackEnd = new MockWebServer();
    mockBackEnd.start();
  }

  @AfterAll
  static void tearDown() throws IOException {
    mockBackEnd.shutdown();
  }

  @BeforeEach
  void initialize() {
    String serviceUrl = String.format("http://localhost:%s/api/v1/asciiArt/", mockBackEnd.getPort());
    Mockito.when(asciiArtServiceUrlProvider.getServiceUrl()).thenReturn(serviceUrl);
    asciiArtServiceClient = new AsciiArtServiceClient(WebClient.create(), asciiArtServiceUrlProvider);
  }

  @Test
  public void whenCallingGetAsciiString_thenClientMakesCorrectCallToService() throws JsonProcessingException {
    mockBackEnd.enqueue(new MockResponse()
      .setBody(objectMapper.writeValueAsString(new AsciiArtResponse("'Hello World' as ascii art","comic")))
      .addHeader(org.springframework.http.HttpHeaders.CONTENT_TYPE,
        org.springframework.http.MediaType.APPLICATION_JSON_VALUE));
    Assertions.assertEquals(asciiArtServiceClient.getAsciiString(WITH_VALID_ARGS),"'Hello World' as ascii art");
  }

  @Test
  public void whenRequestingWithInvalidRequest_thenInvalidRequestExceptionIsThrown() {
    mockBackEnd.enqueue(new MockResponse().setResponseCode(400));
    org.assertj.core.api.Assertions.assertThatThrownBy(()->asciiArtServiceClient.getAsciiString(WITH_UNKNOWN_FONT_ID)).isInstanceOf(
      InvalidRequestException.class);
  }
}