package goorm.nuto.Nuto.Entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CardType {
    SHINHAN("신한"),
    KB_KOOKMIN("국민"),
    HYUNDAI("현대"),
    SAMSUNG("삼성"),
    LOTTE("롯데"),
    NH_NONGHYUP("농협"),
    WOORI("우리"),
    HANA("하나"),
    IBK("IBK기업"),
    KEB_HANA("KEB하나"),
    KAKAOBANK("카카오뱅크"),
    TOSS("토스"),
    BC("비씨"),
    SC_FIRST("SC제일"),
    CITY("씨티"),
    SUHYUP("수협"),
    JEONBUK_BANK("전북은행"),
    DAEGU_BANK("대구은행"),
    BUSAN_BANK("부산은행"),
    GWANGJU_BANK("광주은행"),
    JEJU_BANK("제주은행"),
    K_BANK("케이뱅크"),
    POST_OFFICE("우체국"),
    SHINHYEOP("신협"),
    MG_SAEMAUL("MG새마을금고"),
    KB_PAY("KB페이"),
    PAYCO("페이코"),
    ZERO_PAY("제로페이"),
    OTHER("기타");

    private final String displayName;

    CardType(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    @Override
    public String toString() {
        return displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
