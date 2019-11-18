package com.ahl.server.entity;

import com.ahl.server.enums.CardType;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cards")
public class Card {
    @Id
    private ObjectId id;
    private ObjectId matchId;
    private ObjectId playerId;
    private CardType cardType;
    private int time;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public static boolean validateCard(Card card)
    {
        if(!ObjectUtils.allNotNull(card.getPlayerId(),card.getMatchId(),card.getCardType()) ||
        ObjectUtils.isEmpty(card.getPlayerId()) || ObjectUtils.isEmpty(card.getMatchId()) || ObjectUtils.isEmpty(card.getCardType()))
            return false;
        return true;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getMatchId() {
        return matchId;
    }

    public void setMatchId(ObjectId matchId) {
        this.matchId = matchId;
    }

    public ObjectId getPlayerId() {
        return playerId;
    }

    public void setPlayerId(ObjectId playerId) {
        this.playerId = playerId;
    }
}
