package kr.hhplus.be.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Getter
public enum Category {
    ELECTRONICS("전자기기"),
    FASHION("패션"),
    HOME_APPLIANCES("가전제품"),
    BEAUTY("화장품"),
    FOOD("식품"),
    SPORTS("스포츠/레저"),
    BOOKS("도서"),
    TOYS("장난감"),
    AUTOMOTIVE("자동차 용품"),
    HEALTH("건강/의료"),
    PET_SUPPLIES("반려동물 용품"),
    FURNITURE("가구"),
    OFFICE_SUPPLIES("사무용품"),
    MUSIC("음악/악기"),
    GAMES("게임/콘솔"),
    BABY("유아용품"),
    JEWELRY("주얼리/액세서리");

    private final String description;
}
