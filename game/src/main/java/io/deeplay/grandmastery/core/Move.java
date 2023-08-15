package io.deeplay.grandmastery.core;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.deeplay.grandmastery.domain.FigureType;

@JsonSerialize
public record Move(Position from, Position to, FigureType promotionPiece) {}
