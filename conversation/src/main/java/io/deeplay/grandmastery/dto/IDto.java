package io.deeplay.grandmastery.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/** Базовый класс для DTO. */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = StartGameResponse.class),
  @JsonSubTypes.Type(value = StartGameRequest.class),
  @JsonSubTypes.Type(value = ResultGame.class),
  @JsonSubTypes.Type(value = WaitMove.class),
  @JsonSubTypes.Type(value = SendMove.class),
  @JsonSubTypes.Type(value = WrongMove.class),
  @JsonSubTypes.Type(value = AcceptMove.class),
  @JsonSubTypes.Type(WaitAnswerDraw.class),
  @JsonSubTypes.Type(SendAnswerDraw.class)
})
public abstract class IDto {
  public IDto() {}
}
